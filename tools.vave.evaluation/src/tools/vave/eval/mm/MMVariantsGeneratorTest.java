package tools.vave.eval.mm;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class MMVariantsGeneratorTest {

	private static final String LABEL = "Label";
	private static final String INCLUDE_SORTING = "includeSorting";
	private static final String INCLUDE_FAVOURITES = "includeFavourites";
	private static final String INCLUDE_COPY_PHOTO = "includeCopyPhoto";
	private static final String INCLUDE_SMS_FEATURE = "includeSmsFeature";

	private static String[] mandatory;

	private static String[] optional;

	@Test
	public void generateAllVariantsTest() {
		int rev = 1;

		mandatory = new String[] { "device_screen_176x205", "MobileMedia", "MediaManagement", "CreateDelete", "ViewPlay", "Photo", "Exception" };
		optional = new String[] {};
		process(rev++);

		mandatory = new String[] { "device_screen_176x205", "MobileMedia", "MediaManagement", "CreateDelete", "ViewPlay", "Photo", "Exception", LABEL };
		optional = new String[] { INCLUDE_SORTING };
		process(rev++);

		mandatory = new String[] { "device_screen_176x205", "MobileMedia", "MediaManagement", "CreateDelete", "ViewPlay", "Photo", "Exception", LABEL };
		optional = new String[] { INCLUDE_SORTING, INCLUDE_FAVOURITES };
		process(rev++);

		mandatory = new String[] { "device_screen_176x205", "MobileMedia", "MediaManagement", "CreateDelete", "ViewPlay", "Photo", "Exception", LABEL };
		optional = new String[] { INCLUDE_SORTING, INCLUDE_FAVOURITES, INCLUDE_COPY_PHOTO };
		process(rev++);

		mandatory = new String[] { "device_screen_176x205", "MobileMedia", "MediaManagement", "CreateDelete", "ViewPlay", "Photo", "Exception", LABEL };
		optional = new String[] { INCLUDE_SORTING, INCLUDE_FAVOURITES, INCLUDE_COPY_PHOTO, INCLUDE_SMS_FEATURE };
		process(rev++);

//		mandatory = new String[] { "device_screen_176x205", "MobileMedia", "MediaManagement", "CreateDelete", "ViewPlay", "Exception", "Label" };
//		optional = new String[] { "includeSorting", "includeFavourites", "includeCopyPhoto", "includeSmsFeature", "includePhotoAlbum", "includeMusic" };
//		process(rev++);
//
//		mandatory = new String[] { "device_screen_176x205", "MobileMedia", "MediaManagement", "CreateDelete", "ViewPlay", "Exception", "Label" };
//		optional = new String[] { "includeSorting", "includeFavourites", "includeCopyPhoto", "includeSmsFeature", "includePhoto", "includeMusic", "includeVideo" };
//		process(rev++);
	}

	private static void process(int rev) {
		int i = 0;

		long binarycounter = 0;
		long max = (int) Math.pow(2, optional.length);

		AntennaGenerator generator = new AntennaGenerator(new File("C:\\FZI\\git\\case_study_systems\\MM\\MM_templates\\" + rev + "\\src\\"), new File("C:\\FZI\\git\\case_study_systems\\MM\\MM_templates\\" + rev + "\\features.txt"));

		while (binarycounter < max) {
			ArrayList<String> current = new ArrayList<String>(Arrays.asList(mandatory));
			for (int j = 0; j < optional.length; j++) {
				// if bit on position j is 1
				if (((binarycounter >> j) & 1) == 1) {
					current.add(optional[j]);
				}
			}

			String variantName = "V-CORE";
			if (current.contains(LABEL))
				variantName += "-LABEL";
			if (current.contains(INCLUDE_SORTING))
				variantName += "-SORT";
			if (current.contains(INCLUDE_FAVOURITES))
				variantName += "-FAV";
			if (current.contains(INCLUDE_SMS_FEATURE))
				variantName += "-SMS";

			if (!(current.contains(INCLUDE_SMS_FEATURE) && !current.contains(INCLUDE_COPY_PHOTO))) { // sms requires copy
				generator.setFeatures(current.toArray(new String[current.size()]));
				generator.generateFiles(new File("MM_variants/R" + rev + "_variants/" + variantName + "/"));
			}

			binarycounter++;
		}
	}

}
