package main.java.mytool.renderer;

import main.java.mytool.parser.SBOMData;

/**
 * Converts SBOMData into a final output string (JSON, text, etc.).
 */
public interface SBOMRenderer {
  String render(SBOMData data) throws Exception;
}
