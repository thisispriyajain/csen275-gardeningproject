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
- EventBus is ready but idle (no API events)
- Normal operation via polling updates

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
- Events are published but ignored (no UI to receive them)
- Logs written to `log.txt`

---

### Option 3: Run UI + API Together (Shared State)

This runs UI and API together with **shared garden state** (like Smart_Garden_System 3).

**Method A: Use Helper Script (Recommended)**
```batch
run-ui-with-api.bat
```
This launches UI with API mode enabled. The API uses the same controller as the UI, so they share the same garden state.

**Method B: Manual Launch**
```batch
mvnw javafx:run -Dsmartgarden.api.enabled=true
```

**What happens:**
- UI launches with API mode enabled
- API is created using the same `GardenController` (shared state)
- Automatic pest spawning: **DISABLED** (only via API calls)
- Automatic weather changes: **DISABLED** (only via API calls)
- All other systems work automatically (pesticide, heating, sprinklers, water decrease)
- External API calls (from another terminal/script) will update the UI via EventBus

**To make API calls:**
- Option 1: Use `run-api.bat` in another terminal (option 1 - API only)
- Option 2: Use `run-quick-test.bat` in another terminal
- Option 3: Create your own API script that uses the same controller

**Note:** When API mode is enabled, the UI's weather rotation is automatically disabled to prevent conflicts.

---

## Testing EventBus Connection

### Test Script (Create `test-api.bat`):
```batch
@echo off
echo Testing API with EventBus...
echo Make sure UI is running first!
pause

mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.GardenSimulationAPIExample"
```

### Manual Testing:
1. Start UI: `run-ui.bat`
2. Wait for UI to fully load
3. In another terminal, run:
   ```bash
   mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.GardenSimulationAPIExample"
   ```
4. Watch the UI - you should see:
   - Rain animation when `rain()` is called
   - Weather icon changes when `temperature()` is called
   - Pest animations when `parasite()` is called

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

### UI doesn't show API updates?
- Make sure UI is running BEFORE you call API
- Check console for EventBus messages: `[SmartGardenApplication] Received RainEvent...`
- Verify EventBus subscriptions are set up (check `setupEventBusSubscriptions()` was called)

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
- **EventBus**: `util/EventBus.java`
- **Events**: `events/RainEvent.java`, `events/TemperatureEvent.java`, etc.
- **Logs**: `log.txt` (in project root)

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
| UI + API | `run-ui.bat` then `run-api.bat` | API triggers UI updates via EventBus |
| 24-Hour Test | `run-24hour-test.bat` | Automated 24-day simulation test |

**The EventBus pattern allows API and UI to work independently OR together!**

