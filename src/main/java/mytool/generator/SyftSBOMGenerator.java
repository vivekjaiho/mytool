package main.java.mytool.generator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SyftSBOMGenerator implements SBOMGenerator {

  @Override
  public String generateSBOM(String source, SBOMFormat format) throws Exception {
    // Map format enum to syft's -o arg
    String syftArg = (format == SBOMFormat.CYCLONEDX_JSON) ? "cyclonedx-json" : "spdx-json";

    List<String> cmd = new ArrayList<>();
    cmd.add("syft");
    cmd.add(source);
    cmd.add("-o");
    cmd.add(syftArg);

    ProcessBuilder pb = new ProcessBuilder(cmd);
    Process process = pb.start();

    // Capture output
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      sb.append(line).append("\n");
    }

   // System.out.println("Syft command output: " + sb.toString());

    int exitCode = process.waitFor();
    if (exitCode != 0) {
      throw new RuntimeException("Syft command failed with exit code: " + exitCode);
    }

    return sb.toString();
  }
}
