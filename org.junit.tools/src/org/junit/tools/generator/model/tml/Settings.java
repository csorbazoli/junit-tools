//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.22 at 02:08:56 AM CET 
//

package org.junit.tools.generator.model.tml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Settings complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Settings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="setUp" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="setUpBeforeClass" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="tearDown" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="tearDownBeforeClass" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="logger" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Settings", propOrder = {
	"setUp",
	"setUpBeforeClass",
	"tearDown",
	"tearDownBeforeClass",
	"logger",
})
public class Settings {

    protected boolean setUp;
    protected boolean setUpBeforeClass;
    protected boolean tearDown;
    protected boolean tearDownBeforeClass;
    protected boolean logger;

    /**
     * Gets the value of the setUp property.
     * 
     */
    public boolean isSetUp() {
	return setUp;
    }

    /**
     * Sets the value of the setUp property.
     * 
     */
    public void setSetUp(boolean value) {
	this.setUp = value;
    }

    /**
     * Gets the value of the setUpBeforeClass property.
     * 
     */
    public boolean isSetUpBeforeClass() {
	return setUpBeforeClass;
    }

    /**
     * Sets the value of the setUpBeforeClass property.
     * 
     */
    public void setSetUpBeforeClass(boolean value) {
	this.setUpBeforeClass = value;
    }

    /**
     * Gets the value of the tearDown property.
     * 
     */
    public boolean isTearDown() {
	return tearDown;
    }

    /**
     * Sets the value of the tearDown property.
     * 
     */
    public void setTearDown(boolean value) {
	this.tearDown = value;
    }

    /**
     * Gets the value of the tearDownBeforeClass property.
     * 
     */
    public boolean isTearDownBeforeClass() {
	return tearDownBeforeClass;
    }

    /**
     * Sets the value of the tearDownBeforeClass property.
     * 
     */
    public void setTearDownBeforeClass(boolean value) {
	this.tearDownBeforeClass = value;
    }

    /**
     * Gets the value of the logger property.
     * 
     */
    public boolean isLogger() {
	return logger;
    }

    /**
     * Sets the value of the logger property.
     * 
     */
    public void setLogger(boolean value) {
	this.logger = value;
    }

}
