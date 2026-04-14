# Password Type Detection Utility

A Java utility for detecting and analyzing password encryption types used in IBM WebSphere/Liberty environments.

## Overview

This utility analyzes encrypted password hashes to determine their encryption algorithm and specific encryption type. It's particularly useful for IBM WebSphere Liberty administrators who need to identify the encryption methods used for stored passwords.

## Features

- Detects valid crypto algorithm tags in password hashes
- Identifies specific AES encryption variants (AES-128, AES-256)
- Determines if AES passwords use built-in or external encryption keys
- Supports multiple encryption types including XOR and AES
- Command-line interface for easy integration into scripts and workflows

## Requirements

- Java 8 or higher
- Maven 3.6+ (for building)

## Building

To build the project, run:

```bash
mvn clean package
```

This will create a JAR file with all dependencies included: `detect-password-type.jar`

## Usage

### Command Line

```bash
java -jar detect-password-type.jar <password_hash>
```

### Examples

```bash
# Detect XOR encrypted password
java -jar detect-password-type.jar "{xor}Lz4sLCgwLTs="

# Detect AES encrypted password
java -jar detect-password-type.jar "{aes}AbCdEfGhIjKlMnOpQrStUvWxYz=="
```

### Expected Output

For a valid password hash, the utility will output:
```
Password Type Detection Utility
Input: {xor}Lz4sLCgwLTs=
Detected Type: xor
```

For AES encrypted passwords, additional encryption variant and key type information is provided:
```
Password Type Detection Utility
Input: {aes}AbCdEfGhIjKlMnOpQrStUvWxYz==
Detected Type: aes
AES Encryption type: AES_V1 : AES-256
KEY: Built-in
```

### Key Type Detection

For AES encrypted passwords, the utility automatically detects whether the password was encrypted using:

- **Built-in key**: The default encryption key embedded in Liberty
- **External key**: A custom encryption key configured in the server

This information is displayed as the last line of output for AES passwords:
- `KEY: Built-in` - Password uses the default Liberty encryption key
- `KEY: External key` - Password uses a custom encryption key (requires proper key configuration to decode)

### AES Encryption Variants

The utility identifies three AES encryption variants:

- **AES_V0**: AES-128 encryption
- **AES_V1**: AES-256 encryption (most common)
- **AES_V2**: AES-256 encryption (newer variant)
