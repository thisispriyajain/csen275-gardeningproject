# System Architecture Overview

## 5.1 System Architecture

The system is designed based on the Model-View-Controller (MVC) architecture to promote separation of concerns and improve maintainability.

**Model:** Manages the state of the garden, including plants (9 types across Fruits, Vegetables, Flowers), zones (9 zones in 3x3 grid), sensors (27 total: moisture, temperature, pest detectors), and module data (watering, heating, pest control systems). Ensures efficient data handling for real-time garden monitoring with plant growth stages, health tracking, and environmental conditions.

**View:** Provides an interactive JavaFX-based graphical interface for user interaction with 21 UI components including animated garden grid (9x9 tiles), weather display, resource meters, and real-time status panels. Displays real-time garden conditions with 60 FPS animations, color-coded health indicators, weather effects (rain, snow), watering animations, and pest visualizations. Allows users to configure system settings, plant seeds, monitor resources, and control simulation speed.

**Controller:** Handles the logic for various modules, including watering (weather-aware automatic irrigation with 9 sprinklers), pest control (automated detection and treatment), heating (temperature regulation with 4 modes), and sensors (27 sensors across 9 zones). Facilitates smooth communication between the model and the view through GardenController and SimulationController, ensuring efficient system operations with real-time updates every 0.5 seconds. Coordinates SimulationEngine which manages time progression (1 tick = 1 minute), WeatherSystem that affects all subsystems, and event-driven automation triggers.

## 5.2 UML Diagrams

The system design is documented through comprehensive UML diagrams located in `docs/design/`: Class Diagram (complete class structure), Sequence Diagrams (interaction flows), State Diagrams (system and component states), Activity Diagrams (process flows), and Component Diagram (high-level architecture).

