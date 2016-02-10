
package com.btkelly.gnag.models.android_lint;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.btkelly.gnag.models.android_lint package. 
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

    private final static QName _Issues_QNAME = new QName("", "issues");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.btkelly.gnag.models.android_lint
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Issues }
     * 
     */
    public Issues createIssues() {
        return new Issues();
    }

    /**
     * Create an instance of {@link Issue }
     * 
     */
    public Issue createIssue() {
        return new Issue();
    }

    /**
     * Create an instance of {@link Location }
     * 
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Issues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "issues")
    public JAXBElement<Issues> createIssues(Issues value) {
        return new JAXBElement<Issues>(_Issues_QNAME, Issues.class, null, value);
    }

}
