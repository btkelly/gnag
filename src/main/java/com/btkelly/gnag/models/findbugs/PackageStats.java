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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PackageStats complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PackageStats">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ClassStats" type="{}ClassStats" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="package" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="total_bugs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="total_types" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="total_size" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="priority_1" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PackageStats", propOrder = {
    "classStats"
})
public class PackageStats {

    @XmlElement(name = "ClassStats")
    protected List<ClassStats> classStats;
    @XmlAttribute(name = "package")
    protected String _package;
    @XmlAttribute(name = "total_bugs")
    protected String totalBugs;
    @XmlAttribute(name = "total_types")
    protected String totalTypes;
    @XmlAttribute(name = "total_size")
    protected String totalSize;
    @XmlAttribute(name = "priority_1")
    protected String priority1;

    /**
     * Gets the value of the classStats property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classStats property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassStats().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassStats }
     * 
     * 
     */
    public List<ClassStats> getClassStats() {
        if (classStats == null) {
            classStats = new ArrayList<ClassStats>();
        }
        return this.classStats;
    }

    /**
     * Gets the value of the package property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackage() {
        return _package;
    }

    /**
     * Sets the value of the package property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackage(String value) {
        this._package = value;
    }

    /**
     * Gets the value of the totalBugs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalBugs() {
        return totalBugs;
    }

    /**
     * Sets the value of the totalBugs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalBugs(String value) {
        this.totalBugs = value;
    }

    /**
     * Gets the value of the totalTypes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalTypes() {
        return totalTypes;
    }

    /**
     * Sets the value of the totalTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalTypes(String value) {
        this.totalTypes = value;
    }

    /**
     * Gets the value of the totalSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalSize() {
        return totalSize;
    }

    /**
     * Sets the value of the totalSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalSize(String value) {
        this.totalSize = value;
    }

    /**
     * Gets the value of the priority1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority1() {
        return priority1;
    }

    /**
     * Sets the value of the priority1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority1(String value) {
        this.priority1 = value;
    }

}
