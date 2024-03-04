//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.22 at 02:08:56 AM CET 
//

package org.junit.tools.generator.model.tml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="settings" type="{http://www.junit-tools.org/tml/tml.xsd}Settings"/>
 *         &lt;element name="testBases" type="{http://www.junit-tools.org/tml/tml.xsd}TestBases"/>
 *         &lt;element name="method" type="{http://www.junit-tools.org/tml/tml.xsd}Method" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="testBase" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="testClass" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="testPrio" type="{http://www.junit-tools.org/tml/tml.xsd}Testprio" />
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="spring" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	"settings",
	"testBases",
	"method"
})
@XmlRootElement(name = "test")
public class Test {

    @XmlElement(required = true)
    protected Settings settings;
    @XmlElement(required = true)
    protected TestBases testBases;
    @XmlElement(required = true)
    protected List<Method> method;
    @XmlAttribute(name = "testBase")
    protected String testBase;
    @XmlAttribute(name = "testClass")
    protected String testClass;
    @XmlAttribute(name = "version")
    protected String version;
    @XmlAttribute(name = "spring")
    protected boolean spring;
    @XmlAttribute(name = "onlyStaticMethods")
    private boolean onlyStaticMethods;

    /**
     * Gets the value of the settings property.
     * 
     * @return possible object is {@link Settings }
     * 
     */
    public Settings getSettings() {
	return settings;
    }

    /**
     * Sets the value of the settings property.
     * 
     * @param value allowed object is {@link Settings }
     * 
     */
    public void setSettings(Settings value) {
	this.settings = value;
    }

    /**
     * Gets the value of the testBases property.
     * 
     * @return possible object is {@link TestBases }
     * 
     */
    public TestBases getTestBases() {
	return testBases;
    }

    /**
     * Sets the value of the testBases property.
     * 
     * @param value allowed object is {@link TestBases }
     * 
     */
    public void setTestBases(TestBases value) {
	this.testBases = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present
     * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
     * for the method property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Method }
     * 
     * 
     */
    public List<Method> getMethod() {
	if (method == null) {
	    method = new ArrayList<Method>();
	}
	return this.method;
    }

    /**
     * Gets the value of the testBase property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTestBase() {
	return testBase;
    }

    /**
     * Sets the value of the testBase property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setTestBase(String value) {
	this.testBase = value;
    }

    /**
     * Gets the value of the testClass property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTestClass() {
	return testClass;
    }

    /**
     * Sets the value of the testClass property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setTestClass(String value) {
	this.testClass = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getVersion() {
	return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setVersion(String value) {
	this.version = value;
    }

    public boolean isSpring() {
	return spring;
    }

    public void setSpring(boolean spring) {
	this.spring = spring;
    }

    /**
     * All methods to generate tests for are static (or constructors)
     */
    public boolean isOnlyStaticMethods() {
	return onlyStaticMethods;
    }

    public void setOnlyStaticMethods(boolean value) {
	this.onlyStaticMethods = value;
    }

}
