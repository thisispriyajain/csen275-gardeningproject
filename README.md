# Smart Garden Simulation System

**Course:** CSEN 275 - Object-Oriented Analysis & Design  
**Version:** 1.0.0  
**Authors:** Smart Garden Team  
**Technology Stack:** Java 21, JavaFX 23.0.1, Maven

## Overview

A comprehensive computerized garden simulation system that models an automated gardening environment with multiple plant types, environmental sensors, and automated control systems. Features a beautiful, game-like UI inspired by Gardenscapes with smooth animations, particle effects, and real-time monitoring.

---

## 🌟 Key Features

- **🌱 9 Plant Types**: Fruits, Vegetables, and Flowers with unique growth characteristics
- **🦠 Pest Management**: 4 types of harmful pests with automated detection and treatment
- **💧 Smart Watering System**: Weather-aware sprinklers that automatically stop when raining
- **🌡️ Automated Heating**: Temperature control with 4 modes (OFF, LOW, MEDIUM, HIGH)
- **🌦️ Dynamic Weather**: 5 weather types that affect plant growth and system behavior
- **📊 Real-time Monitoring**: 27 sensors (moisture, temperature, pest detection) across 9 zones
- **🎨 Beautiful UI**: Modern JavaFX interface with 60 FPS animations and visual effects
- **📝 Comprehensive Logging**: Detailed event tracking and analysis

---

## 🌿 Plant Types

### Fruits (3 types)
- 🍓 **Strawberry** - Moderate growth, medium water needs
- 🍇 **Grapevine** - Moderate to fast growth, medium-high water needs
- 🍎 **Apple Sapling** - Slow growth (tree), medium water needs

### Vegetables (3 types)
- 🥕 **Carrot** - Moderate growth, medium water needs
- 🍅 **Tomato** - Fast growth, high water needs, pest-prone
- 🧅 **Onion** - Moderate growth, medium water needs

### Flowers (3 types)
- 🌻 **Sunflower** - Fast growth, high sunlight requirements
- 🌸 **Tulip** - Moderate growth, colorful blooms
- 🌹 **Rose** - Moderate growth, requires careful maintenance

**Growth Stages:** Seed → Seedling → Mature → Flowering/Fruiting

---

## 🦠 Pest Control System

### Pest Types (All Harmful)
- **Red Mite** - Damage rate: 2 (default)
- **Green Leaf Worm** - Damage rate: 3
- **Black Beetle** - Damage rate: 4 (highest damage)
- **Brown Caterpillar** - Damage rate: 2

### Features
- Automatic pest detection and spawning (5% probability)
- Pesticide stock: 50 applications initially
- Treatment threshold: 30% infestation level
- Visual pest sprites with damage animations
- Automated treatment with 3-second visibility delay

**Threat Levels:** LOW → MEDIUM → HIGH → CRITICAL

---

## 💧 Watering System

### Components
- **9 Sprinklers** (one per zone, 3x3 grid)
- **9 Moisture Sensors** (one per zone)
- **Water Supply:** 10,000 liters initially
- **Flow Rate:** 10 liters/minute per sprinkler

### Smart Features
- ✅ Automatic watering when moisture < 40%
- ✅ **Weather-aware**: Stops automatically when raining
- ✅ Distributes 30 units per cycle
- ✅ Even distribution across all plants in zone
- ✅ Manual override available
- ✅ Real-time water supply monitoring

---

## 🌦️ Weather System

### Weather Types (5)
1. **☀ Sunny** - Optimal for growth (20°C), increases health, evaporates moisture
2. **☁ Cloudy** - Neutral effect, moderate conditions
3. **🌧 Rainy** - Adds moisture (+5% per cycle, 10°C), **automatically stops sprinklers**
4. **💨 Windy** - Slightly stresses plants, increases evaporation
5. **❄ Snowy** - Damages plants (5°C), requires heating activation

### Behavior
- Weather changes every 30-120 simulation minutes
- Realistic transitions (sunny → cloudy → rainy)
- Affects plant growth rates and health
- Controls ambient temperature
- Triggers heating system in cold conditions

---

## 🌡️ Heating System

### Features
- **Target Range:** 15°C - 28°C
- **Automatic Activation:** When temperature < 15°C
- **9 Temperature Sensors** (one per zone)

### Heating Modes
- **OFF** - Temperature is optimal (≥15°C)
- **LOW** - Temp deficit: 5-10°C (increases temp by +1°C)
- **MEDIUM** - Temp deficit: 5-10°C (increases temp by +2°C)
- **HIGH** - Temp deficit: >10°C (increases temp by +3°C)

**Smart Behavior:** Automatically activates in cold weather (rainy/snowy) and monitors average temperature across all zones.

