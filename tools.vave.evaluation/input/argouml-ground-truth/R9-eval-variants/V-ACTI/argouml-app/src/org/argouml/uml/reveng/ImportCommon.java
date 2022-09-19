package org.argouml.uml.reveng;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.layout.ClassdiagramLayouter;
import org.argouml.util.SuffixFilter;
import org.tigris.gef.base.Globals;
import org.argouml.uml.reveng.ImportInterface;


public abstract class ImportCommon implements ImportSettingsInternal {
	protected static final int MAX_PROGRESS_PREPARE = 1;
	protected static final int MAX_PROGRESS_IMPORT = 99;
	protected static final int MAX_PROGRESS = MAX_PROGRESS_PREPARE + MAX_PROGRESS_IMPORT;
	private Hashtable<String,ImportInterface>modules;
	private ImportInterface currentModule;
	private String srcPath;
	private DiagramInterface diagramInterface;
	private File[]selectedFiles;
	private SuffixFilter selectedSuffixFilter;
	protected ImportCommon() {
		super();
		modules = new Hashtable<String,ImportInterface>();
		for (ImportInterface importer:ImporterManager.getInstance().getImporters()) {
			modules.put(importer.getName(),importer);
		}
		if (modules.isEmpty()) {
			throw new RuntimeException("Internal error. " + "No importer modules found.");
		}
		currentModule = modules.get("Java");
		if (currentModule == null) {
			currentModule = modules.elements().nextElement();
		}
	}
	public abstract int getImportLevel();
	protected void initCurrentDiagram() {
		diagramInterface = getCurrentDiagram();
	}
	private DiagramInterface getCurrentDiagram() {
		DiagramInterface result = null;
		if (Globals.curEditor().getGraphModel()instanceof ClassDiagramGraphModel) {
			result = new DiagramInterface(Globals.curEditor());
		}
		return result;
	}
	public abstract String getInputSourceEncoding();
	protected List<File>getFileList(ProgressMonitor monitor) {
		List<File>files = Arrays.asList(getSelectedFiles());
		if (files.size() == 1) {
			File file = files.get(0);
			SuffixFilter suffixFilters[] =  {selectedSuffixFilter};
			if (suffixFilters[0] == null) {
				suffixFilters = currentModule.getSuffixFilters();
			}
			files = FileImportUtils.getList(file,isDescendSelected(),suffixFilters,monitor);
			if (file.isDirectory()) {
				setSrcPath(file.getAbsolutePath());
			}else {
				setSrcPath(null);
			}
		}
		if (isChangedOnlySelected()) {
			Object model = ProjectManager.getManager().getCurrentProject().getModel();
			for (int i = files.size() - 1;i >= 0;i--) {
				File f = files.get(i);
				String fn = f.getAbsolutePath();
				String lm = String.valueOf(f.lastModified());
				if (lm.equals(Model.getFacade().getTaggedValueValue(model,fn))) {
					files.remove(i);
				}
			}
		}
		return files;
	}
	public void setSrcPath(String path) {
		srcPath = path;
	}
	public String getSrcPath() {
		return srcPath;
	}
	private void setLastModified(Project project,File file) {
		String fn = file.getAbsolutePath();
		String lm = String.valueOf(file.lastModified());
		if (lm != null) {
			Model.getCoreHelper().setTaggedValue(project.getModel(),fn,lm);
		}
	}
	public abstract boolean isCreateDiagramsSelected();
	public abstract boolean isMinimizeFigsSelected();
	public abstract boolean isDiagramLayoutSelected();
	public abstract boolean isDescendSelected();
	public abstract boolean isChangedOnlySelected();
	protected Hashtable<String,ImportInterface>getModules() {
		return modules;
	}
	protected void setSelectedFiles(final File[]files) {
		selectedFiles = files;
	}
	protected void setSelectedSuffixFilter(final SuffixFilter suffixFilter) {
		selectedSuffixFilter = suffixFilter;
	}
	protected File[]getSelectedFiles() {
		File[]copy = new File[selectedFiles.];
		for (int i = 0;i < selectedFiles.;i++) {
			copy[i] = selectedFiles[i];
		}
		return copy;
	}
	protected void setCurrentModule(ImportInterface module) {
		currentModule = module;
	}
	protected ImportInterface getCurrentModule() {
		return currentModule;
	}
	public List<String>getLanguages() {
		return Collections.unmodifiableList(new ArrayList<String>(modules.keySet()));
	}
	public boolean isDescend() {
		String flags = Configuration.getString(Argo.KEY_IMPORT_GENERAL_SETTINGS_FLAGS);
		if (flags != null&&flags.length() > 0) {
			StringTokenizer st = new StringTokenizer(flags,",");
			if (st.hasMoreTokens()&&st.nextToken().equals("false")) {
				return false;
			}
		}
		return true;
	}
	public boolean isChangedOnly() {
		String flags = Configuration.getString(Argo.KEY_IMPORT_GENERAL_SETTINGS_FLAGS);
		if (flags != null&&flags.length() > 0) {
			StringTokenizer st = new StringTokenizer(flags,",");
			skipTokens(st,1);
			if (st.hasMoreTokens()&&st.nextToken().equals("false")) {
				return false;
			}
		}
		return true;
	}
	public boolean isCreateDiagrams() {
		String flags = Configuration.getString(Argo.KEY_IMPORT_GENERAL_SETTINGS_FLAGS);
		if (flags != null&&flags.length() > 0) {
			StringTokenizer st = new StringTokenizer(flags,",");
			skipTokens(st,2);
			if (st.hasMoreTokens()&&st.nextToken().equals("false")) {
				return false;
			}
		}
		return true;
	}
	public boolean isMinimizeFigs() {
		String flags = Configuration.getString(Argo.KEY_IMPORT_GENERAL_SETTINGS_FLAGS);
		if (flags != null&&flags.length() > 0) {
			StringTokenizer st = new StringTokenizer(flags,",");
			skipTokens(st,3);
			if (st.hasMoreTokens()&&st.nextToken().equals("false")) {
				return false;
			}
		}
		return true;
	}
	private void skipTokens(StringTokenizer st,int count) {
		for (int i = 0;i < count;i++) {
			if (st.hasMoreTokens()) {
				st.nextToken();
			}
		}
	}
	public boolean isDiagramLayout() {
		String flags = Configuration.getString(Argo.KEY_IMPORT_GENERAL_SETTINGS_FLAGS);
		if (flags != null&&flags.length() > 0) {
			StringTokenizer st = new StringTokenizer(flags,",");
			skipTokens(st,4);
			if (st.hasMoreTokens()&&st.nextToken().equals("false")) {
				return false;
			}
		}
		return true;
	}
	public String getEncoding() {
		String enc = Configuration.getString(Argo.KEY_INPUT_SOURCE_ENCODING);
		if (enc == null||enc.trim().equals("")) {
			enc = System.getProperty("file.encoding");
		}
		return enc;
	}
	public void layoutDiagrams(ProgressMonitor monitor,int startingProgress) {
		if (diagramInterface == null) {
			return;
		}
		List<ArgoDiagram>diagrams = diagramInterface.getModifiedDiagramList();
		int total = startingProgress + diagrams.size() / 10;
		for (int i = 0;i < diagrams.size();i++) {
			ArgoDiagram diagram = diagrams.get(i);
			ClassdiagramLayouter layouter = new ClassdiagramLayouter(diagram);
			layouter.layout();
			int act = startingProgress + (i + 1) / 10;
			int progress = MAX_PROGRESS_PREPARE + MAX_PROGRESS_IMPORT * act / total;
			if (monitor != null) {
				monitor.updateProgress(progress);
			}
		}
	}
	protected void doImport(ProgressMonitor monitor) {
		monitor.setMaximumProgress(MAX_PROGRESS);
		int progress = 0;
		monitor.updateSubTask(Translator.localize("dialog.import.preImport"));
		List<File>files = getFileList(monitor);
		progress += MAX_PROGRESS_PREPARE;
		monitor.updateProgress(progress);
		if (files.size() == 0) {
			monitor.notifyNullAction();
			return;
		}
		Model.getPump().stopPumpingEvents();
		try {
			doImportInternal(files,monitor,progress);
		}finally {
			ExplorerEventAdaptor.getInstance().structureChanged();
			Model.getPump().startPumpingEvents();
		}
	}
	private void doImportInternal(List<File>filesLeft,final ProgressMonitor monitor,int progress) {
		Project project = ProjectManager.getManager().getCurrentProject();
		initCurrentDiagram();
		final StringBuffer problems = new StringBuffer();
		Collection newElements = new HashSet();
		try {
			newElements.addAll(currentModule.parseFiles(project,filesLeft,this,monitor));
		}catch (Exception e) {
			problems.append(printToBuffer(e));
		}
		if (isCreateDiagramsSelected()) {
			addFiguresToDiagrams(newElements);
		}
		if (isDiagramLayoutSelected()) {
			monitor.updateMainTask(Translator.localize("dialog.import.postImport"));
			monitor.updateSubTask(Translator.localize("dialog.import.layoutAction"));
			layoutDiagrams(monitor,progress + filesLeft.size());
		}
		if (problems != null&&problems.length() > 0) {
			monitor.notifyMessage(Translator.localize("dialog.title.import-problems"),Translator.localize("label.import-problems"),problems.toString());
		}
		monitor.updateMainTask(Translator.localize("dialog.import.done"));
		monitor.updateSubTask("");
		monitor.updateProgress(MAX_PROGRESS);
	}
	private void addFiguresToDiagrams(Collection newElements) {
		for (Object element:newElements) {
			if (Model.getFacade().isAClassifier(element)||Model.getFacade().isAPackage(element)) {
				Object ns = Model.getFacade().getNamespace(element);
				if (ns == null) {
					diagramInterface.createRootClassDiagram();
				}else {
					String packageName = getQualifiedName(ns);
					if (packageName != null&&!packageName.equals("")) {
						diagramInterface.selectClassDiagram(ns,packageName);
					}else {
						diagramInterface.createRootClassDiagram();
					}
					if (Model.getFacade().isAInterface(element)) {
						diagramInterface.addInterface(element,isMinimizeFigsSelected());
					}else if (Model.getFacade().isAClass(element)) {
						diagramInterface.addClass(element,isMinimizeFigsSelected());
					}else if (Model.getFacade().isAPackage(element)) {
						diagramInterface.addPackage(element);
					}
				}
			}
		}
	}
	private String getQualifiedName(Object element) {
		StringBuffer sb = new StringBuffer();
		Object ns = element;
		while (ns != null) {
			String name = Model.getFacade().getName(ns);
			if (name == null) {
				name = "";
			}
			sb.insert(0,name);
			ns = Model.getFacade().getNamespace(ns);
			if (ns != null) {
				sb.insert(0,".");
			}
		}
		return sb.toString();
	}
	private StringBuffer printToBuffer(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new java.io.PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.getBuffer();
	}
}



