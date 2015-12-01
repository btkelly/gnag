
package com.btkelly.gnag.models.findbugs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for FileStats complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FileStats">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="path" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bugCount" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="size" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bugHash" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileStats", propOrder = {
    "value"
})
public class FileStats {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "path")
    protected String path;
    @XmlAttribute(name = "bugCount")
    protected String bugCount;
    @XmlAttribute(name = "size")
    protected String size;
    @XmlAttribute(name = "bugHash")
    protected String bugHash;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * Gets the value of the bugCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBugCount() {
        return bugCount;
    }

    /**
     * Sets the value of the bugCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBugCount(String value) {
        this.bugCount = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSize(String value) {
        this.size = value;
    }

    /**
     * Gets the value of the bugHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBugHash() {
        return bugHash;
    }

    /**
     * Sets the value of the bugHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBugHash(String value) {
        this.bugHash = value;
    }

}