---

## 📊 Sensor System

### Sensor Types (27 total - 9 per zone)

#### MoistureSensor
- Measures soil moisture level (0-100%)
- Threshold: 40% for automatic watering
- Used by watering system for smart irrigation

#### TemperatureSensor
- Measures ambient temperature (°C)
- Range: 0-30°C typical
- Used by heating system for climate control

#### PestDetector
- Detects pest presence in zones
- Identifies infestation levels
- Triggers treatment when threshold exceeded

**Sensor Status:** ACTIVE | INACTIVE | ERROR

---

## 🎨 User Interface

### UI Features
- **Animated Background** - Moving clouds and sunlight rays
- **Garden Grid** - Interactive 9x9 grid with hover effects
- **Real-time Monitoring** - Live updates every 0.5 seconds
- **Particle Effects** - Sparkles and pollen animations
- **Decorative Elements** - Butterflies, bees, birds, falling leaves
- **Weather Animations** - Full-screen rain and snow effects
- **Color-coded Health** - Visual indicators for plant status
- **Tooltips** - Detailed plant information on hover

### Visual Effects
- 60 FPS smooth animations
- Coin float rewards for planting
- Water ripple effects
- Pesticide spray animations
- Damage text overlays
- Sprinkler arc animations

---

## 📈 System Statistics

- **Garden Grid:** 9x9 (81 cells)
- **Zones:** 9 zones (3x3 arrangement)
- **Plant Types:** 9 plants
- **Pest Types:** 4 harmful pests
- **Weather Types:** 5 conditions
- **Sprinklers:** 9 devices
- **Sensors:** 27 total (9 moisture + 9 temperature + 9 pest detectors)
- **Automation Systems:** 3 (Watering, Heating, Pest Control)

---

## 🚀 Quick Start

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
or
```
Double-click run-ui.bat  (for UI mode)
Double-click run-api.bat (for API/console mode)
```

**📝 Manual Build:**
```powershell
# Navigate to project
cd smartGarden

# Build the project (using Maven wrapper - no Maven install needed!)
.\mvnw.cmd clean install

# Run the UI application
.\mvnw.cmd javafx:run
```

**OR if you have Maven installed:**
```bash
mvn clean install
mvn javafx:run
```

### First Steps

1. **Start the simulation** - Click the ▶️ Start button
2. **Select a plant** - Choose from the dropdown (e.g., 🍓 Strawberry)
3. **Plant it** - Click an empty tile in the 9x9 grid
4. **Watch it grow** - Monitor health, growth stages, and weather effects
5. **Monitor systems** - Check right panel for resources, weather, and stats
6. **Watch for pests** - Pests appear randomly; system treats automatically
7. **Adjust speed** - Use speed selector (1x, 2x, 5x, 10x) to speed up time

### Controls

- **▶️ Start** - Begin simulation
- **⏸ Pause/Resume** - Pause or continue simulation
- **⏹ Stop** - Stop and reset simulation
- **Speed Selector** - Control simulation speed (1x to 10x)
- **Plant Selector** - Choose plant type to plant
- **💧 Water All Zones** - Manual watering override
- **Refill Buttons** - Refill water supply or pesticide stock

---

## 📁 Project Structure

```
smartGarden/
├── src/main/java/
│   └── edu/scu/csen275/smartgarden/
│       ├── SmartGardenApplication.java  # Main JavaFX application
│       ├── controller/                  # Garden controller
│       ├── model/                       # Domain models (Plant, Garden, Zone, etc.)
│       ├── system/                      # Automation systems
│       │   ├── WateringSystem.java      # Smart watering
│       │   ├── HeatingSystem.java       # Temperature control
│       │   ├── PestControlSystem.java   # Pest management
│       │   ├── Sensor.java              # Base sensor class
│       │   ├── MoistureSensor.java      # Soil moisture
│       │   ├── TemperatureSensor.java   # Temperature
│       │   └── Sprinkler.java           # Watering device
│       ├── simulation/                  # Simulation engine
│       │   ├── SimulationEngine.java    # Main engine
│       │   └── WeatherSystem.java       # Weather simulation
│       ├── ui/                          # UI components (21 files)
│       │   ├── AnimatedBackgroundPane.java
│       │   ├── GardenGridPanel.java
│       │   ├── InfoPanel.java
│       │   ├── ModernToolbar.java
│       │   └── [17 more UI components]
│       └── util/                        # Utilities
│           └── Logger.java              # Logging system
├── src/main/resources/
│   ├── styles/
│   │   └── garden-theme.css            # UI styling
│   └── images/                          # Plant images
├── docs/
│   ├── requirements/                    # Requirements analysis
│   ├── design/                          # UML diagrams & design
│   └── manual/                          # User & developer guides
├── logs/                                # Simulation log files
├── pom.xml                              # Maven configuration
├── README.md                            # This file
└── run.bat / run-ui.bat / run-api.bat  # Launch scripts
```

