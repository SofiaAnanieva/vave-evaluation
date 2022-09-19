package tools.vave.eval.argouml;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import tools.vitruv.testutils.RegisterMetamodelsInStandalone;
import tools.vitruv.testutils.TestLogging;
import tools.vitruv.testutils.TestProjectManager;

/**
 * Use JavaPP and Papyrus to generate source code variants and corresponding UML diagrams.
 */
@ExtendWith({ TestProjectManager.class, TestLogging.class, RegisterMetamodelsInStandalone.class })
//@Disabled
public class ArgoUMLVariantsGeneratorTest {

	@Test
	public void addImportsToJavaFiles() throws IOException {
		Path targetLocation = Paths.get("C:\\FZI\\git\\argouml-spl-revisions-variants");

		for (int rev = 0; rev <= 9; rev++) {
			Path variantsLocation = targetLocation.resolve("R" + rev + "_variants");

			// for every variant
			Files.list(variantsLocation).filter(f -> Files.isDirectory(f)).forEach(variantLocation -> {
				Path location = variantLocation.resolve("src");

				// collect files per directory
				Path[] sourceFolders = new Path[] { location.resolve("argouml-core-model\\src"), location.resolve("argouml-core-model-euml\\src"), location.resolve("argouml-core-model-mdr\\src"), location.resolve("argouml-app\\src"), location.resolve("argouml-core-diagrams-sequence2\\src") };
				// Path[] sourceFolders = new Path[] { location };
				for (Path sourceFolder : sourceFolders) {

					try {
						Map<Path, Collection<Path>> dirToJavaFilesMap = new HashMap<>();

						Files.walk(sourceFolder).forEach(f -> {
							if (Files.isDirectory(f) && !f.equals(sourceFolder) && !f.getFileName().toString().startsWith(".") && !f.getFileName().toString().equals("META-INF") && !f.getFileName().toString().equals("test_project.marker_vitruv") && !f.getFileName().toString().equals("umloutput") && !f.getFileName().toString().contains("-") && !f.getFileName().toString().startsWith("build-eclipse")
									&& !f.getFileName().toString().startsWith("bin") && !f.getFileName().toString().startsWith("template")) {
							} else if (Files.isRegularFile(f) && f.getFileName().toString().endsWith(".java") && !f.getFileName().toString().equals("package-info.java")) {
								Path relPath = sourceFolder.relativize(f);
								Collection<Path> filesInDir = dirToJavaFilesMap.get(relPath.getParent());
								if (filesInDir == null) {
									filesInDir = new ArrayList<>();
									dirToJavaFilesMap.put(relPath.getParent(), filesInDir);
								}
								filesInDir.add(f.getFileName());
							}
						});

						Map<Path, List<String>> dirToImportStringMap = new HashMap<>();
						// compute import string per directory
						for (Map.Entry<Path, Collection<Path>> entry : dirToJavaFilesMap.entrySet()) {
							Path packagePath = entry.getKey();
							StringBuffer sb = new StringBuffer();
							for (int i = 0; i < packagePath.getNameCount(); i++) {
								sb.append(packagePath.getName(i));
								sb.append(".");
							}
							String packagePrefix = sb.toString();
							System.out.println("Package Prefix: " + packagePrefix);
							List<String> importLines = new ArrayList<>();
							for (Path cu : entry.getValue()) {
								importLines.add("import " + packagePrefix + cu.getFileName().toString().substring(0, cu.getFileName().toString().length() - 5) + ";");
							}
							dirToImportStringMap.put(packagePath, importLines);
						}

						// add import string to every file
						for (Map.Entry<Path, Collection<Path>> entry : dirToJavaFilesMap.entrySet()) {
							for (Path cu : entry.getValue()) {
								List<String> importLines = dirToImportStringMap.get(entry.getKey());
								Path filePath = sourceFolder.resolve(entry.getKey()).resolve(cu);
								List<String> lines = null;
								Charset charset = null;
								try {
									lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
									charset = StandardCharsets.UTF_8;
								} catch (MalformedInputException mie) {
									lines = Files.readAllLines(filePath, StandardCharsets.ISO_8859_1);
									charset = StandardCharsets.ISO_8859_1;
								}
								List<String> updatedLines = new ArrayList<>();
								for (String line : lines) {
									updatedLines.add(line);
									if (line.stripLeading().startsWith("package org.argouml")) {
										updatedLines.add("");
										updatedLines.addAll(importLines);
										updatedLines.add("");
									}
								}
								updatedLines.add("");
								Files.writeString(filePath, updatedLines.stream().collect(Collectors.joining("\n")), charset);
							}
						}

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			});
		}
	}

	String COGNITIVE = "COGNITIVE";
	String LOGGING = "LOGGING";
	String ACTIVITYDIAGRAM = "ACTIVITYDIAGRAM";
	String STATEDIAGRAM = "STATEDIAGRAM";
	String SEQUENCEDIAGRAM = "SEQUENCEDIAGRAM";
	String USECASEDIAGRAM = "USECASEDIAGRAM";
	String COLLABORATIONDIAGRAM = "COLLABORATIONDIAGRAM";
	String DEPLOYMENTDIAGRAM = "DEPLOYMENTDIAGRAM";
	String Diagrams = "Diagrams";
	String Class = "Class";
	String ArgoUML = "ArgoUML";
	String Core = "Core";

	String[] mandatory = new String[] { "java.home", "user.home", "user.dir", Diagrams, Class, ArgoUML };

	String[] optional = new String[] { COGNITIVE, LOGGING, ACTIVITYDIAGRAM, COLLABORATIONDIAGRAM, DEPLOYMENTDIAGRAM, SEQUENCEDIAGRAM, STATEDIAGRAM, USECASEDIAGRAM };

	@Test
	public void createSpecificArgoUMLVariantJava() throws IOException {
		Path targetLocation = Paths.get("C:\\FZI\\git\\argouml-spl-revisions-variants");
		Path splLocations = Paths.get("C:\\FZI\\git\\argouml-spl-revisions");

		for (int rev = 0; rev <= 0; rev++) {
			Path splLocation = splLocations.resolve("R" + rev);

			// all features
			String[] allConfig = optional.clone();
			this.createVariant(allConfig, splLocation, targetLocation);
		}
	}

	@Test
	public void createSpecificFilesForArgoUMLVariantsJavaForInternalization() throws IOException {
		Path targetLocation = Paths.get("C:\\FZI\\git\\argouml-spl-revisions-variants");
		Path splLocations = Paths.get("C:\\FZI\\git\\argouml-spl-revisions");

		for (int rev = 1; rev <= 9; rev++) {
			Path splLocation = splLocations.resolve("R" + rev);

			// only core
			String[] emptyConfig = new String[] {};
			this.createOrUpdateSpecificFilesForVariant(emptyConfig, splLocation, targetLocation);

			// single features
			for (int i = 0; i < optional.length; i++) {
				String[] config = new String[] { optional[i] };
				this.createOrUpdateSpecificFilesForVariant(config, splLocation, targetLocation);
			}

			// pair-wise feature interactions
			for (int i = 0; i < optional.length; i++) {
				for (int j = i + 1; j < optional.length; j++) {
					String[] config = { optional[i], optional[j] };
					this.createOrUpdateSpecificFilesForVariant(config, splLocation, targetLocation);
				}
			}

			// selected three-wise feature interactions
			String[] threeWiseConfig = new String[] { LOGGING, COLLABORATIONDIAGRAM, SEQUENCEDIAGRAM };
			this.createOrUpdateSpecificFilesForVariant(threeWiseConfig, splLocation, targetLocation);

			// all features
			String[] allConfig = optional.clone();
			this.createOrUpdateSpecificFilesForVariant(allConfig, splLocation, targetLocation);
		}
	}

	private void createOrUpdateSpecificFilesForVariant(String[] currentArray, Path splLocation, Path targetLocation) throws IOException {
		List<String> current = new ArrayList<>(Arrays.asList(currentArray));

		String variantName = current.stream().map(s -> s.substring(0, 4)).collect(Collectors.joining("-"));

		System.out.println("BEGIN: " + variantName);

		current.addAll(Arrays.asList(mandatory));

		Path variantsLocation = targetLocation.resolve(splLocation.getFileName().toString() + "_variants");
		Path variantLocation = variantsLocation.resolve("V" + (variantName.isEmpty() ? "" : "-") + variantName);

		Path splSourceFolder = splLocation.resolve("src");

		Files.createDirectories(variantLocation);

		JavaPpGenerator generator = new JavaPpGenerator(splSourceFolder.toFile(), current.toArray(new String[current.size()]));

		// TODO: update specific files here!
		generator.generateFile(variantLocation.toFile(), new File(splSourceFolder.toFile(), "argouml-core-model-euml/src/org/argouml/model/euml/CoreHelperEUMLImpl.java"));
		generator.generateFile(variantLocation.toFile(), new File(splSourceFolder.toFile(), "argouml-core-model-euml/src/org/argouml/model/euml/DataTypesHelperEUMLImpl.java"));

		System.out.println("END: " + variantName);
	}

	/**
	 * Creates a subset of variants for the first nine revisions of ArgoUML-SPL.
	 * 
	 * @throws IOException
	 */
	@Test
	public void createArgoUMLVariantsJavaForInternalization() throws IOException {
		Path targetLocation = Paths.get("C:\\FZI\\git\\argouml-spl-revisions-variants");
		Path splLocations = Paths.get("C:\\FZI\\git\\argouml-spl-revisions");

		for (int rev = 0; rev <= 9; rev++) {
			Path splLocation = splLocations.resolve("R" + rev);

			// only core
			String[] emptyConfig = new String[] {};
			this.createVariant(emptyConfig, splLocation, targetLocation);

			// single features
			for (int i = 0; i < optional.length; i++) {
				String[] config = new String[] { optional[i] };
				this.createVariant(config, splLocation, targetLocation);
			}

			// pair-wise feature interactions
			for (int i = 0; i < optional.length; i++) {
				for (int j = i + 1; j < optional.length; j++) {
					String[] config = { optional[i], optional[j] };
					this.createVariant(config, splLocation, targetLocation);
				}
			}

			// selected three-wise feature interactions
			String[] threeWiseConfig = new String[] { LOGGING, COLLABORATIONDIAGRAM, SEQUENCEDIAGRAM };
			this.createVariant(threeWiseConfig, splLocation, targetLocation);

			// all features
			String[] allConfig = optional.clone();
			this.createVariant(allConfig, splLocation, targetLocation);
		}
	}

	/**
	 * Adds mandatory features and creates variant.
	 * 
	 * @param currentArray
	 * @param splLocation
	 * @param targetLocation
	 * @throws IOException
	 */
	private void createVariant(String[] currentArray, Path splLocation, Path targetLocation) throws IOException {
		List<String> current = new ArrayList<>(Arrays.asList(currentArray));

		String variantName = current.stream().map(s -> s.substring(0, 4)).collect(Collectors.joining("-"));

		System.out.println("BEGIN: " + variantName);

		current.addAll(Arrays.asList(mandatory));

		Path variantsLocation = targetLocation.resolve(splLocation.getFileName().toString() + "_variants");
		Path variantLocation = variantsLocation.resolve("V" + (variantName.isEmpty() ? "" : "-") + variantName);

		Path splSourceFolder = splLocation.resolve("src");

		Files.createDirectories(variantLocation);

		JavaPpGenerator generator = new JavaPpGenerator(splSourceFolder.toFile(), current.toArray(new String[current.size()]));
		generator.generateFiles(variantLocation.toFile());

		System.out.println("END: " + variantName);
	}

	/**
	 * Uses Vitruvius to create a UML model for every ArgoUML variant.
	 */
	@Test
	public void addVitruvUMLModelToAllArgoUMLVariants() {
		// TODO
	}

	/**
	 * This test creates all variants of a given ArgoUML-SPL revision.
	 */
	@Test
	public void generateAllArgoUMLVariants() throws IOException {
		// Path splLocation = Paths.get("C:\\FZI\\git\\argouml-spl-revisions\\R1");
		Path targetLocation = Paths.get("C:\\FZI\\git\\argouml-spl-revisions-variants");
		Path splLocations = Paths.get("C:\\FZI\\git\\argouml-spl-revisions");

		for (int i = 0; i < 26; i++) {
			Path splLocation = splLocations.resolve("R" + i);

			long binarycounter = 0;
			long max = (int) Math.pow(2, optional.length);

			while (binarycounter < max) {
				ArrayList<String> current = new ArrayList<String>();
				for (int j = 0; j < optional.length; j++) {
					// if bit on position j is 1
					if (((binarycounter >> j) & 1) == 1) {
						current.add(optional[j]);
					}
				}

				String variantName = current.stream().map(s -> s.substring(0, 4)).collect(Collectors.joining("-"));

				System.out.println("BEGIN: " + variantName);

				current.addAll(Arrays.asList(mandatory));

				Path variantsLocation = targetLocation.resolve(splLocation.getFileName().toString() + "_variants");
				Path variantLocation = variantsLocation.resolve("V" + (variantName.isEmpty() ? "" : "-") + variantName);

				Path splSourceFolder = splLocation.resolve("src");

				Files.createDirectories(variantLocation);

				JavaPpGenerator generator = new JavaPpGenerator(splSourceFolder.toFile(), current.toArray(new String[current.size()]));
				generator.generateFiles(variantLocation.toFile());

				System.out.println("END: " + variantName);

				binarycounter++;
			}
		}
	}

}
