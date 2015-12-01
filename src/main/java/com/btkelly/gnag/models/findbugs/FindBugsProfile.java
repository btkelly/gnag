
package com.btkelly.gnag.models.findbugs;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FindBugsProfile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindBugsProfile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ClassProfile" type="{}ClassProfile" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindBugsProfile", propOrder = {
    "classProfile"
})
public class FindBugsProfile {

    @XmlElement(name = "ClassProfile")
    protected List<ClassProfile> classProfile;

    /**
     * Gets the value of the classProfile property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classProfile property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassProfile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassProfile }
     * 
     * 
     */
    public List<ClassProfile> getClassProfile() {
        if (classProfile == null) {
            classProfile = new ArrayList<ClassProfile>();
        }
        return this.classProfile;
    }

}
