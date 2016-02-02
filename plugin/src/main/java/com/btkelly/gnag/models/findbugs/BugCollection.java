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

import com.btkelly.gnag.models.Report;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for BugCollection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BugCollection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Project" type="{}Project"/>
 *         &lt;element name="BugInstance" type="{}BugInstance" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BugCategory" type="{}BugCategory" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BugPattern" type="{}BugPattern" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BugCode" type="{}BugCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Errors" type="{}Errors"/>
 *         &lt;element name="FindBugsSummary" type="{}FindBugsSummary"/>
 *         &lt;element name="ClassFeatures" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="History" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sequence" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="timestamp" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="analysisTimestamp" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="release" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BugCollection", propOrder = {
    "project",
    "bugInstance",
    "bugCategory",
    "bugPattern",
    "bugCode",
    "errors",
    "findBugsSummary",
    "classFeatures",
    "history"
})
@XmlRootElement(name = "BugCollection")
public class BugCollection implements Report {

    @XmlElement(name = "Project", required = true)
    protected Project project;
    @XmlElement(name = "BugInstance")
    protected List<BugInstance> bugInstance;
    @XmlElement(name = "BugCategory")
    protected List<BugCategory> bugCategory;
    @XmlElement(name = "BugPattern")
    protected List<BugPattern> bugPattern;
    @XmlElement(name = "BugCode")
    protected List<BugCode> bugCode;
    @XmlElement(name = "Errors", required = true)
    protected Errors errors;
    @XmlElement(name = "FindBugsSummary", required = true)
    protected FindBugsSummary findBugsSummary;
    @XmlElement(name = "ClassFeatures", required = true)
    protected String classFeatures;
    @XmlElement(name = "History", required = true)
    protected String history;
    @XmlAttribute(name = "version")
    protected String version;
    @XmlAttribute(name = "sequence")
    protected String sequence;
    @XmlAttribute(name = "timestamp")
    protected String timestamp;
    @XmlAttribute(name = "analysisTimestamp")
    protected String analysisTimestamp;
    @XmlAttribute(name = "release")
    protected String release;

    /**
     * Gets the value of the project property.
     * 
     * @return
     *     possible object is
     *     {@link Project }
     *     
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the value of the project property.
     * 
     * @param value
     *     allowed object is
     *     {@link Project }
     *     
     */
    public void setProject(Project value) {
        this.project = value;
    }

    /**
     * Gets the value of the bugInstance property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bugInstance property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBugInstance().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BugInstance }
     * 
     * 
     */
    public List<BugInstance> getBugInstance() {
        if (bugInstance == null) {
            bugInstance = new ArrayList<BugInstance>();
        }
        return this.bugInstance;
    }

    /**
     * Gets the value of the bugCategory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bugCategory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBugCategory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BugCategory }
     * 
     * 
     */
    public List<BugCategory> getBugCategory() {
        if (bugCategory == null) {
            bugCategory = new ArrayList<BugCategory>();
        }
        return this.bugCategory;
    }

    /**
     * Gets the value of the bugPattern property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bugPattern property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBugPattern().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BugPattern }
     * 
     * 
     */
    public List<BugPattern> getBugPattern() {
        if (bugPattern == null) {
            bugPattern = new ArrayList<BugPattern>();
        }
        return this.bugPattern;
    }

    /**
     * Gets the value of the bugCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bugCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBugCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BugCode }
     * 
     * 
     */
    public List<BugCode> getBugCode() {
        if (bugCode == null) {
            bugCode = new ArrayList<BugCode>();
        }
        return this.bugCode;
    }

    /**
     * Gets the value of the errors property.
     * 
     * @return
     *     possible object is
     *     {@link Errors }
     *     
     */
    public Errors getErrors() {
        return errors;
    }

    /**
     * Sets the value of the errors property.
     * 
     * @param value
     *     allowed object is
     *     {@link Errors }
     *     
     */
    public void setErrors(Errors value) {
        this.errors = value;
    }

    /**
     * Gets the value of the findBugsSummary property.
     * 
     * @return
     *     possible object is
     *     {@link FindBugsSummary }
     *     
     */
    public FindBugsSummary getFindBugsSummary() {
        return findBugsSummary;
    }

    /**
     * Sets the value of the findBugsSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link FindBugsSummary }
     *     
     */
    public void setFindBugsSummary(FindBugsSummary value) {
        this.findBugsSummary = value;
    }

    /**
     * Gets the value of the classFeatures property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassFeatures() {
        return classFeatures;
    }

    /**
     * Sets the value of the classFeatures property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassFeatures(String value) {
        this.classFeatures = value;
    }

    /**
     * Gets the value of the history property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHistory() {
        return history;
    }

    /**
     * Sets the value of the history property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHistory(String value) {
        this.history = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSequence(String value) {
        this.sequence = value;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestamp(String value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the analysisTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalysisTimestamp() {
        return analysisTimestamp;
    }

    /**
     * Sets the value of the analysisTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalysisTimestamp(String value) {
        this.analysisTimestamp = value;
    }

    /**
     * Gets the value of the release property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelease() {
        return release;
    }

    /**
     * Sets the value of the release property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelease(String value) {
        this.release = value;
    }

    @Override
    public boolean shouldFailBuild() {
        return bugInstance != null && bugInstance.size() > 0;
    }
}
