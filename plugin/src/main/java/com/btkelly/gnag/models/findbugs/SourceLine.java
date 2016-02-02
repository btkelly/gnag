/**
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.btkelly.gnag.models.findbugs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SourceLine complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SourceLine">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Message" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="At ActivityStateUtil.java:[lines 27-61]"/>
 *               &lt;enumeration value="At ActivityStateUtil.java:[line 61]"/>
 *               &lt;enumeration value="At SystemTimeUtil.java:[lines 25-45]"/>
 *               &lt;enumeration value="At SystemTimeUtil.java:[line 45]"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="classname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="end" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sourcefile" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sourcepath" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="startBytecode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="endBytecode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="primary" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="synthetic" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SourceLine", propOrder = {
    "content"
})
public class SourceLine {

    @XmlElementRef(name = "Message", type = JAXBElement.class, required = false)
    @XmlMixed
    protected List<Serializable> content;
    @XmlAttribute(name = "classname")
    protected String classname;
    @XmlAttribute(name = "start")
    protected String start;
    @XmlAttribute(name = "end")
    protected String end;
    @XmlAttribute(name = "sourcefile")
    protected String sourcefile;
    @XmlAttribute(name = "sourcepath")
    protected String sourcepath;
    @XmlAttribute(name = "startBytecode")
    protected String startBytecode;
    @XmlAttribute(name = "endBytecode")
    protected String endBytecode;
    @XmlAttribute(name = "primary")
    protected String primary;
    @XmlAttribute(name = "synthetic")
    protected String synthetic;

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link String }
     * 
     * 
     */
    public List<Serializable> getContent() {
        if (content == null) {
            content = new ArrayList<Serializable>();
        }
        return this.content;
    }

    /**
     * Gets the value of the classname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassname() {
        return classname;
    }

    /**
     * Sets the value of the classname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassname(String value) {
        this.classname = value;
    }

    /**
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStart(String value) {
        this.start = value;
    }

    /**
     * Gets the value of the end property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnd(String value) {
        this.end = value;
    }

    /**
     * Gets the value of the sourcefile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcefile() {
        return sourcefile;
    }

    /**
     * Sets the value of the sourcefile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcefile(String value) {
        this.sourcefile = value;
    }

    /**
     * Gets the value of the sourcepath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcepath() {
        return sourcepath;
    }

    /**
     * Sets the value of the sourcepath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcepath(String value) {
        this.sourcepath = value;
    }

    /**
     * Gets the value of the startBytecode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartBytecode() {
        return startBytecode;
    }

    /**
     * Sets the value of the startBytecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartBytecode(String value) {
        this.startBytecode = value;
    }

    /**
     * Gets the value of the endBytecode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndBytecode() {
        return endBytecode;
    }

    /**
     * Sets the value of the endBytecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndBytecode(String value) {
        this.endBytecode = value;
    }

    /**
     * Gets the value of the primary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimary() {
        return primary;
    }

    /**
     * Sets the value of the primary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimary(String value) {
        this.primary = value;
    }

    /**
     * Gets the value of the synthetic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSynthetic() {
        return synthetic;
    }

    /**
     * Sets the value of the synthetic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSynthetic(String value) {
        this.synthetic = value;
    }

}
