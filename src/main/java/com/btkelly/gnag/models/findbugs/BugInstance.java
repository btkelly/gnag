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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BugInstance complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BugInstance">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ShortMessage">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="equals method always returns true"/>
 *               &lt;enumeration value="Class defines equals() and uses Object.hashCode()"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LongMessage">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="com.github.stkent.amplify.utils.ActivityStateUtil.equals(Object) always returns true"/>
 *               &lt;enumeration value="com.github.stkent.amplify.utils.ActivityStateUtil defines equals and uses Object.hashCode()"/>
 *               &lt;enumeration value="com.github.stkent.amplify.utils.time.SystemTimeUtil.equals(Object) always returns true"/>
 *               &lt;enumeration value="com.github.stkent.amplify.utils.time.SystemTimeUtil defines equals and uses Object.hashCode()"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Class" type="{}Class"/>
 *         &lt;element name="Method" type="{}Method"/>
 *         &lt;element name="SourceLine" type="{}SourceLine"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="priority" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rank" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="abbrev" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="category" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="instanceHash" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="instanceOccurrenceNum" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="instanceOccurrenceMax" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BugInstance", propOrder = {
    "shortMessage",
    "longMessage",
    "clazz",
    "method",
    "sourceLine"
})
public class BugInstance {

    @XmlElement(name = "ShortMessage", required = true)
    protected String shortMessage;
    @XmlElement(name = "LongMessage", required = true)
    protected String longMessage;
    @XmlElement(name = "Class", required = true)
    protected Class clazz;
    @XmlElement(name = "Method", required = true)
    protected Method method;
    @XmlElement(name = "SourceLine", required = true)
    protected SourceLine sourceLine;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "priority")
    protected String priority;
    @XmlAttribute(name = "rank")
    protected String rank;
    @XmlAttribute(name = "abbrev")
    protected String abbrev;
    @XmlAttribute(name = "category")
    protected String category;
    @XmlAttribute(name = "instanceHash")
    protected String instanceHash;
    @XmlAttribute(name = "instanceOccurrenceNum")
    protected String instanceOccurrenceNum;
    @XmlAttribute(name = "instanceOccurrenceMax")
    protected String instanceOccurrenceMax;

    /**
     * Gets the value of the shortMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortMessage() {
        return shortMessage;
    }

    /**
     * Sets the value of the shortMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortMessage(String value) {
        this.shortMessage = value;
    }

    /**
     * Gets the value of the longMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongMessage() {
        return longMessage;
    }

    /**
     * Sets the value of the longMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongMessage(String value) {
        this.longMessage = value;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link Class }
     *     
     */
    public Class getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link Class }
     *     
     */
    public void setClazz(Class value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link Method }
     *     
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link Method }
     *     
     */
    public void setMethod(Method value) {
        this.method = value;
    }

    /**
     * Gets the value of the sourceLine property.
     * 
     * @return
     *     possible object is
     *     {@link SourceLine }
     *     
     */
    public SourceLine getSourceLine() {
        return sourceLine;
    }

    /**
     * Sets the value of the sourceLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceLine }
     *     
     */
    public void setSourceLine(SourceLine value) {
        this.sourceLine = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Gets the value of the rank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRank() {
        return rank;
    }

    /**
     * Sets the value of the rank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRank(String value) {
        this.rank = value;
    }

    /**
     * Gets the value of the abbrev property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbbrev() {
        return abbrev;
    }

    /**
     * Sets the value of the abbrev property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbbrev(String value) {
        this.abbrev = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategory(String value) {
        this.category = value;
    }

    /**
     * Gets the value of the instanceHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceHash() {
        return instanceHash;
    }

    /**
     * Sets the value of the instanceHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceHash(String value) {
        this.instanceHash = value;
    }

    /**
     * Gets the value of the instanceOccurrenceNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceOccurrenceNum() {
        return instanceOccurrenceNum;
    }

    /**
     * Sets the value of the instanceOccurrenceNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceOccurrenceNum(String value) {
        this.instanceOccurrenceNum = value;
    }

    /**
     * Gets the value of the instanceOccurrenceMax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceOccurrenceMax() {
        return instanceOccurrenceMax;
    }

    /**
     * Sets the value of the instanceOccurrenceMax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceOccurrenceMax(String value) {
        this.instanceOccurrenceMax = value;
    }

}
