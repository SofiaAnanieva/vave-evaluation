package org.argouml.uml.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.reveng.ImportInterface;


public class SourcePathControllerImpl implements SourcePathController {
	public File getSourcePath(Object modelElement) {
		Object tv = Model.getFacade().getTaggedValue(modelElement,ImportInterface.SOURCE_PATH_TAG);
		if (tv != null) {
			String srcPath = Model.getFacade().getValueOfTag(tv);
			if (srcPath != null) {
				return new File(srcPath);
			}
		}
		return null;
	}
	public SourcePathTableModel getSourcePathSettings() {
		return new SourcePathTableModel(this);
	}
	public void setSourcePath(SourcePathTableModel srcPaths) {
		for (int i = 0;i < srcPaths.getRowCount();i++) {
			setSourcePath(srcPaths.getModelElement(i),new File(srcPaths.getMESourcePath(i)));
		}
	}
	public void setSourcePath(Object modelElement,File sourcePath) {
		Object tv = Model.getFacade().getTaggedValue(modelElement,ImportInterface.SOURCE_PATH_TAG);
		if (tv == null) {
			Model.getExtensionMechanismsHelper().addTaggedValue(modelElement,Model.getExtensionMechanismsFactory().buildTaggedValue(ImportInterface.SOURCE_PATH_TAG,sourcePath.toString()));
		}else {
			Model.getExtensionMechanismsHelper().setValueOfTag(tv,sourcePath.toString());
		}
	}
	public String toString() {
		return"ArgoUML default source path controller.";
	}
	public void deleteSourcePath(Object modelElement) {
		Object taggedValue = Model.getFacade().getTaggedValue(modelElement,ImportInterface.SOURCE_PATH_TAG);
		Model.getExtensionMechanismsHelper().removeTaggedValue(modelElement,taggedValue);
	}
	public Collection getAllModelElementsWithSourcePath() {
		Project p = ProjectManager.getManager().getCurrentProject();
		Object model = p.getRoot();
		Collection elems = Model.getModelManagementHelper().getAllModelElementsOfKindWithModel(model,Model.getMetaTypes().getModelElement());
		ArrayList mElemsWithSrcPath = new ArrayList();
		Iterator iter = elems.iterator();
		while (iter.hasNext()) {
			Object me = iter.next();
			if (getSourcePath(me) != null) {
				mElemsWithSrcPath.add(me);
			}
		}
		return mElemsWithSrcPath;
	}
}



