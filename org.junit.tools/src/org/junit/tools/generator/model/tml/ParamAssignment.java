//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.22 at 02:08:56 AM CET 
//

package org.junit.tools.generator.model.tml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ParamAssignment complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ParamAssignment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="paramName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="assignment" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParamAssignment")
public class ParamAssignment {

    @XmlAttribute(name = "paramType")
    protected String paramType;
    @XmlAttribute(name = "paramName")
    protected String paramName;
    @XmlAttribute(name = "assignment")
    protected String assignment;

    public String getParamType() {
	return paramType;
    }

    public void setParamType(String value) {
	this.paramType = value;
    }

    /**
     * Gets the value of the paramName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getParamName() {
	return paramName;
    }

    /**
     * Sets the value of the paramName property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setParamName(String value) {
	this.paramName = value;
    }

    /**
     * Gets the value of the assignment property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAssignment() {
	return assignment;
    }

    /**
     * Sets the value of the assignment property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setAssignment(String value) {
	this.assignment = value;
    }

}
