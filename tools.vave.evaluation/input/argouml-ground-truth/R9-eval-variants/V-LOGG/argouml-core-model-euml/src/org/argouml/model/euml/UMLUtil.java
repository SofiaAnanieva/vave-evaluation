package org.argouml.model.euml;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.StrictCompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.CopyToClipboardCommand;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.argouml.model.euml.EUMLModelImplementation;
import org.argouml.model.euml.ModelEventPumpEUMLImpl;


public class UMLUtil extends org.eclipse.uml2.uml.util.UMLUtil {
	public static final URI DEFAULT_URI = URI.createURI("http://argouml.tigris.org/euml/resource/default_uri.xmi");
	public static EList<Property>getOwnedAttributes(Type type) {
		if (type instanceof AssociationClass) {
			return((AssociationClass) type).getOwnedAttributes();
		}else if (type instanceof Association) {
			return((Association) type).getOwnedEnds();
		}else {
			return org.eclipse.uml2.uml.util.UMLUtil.getOwnedAttributes(type);
		}
	}
	public static EList<Operation>getOwnedOperations(Type type) {
		return org.eclipse.uml2.uml.util.UMLUtil.getOwnedOperations(type);
	}
	public static Element copy(EUMLModelImplementation modelImplementation,Element source,Element destination) {
		Command copyToClipboard = CopyToClipboardCommand.create(modelImplementation.getEditingDomain(),source);
		Command pasteFromClipboard = PasteFromClipboardCommand.create(modelImplementation.getEditingDomain(),destination,null);
		StrictCompoundCommand copyCommand = new StrictCompoundCommand() {
	 {
		isPessimistic = true;
	}
};
		copyCommand.append(copyToClipboard);
		copyCommand.append(pasteFromClipboard);
		copyCommand.setLabel("Copy a tree of UML elements to a destination");
		if (copyCommand.canExecute()) {
			modelImplementation.getModelEventPump().getRootContainer().setHoldEvents(true);
			modelImplementation.getEditingDomain().getCommandStack().execute(copyCommand);
			if (modelImplementation.getEditingDomain().getCommandStack().getMostRecentCommand().getAffectedObjects().size() == 1) {
				modelImplementation.getModelEventPump().getRootContainer().setHoldEvents(false);
				return(Element) modelImplementation.getEditingDomain().getCommandStack().getMostRecentCommand().getAffectedObjects().iterator().next();
			}else {
				modelImplementation.getEditingDomain().getCommandStack().undo();
				modelImplementation.getModelEventPump().getRootContainer().clearHeldEvents();
				modelImplementation.getModelEventPump().getRootContainer().setHoldEvents(false);
			}
		}
		return null;
	}
	public static String toString(Object o) {
		if (o == null) {
			return"null";
		}
		if (!(o instanceof Element)) {
			return o.toString();
		}
		StringBuilder sb = new StringBuilder("\'");
		boolean named = false;
		if (o instanceof NamedElement&&((NamedElement) o).getName() != null&&!((NamedElement) o).getName().equals("")) {
			named = true;
			sb.append(((NamedElement) o).getName() + " [");
		}
		sb.append(((Element) o).eClass().getName());
		if (named) {
			sb.append("]");
		}
		sb.append("\'");
		return sb.toString();
	}
	static Resource getResource(EUMLModelImplementation modelImplementation,URI uri,boolean readOnly) {
		if (!"xmi".equals(uri.fileExtension())) {
			uri = uri.appendFileExtension("xmi");
		}
		Resource r = modelImplementation.getEditingDomain().getResourceSet().getResource(uri,false);
		if (r == null) {
			r = modelImplementation.getEditingDomain().getResourceSet().createResource(uri);
		}
		if (r == null) {
			throw new NullPointerException("Failed to create resource for URI " + uri);
		}
		modelImplementation.getReadOnlyMap().put(r,Boolean.valueOf(readOnly));
		return r;
	}
}



