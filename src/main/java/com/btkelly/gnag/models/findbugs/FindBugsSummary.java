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
 * <p>Java class for FindBugsSummary complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindBugsSummary">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FileStats" type="{}FileStats" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PackageStats" type="{}PackageStats" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FindBugsProfile" type="{}FindBugsProfile"/>
 *       &lt;/sequence>
 *       &lt;attribute name="timestamp" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="total_classes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="referenced_classes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="total_bugs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="total_size" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="num_packages" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="java_version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="vm_version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cpu_seconds" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="clock_seconds" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="peak_mbytes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="alloc_mbytes" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="gc_seconds" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="priority_1" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindBugsSummary", propOrder = {
    "fileStats",
    "packageStats",
    "findBugsProfile"
})
public class FindBugsSummary {

    @XmlElement(name = "FileStats")
    protected List<FileStats> fileStats;
    @XmlElement(name = "PackageStats")
    protected List<PackageStats> packageStats;
    @XmlElement(name = "FindBugsProfile", required = true)
    protected FindBugsProfile findBugsProfile;
    @XmlAttribute(name = "timestamp")
    protected String timestamp;
    @XmlAttribute(name = "total_classes")
    protected String totalClasses;
    @XmlAttribute(name = "referenced_classes")
    protected String referencedClasses;
    @XmlAttribute(name = "total_bugs")
    protected String totalBugs;
    @XmlAttribute(name = "total_size")
    protected String totalSize;
    @XmlAttribute(name = "num_packages")
    protected String numPackages;
    @XmlAttribute(name = "java_version")
    protected String javaVersion;
    @XmlAttribute(name = "vm_version")
    protected String vmVersion;
    @XmlAttribute(name = "cpu_seconds")
    protected String cpuSeconds;
    @XmlAttribute(name = "clock_seconds")
    protected String clockSeconds;
    @XmlAttribute(name = "peak_mbytes")
    protected String peakMbytes;
    @XmlAttribute(name = "alloc_mbytes")
    protected String allocMbytes;
    @XmlAttribute(name = "gc_seconds")
    protected String gcSeconds;
    @XmlAttribute(name = "priority_1")
    protected String priority1;

    /**
     * Gets the value of the fileStats property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fileStats property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFileStats().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FileStats }
     * 
     * 
     */
    public List<FileStats> getFileStats() {
        if (fileStats == null) {
            fileStats = new ArrayList<FileStats>();
        }
        return this.fileStats;
    }

    /**
     * Gets the value of the packageStats property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the packageStats property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPackageStats().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PackageStats }
     * 
     * 
     */
    public List<PackageStats> getPackageStats() {
        if (packageStats == null) {
            packageStats = new ArrayList<PackageStats>();
        }
        return this.packageStats;
    }

    /**
     * Gets the value of the findBugsProfile property.
     * 
     * @return
     *     possible object is
     *     {@link FindBugsProfile }
     *     
     */
    public FindBugsProfile getFindBugsProfile() {
        return findBugsProfile;
    }

    /**
     * Sets the value of the findBugsProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link FindBugsProfile }
     *     
     */
    public void setFindBugsProfile(FindBugsProfile value) {
        this.findBugsProfile = value;
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
     * Gets the value of the totalClasses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalClasses() {
        return totalClasses;
    }

    /**
     * Sets the value of the totalClasses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalClasses(String value) {
        this.totalClasses = value;
    }

    /**
     * Gets the value of the referencedClasses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferencedClasses() {
        return referencedClasses;
    }

    /**
     * Sets the value of the referencedClasses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferencedClasses(String value) {
        this.referencedClasses = value;
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
     * Gets the value of the numPackages property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumPackages() {
        return numPackages;
    }

    /**
     * Sets the value of the numPackages property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumPackages(String value) {
        this.numPackages = value;
    }

    /**
     * Gets the value of the javaVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJavaVersion() {
        return javaVersion;
    }

    /**
     * Sets the value of the javaVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJavaVersion(String value) {
        this.javaVersion = value;
    }

    /**
     * Gets the value of the vmVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVmVersion() {
        return vmVersion;
    }

    /**
     * Sets the value of the vmVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVmVersion(String value) {
        this.vmVersion = value;
    }

    /**
     * Gets the value of the cpuSeconds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpuSeconds() {
        return cpuSeconds;
    }

    /**
     * Sets the value of the cpuSeconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpuSeconds(String value) {
        this.cpuSeconds = value;
    }

    /**
     * Gets the value of the clockSeconds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClockSeconds() {
        return clockSeconds;
    }

    /**
     * Sets the value of the clockSeconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClockSeconds(String value) {
        this.clockSeconds = value;
    }

    /**
     * Gets the value of the peakMbytes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeakMbytes() {
        return peakMbytes;
    }

    /**
     * Sets the value of the peakMbytes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeakMbytes(String value) {
        this.peakMbytes = value;
    }

    /**
     * Gets the value of the allocMbytes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAllocMbytes() {
        return allocMbytes;
    }

    /**
     * Sets the value of the allocMbytes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAllocMbytes(String value) {
        this.allocMbytes = value;
    }

    /**
     * Gets the value of the gcSeconds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGcSeconds() {
        return gcSeconds;
    }

    /**
     * Sets the value of the gcSeconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGcSeconds(String value) {
        this.gcSeconds = value;
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
