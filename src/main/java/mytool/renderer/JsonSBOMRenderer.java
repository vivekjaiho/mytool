package main.java.mytool.renderer;

import main.java.mytool.parser.SBOMData;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Renders SBOMData as JSON string.
 */
public class JsonSBOMRenderer implements SBOMRenderer {

  @Override
  public String render(SBOMData data) throws Exception {
    if (data.getRoot() == null) {
      return "{}"; // or some fallback
    }
    // Re-serialize the updated JSON
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data.getRoot());
  }
}
