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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Method complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Method">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="param" type="{http://www.junit-tools.org/tml/tml.xsd}Param" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="paramsDefault" type="{http://www.junit-tools.org/tml/tml.xsd}ParamAssignment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="preconditions" type="{http://www.junit-tools.org/tml/tml.xsd}Precondition" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="testCase" type="{http://www.junit-tools.org/tml/tml.xsd}TestCase" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="result" type="{http://www.junit-tools.org/tml/tml.xsd}Result"/>
 *         &lt;element name="assertion" type="{http://www.junit-tools.org/tml/tml.xsd}Assertion" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="annotations" type="{http://www.junit-tools.org/tml/tml.xsd}Annotation" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="modifier" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="static" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="signature" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Method", propOrder = {
	"param",
	"paramsDefault",
	"preconditions",
	"testCase",
	"result",
	"assertion",
	"annotations"
})
public class Method {

    protected List<Param> param;
    protected List<ParamAssignment> paramsDefault;
    protected List<Precondition> preconditions;
    protected List<TestCase> testCase;
    @XmlElement(required = true)
    protected Result result;
    protected List<Assertion> assertion;
    protected List<Annotation> annotations;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "modifier")
    protected String modifier;
    @XmlAttribute(name = "static")
    protected Boolean _static;
    @XmlAttribute(name = "signature")
    protected String signature;

    /**
     * Gets the value of the param property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present
     * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
     * for the param property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Param }
     * 
     * 
     */
    public List<Param> getParam() {
	if (param == null) {
	    param = new ArrayList<Param>();
	}
	return this.param;
    }

    /**
     * Gets the value of the paramsDefault property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present
     * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
     * for the paramsDefault property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getParamsDefault().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParamAssignment }
     * 
     * 
     */
    public List<ParamAssignment> getParamsDefault() {
	if (paramsDefault == null) {
	    paramsDefault = new ArrayList<ParamAssignment>();
	}
	return this.paramsDefault;
    }

    /**
     * Gets the value of the preconditions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present
     * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
     * for the preconditions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getPreconditions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Precondition
     * }
     * 
     * 
     */
    public List<Precondition> getPreconditions() {
	if (preconditions == null) {
	    preconditions = new ArrayList<Precondition>();
	}
	return this.preconditions;
    }

    /**
     * Gets the value of the testCase property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present
     * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
     * for the testCase property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getTestCase().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link TestCase }
     * 
     * 
     */
    public List<TestCase> getTestCase() {
	if (testCase == null) {
	    testCase = new ArrayList<TestCase>();
	}
	return this.testCase;
    }

    /**
     * Gets the value of the result property.
     * 
     * @return possible object is {@link Result }
     * 
     */
    public Result getResult() {
	return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value allowed object is {@link Result }
     * 
     */
    public void setResult(Result value) {
	this.result = value;
    }

    /**
     * Gets the value of the assertion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present
     * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
     * for the assertion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getAssertion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Assertion }
     * 
     * 
     */
    public List<Assertion> getAssertion() {
	if (assertion == null) {
	    assertion = new ArrayList<Assertion>();
	}
	return this.assertion;
    }

    public List<Annotation> getAnnotations() {
	if (annotations == null) {
	    annotations = new ArrayList<>();
	}
	return this.annotations;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setName(String value) {
	this.name = value;
    }

    /**
     * Gets the value of the modifier property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getModifier() {
	return modifier;
    }

    /**
     * Sets the value of the modifier property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setModifier(String value) {
	this.modifier = value;
    }

    /**
     * Gets the value of the static property.
     * 
     * @return possible object is {@link Boolean }
     * 
     */
    public Boolean isStatic() {
	return _static;
    }

    /**
     * Sets the value of the static property.
     * 
     * @param value allowed object is {@link Boolean }
     * 
     */
    public void setStatic(Boolean value) {
	this._static = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSignature() {
	return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setSignature(String value) {
	this.signature = value;
    }

}
