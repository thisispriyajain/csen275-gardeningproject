# GardenSimulationAPI Documentation

## Overview

The `GardenSimulationAPI` provides a programmatic interface for automated testing and monitoring of the Smart Garden Simulation system. It wraps smartGarden's existing systems (`GardenController`, `WateringSystem`, `HeatingSystem`, `PestControlSystem`, `WeatherSystem`) to provide a simple, unified API similar to Group9's API.

## Location

- **Package**: `edu.scu.csen275.smartgarden.api`
- **Main Class**: `GardenSimulationAPI.java`
- **Example**: `GardenSimulationAPIExample.java`

## Usage

### Creating an API Instance

```java
import edu.scu.csen275.smartgarden.controller.GardenController;
import edu.scu.csen275.smartgarden.api.GardenSimulationAPI;

// Create a GardenController (typically 9x9 grid)
GardenController controller = new GardenController(9, 9);

// Create the API wrapper
GardenSimulationAPI api = new GardenSimulationAPI(controller);
```

## API Methods

### 1. `initializeGarden()`

Initializes the garden with a predefined set of plants at fixed positions:
- **Strawberry** at (1, 1)
- **Carrot** at (2, 2)
- **Tomato** at (3, 3)
- **Sunflower** at (4, 4)

This method marks the beginning of the simulation clock (dayCount = 0).

```java
api.initializeGarden();
```

### 2. `getPlants()`

Retrieves plant information including names, water levels, and pest vulnerabilities.

**Returns**: `Map<String, Object>` containing:
- `"plants"`: List of plant names
- `"waterRequirement"`: List of current water levels
- `"parasites"`: List of lists of pest types that can attack each plant

```java
Map<String, Object> plants = api.getPlants();
System.out.println("Plants: " + plants.get("plants"));
System.out.println("Water Levels: " + plants.get("waterRequirement"));
System.out.println("Pest Vulnerabilities: " + plants.get("parasites"));
```

### 3. `rain(int amount)`

Simulates rainfall in the garden by:
- Setting weather to RAINY
- Adding water units to all plants
- Applying weather effects

**Parameters**:
- `amount`: Amount of water units to add to each plant

```java
api.rain(10);  // Add 10 units of water to all plants
```

### 4. `temperature(int temp)`

Simulates temperature changes in the garden by:
- Setting ambient temperature using HeatingSystem
- Setting appropriate weather based on temperature:
  - `>35°C` → SUNNY
  - `<5°C` → SNOWY
  - Otherwise → CLOUDY
- Applying temperature and weather effects to all plants

**Parameters**:
- `temp`: Temperature in Celsius

```java
api.temperature(25);  // Set temperature to 25°C
```

### 5. `parasite(String parasiteType)`

Triggers a pest infestation. Only affects plants that are vulnerable to the specified pest type.

**Supported Pest Types**:
- "Red Mite"
- "Green Leaf Worm"
- "Black Beetle"
- "Brown Caterpillar"

**Pest Vulnerabilities**:
- **Strawberry**: Red Mite, Green Leaf Worm
- **Grapevine**: Black Beetle, Red Mite
- **Apple Sapling**: Brown Caterpillar, Green Leaf Worm
- **Carrot**: Red Mite, Brown Caterpillar
- **Tomato**: Black Beetle, Red Mite
- **Onion**: Green Leaf Worm
- **Sunflower**: Red Mite, Brown Caterpillar
- **Tulip**: Green Leaf Worm
- **Rose**: Black Beetle, Red Mite

```java
api.parasite("Red Mite");  // Attack vulnerable plants with Red Mites
```

### 6. `getState()`

Logs details about the garden's current state, including:
- Day count
- Number of alive/dead plants
- Total plants and zones
- Individual plant status (health, water level)

```java
api.getState();
```

### 7. Additional Methods

- `getController()`: Returns the `GardenController` instance (for advanced use)
- `getGarden()`: Returns the `Garden` instance (for advanced use)
- `getDayCount()`: Returns the current day count

## Complete Example

```java
import edu.scu.csen275.smartgarden.controller.GardenController;
import edu.scu.csen275.smartgarden.api.GardenSimulationAPI;
import java.util.Map;

public class MyGardenTest {
    public static void main(String[] args) {
        // Setup
        GardenController controller = new GardenController(9, 9);
        GardenSimulationAPI api = new GardenSimulationAPI(controller);
        
        // 1. Initialize
        api.initializeGarden();
        
        // 2. Query plants
        Map<String, Object> info = api.getPlants();
        System.out.println("Plants: " + info.get("plants"));
        
        // 3. Simulate environmental events
        api.rain(10);           // Rainfall
        api.temperature(20);    // Temperature change
        api.parasite("Red Mite"); // Pest attack
        
        // 4. Check state
        api.getState();
        
        System.out.println("Day: " + api.getDayCount());
    }
}
```

## Integration with Existing Systems

The API does **NOT** disrupt existing smartGarden code. It:
- ✅ Uses existing `GardenController` and systems
- ✅ Works with existing UI and simulation
- ✅ Uses existing logging system
- ✅ No changes to existing classes
- ✅ Safe to use alongside normal UI operation

## Benefits

1. **Automated Testing**: Easy to write test scripts
2. **Monitoring**: Programmatic access to garden state
3. **Integration**: Can be used by external systems
4. **No Disruption**: Works with existing code

## Notes

- The API uses smartGarden's **Celsius** temperature scale (not Fahrenheit)
- Day count increments automatically with environmental events
- All operations are logged through smartGarden's Logger system
- The API is thread-safe when used with a single GardenController instance

