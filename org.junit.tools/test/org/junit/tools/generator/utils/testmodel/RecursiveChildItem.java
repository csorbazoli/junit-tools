package org.junit.tools.generator.utils.testmodel;

import java.util.LinkedList;
import java.util.List;

public class RecursiveChildItem {

    private String id;
    private List<RecursiveChildItem> children = new LinkedList<>();

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public List<RecursiveChildItem> getChildren() {
	return children;
    }

    public void setChildren(List<RecursiveChildItem> children) {
	this.children = children;
    }

}
