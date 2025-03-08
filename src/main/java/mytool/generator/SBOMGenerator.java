package main.java.mytool.generator;

public interface SBOMGenerator {
  /**
   * Generate an SBOM for the given source in the specified format.
   * Returns raw SBOM text (likely JSON).
   */
  String generateSBOM(String source, SBOMFormat format) throws Exception;
}
