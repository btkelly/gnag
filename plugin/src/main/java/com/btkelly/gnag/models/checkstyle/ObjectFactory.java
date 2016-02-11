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
package com.btkelly.gnag.models.checkstyle;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.btkelly.gnag.models.checkstyle package. 
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

    private final static QName _Checkstyle_QNAME = new QName("", "checkstyle");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.btkelly.gnag.models.checkstyle
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Checkstyle }
     * 
     */
    public Checkstyle createCheckstyle() {
        return new Checkstyle();
    }

    /**
     * Create an instance of {@link Error }
     * 
     */
    public Error createError() {
        return new Error();
    }

    /**
     * Create an instance of {@link File }
     * 
     */
    public File createFile() {
        return new File();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Checkstyle }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "checkstyle")
    public JAXBElement<Checkstyle> createCheckstyle(Checkstyle value) {
        return new JAXBElement<Checkstyle>(_Checkstyle_QNAME, Checkstyle.class, null, value);
    }

}
