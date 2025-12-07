# How to Run the GardenSimulationAPI

## Important: API vs UI

**The API runs in CONSOLE-ONLY mode** - it does **NOT** open the visual UI window. The API is designed for programmatic testing and monitoring without a graphical interface.

If you want to see the garden visually:
- Use `run-ui.bat` or `run.bat` to launch the JavaFX UI
- The UI and API use separate garden instances, so they don't share state

---

## Quick Start (Windows)

### Option 1: Using the Batch Script (Easiest)

Simply double-click or run:
```bash
run-api.bat
```

This script will:
- Build the project
- Run the API example
- Generate `log.txt` with all events

---

## Manual Run (Any Platform)

### Step 1: Build the Project

```bash
# Using Maven wrapper (recommended)
./mvnw clean compile

# Or if Maven is installed globally
mvn clean compile
```

### Step 2: Run the API Example

```bash
# Using Maven exec plugin
./mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.GardenSimulationAPIExample"

# Or using Maven directly
mvn exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.GardenSimulationAPIExample"
```

---

## Running with Java Directly

After building with Maven:

```bash
# Navigate to target/classes directory
cd target/classes

# Run the example (adjust module path as needed)
java --module-path "../../target/lib" --add-modules javafx.controls edu.scu.csen275.smartgarden.api.GardenSimulationAPIExample
```

---

## What the API Example Does

The `GardenSimulationAPIExample` demonstrates all API methods:

1. **Initialize Garden**: Creates 4 plants at fixed positions
   - Strawberry at (1, 1)
   - Carrot at (2, 2)
   - Tomato at (3, 3)
   - Sunflower at (4, 4)

2. **Get Plant Information**: Retrieves plant names, water levels, and pest vulnerabilities

3. **Simulate Rainfall**: Adds 10 units of water to all plants

4. **Change Temperature**: Sets temperature to 25°C (77°F)

5. **Trigger Pest Attack**: Simulates "Red Mite" infestation on vulnerable plants

6. **Get Garden State**: Reports alive/dead plants and their status

---

## Output Files

After running the API, you will see:

1. **Console Output**: 
   - Progress messages
   - Plant information
   - Day count

2. **log.txt** (in project root):
   - All API events with timestamps
   - Initialization logs
   - Rain events
   - Temperature changes
   - Pest attacks
   - Garden state reports

3. **logs/garden_YYYYMMDD_HHMMSS.log** (timestamped log):
   - Detailed system logs
   - Internal garden events

---

## Using the API in Your Own Code

Create your own class with a `main` method:

```java
package your.package;

import edu.scu.csen275.smartgarden.controller.GardenController;
import edu.scu.csen275.smartgarden.api.GardenSimulationAPI;
import java.util.Map;

public class MyAPIUsage {
    public static void main(String[] args) {
        // Create controller
        GardenController controller = new GardenController(9, 9);
        
        // Create API wrapper
        GardenSimulationAPI api = new GardenSimulationAPI(controller);
        
        // Use API methods
        api.initializeGarden();
        
        // ... your custom logic ...
        
        // Get results
        Map<String, Object> plants = api.getPlants();
        System.out.println(plants);
        
        // Cleanup (optional - closes log.txt writer)
        GardenSimulationAPI.closeApiLog();
    }
}
```

---

## Requirements

- **Java 21** or higher
- **Maven** (or use the included `mvnw` wrapper)
- **No JavaFX required** for API-only usage (API doesn't need GUI)

---

## Troubleshooting

### "Java not found"
- Install Java 21 from [Adoptium](https://adoptium.net/temurin/releases/?version=21)
- Add Java to your PATH environment variable

### "Maven not found"
- Use the included `mvnw` or `mvnw.cmd` wrapper
- Or install Maven from [maven.apache.org](https://maven.apache.org/download.cgi)

### "Cannot find main class"
- Ensure project is compiled: `./mvnw clean compile`
- Check that you're in the project root directory

### "log.txt not created"
- Check file permissions in project directory
- Ensure Java has write access

---

## API Documentation

For detailed API method documentation, see:
- `API_DOCUMENTATION.md`
- `src/main/java/edu/scu/csen275/smartgarden/api/GardenSimulationAPI.java`

