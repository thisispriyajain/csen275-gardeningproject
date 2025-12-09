# Professor's Guide: Running Your Own API Test Script

## 📋 Important Information Before Running Your Script

### 1. **API Constructor**
The `GardenSimulationAPI` class has a **no-argument constructor** that matches the specification:

```java
GardenSimulationAPI gardenAPI = new GardenSimulationAPI();
```

**No `GardenController` parameter needed!** The API creates its own internal controller (9x9 grid).

### 2. **Required Method Call Order**
1. **MUST call `initializeGarden()` first** - This starts the simulation clock and enables logging
2. Then call `rain()`, `temperature()`, or `parasite()` as needed
3. Call `getState()` at the end to see final results
4. **Cleanup is automatic** - No need to call `stopHeadlessSimulation()` or `closeApiLog()` - shutdown hook handles it

### 3. **Automatic System Behavior**
When API mode is active (after `initializeGarden()` is called):
- ✅ **Automatic sprinkler activation** when water levels drop
- ✅ **Automatic heating activation** when temperature is too low
- ✅ **Automatic cooling activation** when temperature exceeds plant maximum
- ✅ **Automatic pesticide application** when pests are detected
- ✅ **Automatic water supply refill** when below threshold
- ✅ **Continuous water decrease** (plants consume water over time)
- ❌ **NO automatic pest spawning** (only via `parasite()` API call)
- ❌ **NO automatic weather changes** (only via `rain()` API call)

### 4. **Simulation Time Scale**
- **1 second real time = 1 minute simulation time**
- **1 simulation day = 1,440 ticks = 24 minutes real time**
- **24 simulation days = 9.6 hours real time**

The headless simulation engine runs continuously in the background, updating plants and systems every second.

### 5. **Configuration Files**
The API reads from these configuration files (in `src/main/resources/`):
- **`garden-config.json`** - Initial plant placement
- **`plants.json`** - Plant type definitions (water requirements, growth rates, etc.)
- **`parasites.json`** - Parasite definitions and plant vulnerabilities

If config files are missing, the API falls back to default plants.

---

## 📁 Log File Location

### Primary Log File: `log.txt`
- **Location:** `D:\smartGarden\log.txt` (project root directory)
- **Created when:** `initializeGarden()` is called
- **Contains:** All API events, system responses, and simulation updates
- **Format:** `[YYYY-MM-DD HH:MM:SS] INFO [Category] Message`

**Full Path:** `D:\smartGarden\log.txt`

### Log File Details
- **File Size:** Approximately 1-2 MB for a 24-day simulation
- **Log Entries:** ~7,000-8,000 entries for 24 days
- **No file size limit:** Can handle extended simulations
- **Auto-created:** File is created automatically when API runs
- **Append mode:** If file exists, new logs are appended

---

## 📖 How to Read log.txt

### Step 1: Open the Log File
Open `D:\smartGarden\log.txt` in any text editor (Notepad, VS Code, Notepad++, etc.)

### Step 2: Understand Log Categories

Log entries are tagged with categories in brackets:

- **`[API]`** - API method calls and responses
  - `initializeGarden()` initialization
  - `rain()`, `temperature()`, `parasite()` events
  - `getState()` reports

- **`[Watering]`** - Sprinkler and watering system
  - Sprinkler activation/deactivation
  - Zone watering events
  - Water supply levels

- **`[PestControl]`** - Pest detection and treatment
  - Threat detection (HIGH/CRITICAL)
  - Treatment application
  - Pesticide stock levels

- **`[Heating]`** - Temperature control system (heating)
  - Heating activation/deactivation
  - Temperature increases/decreases

- **`[Cooling]`** - Temperature control system (cooling)
  - Cooling activation/deactivation
  - Temperature decreases when too high
  - Activates when temperature exceeds plant maximum threshold

- **`[Weather]`** - Weather system events
  - Weather changes (RAINY, SUNNY, SNOWY)

- **`[Simulation]`** - Headless simulation updates
  - Day completion
  - Tick updates (every 100 ticks)

### Step 3: Search for Key Events

**Search for initialization:**
```
[API] Initializing garden
```

**Search for rain events:**
```
[API] Rainfall event
```

