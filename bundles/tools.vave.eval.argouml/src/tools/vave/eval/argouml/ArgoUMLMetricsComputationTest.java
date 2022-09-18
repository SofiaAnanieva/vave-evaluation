package tools.vave.eval.argouml;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.diff.FeatureFilter;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.diff.IDiffProcessor;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.junit.jupiter.api.Test;

import com.google.common.cache.CacheBuilder;

import edu.kit.ipd.sdq.commons.util.org.eclipse.emf.ecore.resource.ResourceSetUtil;
import tools.vave.uml.UMLMatchEngineFactory;

/**
 * Computes the metrics used for the evaluation with ArgoUML. This involves a comparison of all generated variants with / without consistency preservation with the ground-truth variants by using EMFCompare.
 */
public class ArgoUMLMetricsComputationTest {

	@Test
	public void computeMetricsForEval() throws IOException {
		Path groundTruthLocation = Paths.get("C:\\FZI\\git\\argoumlspl-eval-results\\groundtruth-vitruv-new");
		Path vaveLocation = Paths.get("C:\\FZI\\ArgoUML-NEW-EVAL-RESULTS\\vave-project-folder-all-eval-results");

		{ // compute differences between ground truth uml models at revision R and R-1 per product variant.
			FileWriter fw = new FileWriter("C:\\FZI\\ArgoUML-NEW-EVAL-RESULTS\\vave-project-folder-all-eval-results\\res1.csv");

			List<Map<String, Integer>> revisionMap = new ArrayList<>();

			for (int rev = 1; rev <= 9; rev++) {
				Path previousRevisionLocation = groundTruthLocation.resolve("R" + (rev - 1) + "-eval-variants");
				Path revisionLocation = groundTruthLocation.resolve("R" + rev + "-eval-variants");

				Collection<Path> variantLocations = Files.list(revisionLocation).filter(f -> !f.getFileName().toString().endsWith("-vsum")).collect(Collectors.toList());

				Map<String, Integer> differencesPerVariant = new HashMap<>();

				for (Path variantLocation : variantLocations) {
					int numDiffs = this.compareModelsOfArgoUMLVariants(variantLocation.resolve("umloutput/umloutput.uml"), previousRevisionLocation.resolve(variantLocation.getFileName()).resolve("umloutput/umloutput.uml"));

					differencesPerVariant.put(variantLocation.getFileName().toString(), numDiffs);
				}

				revisionMap.add(differencesPerVariant);
			}
			// write results to csv file (product id; system revision; differences).
			for (int rev = 1; rev <= 9; rev++) {
				for (Map.Entry<String, Integer> entry : revisionMap.get(rev - 1).entrySet()) {
					fw.write(entry.getKey() + ";" + rev + ";" + entry.getValue() + "\n");
				}
			}

			fw.close();
		}

		{ // compute differences between ground truth uml models at revisions R and computed uml models at revision R per product variant.
			FileWriter fw = new FileWriter("C:\\FZI\\ArgoUML-NEW-EVAL-RESULTS\\vave-project-folder-all-eval-results\\res2.csv");

			List<Map<String, Integer>> revisionMap = new ArrayList<>();

			for (int rev = 0; rev <= 9; rev++) {
				Path groundTruthRevisionLocation = groundTruthLocation.resolve("R" + rev + "-eval-variants");
				Path revisionLocation = vaveLocation.resolve("R" + rev + "-eval-variants");

				Collection<Path> groundTruthVariantLocations = Files.list(groundTruthRevisionLocation).filter(f -> !f.getFileName().toString().endsWith("-vsum")).collect(Collectors.toList());

				Map<String, Integer> differencesPerVariant = new HashMap<>();

				for (Path groundTruthVariantLocation : groundTruthVariantLocations) {
					Path vaveVariantLocation = revisionLocation.resolve(groundTruthRevisionLocation.relativize(groundTruthVariantLocation));
					if (Files.exists(vaveVariantLocation)) {
						int numDiffs = this.compareModelsOfArgoUMLVariants(groundTruthVariantLocation.resolve("umloutput/umloutput.uml"), vaveVariantLocation.resolve("umloutput/umloutput.uml"));

						differencesPerVariant.put(groundTruthVariantLocation.getFileName().toString(), numDiffs);
					}
				}

				revisionMap.add(differencesPerVariant);
			}
			// write results to csv file (product id; system revision; differences).
			for (int rev = 0; rev <= 9; rev++) {
				for (Map.Entry<String, Integer> entry : revisionMap.get(rev).entrySet()) {
					fw.write(entry.getKey() + ";" + rev + ";" + entry.getValue() + "\n");
				}
			}

			fw.close();
		}

		// aggregate over revisions and total? or use excel or R for that?
	}

