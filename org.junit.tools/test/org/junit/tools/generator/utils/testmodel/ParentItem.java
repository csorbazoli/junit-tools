package org.junit.tools.generator.utils.testmodel;

import java.util.List;

public class ParentItem extends AbstractNamedItem {

  private List<ChildItem> children;

  public List<ChildItem> getChildren() {
    return children;
  }

  public void setChildren(List<ChildItem> children) {
    this.children = children;
  }

}
