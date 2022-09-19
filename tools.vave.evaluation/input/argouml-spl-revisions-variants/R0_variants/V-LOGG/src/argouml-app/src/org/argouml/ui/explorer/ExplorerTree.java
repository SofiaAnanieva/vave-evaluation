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

package org.argouml.ui.explorer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Set;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;

import org.argouml.ui.DisplayTextTree;

import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;


public class ExplorerTree
    extends DisplayTextTree {

    private boolean updatingSelection;
	
    private boolean updatingSelectionViaTreeSelection;

    public ExplorerTree() {
        super();
    }

    class ExplorerMouseListener extends MouseAdapter {

        private JTree mLTree;

        public ExplorerMouseListener(JTree newtree) {
            super();
        }

        @Override
        public void mousePressed(MouseEvent me) {
        }

        @Override
        public void mouseReleased(MouseEvent me) {
        }

        @Override
        public void mouseClicked(MouseEvent me) {
        }

        private void myDoubleClick() {

        }

        public void showPopupMenu(MouseEvent me) {
        }
    }

    class ExplorerTreeWillExpandListener implements TreeWillExpandListener {

        public void treeWillCollapse(TreeExpansionEvent tee) {
        }

        public void treeWillExpand(TreeExpansionEvent tee) {
        }
    }


    class ExplorerTreeExpansionListener implements TreeExpansionListener {

        public void treeCollapsed(TreeExpansionEvent event) {
        }

        public void treeExpanded(TreeExpansionEvent event) {
        }
    }


    public void refreshSelection() {
    }

    private void setSelection(Object[] targets) {
    }
 
    private void addTargetsInternal(Object[] addedTargets) {    
    }

    private void selectVisible(Object target) {
    }

    private void selectAll(Set targets) {
    }  

    private void selectChildren(ExplorerTreeModel model, ExplorerTreeNode node,
            Set targets) {
    }


    class ExplorerTreeSelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
        }
    }

    class ExplorerTargetListener implements TargetListener {

        private void setTargets(Object[] targets) {
        }

        public void targetAdded(TargetEvent e) {
        }

        public void targetRemoved(TargetEvent e) {
        }

        public void targetSet(TargetEvent e) {
        }
    }

    private static final long serialVersionUID = 992867483644759920L;
}