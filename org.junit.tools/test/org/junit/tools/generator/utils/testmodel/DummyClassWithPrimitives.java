package org.junit.tools.generator.utils.testmodel;

import java.io.Serializable;

public class DummyClassWithPrimitives implements Serializable {

    private static final long serialVersionUID = -3683335311790805496L;

    private int numericInteger;
    private boolean logical;
    private double numericDouble;
    private float numericFloat;
    private byte numericByte;
    private long numericLong;
    private char character;

    public DummyClassWithPrimitives() {
	// default constructor
    }

    public int getNumericInteger() {
	return numericInteger;
    }

    public void setNumericInteger(int numericInteger) {
	this.numericInteger = numericInteger;
    }

    public boolean isLogical() {
	return logical;
    }

    public void setLogical(boolean logical) {
	this.logical = logical;
    }

    public double getNumericDouble() {
	return numericDouble;
    }

    public void setNumericDouble(double numericDouble) {
	this.numericDouble = numericDouble;
    }

    public float getNumericFloat() {
	return numericFloat;
    }

    public void setNumericFloat(float numericFloat) {
	this.numericFloat = numericFloat;
    }

    public byte getNumericByte() {
	return numericByte;
    }

    public void setNumericByte(byte numericByte) {
	this.numericByte = numericByte;
    }

    public long getNumericLong() {
	return numericLong;
    }

    public void setNumericLong(long numericLong) {
	this.numericLong = numericLong;
    }

    public char getCharacter() {
	return character;
    }

    public void setCharacter(char character) {
	this.character = character;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

}
