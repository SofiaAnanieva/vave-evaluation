package tools.vave.eval.mm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import antenna.preprocessor.v3.ILineFilter;
import antenna.preprocessor.v3.ILogger;
import antenna.preprocessor.v3.PPException;
import antenna.preprocessor.v3.Preprocessor;

public class AntennaGenerator {

	private static final int MIN_FILESIZE = 5;

	private Preprocessor pp;

	private File templatesFolder;
	private File featuresFile;
	private File outputFolder;

	private String[] features;

	public AntennaGenerator(File templatesFolder, File featuresFile) {
		this.templatesFolder = templatesFolder;
		this.featuresFile = featuresFile;
		this.features = null;
	}

	public void setFeatures(String[] args) {
		this.features = args;
	}

	public void generateFiles(File dir) {
		if (this.features == null)
			return;

		init();

		outputFolder = new File(dir, "src");
		if (!outputFolder.exists())
			outputFolder.mkdirs();

		// Generate features file
		generateClass(new File(dir, "features.txt"), this.featuresFile.toString());

		// Generate Java files
		this.generateFilesRecursively(this.templatesFolder);
	}

	/* Helper Functions */

	private void generateFilesRecursively(File dir) {
		File[] filelist = dir.listFiles();
		System.out.println(dir);
		for (File f : filelist) {
			if (f.isDirectory() && !f.getName().contains(".svn")) {
				this.generateFilesRecursively(f);
			} else if (f.isFile()) {
				if (f.getName().endsWith(".java")) {
					String pathname = f.getPath().substring(f.getPath().indexOf(this.templatesFolder.getPath()) + this.templatesFolder.getPath().length());

					File srcfile = new File(this.outputFolder, pathname);

					if (!srcfile.exists())
						srcfile.getParentFile().mkdirs();

					this.generateClass(srcfile, f.getPath());
				}
			}
		}
	}

	/**
	 * Initialisation.
	 */
	private void init() {

		ILogger logger = new ILogger() {
			@Override
			public void log(String arg0) {
				System.out.println(arg0);
			}
		};
		ILineFilter filter = new ILineFilter() {
			@Override
			public String filter(String arg0) {
				return arg0;
			}
		};

		this.pp = new Preprocessor(logger, filter);
		try {
			for (String feature : this.features) {
				this.pp.addDefines(feature);
			}
		} catch (PPException e) {
			e.printStackTrace();
		}
	}

	/** Generates the file for a class. */
	private void generateClass(File file, String templateName) {
		try {
			pp.preprocess(new FileInputStream(new File(templateName)), new FileOutputStream(file), "UTF-8");

			if (file.length() <= MIN_FILESIZE)
				file.delete();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (PPException e2) {
			e2.printStackTrace();
		}
	}

}
