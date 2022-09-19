package org.argouml.uml.diagram.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoDiagramAppearanceEvent;
import org.argouml.application.events.ArgoDiagramAppearanceEventListener;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Highlightable;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.ui.ActionGoToCritique;
import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.kernel.Project;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.DiElement;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.NotationSettings;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.Clarifier;
import org.argouml.ui.ProjectActions;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.StereotypeUtility;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramAppearance;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.PathContainer;
import org.argouml.uml.diagram.DiagramSettings.StereotypeStyle;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.util.IItemUID;
import org.argouml.util.ItemUID;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.MutableGraphSupport;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigImage;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.argouml.uml.diagram.ui.StereotypeStyled;
import org.argouml.uml.diagram.ui.Clarifiable;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigSingleLineText;
import org.argouml.uml.diagram.ui.ArgoFigText;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.profile.FigNodeStrategy;
import org.argouml.uml.diagram.ui.CompartmentFigText;


public abstract class FigNodeModelElement extends FigNode implements VetoableChangeListener,DelayedVChangeListener,MouseListener,KeyListener,PropertyChangeListener,PathContainer,ArgoDiagramAppearanceEventListener,ArgoNotationEventListener,Highlightable,IItemUID,Clarifiable,ArgoFig,StereotypeStyled {
	private static final Logger LOG = Logger.getLogger(FigNodeModelElement.class);
	protected static final int WIDTH = 64;
	protected static final int NAME_FIG_HEIGHT = 21;
	protected static final int NAME_V_PADDING = 2;
	private DiElement diElement;
	private NotationProvider notationProviderName;
	private HashMap<String,Object>npArguments;
	protected boolean invisibleAllowed = false;
	private boolean checkSize = true;
	private static int popupAddOffset;
	protected static final int ROOT = 1;
	protected static final int ABSTRACT = 2;
	protected static final int LEAF = 4;
	protected static final int ACTIVE = 8;
	private Fig bigPort;
	private FigText nameFig;
	private FigStereotypesGroup stereotypeFig;
	private FigProfileIcon stereotypeFigProfileIcon;
	private List<Fig>floatingStereotypes = new ArrayList<Fig>();
	private Object stereotypeStyle = null;
	private static final int ICON_WIDTH = 16;
	private FigText originalNameFig;
	private Vector<Fig>enclosedFigs = new Vector<Fig>();
	private Fig encloser;
	private boolean readyToEdit = true;
	private boolean suppressCalcBounds;
	private static boolean showBoldName;
	private ItemUID itemUid;
	private boolean removeFromDiagram = true;
	private boolean editable = true;
	private Set<Object[]>listeners = new HashSet<Object[]>();
	private DiagramSettings settings;
	private NotationSettings notationSettings;
	@Deprecated protected FigNodeModelElement() {
	}
	private void constructFigs() {
	}
	@Deprecated protected FigNodeModelElement(Object element,int x,int y) {
	}
	protected FigNodeModelElement(Object element,Rectangle bounds,DiagramSettings renderSettings) {
		super();
	}
	protected FigStereotypesGroup createStereotypeFig() {
		return null;
	}
	@Override public void setLayer(Layer lay) {
	}
	@Override public Object clone() {
		return null;
	}
	public String placeString() {
		return"";
	}
	public void setItemUID(ItemUID id) {
	}
	public ItemUID getItemUID() {
		return null;
	}
	protected FigText getNameFig() {
		return null;
	}
	public Rectangle getNameBounds() {
		return null;
	}
	protected void setNameFig(FigText fig) {
	}
	public String getName() {
		return"";
	}
	public void setName(String n) {
	}
	@Override public Vector getPopUpActions(MouseEvent me) {
		return null;
	}
	protected ArgoJMenu buildShowPopUp() {
		return null;
	}
	protected Object buildVisibilityPopUp() {
		return null;
	}
	protected Object buildModifierPopUp(int items) {
		return null;
	}
	@Override public Fig getEnclosingFig() {
		return null;
	}
	@Override public void setEnclosingFig(Fig newEncloser) {
	}
	protected void moveIntoComponent(Fig newEncloser) {
	}
	public void addEnclosedFig(Fig fig) {
	}
	public void removeEnclosedFig(Fig fig) {
	}
	@Override public Vector<Fig>getEnclosedFigs() {
		return null;
	}
	@Override public Selection makeSelection() {
		return null;
	}
	public void paintClarifiers(Graphics g) {
	}
	protected ToDoItem hitClarifier(int x,int y) {
		return null;
	}
	@Override public String getTipString(MouseEvent me) {
		return"";
	}
	public void vetoableChange(PropertyChangeEvent pce) {
	}
	public void delayedVetoableChange(PropertyChangeEvent pce) {
	}
	protected void updateBounds() {
	}
	public void propertyChange(final PropertyChangeEvent pve) {
	}
	private String formatEvent(PropertyChangeEvent event) {
		return"";
	}
	private boolean isReadOnly() {
		return true;
	}
	private void stereotypeChanged(final UmlChangeEvent event) {
	}
	protected void textEditStarted(FigText ft) {
	}
	protected void showHelp(String s) {
	}
	protected void textEdited(FigText ft)throws PropertyVetoException {
	}
	@Override public void mouseClicked(MouseEvent me) {
	}
	public void keyPressed(KeyEvent ke) {
	}
	public void keyReleased(KeyEvent ke) {
	}
	public void keyTyped(KeyEvent ke) {
	}
	protected void modelChanged(PropertyChangeEvent event) {
	}
	protected void updateLayout(UmlChangeEvent event) {
	}
	protected void createContainedModelElement(FigGroup fg,InputEvent me) {
	}
	protected boolean isPartlyOwner(Object o) {
		return false;
	}
	protected boolean isPartlyOwner(Fig fig,Object o) {
		return false;
	}
	@Override public void deleteFromModel() {
	}
	public void setOwner(Object owner) {
	}
	protected void initNotationProviders(Object own) {
	}
	protected int getNotationProviderType() {
		return 0;
	}
	protected void updateStereotypeText() {
	}
	protected void updateNameText() {
	}
	public boolean isPathVisible() {
		return false;
	}
	public void setPathVisible(boolean visible) {
	}
	protected void determineDefaultPathVisible() {
	}
	@Deprecated@Override public String classNameAndBounds() {
		return"";
	}
	protected void updateListeners(Object oldOwner,Object newOwner) {
	}
	@Deprecated public void notationChanged(ArgoNotationEvent event) {
	}
	@Deprecated public void notationAdded(ArgoNotationEvent event) {
	}
	@Deprecated public void notationRemoved(ArgoNotationEvent event) {
	}
	@Deprecated public void notationProviderAdded(ArgoNotationEvent event) {
	}
	@Deprecated public void notationProviderRemoved(ArgoNotationEvent event) {
	}
	public void renderingChanged() {
	}
	protected void updateStereotypeIcon() {
	}
	public void calcBounds() {
	}
	public void enableSizeChecking(boolean flag) {
	}
	@Override public boolean hit(Rectangle r) {
		return false;
	}
	@Override public final void removeFromDiagram() {
	}
	protected Fig getRemoveDelegate() {
		return null;
	}
	protected void removeFromDiagramImpl() {
	}
	protected FigStereotypesGroup getStereotypeFig() {
		return null;
	}
	protected void setBigPort(Fig bp) {
	}
	public Fig getBigPort() {
		return null;
	}
	protected boolean isCheckSize() {
		return false;
	}
	public boolean isDragConnectable() {
		return false;
	}
	protected void setEncloser(Fig e) {
	}
	protected Fig getEncloser() {
		return null;
	}
	protected boolean isReadyToEdit() {
		return false;
	}
	protected void setReadyToEdit(boolean v) {
	}
	protected void setSuppressCalcBounds(boolean scb) {
	}
	public void setVisible(boolean visible) {
	}
	public void displace(int xInc,int yInc) {
	}
	protected void allowRemoveFromDiagram(boolean allowed) {
	}
	public void setDiElement(DiElement element) {
	}
	public DiElement getDiElement() {
		return null;
	}
	protected static int getPopupAddOffset() {
		return 0;
	}
	public boolean isEditable() {
		return false;
	}
	protected void setEditable(boolean canEdit) {
	}
	protected void addElementListener(Object element) {
	}
	protected void addElementListener(Object element,String property) {
	}
	protected void addElementListener(Object element,String[]property) {
	}
	protected void removeElementListener(Object element) {
	}
	protected void removeAllElementListeners() {
	}
	private void removeElementListeners(Set<Object[]>listenerSet) {
	}
	private void addElementListeners(Set<Object[]>listenerSet) {
	}
	protected void updateElementListeners(Set<Object[]>listenerSet) {
	}
	@Deprecated protected HashMap<String,Object>getNotationArguments() {
		return null;
	}
	@Deprecated protected void putNotationArgument(String key,Object value) {
	}
	@SuppressWarnings("deprecation")@Deprecated public void setProject(Project project) {
	}
	@Deprecated public Project getProject() {
		return null;
	}
	protected boolean isSingleTarget() {
		return false;
	}
	public int getStereotypeView() {
		return 0;
	}
	public Object getStereotypeStyle() {
		return null;
	}
	private int getPracticalView() {
		return 0;
	}
	public int getStereotypeCount() {
		return 0;
	}
	public void setStereotypeView(int s) {
	}
	public void setStereotypeStyle(Object style) {
	}
	@Override protected void setBoundsImpl(final int x,final int y,final int w,final int h) {
	}
	private void updateSmallIcons(int wid) {
	}
	protected void setStandardBounds(final int x,final int y,final int w,final int h) {
	}
	public void diagramFontChanged(ArgoDiagramAppearanceEvent e) {
	}
	protected void updateFont() {
	}
	protected int getNameFigFontStyle() {
		return 0;
	}
	private void deepUpdateFont(FigGroup fg) {
	}
	public DiagramSettings getSettings() {
		return null;
	}
	public void setSettings(DiagramSettings renderSettings) {
	}
	protected NotationSettings getNotationSettings() {
		return null;
	}
	public void setLineWidth(int w) {
	}
	class SelectionDefaultClarifiers extends SelectionNodeClarifiers2 {
		private SelectionDefaultClarifiers(Fig f) {
			super(f);
		}
		@Override protected Icon[]getIcons() {
			return null;
		}
		@Override protected String getInstructions(int index) {
			return null;
		}
		@Override protected Object getNewNodeType(int index) {
			return null;
		}
		@Override protected Object getNewEdgeType(int index) {
			return null;
		}
		@Override protected boolean isReverseEdge(int index) {
			return false;
		}
	}
}



