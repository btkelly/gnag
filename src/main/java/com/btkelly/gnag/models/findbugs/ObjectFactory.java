
package com.btkelly.gnag.models.findbugs;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.btkelly.gnag.models.findbugs package. 
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

    private final static QName _BugCollection_QNAME = new QName("", "BugCollection");
    private final static QName _SourceLineMessage_QNAME = new QName("", "Message");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.btkelly.gnag.models.findbugs
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BugCollection }
     * 
     */
    public BugCollection createBugCollection() {
        return new BugCollection();
    }

    /**
     * Create an instance of {@link BugCode }
     * 
     */
    public BugCode createBugCode() {
        return new BugCode();
    }

    /**
     * Create an instance of {@link Method }
     * 
     */
    public Method createMethod() {
        return new Method();
    }

    /**
     * Create an instance of {@link FindBugsProfile }
     * 
     */
    public FindBugsProfile createFindBugsProfile() {
        return new FindBugsProfile();
    }

    /**
     * Create an instance of {@link FileStats }
     * 
     */
    public FileStats createFileStats() {
        return new FileStats();
    }

    /**
     * Create an instance of {@link Errors }
     * 
     */
    public Errors createErrors() {
        return new Errors();
    }

    /**
     * Create an instance of {@link SourceLine }
     * 
     */
    public SourceLine createSourceLine() {
        return new SourceLine();
    }

    /**
     * Create an instance of {@link Error }
     * 
     */
    public Error createError() {
        return new Error();
    }

    /**
     * Create an instance of {@link PackageStats }
     * 
     */
    public PackageStats createPackageStats() {
        return new PackageStats();
    }

    /**
     * Create an instance of {@link ClassProfile }
     * 
     */
    public ClassProfile createClassProfile() {
        return new ClassProfile();
    }

    /**
     * Create an instance of {@link BugCategory }
     * 
     */
    public BugCategory createBugCategory() {
        return new BugCategory();
    }

    /**
     * Create an instance of {@link FindBugsSummary }
     * 
     */
    public FindBugsSummary createFindBugsSummary() {
        return new FindBugsSummary();
    }

    /**
     * Create an instance of {@link BugInstance }
     * 
     */
    public BugInstance createBugInstance() {
        return new BugInstance();
    }

    /**
     * Create an instance of {@link ClassStats }
     * 
     */
    public ClassStats createClassStats() {
        return new ClassStats();
    }

    /**
     * Create an instance of {@link Class }
     * 
     */
    public Class createClass() {
        return new Class();
    }

    /**
     * Create an instance of {@link BugPattern }
     * 
     */
    public BugPattern createBugPattern() {
        return new BugPattern();
    }

    /**
     * Create an instance of {@link Project }
     * 
     */
    public Project createProject() {
        return new Project();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BugCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "BugCollection")
    public JAXBElement<BugCollection> createBugCollection(BugCollection value) {
        return new JAXBElement<BugCollection>(_BugCollection_QNAME, BugCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Message", scope = SourceLine.class)
    public JAXBElement<String> createSourceLineMessage(String value) {
        return new JAXBElement<String>(_SourceLineMessage_QNAME, String.class, SourceLine.class, value);
    }

}
