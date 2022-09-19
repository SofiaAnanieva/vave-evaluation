package org.argouml.util;

import javax.swing.filechooser.FileFilter;
import org.argouml.i18n.Translator;


public class FileFilters {
	public static final SuffixFilter UNCOMPRESSED_FILE_FILTER = new SuffixFilter(FileConstants.UNCOMPRESSED_FILE_EXT.substring(1),"Argo uncompressed project file");
	public static final SuffixFilter COMPRESSED_FILE_FILTER = new SuffixFilter(FileConstants.COMPRESSED_FILE_EXT.substring(1),"Argo compressed project file");
	public static final SuffixFilter XMI_FILTER = new SuffixFilter("xmi","XML Metadata Interchange");
	public static final SuffixFilter PGML_FILTER = new SuffixFilter("pgml","Argo diagram");
	public static final SuffixFilter CONFIG_FILTER = new SuffixFilter("config","Argo configutation file");
	public static final SuffixFilter HIST_FILTER = new SuffixFilter("hist","Argo history file");
	public static final SuffixFilter LOG_FILTER = new SuffixFilter("log","Argo usage log");
	public static final SuffixFilter GIF_FILTER = new SuffixFilter("gif",Translator.localize("combobox.filefilter.gif"));
	public static final SuffixFilter PNG_FILTER = new SuffixFilter("png",Translator.localize("combobox.filefilter.png"));
	public static final SuffixFilter PS_FILTER = new SuffixFilter("ps",Translator.localize("combobox.filefilter.ps"));
	public static final SuffixFilter EPS_FILTER = new SuffixFilter("eps",Translator.localize("combobox.filefilter.eps"));
	public static final SuffixFilter SVG_FILTER = new SuffixFilter("svg",Translator.localize("combobox.filefilter.svg"));
	public static String getSuffix(FileFilter filter) {
		if (filter instanceof SuffixFilter) {
			return((SuffixFilter) filter).getSuffix();
		}
		return null;
	}
}



