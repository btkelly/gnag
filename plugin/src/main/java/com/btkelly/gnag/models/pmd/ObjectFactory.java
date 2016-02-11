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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.btkelly.gnag.models.pmd package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Pmd_QNAME = new QName("", "pmd");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.btkelly.gnag.models.pmd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Pmd }
     * 
     */
    public Pmd createPmd() {
        return new Pmd();
    }

    /**
     * Create an instance of {@link Violation }
     * 
     */
    public Violation createViolation() {
        return new Violation();
    }

    /**
     * Create an instance of {@link File }
     * 
     */
    public File createFile() {
        return new File();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Pmd }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pmd")
    public JAXBElement<Pmd> createPmd(Pmd value) {
        return new JAXBElement<Pmd>(_Pmd_QNAME, Pmd.class, null, value);
    }

}
