package main.java.mytool.parser;

public interface SBOMParser {
  /**
   * Parse raw SBOM text (usually JSON) into SBOMData.
   */
  SBOMData parse(String rawSBOM) throws Exception;
}