---

## 📚 Documentation

### Requirements & Design
- [Problem Statement](docs/requirements/ProblemStatement.md) - Project context and goals
- [Feature List](docs/requirements/FeatureList.md) - 160+ features organized by category
- [Requirements List](docs/requirements/RequirementsList.md) - Functional and non-functional requirements
- [Domain Model](docs/requirements/DomainModel.md) - Core domain concepts
- [Use Cases](docs/requirements/UseCases.md) - Detailed use case scenarios
- [User Stories](docs/requirements/UserStories.md) - User story epics

### Design Documentation
- [Design Overview](docs/design/DesignOverview.md) - Architecture and design patterns
- [Class Diagram](docs/design/ClassDiagram.puml) - Complete class structure
- [Sequence Diagrams](docs/design/) - Interaction flows
- [State Diagrams](docs/design/) - System and component states
- [Activity Diagrams](docs/design/) - Process flows
- [Component Diagram](docs/design/ComponentDiagram.puml) - System architecture

### User Guides
- [User Manual](docs/manual/UserManual.md) - Complete user guide
- [Developer Guide](docs/manual/DeveloperGuide.md) - Development documentation
- [UI Report](UI_REPORT.md) - Comprehensive UI documentation

### Additional Documentation
- [UI Report](UI_REPORT.md) - Complete UI component documentation
- [Installation Guide](INSTALL_JAVA_WINDOWS.md) - Java installation for Windows
- [Setup Instructions](SETUP_INSTRUCTIONS.md) - Project setup guide
- [API Documentation](RUN_API.md) - API usage guide

---

## 🏗️ Architecture

### Design Patterns
- **Model-View-Controller (MVC)** - Separation of concerns
- **Observer Pattern** - UI updates on state changes
- **Strategy Pattern** - Different watering/heating strategies
- **State Pattern** - Plant lifecycle and simulation states
- **Factory Pattern** - Plant creation
- **Singleton Pattern** - Logger and configuration
- **Facade Pattern** - Simplified subsystem access

### System Layers
1. **Presentation Layer** - JavaFX UI components
2. **Application Layer** - Controllers and coordination
3. **Domain Layer** - Core business logic (Garden, Plant, etc.)
4. **System Layer** - Automation subsystems (Watering, Heating, Pest Control)
5. **Infrastructure Layer** - Cross-cutting concerns (Logger, etc.)

---

## 🔧 Technical Details

### Build Configuration
- **Java Version:** 21
- **JavaFX Version:** 23.0.1
- **Build Tool:** Maven
- **Module System:** Java Platform Module System (JPMS)

### Key Technologies
- JavaFX for GUI
- JavaFX Properties for reactive UI
- Timeline for animations
- Multi-threading for simulation engine
- Property binding for real-time updates

### Performance
- **UI Update Rate:** 0.5 seconds (2 updates/second)
- **Animation FPS:** 60 FPS target
- **Simulation Tick:** 1 minute per tick
- **Speed Multipliers:** 1x, 2x, 5x, 10x

---

## 🎮 How It Works

1. **Plant Management** - Plant seeds, watch them grow through 5 stages
2. **Automatic Watering** - Sensors detect low moisture, sprinklers activate
3. **Weather Adaptation** - System responds to weather changes (rain stops sprinklers)
4. **Temperature Control** - Heating activates when temperature drops below 15°C
5. **Pest Detection** - Pests spawn randomly, system detects and treats automatically
6. **Real-time Monitoring** - All systems monitored via sensors and displayed in UI

---

## 📊 Features Summary

✅ **Complete Plant Lifecycle** - From seed to flowering/fruiting  
✅ **Smart Automation** - Three automated systems work together  
✅ **Weather Integration** - Weather affects all systems intelligently  
✅ **Visual Feedback** - Color-coded health, animations, tooltips  
✅ **Comprehensive Logging** - All events logged with timestamps  
✅ **Interactive UI** - Click, hover, and watch animations  
✅ **Resource Management** - Water and pesticide supplies  
✅ **Pest Management** - Detection, visualization, and treatment  

---

## 🐛 Known Limitations

- Data is in-memory only (no persistence between runs)
- Single application instance (no client-server)
- Designed for educational purposes

---

## 📝 License

Educational use only - CSEN 275 Project

---

## 👥 Authors

Smart Garden Team - CSEN 275 Course Project

---

## 🙏 Acknowledgments

Built for CSEN 275 - Object-Oriented Analysis & Design course at SCU.

