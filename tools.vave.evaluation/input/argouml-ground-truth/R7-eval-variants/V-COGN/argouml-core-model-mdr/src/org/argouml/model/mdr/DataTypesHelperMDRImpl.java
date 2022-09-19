package org.argouml.model.mdr;

import java.util.Iterator;
import javax.jmi.reflect.InvalidObjectException;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.InvalidElementException;
import org.omg.uml.foundation.datatypes.Expression;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.PseudostateKind;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;


class DataTypesHelperMDRImpl implements DataTypesHelper {
	private MDRModelImplementation modelImpl;
	public DataTypesHelperMDRImpl(MDRModelImplementation implementation) {
		modelImpl = implementation;
	}
	public boolean equalsINITIALKind(Object kind) {
		if (!(kind instanceof PseudostateKind)) {
			throw new IllegalArgumentException();
		}
		try {
			return PseudostateKindEnum.PK_INITIAL.equals(kind);
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	public boolean equalsDeepHistoryKind(Object kind) {
		if (!(kind instanceof PseudostateKind)) {
			throw new IllegalArgumentException();
		}
		try {
			return PseudostateKindEnum.PK_DEEP_HISTORY.equals(kind);
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	public boolean equalsShallowHistoryKind(Object kind) {
		if (!(kind instanceof PseudostateKind)) {
			throw new IllegalArgumentException();
		}
		try {
			return PseudostateKindEnum.PK_SHALLOW_HISTORY.equals(kind);
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	public boolean equalsFORKKind(Object kind) {
		if (!(kind instanceof PseudostateKind)) {
			throw new IllegalArgumentException();
		}
		try {
			return PseudostateKindEnum.PK_FORK.equals(kind);
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	public boolean equalsJOINKind(Object kind) {
		if (!(kind instanceof PseudostateKind)) {
			throw new IllegalArgumentException();
		}
		try {
			return PseudostateKindEnum.PK_JOIN.equals(kind);
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	public boolean equalsCHOICEKind(Object kind) {
		if (!(kind instanceof PseudostateKind)) {
			throw new IllegalArgumentException();
		}
		try {
			return PseudostateKindEnum.PK_CHOICE.equals(kind);
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	public boolean equalsJUNCTIONKind(Object kind) {
		if (!(kind instanceof PseudostateKind)) {
			throw new IllegalArgumentException();
		}
		try {
			return PseudostateKindEnum.PK_JUNCTION.equals(kind);
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	public String multiplicityToString(Object multiplicity) {
		if (!(multiplicity instanceof Multiplicity)) {
			throw new IllegalArgumentException("Unrecognized object: " + multiplicity);
		}
		try {
			String rc = "";
			Iterator i = ((Multiplicity) multiplicity).getRange().iterator();
			boolean first = true;
			while (i.hasNext()) {
				if (first) {
					first = false;
				}else {
					rc += ",";
				}
				rc += multiplicityRangeToString((MultiplicityRange) i.next());
			}
			return rc;
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
	}
	private String multiplicityRangeToString(MultiplicityRange range) {
		if (range.getLower() == range.getUpper()) {
			return DataTypesFactoryMDRImpl.boundToString(range.getLower());
		}else {
			return DataTypesFactoryMDRImpl.boundToString(range.getLower()) + ".." + DataTypesFactoryMDRImpl.boundToString(range.getUpper());
		}
	}
	public Object setBody(Object handle,String body) {
		if (handle instanceof Expression) {
			((Expression) handle).setBody(body);
			return handle;
		}
		throw new IllegalArgumentException("handle: " + handle + " body:" + body);
	}
	public String getBody(Object handle) {
		try {
			if (handle instanceof Expression) {
				return((Expression) handle).getBody();
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		throw new IllegalArgumentException("handle: " + handle);
	}
	public Object setLanguage(Object handle,String language) {
		if (handle instanceof Expression) {
			((Expression) handle).setLanguage(language);
			return handle;
		}
		throw new IllegalArgumentException("handle: " + handle + " language: " + language);
	}
	public String getLanguage(Object handle) {
		try {
			if (handle instanceof Expression) {
				return((Expression) handle).getLanguage();
			}
		}catch (InvalidObjectException e) {
			throw new InvalidElementException(e);
		}
		throw new IllegalArgumentException("handle: " + handle);
	}
}



