/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2010 BiBiServ Curator Team, http://bibiserv.cebitec.uni-bielefeld.de, 
 * All rights reserved.
 * 
 * The contents of this file are subject to the terms of the Common
 * Development and Distribution License("CDDL") (the "License"). You 
 * may not use this file except in compliance with the License. You can 
 * obtain a copy of the License at http://www.sun.com/cddl/cddl.html
 * 
 * See the License for the specific language governing permissions and 
 * limitations under the License.  When distributing the software, include 
 * this License Header Notice in each file.  If applicable, add the following 
 * below the License Header, with the fields enclosed by brackets [] replaced
 *  by your own identifying information:
 * 
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 */

package de.unibi.techfak.bibiserv.util.codegen;

import java.io.InputStream;

/**
*  Class CodeGen_WebSubmissionPage_Resulthandler extends CodeGen_WebSubmissionPage_Function,
 *  generates possible result components.

 *
 *  @author Thomas Gatter - tgatter(at)cebitec.uni-bielefeld.de
 */
public class CodeGen_WebSubmissionPage_Resulthandler extends CodeGen_WebSubmissionPage_Function {

    public CodeGen_WebSubmissionPage_Resulthandler(){
        super();
        setExtensionName("_p_3");
    }

      @Override
    public InputStream getDefaultTemplate() {
        return CodeGen_Implementation.class.getResourceAsStream("/templates/page_submission_resulthandler.xhtml");
    }


}