**Search for temperature changes:**
```
[API] Temperature changed
```

**Search for pest attacks:**
```
[API] Parasite infestation
```

**Search for final state:**
```
[API] Garden State Report
```

### Step 4: Verify System Responses

**After `rain()` call, look for:**
- `[Watering] Sprinkler deactivated for Zone X` - Sprinklers should stop during rain
- `[API] Rain added water to PlantName at (x,y). Current water level: XX` - Plants receive water

**After `temperature()` call, look for:**
- `[Heating] Heating activated. Current temp: X°C` - If temp < 15°C
- `[Heating] Heating deactivated. Current temp: X°C` - If temp > 15°C
- `[Heating] Temperature increasing: X°C → Y°C` - Heating system working
- `[Cooling] Cooling activated. Current temp: X°C (max threshold: Y°C)` - If temp > plant max
- `[Cooling] Cooling deactivated. Current temp: X°C` - If temp returns to safe range
- `[Cooling] Temperature decreasing: X°C → Y°C` - Cooling system working

**After `parasite()` call, look for:**
- `[PestControl] Threat detected in Zone X (HIGH/CRITICAL)` - Pests detected
- `[PestControl] Applying treatment to Zone X` - Pesticide applied
- `[PestControl] Treatment complete for Zone X. Eliminated: X, Stock remaining: XX` - Pests eliminated

### Step 5: Check Final State

Look for the final state report:
```
[API] Garden State Report - Day 24
[API] Alive: X, Dead: Y
[API] Total Plants: X
[API]   - PlantName at (x,y): ALIVE (Health: XX%, Water: XX%)
```

---

## 🔍 Example Log Entries

### Garden Initialization
```
[2025-12-08 20:48:41] INFO [API] Initializing garden - Day 0 begins
[2025-12-08 20:48:41] INFO [API] Added plant: Strawberry at (1, 1)
[2025-12-08 20:48:41] INFO [API] Added plant: Carrot at (2, 2)
[2025-12-08 20:48:41] INFO [API] Added plant: Tomato at (3, 3)
[2025-12-08 20:48:41] INFO [API] Added plant: Sunflower at (4, 4)
[2025-12-08 20:48:41] INFO [API] Garden initialized with 4 plants.
```

### Rain Event
```
[2025-12-08 20:48:42] INFO [API] Rainfall event: 25 units
[2025-12-08 20:48:42] INFO [Watering] Rain detected - stopped all active sprinklers
[2025-12-08 20:48:42] INFO [Watering] Sprinkler deactivated for Zone 1
[2025-12-08 20:48:42] INFO [API] Rain added water to Strawberry at (1,1). Current water level: 75
[2025-12-08 20:48:42] INFO [API] Rain added water to Carrot at (2,2). Current water level: 80
```

### Temperature Change (Low - Heating)
```
[2025-12-08 20:48:43] INFO [API] Temperature changed to 40°F (4°C)
[2025-12-08 20:48:43] INFO [Heating] Heating activated. Current temp: 4°C
[2025-12-08 20:48:43] INFO [Heating] Temperature increasing: 4°C → 6°C (increased by 2°C)
[2025-12-08 20:48:44] INFO [Heating] Temperature increasing: 6°C → 8°C (increased by 2°C)
```

### Temperature Change (High - Cooling)
```
[2025-12-08 20:48:45] INFO [API] Temperature changed to 110°F (43°C)
[2025-12-08 20:48:45] INFO [Cooling] Cooling activated. Current temp: 43°C (max threshold: 30°C)
[2025-12-08 20:48:46] INFO [Cooling] Temperature decreasing: 43°C → 42°C (decreased by 1°C)
[2025-12-08 20:48:47] INFO [Cooling] Temperature decreasing: 42°C → 41°C (decreased by 1°C)
[2025-12-08 20:48:48] INFO [Cooling] Cooling deactivated. Current temp: 30°C
```

