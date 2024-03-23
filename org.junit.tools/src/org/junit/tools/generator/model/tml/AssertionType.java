//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.22 at 02:08:56 AM CET 
//

package org.junit.tools.generator.model.tml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for AssertionType.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="AssertionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EQUALS"/>
 *     &lt;enumeration value="IS_TRUE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AssertionType")
@XmlEnum
public enum AssertionType {

    EQUALS("isEqualTo", "assertEquals"),
    TESTFILEEQUALS("TestUtils.assertTestFileEquals", "TestUtils.assertTestFileEquals"),
    IS_TRUE("isTrue", "assertTrue"),
    IS_NOT_EMPTY("isNotEmpty", "assertFalse"),
    ;

    private final String method;
    private final String legacyMethod;

    AssertionType(String method, String legacyMethod) {
	this.method = method;
	this.legacyMethod = legacyMethod;
    }

    public String value() {
	return name();
    }

    public String getMethod() {
	return method;
    }

    public String getLegacyMethod() {
	return legacyMethod;
    }

    public static AssertionType fromValue(String v) {
	return valueOf(v);
    }

}
