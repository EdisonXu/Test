//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.10 at 02:57:51 PM CST 
//


package com.edi.test;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.edi.test.restful.bean.MyEventType;
import com.edi.test.restful.bean.StatusReportType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _User_QNAME = new QName("", "user");
    private final static QName _MyEvent_QNAME = new QName("", "MyEvent");
    private final static QName _StatusReport_QNAME = new QName("", "StatusReport");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MyEventType }
     * 
     */
    public MyEventType createMyEventType() {
        return new MyEventType();
    }
    
    /**
     * Create an instance of {@link StatusReportType }
     * 
     */
    public StatusReportType createStatusReportType() {
        return new StatusReportType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusReportType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "StatusReport")
    public JAXBElement<StatusReportType> createStatusReport(StatusReportType value) {
        return new JAXBElement<StatusReportType>(_StatusReport_QNAME, StatusReportType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MyEventType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MyEvent")
    public JAXBElement<MyEventType> createMyEvent(MyEventType value) {
        return new JAXBElement<MyEventType>(_MyEvent_QNAME, MyEventType.class, null, value);
    }
}