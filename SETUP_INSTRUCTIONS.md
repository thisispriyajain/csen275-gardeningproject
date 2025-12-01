# Smart Garden Simulation - Setup Instructions

## Quick Start Guide

### Prerequisites

1. **Java Development Kit (JDK) 21 or higher**
   - Download: https://www.oracle.com/java/technologies/downloads/
   - OR Eclipse Temurin: https://adoptium.net/temurin/releases/?version=21
   - Verify: `java -version` (should show 21.x.x)
   
   **⚠️ DON'T HAVE JAVA?** See `INSTALL_JAVA_WINDOWS.md` for detailed installation guide!

2. **Maven** (Optional - we provide Maven wrapper!)
   - ✅ **Maven wrapper included** - you don't need to install Maven separately!
   - If you want to install Maven: https://maven.apache.org/download.cgi

### Installation Steps

#### 🚀 EASIEST WAY (Windows):

**Just double-click `run.bat`!** It will:
- Check if Java is installed
- Build the project automatically
- Run the application

---

#### Manual Steps:

1. **Navigate to Project Directory**
   ```powershell
   cd D:\smartGarden
   ```

2. **Build the Project**
   
   Using Maven wrapper (no Maven installation needed):
   ```powershell
   .\mvnw.cmd clean install
   ```
   
   OR if you have Maven installed:
   ```powershell
   mvn clean install
   ```
   
   This will:
   - Download all dependencies
   - Compile the Java source code
   - Package the application

3. **Run the Application**
   
   Using Maven wrapper:
   ```powershell
   .\mvnw.cmd javafx:run
   ```
   
   OR with Maven installed:
   ```powershell
   mvn javafx:run
   ```

### First Time Usage

1. **Launch Application** - The Smart Garden window will appear

2. **Plant Some Seeds**
   - Select a plant type from dropdown (Flower, Tomato, Tree, Grass, or Basil)
   - Click empty (brown) cells in the garden grid
   - Plant at least 5-10 plants for a good simulation

3. **Start Simulation**
   - Click the "Start" button in the top toolbar
   - Watch your garden come to life!

4. **Observe Systems**
   - Green plants = healthy
   - Yellow/orange = needs attention
   - Red = critical condition
   - Watch the event log at bottom for details

5. **Control Speed**
   - Use the speed dropdown to adjust (1x, 2x, 5x, 10x)
   - Start at 1x to learn, increase for faster growth

### Troubleshooting

**Problem: "java command not found" or "JAVA_HOME not found"**
- Solution: Install Java 21 first!
- **See `INSTALL_JAVA_WINDOWS.md`** for detailed installation guide
- Quick: Download from https://adoptium.net/temurin/releases/?version=21
- Set JAVA_HOME environment variable to Java installation path

**Problem: "mvn command not found"**
- Solution: Use the Maven wrapper instead! Run `.\mvnw.cmd` instead of `mvn`
- Or use the provided `run.bat` or `build.bat` files
- Maven wrapper is included - no separate installation needed!

**Problem: "Java version error"**
- Solution: Ensure Java 21 is installed and JAVA_HOME is set
- Check: `java -version` and `mvn -version`

**Problem: "Module not found" error**
- Solution: Run `mvn clean install` again to download dependencies

**Problem: "Cannot start - no plants in garden"**
- Solution: Plant at least one plant before clicking Start

**Problem: Application runs but UI is blank**
- Solution: Check if JavaFX libraries are properly included
- Try: `mvn clean install -U` (force update dependencies)

### Project Structure

```
smartGarden/
├── src/main/java/          # Java source code
├── src/main/resources/     # Resources (if any)
├── docs/                   # All documentation
│   ├── requirements/       # Analysis documents
│   ├── design/            # UML diagrams (PlantUML)
│   └── manual/            # User & developer guides
├── logs/                   # Generated log files
├── pom.xml                # Maven configuration
└── README.md              # Project overview
```

### Documentation

All comprehensive documentation is located in the `docs/` directory:

**Requirements Analysis:**
- Problem Statement
- Feature List
- Requirements List
- User Stories & Scenarios
- Use Cases
- Domain Model
- System Constraints
- Assumptions

**Design Documentation:**
- Design Overview
- UML Class Diagram
- UML Sequence Diagrams (3 scenarios)
- UML State Diagrams (3 types)
- UML Activity Diagrams (2 types)
- UML Component Diagram

