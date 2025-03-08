package main.java.mytool.filter;

import main.java.mytool.parser.SBOMData;

/**
 * Defines how we filter or transform an SBOMData object.
 */
public interface SBOMFilter {
  void apply(SBOMData sbomData);
}
