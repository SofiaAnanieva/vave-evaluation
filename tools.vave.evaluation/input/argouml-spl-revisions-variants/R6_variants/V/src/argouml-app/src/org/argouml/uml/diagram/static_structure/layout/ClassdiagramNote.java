// $Id: ClassdiagramNote.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.layout;

import org.tigris.gef.presentation.FigNode;

/**
 * This class overrides some of the aspects of ClassdiagramNodes to simplify
 * the positioning of notes nearby the commented nodes.
 *
 * @author David Gunkel
 */
public class ClassdiagramNote extends ClassdiagramNode {
    /**
     * @param f the fig
     */
    public ClassdiagramNote(FigNode f) {
        super(f);
    }

    /*
     * @see org.argouml.uml.diagram.static_structure.layout.ClassdiagramNode#getTypeOrderNumer()
     */
    public int getTypeOrderNumer() {
        return first() == null
	    ? super.getTypeOrderNumer()
	    : first().getTypeOrderNumer();
    }
    
    /*
     * @see org.argouml.uml.diagram.static_structure.layout.ClassdiagramNode#calculateWeight()
     */
    @Override
    public float calculateWeight() {
        setWeight(getWeight());
        return getWeight();
    }

    /*
     * @see org.argouml.uml.diagram.static_structure.layout.ClassdiagramNode#getRank()
     */
    @Override
    public int getRank() {
        return first() == null ? 0 : first().getRank();
    }

    /*
     * @see org.argouml.uml.diagram.static_structure.layout.ClassdiagramNode#getWeight()
     */
    @Override
    public float getWeight() {
        return first() == null ? 0 : first().getWeight() * 0.9999999f;
    }

    /*
     * @see org.argouml.uml.diagram.static_structure.layout.ClassdiagramNode#isStandalone()
     */
    @Override
    public boolean isStandalone() {
        return first() == null ? true : first().isStandalone();
    }

    /**
     * Return the first node to which this note is attached to.
     *
     * @return A ClassdiagramNode.
     */
    private ClassdiagramNode first() {
        return getUpNodes().isEmpty() ? null : getUpNodes().get(0);
    }
}
