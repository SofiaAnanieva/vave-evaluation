package org.argouml.model.euml;

import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.argouml.model.euml.EUMLModelImplementation;
import org.argouml.model.euml.ModelEventPumpEUMLImpl;
import org.argouml.model.euml.UMLUtil;
import java.lang.StringBuilder;


public class ChangeCommand extends org.eclipse.uml2.common.edit.command.ChangeCommand {
	private Object objects[];
	private EUMLModelImplementation modelImpl;
	public ChangeCommand(EUMLModelImplementation modelImplementation,Runnable runnable,String label) {
		super(modelImplementation.getEditingDomain(),runnable,label);
		modelImpl = modelImplementation;
	}
	public ChangeCommand(EUMLModelImplementation modelImplementation,Runnable runnable,String label,Object...objects) {
		super(modelImplementation.getEditingDomain(),runnable,label);
		if (!isValid(label,objects)) {
			throw new IllegalArgumentException("The label is not compatible with the objects");
		}
		this.objects = objects;
		modelImpl = modelImplementation;
	}
	public void setObjects(Object...objects) {
		if (!isValid(label,objects)) {
			throw new IllegalArgumentException("The label is not compatible with the objects");
		}
		this.objects = objects;
		modelImpl.getModelEventPump().notifyChanged(new NotificationImpl(ModelEventPumpEUMLImpl.COMMAND_STACK_UPDATE,false,false));
	}
	@Override public String getLabel() {
		if (objects == null) {
			return label;
		}
		StringBuilder sb = new StringBuilder();
		int j = 0;
		for (int i = 0;i < label.length();i++) {
			if (label.charAt(i) == '#') {
				sb.append(UMLUtil.toString(objects[j]));
				j++;
			}else {
				sb.append(label.charAt(i));
			}
		}
		return sb.toString();
	}
	private boolean isValid(String label,Object objects[]) {
		if (objects == null) {
			return true;
		}
		int i = 0;
		for (int j = 0;j < label.length();j++) {
			if (label.charAt(j) == '#') {
				i++;
			}
		}
		return i == objects.;
	}
}



