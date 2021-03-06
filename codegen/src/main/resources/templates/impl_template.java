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
/**
 * Implementation class  for function <#getClassName/#>, implements all request and
 * response methods ...
 *
 * <b>Attention: This is autogenerated code. </b>
 *
 *
 * @author Jan Krueger - jkrueger[aet]cebitec.uni-bielefeld.de (template)
 */
package <#getPackageName/#>;

import de.unibi.techfak.bibiserv.util.ontoaccess.bibiontotypes.OntoRepresentation;
import de.unibi.techfak.bibiserv.util.ontoaccess.bibiontotypes.impl.OntoRepresentationImplementation;
import de.unibi.cebitec.bibiserv.util.convert.ConversionException;
import de.unibi.cebitec.bibiserv.util.validate.ValidationException;
import de.unibi.cebitec.bibiserv.utils.ValidationConnection;
import de.unibi.techfak.bibiserv.BiBiTools;
import de.unibi.techfak.bibiserv.Status;
import de.unibi.techfak.bibiserv.User;
import de.unibi.techfak.bibiserv.cms.TrunnableItem;
import de.unibi.techfak.bibiserv.exception.BiBiToolsException;
import de.unibi.techfak.bibiserv.exception.DBConnectionException;
import de.unibi.techfak.bibiserv.exception.IdNotFoundException;
import de.unibi.techfak.bibiserv.util.Pair;
import de.unibi.techfak.bibiserv.util.ontoaccess.bibiontotypes.BiBiTool;
import de.unibi.techfak.bibiserv.web.beans.session.UserInterface;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

public class <#getClassName/#> {

    private String fct_id = "<#getFunctionId/#>";

    public String getFunctionId(){
        return fct_id;
    }
    
    
    private BiBiTools bibitools;
    private Utilities utilities = new Utilities();


