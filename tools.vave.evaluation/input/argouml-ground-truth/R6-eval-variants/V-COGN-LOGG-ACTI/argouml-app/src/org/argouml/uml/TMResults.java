package org.argouml.uml;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Diagram;


public class TMResults extends AbstractTableModel {
	private List rowObjects;
	private List<UMLDiagram>diagrams;
	private boolean showInDiagramColumn;
	public TMResults() {
		showInDiagramColumn = true;
	}
	public TMResults(boolean showTheInDiagramColumn) {
		showInDiagramColumn = showTheInDiagramColumn;
	}
	public void setTarget(List results,List theDiagrams) {
		rowObjects = results;
		diagrams = theDiagrams;
		fireTableStructureChanged();
	}
	public int getColumnCount() {
		return showInDiagramColumn?4:3;
	}
	public int getRowCount() {
		if (rowObjects == null) {
			return 0;
		}
		return rowObjects.size();
	}
	public String getColumnName(int c) {
		if (c == 0) {
			return Translator.localize("dialog.find.column-name.type");
		}
		if (c == 1) {
			return Translator.localize("dialog.find.column-name.name");
		}
		if (c == 2) {
			return Translator.localize(showInDiagramColumn?"dialog.find.column-name.in-diagram":"dialog.find.column-name.description");
		}
		if (c == 3) {
			return Translator.localize("dialog.find.column-name.description");
		}
		return"XXX";
	}
	public Class getColumnClass(int c) {
		return String.class;
	}
	public boolean isCellEditable(int row,int col) {
		return false;
	}
	public Object getValueAt(int row,int col) {
		if (row < 0||row >= rowObjects.size()) {
			return"bad row!";
		}
		if (col < 0||col >= (showInDiagramColumn?4:3)) {
			return"bad col!";
		}
		Object rowObj = rowObjects.get(row);
		if (rowObj instanceof Diagram) {
			Diagram d = (Diagram) rowObj;
			switch (col) {case 0:
				if (d instanceof UMLDiagram) {
					return((UMLDiagram) d).getLabelName();
				}
				return null;
			case 1:
				return d.getName();
			case 2:
				return showInDiagramColumn?Translator.localize("dialog.find.not-applicable"):countNodesAndEdges(d);
			case 3:
				return countNodesAndEdges(d);
			default:
			}
		}
		if (Model.getFacade().isAModelElement(rowObj)) {
			Diagram d = null;
			if (diagrams != null) {
				d = diagrams.get(row);
			}
			switch (col) {case 0:
				return Model.getFacade().getUMLClassName(rowObj);
			case 1:
				return Model.getFacade().getName(rowObj);
			case 2:
				return(d == null)?Translator.localize("dialog.find.not-applicable"):d.getName();
			case 3:
				return"docs";
			default:
			}
		}
		switch (col) {case 0:
			if (rowObj == null) {
				return"";
			}
			String clsName = rowObj.getClass().getName();
			int lastDot = clsName.lastIndexOf(".");
			return clsName.substring(lastDot + 1);
		case 1:
			return"";
		case 2:
			return"??";
		case 3:
			return"docs";
		default:
		}
		return"unknown!";
	}
	private Object countNodesAndEdges(Diagram d) {
		int numNodes = d.getNodes().size();
		int numEdges = d.getEdges().size();
		Object[]msgArgs =  {Integer.valueOf(numNodes),Integer.valueOf(numEdges)};
		return Translator.messageFormat("dialog.nodes-and-edges",msgArgs);
	}
	@Override public void setValueAt(Object aValue,int rowIndex,int columnIndex) {
	}
	private static final long serialVersionUID = -1444599676429024575l;
}



