package org.argouml.application;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import org.apache.log4j.PropertyConfigurator;
import org.argouml.application.api.Argo;
import org.argouml.application.api.CommandLineInterface;
import org.argouml.application.security.ArgoAwtExceptionHandler;
import org.argouml.cognitive.AbstractCognitiveTranslator;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.checklist.ui.InitCheckListUI;
import org.argouml.cognitive.ui.InitCognitiveUI;
import org.argouml.cognitive.ui.ToDoPane;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.moduleloader.InitModuleLoader;
import org.argouml.moduleloader.ModuleLoader2;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.notation.ui.InitNotationUI;
import org.argouml.persistence.PersistenceManager;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.SplashScreen;
import org.argouml.ui.cmd.ActionExit;
import org.argouml.ui.cmd.InitUiCmdSubsystem;
import org.argouml.ui.cmd.PrintManager;
import org.argouml.uml.diagram.static_structure.ui.InitClassDiagram;
import org.argouml.uml.diagram.ui.InitDiagramAppearanceUI;
import org.argouml.uml.ui.InitUmlUI;
import org.argouml.util.ArgoFrame;
import org.argouml.util.JavaRuntimeUtility;
import org.argouml.util.logging.AwtExceptionHandler;
import org.argouml.util.logging.SimpleTimer;
import org.tigris.gef.util.Util;