	public int compareModelsOfArgoUMLVariants(Path model1Location, Path model2Location) {
		final MatchEngineFactoryImpl matchEngineFactory = new UMLMatchEngineFactory(new EqualityHelper(EqualityHelper.createDefaultCache(CacheBuilder.newBuilder())));
		matchEngineFactory.setRanking(20);

		IMatchEngine.Factory.Registry registry = EMFCompareRCPPlugin.getDefault().getMatchEngineFactoryRegistry();
		registry.add(matchEngineFactory);

		IDiffProcessor diffProcessor = new DiffBuilder();
		IDiffEngine diffEngine = new DefaultDiffEngine(diffProcessor) {
			@Override
			protected FeatureFilter createFeatureFilter() {
				return new FeatureFilter() {
					@Override
					protected boolean isIgnoredReference(Match match, EReference reference) {
						return super.isIgnoredReference(match, reference);
					}

					@Override
					public boolean isIgnoredAttribute(EAttribute attribute) {
						return super.isIgnoredAttribute(attribute);
					}

					@Override
					public boolean checkForOrderingChanges(EStructuralFeature feature) {
						return false;
					}
				};
			}
		};

		EMFCompare comparator = EMFCompare.builder().setMatchEngineFactoryRegistry(registry).setDiffEngine(diffEngine).build();

		// ------------------------------------

		ResourceSet resourceSet1 = ResourceSetUtil.withGlobalFactories(new ResourceSetImpl());
		Resource resource1 = resourceSet1.getResource(URI.createFileURI(model1Location.toString()), true);
		System.out.println("LOADED FIRST RESOURCE");

		org.eclipse.uml2.uml.Package model1 = (org.eclipse.uml2.uml.Package) ((org.eclipse.uml2.uml.Package) ((Model) resource1.getContents().stream().filter(v -> (v instanceof Model)).findFirst().get()));

		ResourceSet resourceSet2 = ResourceSetUtil.withGlobalFactories(new ResourceSetImpl());
		Resource resource2 = resourceSet2.getResource(URI.createFileURI(model2Location.toString()), true);
		System.out.println("LOADED SECOND RESOURCE");

		org.eclipse.uml2.uml.Package model2 = (org.eclipse.uml2.uml.Package) ((org.eclipse.uml2.uml.Package) ((Model) resource2.getContents().stream().filter(v -> (v instanceof Model)).findFirst().get()));

		long timeStart = System.currentTimeMillis();

		IComparisonScope scope = new DefaultComparisonScope(model1, model2, null);
		Comparison comparison = comparator.compare(scope);
		List<Diff> differences = comparison.getDifferences();
		List<Match> matches = comparison.getMatches();

		long timeDiff = System.currentTimeMillis() - timeStart;
		System.out.println("TOTAL TIME: " + timeDiff);

		System.out.println("DIFFERENCES: " + differences.size());
		System.out.println("MATCHES: " + matches.size());

		System.out.println("DONE");

		// count matched elements
		int[] matchCount = new int[] { 0, 0, 0 };
		this.countMatch(comparison.getMatches(), matchCount);
		System.out.println("LEFT/MATCH/RIGHT: " + matchCount[0] + "/" + matchCount[1] + "/" + matchCount[2]);

		return differences.size();
	}

	private void countMatch(Collection<Match> matches, int[] counter) {
		for (Match match : matches) {
			if (match.getLeft() != null && match.getRight() != null) {
				counter[1]++;
				this.countMatch(match.getSubmatches(), counter);
			} else if (match.getLeft() != null) {
				if ((match.getLeft() instanceof LiteralInteger || match.getLeft() instanceof LiteralUnlimitedNatural) && ((((Match) match.eContainer()).getLeft() instanceof Parameter) || ((Match) match.eContainer()).getLeft() instanceof Property)) {// && ((Parameter)((Match)match.eContainer()).getLeft()).getDirection() == ParameterDirectionKind.RETURN_LITERAL) {
					// do nothing
				} else {
					counter[0]++;
					System.out.println("LEFT: " + match.getLeft() + " / " + match.eContainer());
				}
			} else if (match.getRight() != null) {
				counter[2]++;
				System.out.println("RIGHT: " + match.getRight() + " / " + match.eContainer());
			}
		}
	}
}
