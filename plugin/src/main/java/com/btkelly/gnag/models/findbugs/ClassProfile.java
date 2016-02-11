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
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for ClassProfile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClassProfile">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalMilliseconds" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="invocations" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="avgMicrosecondsPerInvocation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="maxMicrosecondsPerInvocation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="standardDeviationMircosecondsPerInvocation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassProfile", propOrder = {
    "value"
})
public class ClassProfile {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "totalMilliseconds")
    protected String totalMilliseconds;
    @XmlAttribute(name = "invocations")
    protected String invocations;
    @XmlAttribute(name = "avgMicrosecondsPerInvocation")
    protected String avgMicrosecondsPerInvocation;
    @XmlAttribute(name = "maxMicrosecondsPerInvocation")
    protected String maxMicrosecondsPerInvocation;
    @XmlAttribute(name = "standardDeviationMircosecondsPerInvocation")
    protected String standardDeviationMircosecondsPerInvocation;

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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the totalMilliseconds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalMilliseconds() {
        return totalMilliseconds;
    }

    /**
     * Sets the value of the totalMilliseconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalMilliseconds(String value) {
        this.totalMilliseconds = value;
    }

    /**
     * Gets the value of the invocations property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvocations() {
        return invocations;
    }

    /**
     * Sets the value of the invocations property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvocations(String value) {
        this.invocations = value;
    }

    /**
     * Gets the value of the avgMicrosecondsPerInvocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvgMicrosecondsPerInvocation() {
        return avgMicrosecondsPerInvocation;
    }

    /**
     * Sets the value of the avgMicrosecondsPerInvocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvgMicrosecondsPerInvocation(String value) {
        this.avgMicrosecondsPerInvocation = value;
    }

    /**
     * Gets the value of the maxMicrosecondsPerInvocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxMicrosecondsPerInvocation() {
        return maxMicrosecondsPerInvocation;
    }

    /**
     * Sets the value of the maxMicrosecondsPerInvocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxMicrosecondsPerInvocation(String value) {
        this.maxMicrosecondsPerInvocation = value;
    }

    /**
     * Gets the value of the standardDeviationMircosecondsPerInvocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardDeviationMircosecondsPerInvocation() {
        return standardDeviationMircosecondsPerInvocation;
    }

    /**
     * Sets the value of the standardDeviationMircosecondsPerInvocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardDeviationMircosecondsPerInvocation(String value) {
        this.standardDeviationMircosecondsPerInvocation = value;
    }

}