**User Documentation:**
- User Manual (comprehensive guide)
- Developer Guide (extension & API reference)

### Key Features Implemented

✅ **Complete Object-Oriented Design**
- Abstract classes and inheritance (Plant hierarchy)
- Interfaces and polymorphism
- Encapsulation and information hiding
- Design patterns (MVC, Observer, Singleton, Factory, Strategy)

✅ **Garden Management**
- 9x9 grid layout with 9 zones
- 5 plant types: Flower, Vegetable (Tomato), Tree, Grass, Herb (Basil)
- Growth stages: Seed → Seedling → Mature → Flowering → Fruiting
- Health tracking and death conditions

✅ **Automated Systems** (3+ as required)
1. **Watering System**
   - Zone-based moisture sensors
   - Automatic sprinkler activation
   - Water supply management
   
2. **Heating System**
   - Temperature monitoring
   - Automatic heating control (Off/Low/Med/High)
   - Energy consumption tracking
   
3. **Pest Control System**
   - Random pest spawning
   - Harmful pests (damage plants) and beneficial insects (help plants)
   - Automatic treatment at high infestation levels
   - Pesticide stock management

✅ **Simulation Engine**
- Multi-threaded JavaFX Timeline
- Variable speed (1x to 10x)
- Discrete time steps (1 tick = 1 minute)
- Start/Pause/Resume/Stop controls
- 24+ hour continuous operation capability

✅ **Weather System**
- 5 weather types: Sunny, Cloudy, Rainy, Windy, Snowy
- Realistic weather transitions
- Weather affects plant growth and moisture
- Visual indicators with icons

✅ **User Interface**
- JavaFX-based GUI
- Real-time garden visualization
- Color-coded plant health (Green/Yellow/Orange/Red)
- Resource meters (water, temperature, pesticide)
- Event log viewer
- Manual override controls

✅ **Logging System**
- File-based logging (logs/ directory)
- Timestamped events
- Multiple log levels (DEBUG, INFO, WARNING, ERROR)
- In-memory buffer for UI display
- Session-based log files

✅ **Exception Handling**
- Try-catch blocks throughout
- Graceful error recovery
- Error logging
- User notifications for critical issues

✅ **Documentation**
- Complete requirements analysis (8 documents)
- Comprehensive design documentation
- UML diagrams in PlantUML format (10 diagrams)
- User manual (40+ pages)
- Developer guide (35+ pages)

### Running for 24+ Hours

The system is designed for long-term stability:

1. **Before Starting:**
   - Plant 30-50 plants
   - Set speed to 5x or 10x
   - Ensure adequate RAM (8GB+)

2. **During Run:**
   - Resource meters will show levels
   - Refill water and pesticide as needed
   - Check logs periodically

3. **Expected Behavior:**
   - Plants grow through full lifecycle
   - Some plants die naturally (reaching lifespan)
   - Weather cycles through all types
   - All systems coordinate automatically

4. **Log Files:**
   - Located in `logs/garden_YYYYMMDD_HHMMSS.log`
   - Contains complete event history
   - Useful for analysis and debugging

### Development

**IDE Setup:**
- Import as Maven project
- Set JDK to version 21
- Enable annotation processing (for JavaFX properties)

**Build Commands:**
- Compile: `mvn compile`
- Test: `mvn test`
- Package: `mvn package`
- Clean: `mvn clean`
- Run: `mvn javafx:run`

**Code Structure:**
- `model/` - Domain entities (Garden, Plant, Zone)
- `system/` - Automation systems (Watering, Heating, Pest Control)
- `simulation/` - Simulation engine and weather
- `controller/` - Application logic
- `util/` - Infrastructure (Logger)

### Additional Resources

- **User Manual**: `docs/manual/UserManual.md`
- **Developer Guide**: `docs/manual/DeveloperGuide.md`
- **Design Docs**: `docs/design/`
- **UML Diagrams**: `docs/design/*.puml` (view with PlantUML plugin)

### Support

For issues or questions:
1. Check log files in `logs/` directory
2. Review User Manual and Developer Guide
3. Examine UML diagrams for system design
4. Contact course instructor or TA

### License

Educational use only - CSEN 275 Project

---

**Ready to Begin!** 🌱

Run `mvn javafx:run` and watch your Smart Garden grow!

