package com.edi.test.restful.util;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class SchemaHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(SchemaHelper.class);
    
    public static final String W3C_XML_SCHEMA_URI = "http://www.w3.org/2001/XMLSchema";
    
    public static Schema buildSchema(String schemaFilename) throws SAXException {

        SchemaFactory sf = SchemaFactory.newInstance( W3C_XML_SCHEMA_URI );

        StreamSource source = new StreamSource(
                SchemaHelper.class.getResourceAsStream(SchemaResourceResolver.SCHEMA_PATH + schemaFilename));
        
        LOGGER.debug("SchemaHelper: Validate request by schema(" + schemaFilename + ").");

        sf.setResourceResolver(new SchemaResourceResolver());
        
        Schema  mySchema = sf.newSchema( source );
        
        return mySchema;
    }
}
