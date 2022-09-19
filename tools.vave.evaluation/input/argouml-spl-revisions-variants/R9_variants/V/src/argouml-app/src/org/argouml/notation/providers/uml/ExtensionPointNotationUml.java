// $Id: ExtensionPointNotationUml.java 127 2010-09-25 22:23:13Z marcusvnac $
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.notation.providers.uml;

import java.util.Map;
import java.util.StringTokenizer;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.ExtensionPointNotation;

/**
 * The notation for an extension point for UML.
 * 
 * @author michiel
 */
public class ExtensionPointNotationUml extends ExtensionPointNotation {

    /**
     * The constructor.
     *
     * @param ep the represented Extension Point
     */
    public ExtensionPointNotationUml(Object ep) {
        super(ep);
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        /* TODO: This try-catch will be needed 
         * once the code below is improved. */
//        try {
        parseExtensionPointFig(modelElement, text);
//        } catch (ParseException pe) {
//            String msg = "statusmsg.bar.error.parsing.extensionpoint";
//            Object[] args = {
//                pe.getLocalizedMessage(),
//                Integer.valueOf(pe.getErrorOffset()),
//            };
//            ProjectBrowser.getInstance().getStatusBar().showStatus(
//                    Translator.messageFormat(msg, args));
//        }
    }
    /**
     * Parse an extension point.<p>
     *
     * The syntax is "name: location", "name:", "location" or "". The fields of
     * the extension point are updated appropriately.
     *
     * @param ep      The extension point concerned
     * @param text    The text to parse
     */
    public void parseExtensionPointFig(Object ep, String text) {
        // We can do nothing if we don't have both the use case and extension
        // point.
        
    }

    
    /*
     * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-extensionpoint";
    }

    /**
     * Generate the display for an extension point.<p>
     *
     * The representation is "name: location". 
     * The "name: " is omitted if there
     * is no name given.
     *
     * {@inheritDoc}
     * @deprecated
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public String toString(Object modelElement, Map args) {
        return toString(modelElement);
    }

    private String toString(final Object modelElement) {

        if (modelElement == null) {
            return "";
        }

        // The string to build
        String s = "";

        // Get the fields we want
        String epName = Model.getFacade().getName(modelElement);
        String epLocation = Model.getFacade().getLocation(modelElement);

        // Put in the name field if it's there
        if ((epName != null) && (epName.length() > 0)) {
            s += epName + ": ";
        }

        // Put in the location field if it's there
        if ((epLocation != null) && (epLocation.length() > 0)) {
            s += epLocation;
        }

        return s;
    }

    /**
     * Generate the text for an extension point.<p>
     *
     * The representation is "name: location". 
     * The "name: " is omitted if there
     * is no name given.
     */
    @Override
    public String toString(Object modelElement, NotationSettings settings) {
        return toString(modelElement);
    }

}