private static Logger log = Logger.getLogger(<#getClassName/#>.class);

    /* #########################################
     * #     DI method setthreadWorker         #
     * ######################################### */
    public <#getFunctionId/#>_threadworker threadworker;

    public void setThreadworker(<#getFunctionId/#>_threadworker threadworker){
        this.threadworker = threadworker;
    }
    
    /* #########################################        
     * #     DI method setUser                 #        
     * ######################################### */     
    private UserInterface user;                         
    
    public void setUser(UserInterface user){            
        this.user = user;                              
    }     

     /* #########################################
     * #           Inputs and Outputs           #
     * ######################################### */
<#generateOntoRepresentationInputs/#>
<#generateOntoRepresentationOutput/#>        
    
     /* #########################################
     * #           Managing Function          #
     * ######################################### */
<#generateRequestImpl#>
    try {
        // create new BiBiTools object
        initializeBiBiTools();
        // get status object from bibitools;

        <#generateRequestImplThreadCall/#>

        // return bibiserv id and finish
        return bibitools.getStatus().getId();
    } catch (IdNotFoundException e){
        throw new BiBiToolsException(706, "ID unknown (or older than 30 days)");
    } catch (DBConnectionException e) {
        throw new BiBiToolsException(722, "Internal Resource Error");
    }
    <#/generateRequestImpl#>
	
    
    
    /** private response function; this Function is called by the public 
     *  response representation. */
    private Object responseImpl(String id, boolean streaming) throws BiBiToolsException {
        try {
                // create new BiBiTools object
                initializeBiBiTools(id);
               // get status object from bibitools;
                Status status = bibitools.getStatus();
                // check if process thread is finished
                if (status.getStatuscode() == 600) {

                    /*
                     * TG: Change behaviour of getOutputFile if needed!
                     */
                    if(streaming) {
                        // if streaming is enabled, only a stream to the outputfile is returned
                        return bibitools.getSpoolFileStream(getOutputFile());
                    } else {
                        // the output is completely written into an object in RAM
                        return bibitools.retrieveOutputData(getOutputFile(), representation_output.getType().name() , representation_output.getImplementationType());
                    } 
                }
                throw new BiBiToolsException(status);
        } catch (IdNotFoundException e) {
                throw new BiBiToolsException(706, "ID unknown (or older than 30 days)");
        } catch (DBConnectionException e) {
                throw new BiBiToolsException(722, "Internal Resource Error");
        } catch (FileNotFoundException e) {
                throw new BiBiToolsException(723, "Internal Resource Error");
        }
    }

    
    /**
     * Returns the outputfile of the tool.
     *  ATTENTION! Depending on tool behaviour the bibitool.getOutputFile(...) has
     *  to extended (with a file extention) or replaced by yourself.
     * 
     *  JK, 08/04/2011, TG, 12/2012 
     * 
     * @return 
     */    
    private File getOutputFile() throws BiBiToolsException, DBConnectionException, IdNotFoundException{
        return bibitools.getOutputFile("<#getFunctionId/#>");
    }
     
    
    /**
     * Tests if the output file if above a certain size-threshold.
     * If this threshold is reached, the Output should only be 
     * stream-converted/streamed for download.
     * @return True: output should be streamed, False: Output should can be handled in RAM
     */
    public boolean isStreamingOutput() {
        try {
            long filesize = bibitools.getSpoolFileSize(getOutputFile());
            
            // TODO: TG 04/14, change threshold if needed!
            long streamingThreshold = 104857600; // = 100MB
            
            if(filesize > streamingThreshold) {
                return true;
            }
            
        } catch (BiBiToolsException | DBConnectionException | IdNotFoundException | FileNotFoundException ex) {
        }
        // default is false
        return false;
    }
         
         
    public String getResultfilePath(String id) throws BiBiToolsException{
        try {
            
            initializeBiBiTools(id);
            return bibitools.getSpoolDir().toString() + "/" + getOutputFile().toString();
            
        } catch (IdNotFoundException e) {
                throw new BiBiToolsException(706, "ID unknown (or older than 30 days)");
        } catch (DBConnectionException e) {
                throw new BiBiToolsException(722, "Internal Resource Error");
        } catch (FileNotFoundException e) {
                throw new BiBiToolsException(723, "Internal Resource Error");
        }
    } 
  
    public Object response(String id, OntoRepresentation representation) throws BiBiToolsException, ConversionException {
         return response(id, representation, false);
     }
    
    
    public Object response(String id, OntoRepresentation representation, boolean streaming) throws BiBiToolsException, ConversionException {
        try {
            Object output = responseImpl(id, streaming);
            Object converted = Utilities.convert(output, representation_output, representation, streaming);
            return converted;
        } catch (BiBiToolsException e) {
            throw BiBiTools.createSOAPFaultException(e);
        }
    }
    
     <#generateRequestRepresentation/#>
        
        
    public int getStatusCode(String id) throws BiBiToolsException{
        try {
            // create new BiBiTools object
            initializeBiBiTools(id);
            return bibitools.getStatus().getStatuscode();
       } catch (IdNotFoundException e) {
            throw new BiBiToolsException(706, "ID unknown (or older than 30 days)");
       } catch (DBConnectionException e) {
            throw new BiBiToolsException(722, "Internal Resource Error");
       }

    }

    public Status getStatus(String id) throws BiBiToolsException {
        try {
            // create new BiBiTools object
            initializeBiBiTools(id);
            return bibitools.getStatus();
        } catch (IdNotFoundException e) {
            throw new BiBiToolsException(706, "ID unknown (or older than 30 days)");
        } catch (DBConnectionException e) {
            throw new BiBiToolsException(722, "Internal Resource Error");
        }
    }

    public String getStatusDescription(String id) throws BiBiToolsException {
        try {
            // create new BiBiTools object
            initializeBiBiTools(id);
            return bibitools.getStatus().getDescription();
        } catch (IdNotFoundException e) {
            throw new BiBiToolsException(706, "ID unknown (or older than 30 days)");
        } catch (DBConnectionException e) {
            throw new BiBiToolsException(722, "Internal Resource Error");
        }
    }

    public List<Pair<String, String>> getUploadDownloadData(String id) throws BiBiToolsException {
        try {
            // create new BiBiTools object
            initializeBiBiTools(id);
            return bibitools.getStatus().getUploadDownloadData();
        } catch (IdNotFoundException e) {
            throw new BiBiToolsException(706, "ID unknown (or older than 30 days)");
        } catch (DBConnectionException e) {
            throw new BiBiToolsException(722, "Internal Resource Error");
        }
    }


    /**
     * Service method that return a tool description. The tool description is
     * currently an RunableItem Object.
     *
     * @return Return the runnableItem.
     */
    public TrunnableItem getRunnableItem() throws BiBiToolsException {
        try {
            // create JAXB Context
            JAXBContext jaxbcontext = JAXBContext.newInstance(TrunnableItem.class.getName());
            // create Unmarshaeller
            Unmarshaller unmarshaller = jaxbcontext.createUnmarshaller();
            // load runnableitem from file and element
            return (TrunnableItem) unmarshaller.unmarshal(getClass().getResourceAsStream("/config/runnableitem.xml"));
        } catch (JAXBException e) {
            throw new BiBiToolsException(721, "Internal Resource Error", e);
        }

    }

    private void initializeBiBiTools() throws BiBiToolsException,  DBConnectionException, IdNotFoundException {
        initializeBiBiTools(null);
    }

    private String bibitools_id; 
    
    private void initializeBiBiTools(String id) throws BiBiToolsException,  DBConnectionException, IdNotFoundException {
        
         if (bibitools == null || bibitools_id == null || !bibitools_id.equals(id)) {
        
            InputStream is = getClass().getResourceAsStream("/runnableitem.xml");
            if (is == null) {
                throw new BiBiToolsException(700, "get resource  '/runnableitem.xml' as stream failed ... ");
            }
            if (user == null) {
                bibitools = new BiBiTools(id,"<#getFunctionId/#>",new User(), is);
            } else {
                bibitools = new BiBiTools(id,"<#getFunctionId/#>",user.getUser(), is);
            }
            bibitools_id = bibitools.getStatus().getId();
         }
    }
    
    
    public List<String> getMatchingFiles(String folder, String wildcard){
        return bibitools.getAllFilesInSpoolfirMatchingWildcard(folder, wildcard);
    }
    
    public byte[] readSpool(String filename){
        try { 
            return bibitools.readSpoolFile(filename);
        } catch (FileNotFoundException ex) {
            return null;
        }
    }
    
    public InputStream getSpoolFileStream(String filename){
        try { 
            return bibitools.getSpoolFileStream(filename);
        } catch (FileNotFoundException ex) {
            return null;
        }
    }
     
}
