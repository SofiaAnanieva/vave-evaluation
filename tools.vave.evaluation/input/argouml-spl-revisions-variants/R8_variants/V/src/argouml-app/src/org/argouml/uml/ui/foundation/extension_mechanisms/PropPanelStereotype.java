// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementGeneralizationListModel;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementSpecializationListModel;


public class PropPanelStereotype extends PropPanelModelElement {

    private static final long serialVersionUID = 8038077991746618130L;

    private List<String> metaClasses;
    
    private static UMLGeneralizableElementSpecializationListModel
    specializationListModel =
            new UMLGeneralizableElementSpecializationListModel();

    private static UMLGeneralizableElementGeneralizationListModel
    generalizationListModel =
            new UMLGeneralizableElementGeneralizationListModel();

    private static UMLStereotypeTagDefinitionListModel
    tagDefinitionListModel =
            new UMLStereotypeTagDefinitionListModel();

    private static UMLExtendedElementsListModel
    extendedElementsListModel =
            new UMLExtendedElementsListModel();

    private JScrollPane generalizationScroll;

    private JScrollPane specializationScroll;

    private JScrollPane tagDefinitionScroll;

    private JScrollPane extendedElementsScroll;


    public PropPanelStereotype() {
        super("label.stereotype-title", lookupIcon("Stereotype"));
    }


    protected JScrollPane getGeneralizationScroll() {
        return null;
    }


    protected JScrollPane getSpecializationScroll() {
        return null;
    }


    protected JScrollPane getTagDefinitionScroll() {
        return null;
    }

    protected JScrollPane getExtendedElementsScroll() {
        return null;
    }

    void initMetaClasses() {
    }


    class UMLStereotypeBaseClassListModel extends UMLModelElementListModel2 {

        UMLStereotypeBaseClassListModel() {
            super("baseClass");
        }

        @Override
        protected void buildModelList() {
        }

        @Override
        protected boolean isValidElement(Object element) {
            return false;
        }
    }


    
    class ActionAddStereotypeBaseClass extends AbstractActionAddModelElement2 {

        @Override
        protected List<String> getChoices() {
            return null;
        }

        @Override
        protected String getDialogTitle() {
            return null;
        }

        @Override
        protected List<String> getSelected() {
            return null;
        }

        @Override
        protected void doIt(Collection selected) {
        }

    }


    
    class ActionDeleteStereotypeBaseClass extends AbstractActionRemoveElement {

        public ActionDeleteStereotypeBaseClass() {
            super(Translator.localize("menu.popup.remove"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}