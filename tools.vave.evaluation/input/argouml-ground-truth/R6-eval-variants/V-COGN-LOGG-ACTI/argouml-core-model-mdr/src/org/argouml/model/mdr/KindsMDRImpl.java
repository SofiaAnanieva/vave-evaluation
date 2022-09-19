package org.argouml.model.mdr;

import org.argouml.model.AggregationKind;
import org.argouml.model.ChangeableKind;
import org.argouml.model.ConcurrencyKind;
import org.argouml.model.DirectionKind;
import org.argouml.model.NotImplementedException;
import org.argouml.model.OrderingKind;
import org.argouml.model.PseudostateKind;
import org.argouml.model.ScopeKind;
import org.argouml.model.VisibilityKind;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.CallConcurrencyKindEnum;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;


@SuppressWarnings("deprecation")class KindsMDRImpl implements ChangeableKind,AggregationKind,PseudostateKind,ScopeKind,ConcurrencyKind,DirectionKind,OrderingKind,VisibilityKind {
	private MDRModelImplementation modelImpl;
	KindsMDRImpl(MDRModelImplementation mi) {
			modelImpl = mi;
		}
	public Object getAddOnly() {
		return ChangeableKindEnum.CK_ADD_ONLY;
	}
	public Object getAggregate() {
		return AggregationKindEnum.AK_AGGREGATE;
	}
	public Object getChoice() {
		return PseudostateKindEnum.PK_CHOICE;
	}
	public Object getChangeable() {
		return ChangeableKindEnum.CK_CHANGEABLE;
	}
	public Object getClassifier() {
		return ScopeKindEnum.SK_CLASSIFIER;
	}
	public Object getComposite() {
		return AggregationKindEnum.AK_COMPOSITE;
	}
	public Object getConcurrent() {
		return CallConcurrencyKindEnum.CCK_CONCURRENT;
	}
	public Object getDeepHistory() {
		return PseudostateKindEnum.PK_DEEP_HISTORY;
	}
	public Object getFork() {
		return PseudostateKindEnum.PK_FORK;
	}
	public Object getFrozen() {
		return ChangeableKindEnum.CK_FROZEN;
	}
	public Object getGuarded() {
		return CallConcurrencyKindEnum.CCK_GUARDED;
	}
	public Object getInParameter() {
		return ParameterDirectionKindEnum.PDK_IN;
	}
	public Object getInitial() {
		return PseudostateKindEnum.PK_INITIAL;
	}
	public Object getInOutParameter() {
		return ParameterDirectionKindEnum.PDK_INOUT;
	}
	public Object getInstance() {
		return ScopeKindEnum.SK_INSTANCE;
	}
	public Object getJoin() {
		return PseudostateKindEnum.PK_JOIN;
	}
	public Object getJunction() {
		return PseudostateKindEnum.PK_JUNCTION;
	}
	public Object getNone() {
		return AggregationKindEnum.AK_NONE;
	}
	public Object getOrdered() {
		return OrderingKindEnum.OK_ORDERED;
	}
	public Object getOutParameter() {
		return ParameterDirectionKindEnum.PDK_OUT;
	}
	public Object getPackage() {
		return VisibilityKindEnum.VK_PACKAGE;
	}
	public Object getPrivate() {
		return VisibilityKindEnum.VK_PRIVATE;
	}
	public Object getProtected() {
		return VisibilityKindEnum.VK_PROTECTED;
	}
	public Object getPublic() {
		return VisibilityKindEnum.VK_PUBLIC;
	}
	public Object getReturnParameter() {
		return ParameterDirectionKindEnum.PDK_RETURN;
	}
	public Object getSequential() {
		return CallConcurrencyKindEnum.CCK_SEQUENTIAL;
	}
	public Object getShallowHistory() {
		return PseudostateKindEnum.PK_SHALLOW_HISTORY;
	}
	public Object getUnordered() {
		return OrderingKindEnum.OK_UNORDERED;
	}
	public Object getEntryPoint() {
		throw new NotImplementedException();
	}
	public Object getExitPoint() {
		throw new NotImplementedException();
	}
	public Object getTerminate() {
		throw new NotImplementedException();
	}
}



