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

import de.unibi.techfak.bibiserv.cms.Tfunction.Outputfileref;
import de.unibi.techfak.bibiserv.cms.ToutputFile;
import de.unibi.techfak.bibiserv.util.ontoaccess.bibiontotypes.OntoRepresentation;
import java.io.InputStream;

/**
 * Creates the Param Bean classes
 *
 * @author jkrueger
 */
public class CodeGen_WebSubmissionBean_Result extends Abstract_CodeGen_WebSubmissionFunctionBean{

      @Override
    public InputStream getDefaultTemplate() {
        return CodeGen_Implementation.class.getResourceAsStream("/templates/bean_result.java");
    }

    @Override
    public String getClassName(String arg) {
        return function.getId()+"_result";
    }
    
    
    public String addAllOutputFiles(String arg) {
        StringBuilder out = new StringBuilder();
        
        for(Outputfileref ref: function.getOutputfileref()){
            ToutputFile file = (ToutputFile) ref.getRef();
            
            String folder = file.getFolder();
            if(folder==null) {
                folder="";
            }
            
            out.append("\tcounter=0;").append(br);
            out.append("\tfor(String filenames: execfunction.getMatchingFiles(\"")
                     .append(folder).append("\",\"")
                     .append(file.getFilename()).append("\")) {").append(br);
            out.append("counter++;").append(br);
            
            if(!folder.isEmpty() && !folder.endsWith("/")){
                folder += "/";
            }
            out.append("\t\ttmp = new Pair<String,String>(filenames, \"").append(file.getContenttype()).append("\");");
            out.append(br);
            out.append("\t\tfiles.add(new Pair<String,Pair<String,String>>(")
               .append("messages.property(\"").append(file.getId()).append("_name\")+ ((counter>1) ? \" \"+counter : \"\") , tmp")
               .append("));").append(br);  
            out.append("}").append(br);
        }
        
        return out.toString();
    }


    /**
     * Return the implementation class belonging to current
     * @param args
     * @return
     */
    public String getImplementation(String args){
        return "de.unibi.techfak.bibiserv.tools." + runnableitem.getId()+"."+function.getId();
    }

}
