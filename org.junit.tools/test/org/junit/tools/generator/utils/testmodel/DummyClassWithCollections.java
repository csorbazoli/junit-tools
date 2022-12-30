package org.junit.tools.generator.utils.testmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DummyClassWithCollections implements Serializable {
  private static final long serialVersionUID = 8485569432319507040L;

  private final List<String> names = new ArrayList<>();
  private final Set<Integer> numbers = new TreeSet<>();
  private final Map<String, Integer> map = new TreeMap<>();

  public DummyClassWithCollections() {
    // default constructor
  }

  public List<String> getNames() {
    return names;
  }

  public Set<Integer> getNumbers() {
    return numbers;
  }

  public Map<String, Integer> getMap() {
    return map;
  }

}
