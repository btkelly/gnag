
package com.btkelly.gnag.models.pmd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.btkelly.gnag.models.pmd package. 
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

    private final static QName _Pmd_QNAME = new QName("", "pmd");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.btkelly.gnag.models.pmd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Pmd }
     * 
     */
    public Pmd createPmd() {
        return new Pmd();
    }

    /**
     * Create an instance of {@link Violation }
     * 
     */
    public Violation createViolation() {
        return new Violation();
    }

    /**
     * Create an instance of {@link File }
     * 
     */
    public File createFile() {
        return new File();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Pmd }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pmd")
    public JAXBElement<Pmd> createPmd(Pmd value) {
        return new JAXBElement<Pmd>(_Pmd_QNAME, Pmd.class, null, value);
    }

}
