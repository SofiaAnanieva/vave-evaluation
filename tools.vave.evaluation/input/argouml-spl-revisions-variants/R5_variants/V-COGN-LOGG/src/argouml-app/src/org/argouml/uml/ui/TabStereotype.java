// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;

import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.UMLModelElementListModel2;

public class TabStereotype extends PropPanel {

    private static final int INSET_PX = 3;

    private static String orientation =
        Configuration.getString(Configuration
            .makeKey("layout", "tabstereotype"));

    private UMLModelElementListModel2 selectedListModel;
    private UMLModelElementListModel2 availableListModel;

    private JScrollPane selectedScroll;
    private JScrollPane availableScroll;
    private JPanel panel;
    private JButton addStButton;
    private JButton removeStButton;
    private JPanel xferButtons;
    private JList selectedList;
    private JList availableList;


    public TabStereotype() {
        super(Translator.localize("tab.stereotype"), (ImageIcon) null);
    }

    private JPanel makePanel() {
        return null;
    }

    public boolean shouldBeEnabled() {
        return false;
    }
    
    @Override
    public boolean shouldBeEnabled(Object target) {
        return false;
    }

    @Override
    public void setTarget(Object theTarget) {

    }

    private void doAddStereotype() {
    }

    private void doRemoveStereotype() {
    }


    private static class UMLModelStereotypeListModel
        extends UMLModelElementListModel2 {

        public UMLModelStereotypeListModel() {
            super("stereotype");
        }

        protected void buildModelList() {
        }

        protected boolean isValidElement(Object element) {
            return false;
        }

        private static final long serialVersionUID = 7247425177890724453L;
    }

    private class AddRemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        }
    }

    private class AvailableListSelectionListener
        implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent lse) {
        }
    }

    private class SelectedListSelectionListener
        implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent lse) {
        }
    }

    private static final long serialVersionUID = -4741653225927138553L;
}