package org.argouml.model.mdr;

import java.util.Map;
import javax.jmi.reflect.RefObject;
import org.netbeans.api.xmi.XMIReferenceProvider;


class XmiReferenceProviderImpl implements XMIReferenceProvider {
	private Map<String,XmiReference>mofIdToXmiId;
	private boolean topSystemIdSaved = false;
	private String topSystemId = null;
	XmiReferenceProviderImpl(Map<String,XmiReference>idMap) {
			mofIdToXmiId = idMap;
		}
	public XMIReferenceProvider.XMIReference getReference(RefObject object) {
		String mofId = object.refMofId();
		XmiReference ref = mofIdToXmiId.get(mofId);
		if (!topSystemIdSaved) {
			if (ref == null) {
				topSystemId = null;
			}else {
				topSystemId = ref.getSystemId();
			}
			topSystemIdSaved = true;
		}
		if (ref == null) {
			return new XMIReferenceProvider.XMIReference(null,mofId);
		}else {
			String systemId = ref.getSystemId();
			if (topSystemId != null&&topSystemId.equals(systemId)) {
				systemId = null;
			}
			return new XMIReferenceProvider.XMIReference(systemId,ref.getXmiId());
		}
	}
}



