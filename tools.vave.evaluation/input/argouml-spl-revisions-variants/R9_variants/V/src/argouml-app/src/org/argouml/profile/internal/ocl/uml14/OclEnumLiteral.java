// $Id: OclEnumLiteral.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.profile.internal.ocl.uml14;

import org.argouml.model.Model;

/**
 * Represents an OCL enumeration literal
 * 
 * @author maurelio1234
 */
public class OclEnumLiteral {

    private String name;
    
    /**
     * Default Constructor
     * 
     * @param literalName name of the literal
     */
    public OclEnumLiteral(String literalName) {
        this.name = literalName;
    }
   
    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof OclEnumLiteral) {
            return name.equals(((OclEnumLiteral) obj).name);
        } else if (Model.getFacade().isAEnumerationLiteral(obj)) {
            return name.equals(Model.getFacade().getName(obj));
        } else {
            return false;
        }
    }
    
    /*
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        // TODO how to implement this method properly?
        
        return name.hashCode();
    }
}
