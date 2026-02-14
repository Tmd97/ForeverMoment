# MomentForeverApp Setup Guide

This guide describes how to set up and run the MomentForeverApp project.

## Prerequisites

- **Java 17 CLI** (Ensure `java -version` returns 17)
- **VS Code** with the **Extension Pack for Java** installed.

## Setup Steps

1.  **Open the Project in VS Code:**
    - Open the `MomentForeverApp` folder in VS Code.
    - When prompted, install the recommended extensions.

2.  **Install Dependencies:**
    - Open a terminal in VS Code (Ctrl+`).
    - Run the following command to download all dependencies and build the project:
      ```powershell
      .\mvnw clean install
      ```
    - The first run may take a few minutes as it downloads dependencies.

3.  **Run the Application:**
    - Locate the main application class (likely in `moment_forever_core` or a dedicated runner module).
    - Right-click and choose "Run".
    - Or run from the command line:
      ```powershell
      .\mvnw spring-boot:run -pl moment_forever_core
      ```
      *(Note: Adjust the module name if the main app is in a different module)*

## Module Structure

- `moment_forever_commons`: Shared utilities and DTOs.
- `moment_forever_data`: Database entities and repositories.
- `moment_forever_core`: Business logic and main application.
- `moment_forever_security`: Security configurations.
- `moment_forever_object_store`: Object storage services.

## Troubleshooting

- If you see "Project configuration is not up-to-date with pom.xml", right-click `pom.xml` and choose **Update Project** or **Reload Project**.
- Ensure your `JAVA_HOME` environment variable points to a valid JDK 17 installation.
