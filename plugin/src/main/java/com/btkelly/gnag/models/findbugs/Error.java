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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Error complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Error">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ErrorMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Exception" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StackTrace" maxOccurs="unbounded" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="edu.umd.cs.findbugs.classfile.impl.ZipFileCodeBase.&lt;init>(ZipFileCodeBase.java:87)"/>
 *               &lt;enumeration value="edu.umd.cs.findbugs.classfile.impl.ZipCodeBaseFactory.makeZipCodeBase(ZipCodeBaseFactory.java:39)"/>
 *               &lt;enumeration value="edu.umd.cs.findbugs.classfile.impl.ClassFactory.createFilesystemCodeBase(ClassFactory.java:121)"/>
 *               &lt;enumeration value="edu.umd.cs.findbugs.classfile.impl.FilesystemCodeBaseLocator.openCodeBase(FilesystemCodeBaseLocator.java:77)"/>
 *               &lt;enumeration value="edu.umd.cs.findbugs.classfile.impl.ClassPathBuilder.processWorkList(ClassPathBuilder.java:607)"/>
 *               &lt;enumeration value="edu.umd.cs.findbugs.classfile.impl.ClassPathBuilder.build(ClassPathBuilder.java:226)"/>
 *               &lt;enumeration value="edu.umd.cs.findbugs.FindBugs2.buildClassPath(FindBugs2.java:677)"/>
 *               &lt;enumeration value="edu.umd.cs.findbugs.FindBugs2.execute(FindBugs2.java:218)"/>
 *               &lt;enumeration value="edu.umd.cs.findbugs.FindBugs.runMain(FindBugs.java:402)"/>
 *               &lt;enumeration value="edu.umd.cs.findbugs.FindBugs2.main(FindBugs2.java:1200)"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Error", propOrder = {
    "errorMessage",
    "exception",
    "stackTrace"
})
public class Error {

    @XmlElement(name = "ErrorMessage", required = true)
    protected String errorMessage;
    @XmlElement(name = "Exception", required = true)
    protected String exception;
    @XmlElement(name = "StackTrace")
    protected List<String> stackTrace;

    /**
     * Gets the value of the errorMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

    /**
     * Gets the value of the exception property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getException() {
        return exception;
    }

    /**
     * Sets the value of the exception property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setException(String value) {
        this.exception = value;
    }

    /**
     * Gets the value of the stackTrace property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stackTrace property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStackTrace().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getStackTrace() {
        if (stackTrace == null) {
            stackTrace = new ArrayList<String>();
        }
        return this.stackTrace;
    }

}
