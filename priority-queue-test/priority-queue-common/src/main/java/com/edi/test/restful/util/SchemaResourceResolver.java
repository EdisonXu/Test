package com.edi.test.restful.util;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class SchemaResourceResolver implements LSResourceResolver {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(SchemaResourceResolver.class);
    
    public static final String SCHEMA_PATH ="/schema/";
    
    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS domImplementationLS = (DOMImplementationLS) registry.getDOMImplementation("LS");

            LSInput lsInput = domImplementationLS.createLSInput();
            InputStream is = getClass().getResourceAsStream(SCHEMA_PATH + systemId);
            lsInput.setByteStream(is);
            lsInput.setSystemId(systemId);

            LOGGER.debug(this.getClass().getName(), "Resolve schema resource '" + systemId + "' successfully.");
            return lsInput;
            
        } catch (Exception e) {
            LOGGER.error("SchemaResourceResolver:resolveResource", e);
        }
        return null;
    }

}
