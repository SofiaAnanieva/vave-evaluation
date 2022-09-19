package org.argouml.persistence;

import java.util.StringTokenizer;
import org.argouml.uml.diagram.ui.PathItemPlacement;
import org.argouml.util.IItemUID;
import org.argouml.util.ItemUID;
import org.tigris.gef.base.PathItemPlacementStrategy;
import org.tigris.gef.persistence.pgml.Container;
import org.tigris.gef.persistence.pgml.FigEdgeHandler;
import org.tigris.gef.persistence.pgml.FigGroupHandler;
import org.tigris.gef.persistence.pgml.PGMLHandler;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.argouml.persistence.PGMLStackParser;


class PrivateHandler extends org.tigris.gef.persistence.pgml.PrivateHandler {
	private Container container;
	public PrivateHandler(PGMLStackParser parser,Container cont) {
		super(parser,cont);
		container = cont;
	}
	public void gotElement(String contents)throws SAXException {
		if (container instanceof PGMLHandler) {
			Object o = getPGMLStackParser().getDiagram();
			if (o instanceof IItemUID) {
				ItemUID uid = getItemUID(contents);
				if (uid != null) {
					((IItemUID) o).setItemUID(uid);
				}
			}
			return;
		}
		if (container instanceof FigGroupHandler) {
			Object o = ((FigGroupHandler) container).getFigGroup();
			if (o instanceof IItemUID) {
				ItemUID uid = getItemUID(contents);
				if (uid != null) {
					((IItemUID) o).setItemUID(uid);
				}
			}
		}
		if (container instanceof FigEdgeHandler) {
			Object o = ((FigEdgeHandler) container).getFigEdge();
			if (o instanceof IItemUID) {
				ItemUID uid = getItemUID(contents);
				if (uid != null) {
					((IItemUID) o).setItemUID(uid);
				}
			}
		}
		super.gotElement(contents);
	}
	public void startElement(String uri,String localname,String qname,Attributes attributes)throws SAXException {
		if ("argouml:pathitem".equals(qname)&&container instanceof FigEdgeHandler) {
			String classname = attributes.getValue("classname");
			String figclassname = attributes.getValue("figclassname");
			String ownerhref = attributes.getValue("ownerhref");
			String angle = attributes.getValue("angle");
			String offset = attributes.getValue("offset");
			if (classname != null&&figclassname != null&&ownerhref != null&&angle != null&&offset != null) {
				if ("org.argouml.uml.diagram.ui.PathItemPlacement".equals(classname)) {
					PathItemPlacementStrategy pips = getPips(figclassname,ownerhref);
					if (pips != null&&classname.equals(pips.getClass().getName())) {
						if (pips instanceof PathItemPlacement) {
							PathItemPlacement pip = (PathItemPlacement) pips;
							pip.setDisplacementVector(Double.parseDouble(angle),Integer.parseInt(offset));
						}
					}
				}
			}
		}
		super.startElement(uri,localname,qname,attributes);
	}
	private PathItemPlacementStrategy getPips(String figclassname,String ownerhref) {
		if (container instanceof FigEdgeHandler) {
			FigEdge fe = ((FigEdgeHandler) container).getFigEdge();
			Object owner = getPGMLStackParser().findOwner(ownerhref);
			for (Object o:fe.getPathItemFigs()) {
				Fig f = (Fig) o;
				if (owner.equals(f.getOwner())&&figclassname.equals(f.getClass().getName())) {
					return fe.getPathItemPlacementStrategy(f);
				}
			}
		}
		return null;
	}
	private ItemUID getItemUID(String privateContents) {
		StringTokenizer st = new StringTokenizer(privateContents,"\n");
		while (st.hasMoreElements()) {
			String str = st.nextToken();
			NameVal nval = splitNameVal(str);
			if (nval != null) {
				if ("ItemUID".equals(nval.getName())&&nval.getValue().length() > 0) {
					return new ItemUID(nval.getValue());
				}
			}
		}
		return null;
	}
	static class NameVal {
	private String name;
	private String value;
	NameVal(String n,String v) {
			name = n.trim();
			value = v.trim();
		}
	String getName() {
		return name;
	}
	String getValue() {
		return value;
	}
}
	protected NameVal splitNameVal(String str) {
		NameVal rv = null;
		int lqpos,rqpos;
		int eqpos = str.indexOf('=');
		if (eqpos < 0) {
			return null;
		}
		lqpos = str.indexOf('\"',eqpos);
		rqpos = str.lastIndexOf('\"');
		if (lqpos < 0||rqpos <= lqpos) {
			return null;
		}
		rv = new NameVal(str.substring(0,eqpos),str.substring(lqpos + 1,rqpos));
		return rv;
	}
}



