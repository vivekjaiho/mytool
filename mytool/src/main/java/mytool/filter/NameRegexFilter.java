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
 * Works for both SPDX (artifacts/packages) and CycloneDX (components).
 */
public class NameRegexFilter implements SBOMFilter {

  private final Pattern pattern;

  public NameRegexFilter(String regex) {
    // case-insensitive
    this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
  }

  @Override
  public void apply(SBOMData sbomData) {
    JsonNode root = sbomData.getRoot();
    if (root == null) {
      return; // nothing to filter
    }

    // 1) Locate the array where Syft places package data:
    //    'artifacts', 'packages', or 'components'
    ArrayNode packagesArray = locatePackagesArray(root);
    if (packagesArray == null) {
      // Not found or not an array => nothing to filter
      return;
    }

    // 2) Collect only the packages/components that match the regex
    List<JsonNode> matched = new ArrayList<>();
    for (JsonNode pkg : packagesArray) {
      // Each pkg typically has a "name" field
      String pkgName = pkg.has("name") ? pkg.get("name").asText("") : "";
      if (pattern.matcher(pkgName).find()) {
        matched.add(pkg);
      }
    }

    // 3) Build a new array with only those matched items
    ArrayNode newArray = packagesArray.arrayNode();
    newArray.addAll(matched);

    // 4) Replace the old array in the JSON tree with this filtered array
    //    We check which key was actually found
    if (root.has("artifacts") && root.get("artifacts").isArray()) {
      ((ObjectNode) root).set("artifacts", newArray);
    } else if (root.has("packages") && root.get("packages").isArray()) {
      ((ObjectNode) root).set("packages", newArray);
    } else if (root.has("components") && root.get("components").isArray()) {
      ((ObjectNode) root).set("components", newArray);
    }

    // Now 'root' is updated to only contain the matched packages (or components).
  }

  /**
   * Helper to find 'artifacts', 'packages', or 'components' array in the root.
   */
  private ArrayNode locatePackagesArray(JsonNode root) {
    // Check 'artifacts' first
    if (root.has("artifacts") && root.get("artifacts").isArray()) {
      return (ArrayNode) root.get("artifacts");
    }
    // Check 'packages'
    //  if (root.has("packages") && root.get("packages").isArray()) {
    //    return (ArrayNode) root.get("packages");
    //  }
    // Check 'components' (CycloneDX)
    if (root.has("components") && root.get("components").isArray()) {
      return (ArrayNode) root.get("components");
    }
    return null;
  }
}