public class Main {
	private static final String DEFAULT_MODEL_IMPLEMENTATION = "org.argouml.model.mdr.MDRModelImplementation";
	private static List<Runnable>postLoadActions = new ArrayList<Runnable>();
	private static boolean doSplash = true;
	private static boolean reloadRecent = false;
	private static boolean batch = false;
	private static List<String>commands;
	private static String projectName = null;
	private static String theTheme;
	public static void main(String[]args) {
		try {
			SimpleTimer st = new SimpleTimer();
			st.mark("begin");
			initPreinitialize();
			st.mark("arguments");
			parseCommandLine(args);
			AwtExceptionHandler.registerExceptionHandler();
			st.mark("create splash");
			SplashScreen splash = null;
			if (!batch) {
				st.mark("initialize laf");
				LookAndFeelMgr.getInstance().initializeLookAndFeel();
				if (theTheme != null) {
					LookAndFeelMgr.getInstance().setCurrentTheme(theTheme);
				}
				if (doSplash) {
					splash = initializeSplash();
				}
			}
			ProjectBrowser pb = initializeSubsystems(st,splash);
			st.mark("perform commands");
			if (batch) {
				performCommandsInternal(commands);
				commands = null;
				System.out.println("Exiting because we are running in batch.");
				new ActionExit().doCommand(null);
				return;
			}
			if (reloadRecent&&projectName == null) {
				projectName = getMostRecentProject();
			}
			URL urlToOpen = null;
			if (projectName != null) {
				projectName = PersistenceManager.getInstance().fixExtension(projectName);
				urlToOpen = projectUrl(projectName,urlToOpen);
			}
			openProject(st,splash,pb,urlToOpen);
			st.mark("perspectives");
			if (splash != null) {
				splash.getStatusBar().showProgress(75);
			}
			st.mark("open window");
			updateProgress(splash,95,"statusmsg.bar.open-project-browser");
			ArgoFrame.getInstance().setVisible(true);
			st.mark("close splash");
			if (splash != null) {
				splash.setVisible(false);
				splash.dispose();
				splash = null;
			}
			performCommands(commands);
			commands = null;
			st.mark("start critics");
			Runnable startCritics = new StartCritics();
			Main.addPostLoadAction(startCritics);
			st.mark("start loading modules");
			Runnable moduleLoader = new LoadModules();
			Main.addPostLoadAction(moduleLoader);
			PostLoad pl = new PostLoad(postLoadActions);
			Thread postLoadThead = new Thread(pl);
			postLoadThead.start();
			st = null;
			ArgoFrame.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			ToolTipManager.sharedInstance().setDismissDelay(50000000);
		}catch (Throwable t) {
			System.out.println("Fatal error on startup.  " + "ArgoUML failed to start.");
			t.printStackTrace();
			System.exit(1);
		}
	}
	private static void initPreinitialize() {
		checkJVMVersion();
		checkHostsFile();
		Configuration.load();
		String directory = Argo.getDirectory();
		org.tigris.gef.base.Globals.setLastDirectory(directory);
		initVersion();
		initTranslator();
		setSystemProperties();
	}
	private static void initTranslator() {
		Translator.init(Configuration.getString(Argo.KEY_LOCALE));
	}
	private static void setSystemProperties() {
		System.setProperty("gef.imageLocation","/org/argouml/Images");
		System.setProperty("apple.laf.useScreenMenuBar","true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name","ArgoUML");
	}
	private static void parseCommandLine(String[]args) {
		doSplash = Configuration.getBoolean(Argo.KEY_SPLASH,true);
		reloadRecent = Configuration.getBoolean(Argo.KEY_RELOAD_RECENT_PROJECT,false);
		commands = new ArrayList<String>();
		theTheme = null;
		for (int i = 0;i < args.;i++) {
			if (args[i].startsWith("-")) {
				String themeName = LookAndFeelMgr.getInstance().getThemeClassNameFromArg(args[i]);
				if (themeName != null) {
					theTheme = themeName;
				}else if (args[i].equalsIgnoreCase("-help")||args[i].equalsIgnoreCase("-h")||args[i].equalsIgnoreCase("--help")||args[i].equalsIgnoreCase("/?")) {
					printUsage();
					System.exit(0);
				}else if (args[i].equalsIgnoreCase("-nosplash")) {
					doSplash = false;
				}else if (args[i].equalsIgnoreCase("-norecentfile")) {
					reloadRecent = false;
				}else if (args[i].equalsIgnoreCase("-command")&&i + 1 < args.) {
					commands.add(args[i + 1]);
					i++;
				}else if (args[i].equalsIgnoreCase("-locale")&&i + 1 < args.) {
					Translator.setLocale(args[i + 1]);
					i++;
				}else if (args[i].equalsIgnoreCase("-batch")) {
					batch = true;
				}else if (args[i].equalsIgnoreCase("-open")&&i + 1 < args.) {
					projectName = args[++i];
				}else if (args[i].equalsIgnoreCase("-print")&&i + 1 < args.) {
					String projectToBePrinted = PersistenceManager.getInstance().fixExtension(args[++i]);
					URL urlToBePrinted = projectUrl(projectToBePrinted,null);
					ProjectBrowser.getInstance().loadProject(new File(urlToBePrinted.getFile()),true,null);
					PrintManager.getInstance().print();
					System.exit(0);
				}else {
					System.err.println("Ignoring unknown/incomplete option \'" + args[i] + "\'");
				}
			}else {
				if (projectName == null) {
					System.out.println("Setting projectName to \'" + args[i] + "\'");
					projectName = args[i];
				}
			}
		}
	}
	private static ProjectBrowser initializeSubsystems(SimpleTimer st,SplashScreen splash) {
		ProjectBrowser pb = null;
		st.mark("initialize model subsystem");
		initModel();
		updateProgress(splash,5,"statusmsg.bar.model-subsystem");
		st.mark("initialize the profile subsystem");
		new InitProfileSubsystem().init();
		st.mark("initialize gui");
		pb = initializeGUI(splash);
		st.mark("initialize subsystems");
		SubsystemUtility.initSubsystem(new InitUiCmdSubsystem());
		SubsystemUtility.initSubsystem(new InitNotationUI());
		SubsystemUtility.initSubsystem(new InitNotation());
		SubsystemUtility.initSubsystem(new InitNotationUml());
		SubsystemUtility.initSubsystem(new InitNotationJava());
		SubsystemUtility.initSubsystem(new InitDiagramAppearanceUI());
		SubsystemUtility.initSubsystem(new InitClassDiagram());
		SubsystemUtility.initSubsystem(new InitUmlUI());
		SubsystemUtility.initSubsystem(new InitCheckListUI());
		SubsystemUtility.initSubsystem(new InitCognitiveUI());
		st.mark("initialize modules");
		SubsystemUtility.initSubsystem(new InitModuleLoader());
		return pb;
	}
	private static void initModel() {
		String className = System.getProperty("argouml.model.implementation",DEFAULT_MODEL_IMPLEMENTATION);
		Throwable ret = Model.initialise(className);
		if (ret != null) {
			System.err.println(className + " is not a working Model implementation.");
			System.exit(1);
		}
	}
	private static void openProject(SimpleTimer st,SplashScreen splash,ProjectBrowser pb,URL urlToOpen) {
		if (splash != null) {
			splash.getStatusBar().showProgress(40);
		}
		st.mark("open project");
		Designer.disableCritiquing();
		Designer.clearCritiquing();
		boolean projectLoaded = false;
		if (urlToOpen != null) {
			if (splash != null) {
				Object[]msgArgs =  {projectName};
				splash.getStatusBar().showStatus(Translator.messageFormat("statusmsg.bar.readingproject",msgArgs));
			}
			String filename = urlToOpen.getFile();
			File file = new File(filename);
			System.err.println("The url of the file to open is " + urlToOpen);
			System.err.println("The filename is " + filename);
			System.err.println("The file is " + file);
			System.err.println("File.exists = " + file.exists());
			projectLoaded = pb.loadProject(file,true,null);
		}else {
			if (splash != null) {
				splash.getStatusBar().showStatus(Translator.localize("statusmsg.bar.defaultproject"));
			}
		}
		if (!projectLoaded) {
			ProjectManager.getManager().setCurrentProject(ProjectManager.getManager().getCurrentProject());
			ProjectManager.getManager().setSaveEnabled(false);
		}
		st.mark("set project");
		Designer.enableCritiquing();
	}
	private static String getMostRecentProject() {
		String s = Configuration.getString(Argo.KEY_MOST_RECENT_PROJECT_FILE,"");
		if (!("".equals(s))) {
			File file = new File(s);
			if (file.exists()) {
				return s;
			}
		}
		return null;
	}
	private static void updateProgress(SplashScreen splash,int percent,String message) {
		if (splash != null) {
			splash.getStatusBar().showStatus(Translator.localize(message));
			splash.getStatusBar().showProgress(percent);
		}
	}
	private static URL projectUrl(final String theProjectName,URL urlToOpen) {
		File projectFile = new File(theProjectName);
		if (!projectFile.exists()) {
			System.err.println("Project file \'" + projectFile + "\' does not exist.");
		}else {
			try {
				urlToOpen = Util.fileToURL(projectFile);
			}catch (Exception e) {
			}
		}
		return urlToOpen;
	}
	private static void printUsage() {
		System.err.println("Usage: [options] [project-file]");
		System.err.println("Options include: ");
		System.err.println("  -help           display this information");
		LookAndFeelMgr.getInstance().printThemeArgs();
		System.err.println("  -nosplash       don\'t display logo at startup");
		System.err.println("  -norecentfile   don\'t reload last saved file");
		System.err.println("  -command <arg>  command to perform on startup");
		System.err.println("  -batch          don\'t start GUI");
		System.err.println("  -locale <arg>   set the locale (e.g. \'en_GB\')");
		System.err.println("");
		System.err.println("You can also set java settings which influence " + "the behaviour of ArgoUML:");
		System.err.println("  -Xms250M -Xmx500M  [makes ArgoUML reserve " + "more memory for large projects]");
		System.err.println("\n\n");
	}
	private static void checkJVMVersion() {
		if (!JavaRuntimeUtility.isJreSupported()) {
			System.err.println("You are using Java " + JavaRuntimeUtility.getJreVersion() + ", Please use Java 5 (aka 1.5) or later" + " with ArgoUML");
			System.exit(0);
		}
	}
	private static void checkHostsFile() {
		try {
			InetAddress.getLocalHost();
		}catch (UnknownHostException e) {
			System.err.println("ERROR: unable to get localhost information.");
			e.printStackTrace(System.err);
			System.err.println("On Unix systems this usually indicates that" + "your /etc/hosts file is incorrectly setup.");
			System.err.println("Stopping execution of ArgoUML.");
			System.exit(0);
		}
	}
	public static void addPostLoadAction(Runnable r) {
		postLoadActions.add(r);
	}
	public static void performCommands(List<String>list) {
		performCommandsInternal(list);
	}
	private static void performCommandsInternal(List<String>list) {
		for (String commandString:list) {
			int pos = commandString.indexOf('=');
			String commandName;
			String commandArgument;
			if (pos == -1) {
				commandName = commandString;
				commandArgument = null;
			}else {
				commandName = commandString.substring(0,pos);
				commandArgument = commandString.substring(pos + 1);
			}
			Class c;
			try {
				c = Class.forName(commandName);
			}catch (ClassNotFoundException e) {
				System.out.println("Cannot find the command: " + commandName);
				continue;
			}
			Object o = null;
			try {
				o = c.newInstance();
			}catch (InstantiationException e) {
				System.out.println(commandName + " could not be instantiated - skipping" + " (InstantiationException)");
				continue;
			}catch (IllegalAccessException e) {
				System.out.println(commandName + " could not be instantiated - skipping" + " (IllegalAccessException)");
				continue;
			}
			if (o == null||!(o instanceof CommandLineInterface)) {
				System.out.println(commandName + " is not a command - skipping.");
				continue;
			}
			CommandLineInterface clio = (CommandLineInterface) o;
			System.out.println("Performing command " + commandName + "( " + (commandArgument == null?"":commandArgument) + " )");
			boolean result = clio.doCommand(commandArgument);
			if (!result) {
				System.out.println("There was an error executing " + "the command " + commandName + "( " + (commandArgument == null?"":commandArgument) + " )");
				System.out.println("Aborting the rest of the commands.");
				return;
			}
		}
	}
	static {
	File argoDir = new File(System.getProperty("user.home") + File.separator + ".argouml");
	if (!argoDir.exists()) {
		argoDir.mkdir();
	}
}
	static {
	System.setProperty("sun.awt.exception.handler",ArgoAwtExceptionHandler.class.getName());
}
	private static SplashScreen initializeSplash() {
		SplashScreen splash = new SplashScreen();
		splash.setVisible(true);
		if (!EventQueue.isDispatchThread()&&Runtime.getRuntime().availableProcessors() == 1) {
			synchronized (splash) {
				while (!splash.isPaintCalled()) {
					try {
						splash.wait();
					}catch (InterruptedException e) {
					}
				}
			}
		}
		return splash;
	}
	private static ProjectBrowser initializeGUI(SplashScreen splash) {
		JPanel todoPanel;
		todoPanel = new ToDoPane(splash);
		ProjectBrowser pb = ProjectBrowser.makeInstance(splash,true,todoPanel);
		JOptionPane.setRootFrame(pb);
		pb.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Rectangle scrSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int configFrameWidth = Configuration.getInteger(Argo.KEY_SCREEN_WIDTH,scrSize.width);
		int w = Math.min(configFrameWidth,scrSize.width);
		if (w == 0) {
			w = 600;
		}
		int configFrameHeight = Configuration.getInteger(Argo.KEY_SCREEN_HEIGHT,scrSize.height);
		int h = Math.min(configFrameHeight,scrSize.height);
		if (h == 0) {
			h = 400;
		}
		int x = Configuration.getInteger(Argo.KEY_SCREEN_LEFT_X,0);
		int y = Configuration.getInteger(Argo.KEY_SCREEN_TOP_Y,0);
		pb.setLocation(x,y);
		pb.setSize(w,h);
		pb.setExtendedState(Configuration.getBoolean(Argo.KEY_SCREEN_MAXIMIZED,false)?Frame.MAXIMIZED_BOTH:Frame.NORMAL);
		UIManager.put("Button.focusInputMap",new UIDefaults.LazyInputMap(new Object[] {"ENTER","pressed","released ENTER","released","SPACE","pressed","released SPACE","released"}));
		return pb;
	}
	public static void initVersion() {
		ArgoVersion.init();
	}
}

class PostLoad implements Runnable {
	private List<Runnable>postLoadActions;
	public PostLoad(List<Runnable>actions) {
		postLoadActions = actions;
	}
	public void run() {
		try {
			Thread.sleep(1000);
		}catch (Exception ex) {
		}
		for (Runnable r:postLoadActions) {
			r.run();
			try {
				Thread.sleep(100);
			}catch (Exception ex) {
			}
		}
	}
}

class LoadModules implements Runnable {
	private static final String[]OPTIONAL_INTERNAL_MODULES =  {"org.argouml.dev.DeveloperModule"};
	private void huntForInternalModules() {
		for (String module:OPTIONAL_INTERNAL_MODULES) {
			try {
				ModuleLoader2.addClass(module);
			}catch (ClassNotFoundException e) {
			}
		}
	}
	public void run() {
		huntForInternalModules();
	}
}