### Parasite Infestation
```
[2025-12-08 20:48:44] INFO [API] Parasite infestation: Red Mite
[2025-12-08 20:48:44] INFO [PestControl] Registered external pest: Red Mite at (1,1)
[2025-12-08 20:48:44] INFO [API] Strawberry at (1,1) attacked by Red Mite
[2025-12-08 20:48:44] INFO [PestControl] Threat detected in Zone 1 (HIGH)
[2025-12-08 20:48:44] INFO [PestControl] Applying treatment to Zone 1
[2025-12-08 20:48:44] INFO [PestControl] Applying treatment to Zone 1 - Infestation: 50%
[2025-12-08 20:48:44] INFO [PestControl] Treatment complete for Zone 1. Eliminated: 2, Stock remaining: 49
```

### Final State Report
```
[2025-12-08 20:49:05] INFO [API] Garden State Report - Day 24
[2025-12-08 20:49:05] INFO [API] Alive: 4, Dead: 0
[2025-12-08 20:49:05] INFO [API] Total Plants: 4
[2025-12-08 20:49:05] INFO [API]   - Strawberry at (1,1): ALIVE (Health: 85%, Water: 60%)
[2025-12-08 20:49:05] INFO [API]   - Carrot at (2,2): ALIVE (Health: 90%, Water: 65%)
[2025-12-08 20:49:05] INFO [API]   - Tomato at (3,3): ALIVE (Health: 88%, Water: 62%)
[2025-12-08 20:49:05] INFO [API]   - Sunflower at (4,4): ALIVE (Health: 92%, Water: 58%)
```

---

## 📝 API Method Signatures

### `void initializeGarden()`
- **Purpose:** Initializes garden with plants from `garden-config.json`
- **Must be called first:** Starts simulation clock and enables logging
- **Returns:** Nothing
- **Side effects:** Creates `log.txt`, starts headless simulation engine

### `Map<String, Object> getPlants()`
- **Purpose:** Returns plant information
- **Returns:** Map with keys:
  - `"plants"`: `List<String>` of plant names
  - `"waterRequirement"`: `List<Integer>` of water requirements
  - `"parasites"`: `List<List<String>>` of parasite vulnerabilities per plant

### `void rain(int amount)`
- **Purpose:** Simulates rainfall
- **Parameter:** `amount` - rainfall units (typically 10-40 based on water requirements)
- **Side effects:** 
  - Stops all active sprinklers
  - Adds water to all plants
  - Sets weather to RAINY
  - Increments day count

### `void temperature(int temp)`
- **Purpose:** Sets ambient temperature
- **Parameter:** `temp` - temperature in Fahrenheit (40-120°F, validated)
- **Side effects:**
  - Sets ambient temperature (converted to Celsius internally)
  - Triggers heating system if temperature is low (< 15°C)
  - Triggers cooling system if temperature exceeds plant maximum
  - Increments day count
  - **Note:** Does NOT automatically change weather

### `void parasite(String parasiteType)`
- **Purpose:** Triggers parasite infestation
- **Parameter:** `parasiteType` - pest name (e.g., "Red Mite", "Green Leaf Worm", "Black Beetle", "Brown Caterpillar")
- **Side effects:**
  - Attacks all vulnerable plants
  - Automatically triggers pesticide application
  - Increments day count

### `void getState()`
- **Purpose:** Logs current garden state
- **Returns:** Nothing (logs to `log.txt` and console)
- **Output:** Lists all plants with health, water level, and status (ALIVE/DEAD)

### `void stopHeadlessSimulation()`
- **Purpose:** Stops the background simulation engine
- **Optional:** Cleanup happens automatically via shutdown hook when JVM exits
- **Use when:** You need to stop simulation before script ends

### `static void closeApiLog()`
- **Purpose:** Closes the `log.txt` file properly
- **Optional:** Cleanup happens automatically via shutdown hook when JVM exits
- **Use when:** You need to close log file before script ends
- **Usage:** `GardenSimulationAPI.closeApiLog();`

---

## ✅ Example Script Pattern

Here's the pattern your script should follow (matching the specification):

