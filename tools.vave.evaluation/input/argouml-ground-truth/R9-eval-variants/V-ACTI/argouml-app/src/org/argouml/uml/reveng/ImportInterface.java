package org.argouml.uml.reveng;

import java.io.File;
import java.util.Collection;
import java.util.List;
import org.argouml.kernel.Project;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.util.SuffixFilter;
import org.argouml.uml.reveng.ImportSettings;
import org.argouml.uml.reveng.SettingsTypes.Setting;
import org.argouml.uml.reveng.ImportSettings;


public interface ImportInterface extends ModuleInterface {
	public static final String SOURCE_PATH_TAG = "src_path";
	public static final String SOURCE_MODIFIERS_TAG = "src_modifiers";
	SuffixFilter[]getSuffixFilters();
	boolean isParseable(File file);
	Collection parseFiles(Project p,final Collection<File>files,ImportSettings settings,ProgressMonitor monitor)throws ImportException;
	List<Setting>getImportSettings();
	public class ImportException extends Exception {
	public ImportException(String message,Throwable cause) {
		super("Import Exception : " + message,cause);
	}
	public ImportException(String message) {
		super(message);
	}
	public ImportException(Throwable cause) {
		super("Import Exception",cause);
	}
}
}



