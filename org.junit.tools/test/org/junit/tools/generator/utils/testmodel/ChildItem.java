package org.junit.tools.generator.utils.testmodel;

public class ChildItem extends AbstractNamedItem {

  private ParentItem myParent;
  private ChildItem sibling;

  public ParentItem getMyParent() {
    return myParent;
  }

  public void setMyParent(ParentItem myParent) {
    this.myParent = myParent;
  }

  public ChildItem getSibling() {
    return sibling;
  }

  public void setSibling(ChildItem sibling) {
    this.sibling = sibling;
  }

}
