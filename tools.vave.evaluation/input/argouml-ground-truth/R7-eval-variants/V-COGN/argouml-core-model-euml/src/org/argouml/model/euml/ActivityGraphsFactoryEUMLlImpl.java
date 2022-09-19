package org.argouml.model.euml;

import java.util.Collection;
import org.argouml.model.AbstractModelFactory;
import org.argouml.model.ActivityGraphsFactory;
import org.argouml.model.euml.EUMLModelImplementation;


class ActivityGraphsFactoryEUMLlImpl implements ActivityGraphsFactory,AbstractModelFactory {
	private EUMLModelImplementation modelImpl;
	public ActivityGraphsFactoryEUMLlImpl(EUMLModelImplementation implementation) {
		modelImpl = implementation;
	}
	public Object buildActivityGraph(Object theContext) {
		throw new NotYetImplementedException();
	}
	public Object buildClassifierInState(Object classifier,Collection state) {
		throw new NotYetImplementedException();
	}
	public Object buildObjectFlowState(Object compositeState) {
		throw new NotYetImplementedException();
	}
	public Object createActionState() {
		throw new NotYetImplementedException();
	}
	public Object createActivityGraph() {
		throw new NotYetImplementedException();
	}
	public Object createCallState() {
		throw new NotYetImplementedException();
	}
	public Object createClassifierInState() {
		throw new NotYetImplementedException();
	}
	public Object createObjectFlowState() {
		throw new NotYetImplementedException();
	}
	public Object createPartition() {
		throw new NotYetImplementedException();
	}
	public Object createSubactivityState() {
		throw new NotYetImplementedException();
	}
}



