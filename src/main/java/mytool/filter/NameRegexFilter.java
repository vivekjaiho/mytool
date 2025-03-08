package main.java.mytool.filter;

import main.java.mytool.parser.SBOMData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A filter that keeps only packages (or components) whose 'name' matches the user's regex.
 * Works for both SPDX (artifacts) and CycloneDX (components).
 */
public class NameRegexFilter implements SBOMFilter {

  private final Pattern pattern;

  public NameRegexFilter(String regex) {
    this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
  }

  @Override
  public void apply(SBOMData sbomData) {
    JsonNode root = sbomData.getRoot();
    if (root == null) {
      return; // nothing to filter
    }

    // Locate the array where Syft places package data:
    ArrayNode packagesArray = locatePackagesArray(root);
    if (packagesArray == null) {
      return;
    }

    // Collect only the packages/components that match the regex
    List<JsonNode> matched = new ArrayList<>();
    for (JsonNode pkg : packagesArray) {
      String pkgName = pkg.has("name") ? pkg.get("name").asText("") : "";
      if (pattern.matcher(pkgName).matches()) {
        matched.add(pkg);
      }
    }

    // 3) Build a new array with only those matched items
    ArrayNode newArray = packagesArray.arrayNode();
    newArray.addAll(matched);

    if (root.has("artifacts") && root.get("artifacts").isArray()) {
      ((ObjectNode) root).set("artifacts", newArray);
    } else if (root.has("packages") && root.get("packages").isArray()) {
      ((ObjectNode) root).set("packages", newArray);
    } else if (root.has("components") && root.get("components").isArray()) {
      ((ObjectNode) root).set("components", newArray);
    }

  }

  /**
   * Helper to find 'artifacts' or 'components' array in the root.
   */
  private ArrayNode locatePackagesArray(JsonNode root) {
    // Check 'artifacts' first
    if (root.has("artifacts") && root.get("artifacts").isArray()) {
      return (ArrayNode) root.get("artifacts");
    }
    if(root.has("packages") && root.get("packages").isArray()) {
      return (ArrayNode) root.get("packages");
    }
    // Check 'components' (CycloneDX)
    if (root.has("components") && root.get("components").isArray()) {
      return (ArrayNode) root.get("components");
    }
    return null;
  }
}