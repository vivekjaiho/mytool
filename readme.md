# MyTool

A Java-based CLI that uses Syft to generate an SBOM for any container image.

## Features
- Generate SBOM in SPDX or CycloneDX format.
- Optional regex-based search on package name.
- Extensible design.

## Requirements
- Syft installed (if running outside Docker).
- Java 17+ (if using JAR).

## Usage
### 1) Local (Linux) usage
1. Download `mytool-linux-amd64`
2. `chmod +x mytool-linux-amd64`
3. `./mytool-linux-amd64 --source nginx --format spdx --output sbom.json`

Or, with Java:

### 2) Docker Usage
docker run  --rm -v "$(pwd)":/result vivek1262/mytool:latest --source nginx --format spdx --search "openssl" --output /result/sbom.json


## Binaries
- [mytool-linux-amd64](https://github.com/vivekjaiho/mytool/releases/tag/main)
- [mytool-linux-arm64](https://github.com/vivekjaiho/mytool/releases/tag/main)
- [mytool-windows-amd64.exe](https://github.com/vivekjaiho/mytool/releases/tag/main)
