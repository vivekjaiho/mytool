package main.java.mytool.parser;
import com.fasterxml.jackson.databind.JsonNode;


/**
 * Holds the raw SBOM text (and optionally filtered text).
 */
public class SBOMData {
  // The entire parsed JSON tree
  private JsonNode root;

  public JsonNode getRoot() {
    return root;
  }

  public void setRoot(JsonNode root) {
    this.root = root;
  }
}