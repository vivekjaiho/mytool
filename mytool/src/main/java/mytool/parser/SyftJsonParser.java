package main.java.mytool.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * A "parser" that does NOT actually parse JSON fields.
 * It just stores the raw SBOM text in SBOMData.
 */
public class SyftJsonParser implements SBOMParser {


  @Override
  public SBOMData parse(String rawText) throws Exception {
    // 1) Use Jackson to parse the entire SBOM JSON
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(rawText);

    // 2) Put the parsed tree into SBOMData
    SBOMData data = new SBOMData();
    data.setRoot(root);

    return data;
  }
}