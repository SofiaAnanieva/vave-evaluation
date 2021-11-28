package tools.vave.eval.argouml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.emftext.language.java.JavaClasspath;

import tools.vitruv.variability.vave.VirtualProductModel;
import tools.vitruv.variability.vave.VirtualProductModelInitializer;

public class VirtualProductModelArgoUMLInitializer implements VirtualProductModelInitializer {

	@Override
	public void initialize(VirtualProductModel vpm) {
		ResourceSet resourceSet = vpm.getResourceSet();
		resourceSet.getLoadOptions().put("DISABLE_LAYOUT_INFORMATION_RECORDING", Boolean.TRUE);
		resourceSet.getLoadOptions().put("DISABLE_LOCATION_MAP", Boolean.TRUE);
		resourceSet.getLoadOptions().put(JavaClasspath.OPTION_USE_LOCAL_CLASSPATH, Boolean.TRUE);
		resourceSet.getLoadOptions().put(JavaClasspath.OPTION_REGISTER_STD_LIB, Boolean.FALSE);

		// register jar files
		System.out.println("REGISTERING JAR FILES");
		JavaClasspath classPath = JavaClasspath.get(resourceSet, JavaClasspath.getInitializers());
		classPath.registerClassifierJar(URI.createFileURI(Paths.get("resources\\jamopp\\rt.jar").toAbsolutePath().toString()));
		classPath.registerClassifierJar(URI.createFileURI(Paths.get("resources\\argouml\\jmi.jar").toAbsolutePath().toString()));
		Path[] libraryFolders = new Path[] { Paths.get("C:\\FZI\\git\\argouml-workaround\\src\\") };

		try {
			for (Path libraryFolder : libraryFolders) {
				Files.walk(libraryFolder).forEach(f -> {
					if (Files.isRegularFile(f) && f.getFileName().toString().endsWith(".jar")) {
						classPath.registerClassifierJar(URI.createFileURI(f.toString()));
						System.out.println("ADDED JAR FILE: " + f);
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
