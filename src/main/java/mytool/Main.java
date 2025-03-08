package main.java.mytool;

import java.io.IOException;
import main.java.mytool.generator.*;
import main.java.mytool.parser.*;
import main.java.mytool.filter.*;
import main.java.mytool.renderer.*;

import java.io.FileWriter;


/**
 * Main entry point CLI.
 * Usage Example:
 *   java -jar mytool-1.0.0-all.jar --source nginx --format spdx --search "openssl.*" --output sbom.json
 */
public class Main {

  public static void main(String[] args) {
    String source = null;
    String formatStr = "spdx"; // default
    String searchPattern = null;
    String outputFile = null;

    // Parse arguments
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "--source":
          if (i + 1 < args.length) source = args[++i];
          break;
        case "--format":
          if (i + 1 < args.length) formatStr = args[++i];
          break;
        case "--search":
          if (i + 1 < args.length) searchPattern = args[++i];
          break;
        case "--output":
        case "-o":
          if (i + 1 < args.length) outputFile = args[++i];
          break;
        default:
          // ignore or handle unknown arguments
          break;
      }
    }

    if (source == null) {
      System.err.println("Error: --source is required.");
      System.exit(1);
    }

    SBOMFormat sbomFormat = parseFormat(formatStr);

    try {
      processSBOM(source, sbomFormat, searchPattern, outputFile);
    } catch (Exception e) {
      handleError(e);
    }
  }

  private static void processSBOM(String source, SBOMFormat sbomFormat, String searchPattern, String outputFile)
      throws Exception {
    String rawSBOM = generateSBOM(source, sbomFormat);
    SBOMData sbomData = parseSBOM(rawSBOM);

    if (searchPattern != null && !searchPattern.isEmpty()) {
      applyFilter(sbomData, searchPattern);
    }

    String finalOutput = renderSBOM(sbomData);
    writeOutput(finalOutput, outputFile);
  }

  private static SBOMFormat parseFormat(String formatStr) {
    if ("cyclonedx".equalsIgnoreCase(formatStr)) {
      return SBOMFormat.CYCLONEDX_JSON;
    }
    return SBOMFormat.SPDX_JSON;
  }

  private static String generateSBOM(String source, SBOMFormat sbomFormat) throws Exception {
    SBOMGenerator generator = new SyftSBOMGenerator();
    return generator.generateSBOM(source, sbomFormat);
  }

  private static SBOMData parseSBOM(String rawSBOM) throws Exception {
    SBOMParser parser = new SyftJsonParser();
    return parser.parse(rawSBOM);
  }

  private static void applyFilter(SBOMData sbomData, String searchPattern) {
    SBOMFilter nameFilter = new NameRegexFilter(searchPattern);
    nameFilter.apply(sbomData);
  }

  private static String renderSBOM(SBOMData sbomData) throws Exception {
    SBOMRenderer renderer = new JsonSBOMRenderer();
    return renderer.render(sbomData);
  }

  private static void writeOutput(String finalOutput, String outputFile) throws IOException {
    if (outputFile != null) {
      try (FileWriter fw = new FileWriter(outputFile)) {
        fw.write(finalOutput);
        System.out.println("SBOM written to " + outputFile);
      }
    } else {
      System.out.println(finalOutput);
    }
  }

  private static void handleError(Exception e) {
    System.err.println("Error: " + e.getMessage());
    e.printStackTrace();
    System.exit(1);
  }
}