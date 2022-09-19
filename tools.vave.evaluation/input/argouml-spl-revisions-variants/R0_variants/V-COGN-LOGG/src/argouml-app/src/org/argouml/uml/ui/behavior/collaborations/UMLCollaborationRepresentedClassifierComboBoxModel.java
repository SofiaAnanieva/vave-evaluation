// $Id: UMLCollaborationRepresentedClassifierComboBoxModel.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.ui.behavior.collaborations;

import java.util.ArrayList;
import java.util.Collection;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;

/**
 * The ComboBox model for the represented classifier 
 * of a collaboration.
 * 
 * @author michiel
 */
class UMLCollaborationRepresentedClassifierComboBoxModel
    extends  UMLComboBoxModel2  {
    
    /**
     * Constructor for UMLCollaborationRepresentedClassifierComboBoxModel.
     */
    public UMLCollaborationRepresentedClassifierComboBoxModel() {
        super("representedClassifier", true);
    }
    
    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        Collection classifiers = new ArrayList();
        Project p = ProjectManager.getManager().getCurrentProject();
        for (Object model : p.getUserDefinedModelList()) {
            Collection c = Model.getModelManagementHelper()
                .getAllModelElementsOfKind(model, 
                    Model.getMetaTypes().getClassifier());
            for (Object cls : c) {
                Collection s = Model.getModelManagementHelper()
                    .getAllSurroundingNamespaces(cls);
                if (!s.contains(getTarget())) {
                    classifiers.add(cls);
                }
            }
        }
        setElements(classifiers);
    }
    
    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAClassifier(element)
            && Model.getFacade().getRepresentedClassifier(getTarget()) 
                == element;
    }
    
    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        return Model.getFacade().getRepresentedClassifier(getTarget());
    }
    
    public void modelChange(UmlChangeEvent evt) {
        /* Do nothing by design. */
    }
}

