# Smart Garden Simulation System

**Course:** CSEN 275 - Object-Oriented Analysis & Design  
**Version:** 1.0.0  
**Authors:** Smart Garden Team

## Overview

A comprehensive computerized garden simulation system that models an automated gardening environment with multiple plant types, environmental sensors, and automated control systems.

## Features

- **Multi-Plant Support**: Flowers, Vegetables, Trees, Grass, and Herbs
- **Automated Systems**: Watering, Heating, and Pest Control
- **Real-time Simulation**: Multi-threaded time-based simulation engine
- **Interactive UI**: JavaFX-based graphical interface
- **Comprehensive Logging**: Detailed event tracking and analysis
- **Random Events**: Weather changes, pest infestations, and environmental factors

## Quick Start

### Prerequisites

- **Java 21 or higher** (Required)
  - Download: https://adoptium.net/temurin/releases/?version=21
  - **No Java?** See `INSTALL_JAVA_WINDOWS.md` for installation guide
- ~~Maven~~ (Not needed! We provide Maven wrapper)
- JavaFX 23.0.1 (automatically downloaded by Maven)

### Build & Run

**🚀 Easiest Way (Windows):**
```
Double-click run.bat
```

**📝 Manual Build:**
```powershell
# Navigate to project
cd smartGarden

# Build the project (using Maven wrapper - no Maven install needed!)
.\mvnw.cmd clean install

# Run the application
.\mvnw.cmd javafx:run
```

**OR if you have Maven installed:**
```bash
mvn clean install
mvn javafx:run
```

## Project Structure

```
smartGarden/
├── src/main/java/          # Java source files
├── src/main/resources/     # FXML, CSS, images
├── docs/                   # Requirements & design documentation
├── logs/                   # Simulation log files
└── design/                 # UML diagrams
```

## Documentation

- [Requirements Analysis](docs/requirements/ProblemStatement.md)
- [Design Documentation](docs/design/ClassDiagram.puml)
- [User Manual](docs/manual/UserManual.md)
- [Developer Guide](docs/manual/DeveloperGuide.md)

## License

Educational use only - CSEN 275 Project