```java
import edu.scu.csen275.smartgarden.api.GardenSimulationAPI;
import java.util.Map;

public class MyTestScript {
    public static void main(String[] args) {
        // Create API instance (no arguments needed)
        GardenSimulationAPI gardenAPI = new GardenSimulationAPI();
        
        // Beginning of simulation - MUST call this first
        gardenAPI.initializeGarden();
        
        // Get initial plant information (optional)
        Map<String, Object> plants = gardenAPI.getPlants();
        System.out.println("Plants: " + plants.get("plants"));
        
        // Simulate 24 days (24 hours in your script)
        for (int day = 1; day <= 24; day++) {
            // Call rain(), temperature(), or parasite()
            gardenAPI.rain(25);
            // OR
            gardenAPI.temperature(60);
            // OR
            gardenAPI.parasite("Red Mite");
            
            // Sleep one hour (simulated)
            sleepOneHour();
        }
        
        // After 24 days - get final state
        gardenAPI.getState();
        
        // Cleanup is automatic - no need to call stopHeadlessSimulation() or closeApiLog()
        // Shutdown hook will handle cleanup when JVM exits
    }
    
    private static void sleepOneHour() {
        try {
            Thread.sleep(3600000); // 1 hour = 3600000 milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

---

## ⚠️ Important Notes

1. **Always call `initializeGarden()` first** - This enables logging and starts the simulation
2. **Cleanup is automatic** - No need to manually call `stopHeadlessSimulation()` or `closeApiLog()` - shutdown hook handles it when JVM exits
3. **Temperature range:** 40-120°F (values outside this range are clamped)
4. **Parasite names:** Case-insensitive - you can use any case (e.g., "Red Mite", "red mite", "RED MITE" all work). Valid names: "Red Mite", "Green Leaf Worm", "Black Beetle", "Brown Caterpillar"
5. **Log file location:** `D:\smartGarden\log.txt` (same directory as your script)
6. **Continuous simulation:** The headless engine runs in the background, updating plants every second
7. **Automatic systems:** Sprinklers, heating, cooling, and pesticides work automatically - you don't need to call them manually
8. **Cooling system:** Activates when temperature exceeds the maximum temperature of any living plant (varies by plant type: 28-30°C)

---

## 🔧 Troubleshooting

### Problem: log.txt not created
- **Solution:** Ensure `initializeGarden()` was called first
- **Check:** Look for `[API] Initializing garden` in console output

### Problem: Can't find log.txt
- **Location:** `D:\smartGarden\log.txt` (project root)
- **Note:** File is created when `initializeGarden()` is called

### Problem: Script hangs or doesn't complete
- **Solution:** Cleanup is automatic, but if you need to stop early, you can call `stopHeadlessSimulation()` and `closeApiLog()`
- **Check:** Look for errors in console output

### Problem: No automatic system responses in logs
- **Solution:** This is normal - systems only activate when conditions are met:
  - Sprinklers: Only when water level < water requirement
  - Heating: Only when temperature < 15°C
  - Cooling: Only when temperature > plant maximum (28-30°C depending on plant type)
  - Pesticides: Only when pests are detected

---

## 📊 What to Look For in log.txt

After running your script, verify in `log.txt`:

- ✅ `[API] Initializing garden - Day 0 begins` - Garden initialized
- ✅ `[API] Garden initialized with X plants` - Plants loaded
- ✅ `[API] Rainfall event` - Rain events logged
- ✅ `[API] Temperature changed` - Temperature events logged
- ✅ `[API] Parasite infestation` - Pest events logged
- ✅ `[Watering] Sprinkler` - Sprinkler responses (when needed)
- ✅ `[Heating] Heating` - Heating system responses (when temp < 15°C)
- ✅ `[Cooling] Cooling` - Cooling system responses (when temp > plant max)
- ✅ `[PestControl] Treatment` - Pesticide applications (when pests detected)
- ✅ `[API] Garden State Report - Day 24` - Final state logged
- ✅ `[API] Alive: X, Dead: Y` - Plant survival status

---

## 📞 Summary

**To run your script:**
1. Use `new GardenSimulationAPI()` (no arguments)
2. Call `initializeGarden()` first
3. Call `rain()`, `temperature()`, or `parasite()` as needed
4. Call `getState()` at the end
5. **Cleanup is automatic** - no manual cleanup needed!

**To read logs:**
1. Open `D:\smartGarden\log.txt`
2. Search for `[API]` tags for API calls
3. Search for `[Watering]`, `[Heating]`, `[Cooling]`, `[PestControl]` for automatic system responses
4. Look for `[API] Garden State Report` for final results

**Log file location:** `D:\smartGarden\log.txt`

