# How to Run Smart Garden Simulation

## Quick Start Options

### Option 1: Run UI Only (Visual Garden)
**Windows:**
```batch
run-ui.bat
```
or
```batch
run.bat
```

**Manual (Maven):**
```bash
mvnw javafx:run
```

**What happens:**
- Opens the JavaFX UI window
- You can plant seeds, start simulation manually
- Normal operation with automatic systems (watering, heating, pest control)

---

### Option 2: Run API Only (Console/Headless)
**Windows:**
```batch
run-api.bat
```
Then choose option `1` when prompted.

**Manual (Maven):**
```bash
mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.GardenSimulationAPIExample"
```

**What happens:**
- Runs in console (no UI window)
- API initializes garden, calls rain/temperature/parasite
- Logs written to `log.txt`

---

## Custom API Usage

You can create your own API script:

**Create `MyAPITest.java`:**
```java
import edu.scu.csen275.smartgarden.controller.GardenController;
import edu.scu.csen275.smartgarden.api.GardenSimulationAPI;

public class MyAPITest {
    public static void main(String[] args) {
        GardenController controller = new GardenController(9, 9);
        GardenSimulationAPI api = new GardenSimulationAPI(controller);
        
        api.initializeGarden();
        api.rain(25);
        api.temperature(60);
        api.parasite("Red Mite");
        api.getState();
        
        GardenSimulationAPI.closeApiLog();
    }
}
```

**Run it:**
```bash
mvnw exec:java -Dexec.mainClass="MyAPITest"
```

---

## 24-Hour Monitoring Scenario

For the professor's 24-hour test:

1. **Start UI** (optional, for visualization):
   ```batch
   run-ui.bat
   ```

2. **Run monitoring script** (calls API every hour):
   ```java
   // Create a script that calls API methods every hour for 24 hours
   // Events will trigger UI updates if UI is running
   // Or run headless if UI not needed
   ```

3. **Check logs**: All events logged to `log.txt`

---

## Troubleshooting

### API runs but nothing happens?
- This is normal if UI is not running
- Events are published but no subscribers exist
- Check `log.txt` to verify API is working

### Compilation errors?
```bash
mvnw clean compile
```

### JavaFX not found?
- Make sure Java 21 is installed
- Check `pom.xml` has JavaFX dependencies
- Run `mvnw clean install` to rebuild

---

## File Locations

- **UI Main Class**: `SmartGardenApplication.java`
- **API Main Class**: `GardenSimulationAPIExample.java`
- **Logs**: `log.txt` (in project root for API), `logs/garden_*.log` (for UI)

---

## 24-Hour Monitoring Test

**For the professor's 24-hour test scenario:**

```batch
run-24hour-test.bat
```

**What it does:**
- Initializes garden with predefined plants
- Runs for 24 simulated days (24 hours)
- Each hour, randomly calls: `rain()`, `temperature()`, or `parasite()`
- After 24 days, calls `getState()` to show final results
- All events logged to `log.txt`

**Manual (Maven):**
```bash
mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.GardenSimulator"
```

**Note:** By default, 1 second = 1 hour for faster testing. For real 24-hour test, modify `HOUR_IN_MILLIS` in `GardenSimulator.java` to `3600000` (1 hour in milliseconds).

---

## Summary

| Mode | Command | Result |
|------|---------|--------|
| UI Only | `run-ui.bat` | Visual garden, manual control |
| API Only | `run-api.bat` → option 1 | Console mode, no UI |
| 24-Hour Test | `run-24hour-test.bat` | Automated 24-day simulation test |

**Note:** UI and API run in separate processes. API writes to `log.txt`, UI writes to `logs/garden_*.log`.

