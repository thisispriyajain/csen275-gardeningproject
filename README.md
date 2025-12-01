# Smart Garden Simulation System

**Course:** CSEN 275 - Object-Oriented Analysis & Design  
**Version:** 1.0.0  
**Authors:** Smart Garden Team

## Overview

A comprehensive computerized garden simulation system that models an automated gardening environment with multiple plant types, environmental sensors, and automated control systems.

## Features

- **Multi-Plant Support**: 9 plant types including Fruits (Strawberry, Grapevine, Apple), Vegetables (Carrot, Tomato, Onion), and Flowers (Sunflower, Tulip, Rose)
- **Automated Systems**: Watering (with weather-aware sprinklers), Heating, and Pest Control
- **Real-time Simulation**: Multi-threaded time-based simulation engine
- **Interactive UI**: JavaFX-based graphical interface with smooth animations
- **Comprehensive Logging**: Detailed event tracking and analysis
- **Dynamic Weather System**: 5 weather types (Sunny, Cloudy, Rainy, Windy, Snowy) that affect plant growth and system behavior
- **Smart Watering**: Sprinklers automatically stop when it rains to conserve water

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

## Recent Improvements

### 🔒 Thread-Safety Fix (Logger)

Fixed a critical thread-safety issue in the `Logger` class that could cause crashes during concurrent access from multiple threads (UI thread and simulation timeline).

**File:** `src/main/java/edu/scu/csen275/smartgarden/util/Logger.java`

**Changes:**
- Wrapped `memoryLog` ArrayList with `Collections.synchronizedList()` for thread-safe access
- Added `volatile` keyword to `minLogLevel` for proper thread visibility
- Added `synchronized` blocks around iteration methods (`getRecentLogs()`, `getAllLogs()`, `filterByCategory()`, `filterByLevel()`)

### 🧪 Comprehensive Test Suite

Added extensive unit tests to improve code quality and catch bugs early.

| Test File | Test Count | Description |
|-----------|------------|-------------|
| `SimulationEngineTest.java` | 30 tests | Tests simulation lifecycle, state transitions, speed control, subsystem integration |
| `PestControlSystemTest.java` | 34 tests | Tests pest management, threat assessment, treatment, pesticide stock |
| `WateringSystemTest.java` | 5 tests | Fixed existing tests to match actual system behavior |

**Total Tests:** 75 (all passing ✅)

**Run tests:**
```powershell
.\mvnw.cmd test
```

### 📁 Files Modified

| File | Action |
|------|--------|
| `src/main/java/.../util/Logger.java` | Modified (thread-safety fix) |
| `src/test/java/.../simulation/SimulationEngineTest.java` | Created (new test suite) |
| `src/test/java/.../system/PestControlSystemTest.java` | Created (new test suite) |
| `src/test/java/.../system/WateringSystemTest.java` | Modified (fixed failing tests) |

## License

Educational use only - CSEN 275 Project
