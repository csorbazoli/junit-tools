package org.junit.tools.generator.utils.testmodel;

public class DummyClassWithDeprecatedFields {
    private String name;
    @Deprecated
    private int number;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getNumber() {
	return number;
    }

    public void setNumber(int number) {
	this.number = number;
    }

}
