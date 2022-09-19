package org.argouml.model;

import java.util.Collection;


public interface ActivityGraphsFactory extends Factory {
	Object createActionState();
	Object createActivityGraph();
	Object createCallState();
	Object createClassifierInState();
	Object createObjectFlowState();
	Object createPartition();
	Object createSubactivityState();
	Object buildActivityGraph(Object theContext);
	Object buildObjectFlowState(Object compositeState);
	Object buildClassifierInState(Object classifier,Collection state);
}



