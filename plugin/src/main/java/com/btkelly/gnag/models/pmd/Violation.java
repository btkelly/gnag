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
package com.btkelly.gnag.models.pmd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for violation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="violation">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="beginline" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="endline" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="begincolumn" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="endcolumn" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rule" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ruleset" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="package" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="method" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="externalInfoUrl" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="priority" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "violation", propOrder = {
    "value"
})
public class Violation {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "beginline")
    protected String beginline;
    @XmlAttribute(name = "endline")
    protected String endline;
    @XmlAttribute(name = "begincolumn")
    protected String begincolumn;
    @XmlAttribute(name = "endcolumn")
    protected String endcolumn;
    @XmlAttribute(name = "rule")
    protected String rule;
    @XmlAttribute(name = "ruleset")
    protected String ruleset;
    @XmlAttribute(name = "package")
    protected String _package;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "method")
    protected String method;
    @XmlAttribute(name = "externalInfoUrl")
    protected String externalInfoUrl;
    @XmlAttribute(name = "priority")
    protected String priority;

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
     * Gets the value of the beginline property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeginline() {
        return beginline;
    }

    /**
     * Sets the value of the beginline property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeginline(String value) {
        this.beginline = value;
    }

    /**
     * Gets the value of the endline property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndline() {
        return endline;
    }

    /**
     * Sets the value of the endline property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndline(String value) {
        this.endline = value;
    }

    /**
     * Gets the value of the begincolumn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBegincolumn() {
        return begincolumn;
    }

    /**
     * Sets the value of the begincolumn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBegincolumn(String value) {
        this.begincolumn = value;
    }

    /**
     * Gets the value of the endcolumn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndcolumn() {
        return endcolumn;
    }

    /**
     * Sets the value of the endcolumn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndcolumn(String value) {
        this.endcolumn = value;
    }

    /**
     * Gets the value of the rule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRule() {
        return rule;
    }

    /**
     * Sets the value of the rule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRule(String value) {
        this.rule = value;
    }

    /**
     * Gets the value of the ruleset property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleset() {
        return ruleset;
    }

    /**
     * Sets the value of the ruleset property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleset(String value) {
        this.ruleset = value;
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
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClazz(String value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethod(String value) {
        this.method = value;
    }

    /**
     * Gets the value of the externalInfoUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalInfoUrl() {
        return externalInfoUrl;
    }

    /**
     * Sets the value of the externalInfoUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalInfoUrl(String value) {
        this.externalInfoUrl = value;
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

}
