/*------------------------------------------------------------------------------
 * COPYRIGHT Ericsson 2011
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *----------------------------------------------------------------------------*/
package com.edi.test.restful.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edi.test.restful.ObjectFactory;
import com.edi.test.restful.bean.MyEventType;
import com.edi.test.restful.bean.StatusReportType;


/**
 * JAXBHelper use to marshal and unmarshal XML type
 * 
 */
public class JAXBHelper {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JAXBHelper.class);
    
	public static <T> String marshallXmlType(T value) {
		return marshallXmlElement(value, true, true);
	}
	
	public static <T> String marshallXmlElement(T value, boolean withXmlDeclaration) {
        return marshallXmlElement(value, withXmlDeclaration, true);
    }
	
	@SuppressWarnings("unchecked")
	public static <T> String marshallXmlElement(T value, boolean withXmlDeclaration, boolean formatXml) {
		try {
			Class<T> clzz = (Class<T>) value.getClass();
			JAXBContext context = JAXBContext.newInstance(clzz);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatXml);
			// if JAXB_FRAGMENT==true, no XML declaration
			m.setProperty(Marshaller.JAXB_FRAGMENT, !withXmlDeclaration); 
			StringWriter writer = new StringWriter();
			JAXBElement<?> element = getJaxbElement(value);
			m.marshal(element, writer);
			
			LOGGER.info("Marshal XML element for class '" + value.getClass().getName() + "' successfully.");
			return writer.toString();
		} catch (Exception e) {
		    LOGGER.error("marshall xml error:", e);
		}
		return null;
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> JAXBElement<?> getJaxbElement(T value)
            throws JAXBException {
        JAXBElement<?> element = null;

        Class<T> clzz = (Class<T>) value.getClass();

        if (clzz.equals(StatusReportType.class)) {
            StatusReportType bvalue = (StatusReportType) value;
            element = (new ObjectFactory()).createStatusReport(bvalue);
        } else if (clzz.equals(MyEventType.class)) {
            MyEventType bvalue = (MyEventType) value;
            element = (new ObjectFactory()).createMyEvent(bvalue);
        } else {
            QName name = new QName(clzz.getSimpleName());
            element = new JAXBElement(name, clzz, value);
        }
        return element;
    }	
	
	public static <T> T unmarshallXmlType(Class<T> clzz, String strXml) {
		try {
			StringReader reader = new StringReader(strXml);
			;
			JAXBContext context = JAXBContext.newInstance(clzz);
			Unmarshaller u = context.createUnmarshaller();
			JAXBElement<T> element = u
					.unmarshal(new StreamSource(reader), clzz);
			
			LOGGER.info("Unmarshal XML type for class '" + clzz.getClass().getName() + "' successfully.");
			return element.getValue();
		} catch (Exception e) {
		    LOGGER.error("UnmarshallXmlType error:", e);
		}
		return null;
	}
	
	
    public static <T> T unmarshallXmlTypeWithSchemaValidation(Class<T> clzz, String strXml, String schemaFilename)
                                                                    throws Exception {
        if (strXml == null || clzz == null) {
            return null;
        }
        
        try {
            StringReader reader = new StringReader(strXml);
    
            JAXBContext context = JAXBContext.newInstance(clzz);
            Unmarshaller u = context.createUnmarshaller();
            
            if (schemaFilename != null) {
                u.setSchema(SchemaHelper.buildSchema(schemaFilename));
            }
            
            JAXBElement<T> element = u.unmarshal(new StreamSource(reader), clzz);
            
            LOGGER.info("Unmarshal XML type for class '" + clzz.getClass().getName() + "' successfully.");
            return element.getValue();
            
        } catch (Exception e) {
            LOGGER.error("JAXBHelper:unmarshallXmlTypeWithSchemaValidation", e);
            throw new Exception(e.getCause());
        }
    }
}
