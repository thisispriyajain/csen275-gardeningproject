# Smart Garden API - Complete Running Guide

## 📋 Table of Contents
1. [Quick Start](#quick-start)
2. [Available Scripts](#available-scripts)
3. [Log Files Location](#log-files-location)
4. [Understanding Logs](#understanding-logs)
5. [Test Scenarios](#test-scenarios)
6. [Troubleshooting](#troubleshooting)

---

## 🚀 Quick Start

### Windows (Easiest Method)

**For 24-Hour Test (Matches Specification):**
```batch
run-24hour-test.bat
```

**For Quick Test:**
```batch
run-quick-test.bat
```

**For Basic API Example:**
```batch
run-api.bat
```
Then choose option `1` when prompted.

---

## 📜 Available Scripts

### 1. `run-24hour-test.bat` ⭐ **RECOMMENDED**
- **What it does:** Runs the complete 24-day simulation test
- **Matches specification:** Yes - exactly as required
- **Duration:** ~24 seconds (1 second = 1 day for testing)
- **Output:** Console + `log.txt`

**What happens:**
1. Initializes garden from `garden-config.json`
2. Randomly calls `rain()`, `temperature()`, or `parasite()` every hour (day)
3. Runs for 24 simulated days
4. Calls `getState()` after 24 days
5. All events logged to `log.txt`

### 2. `run-quick-test.bat`
- **What it does:** Quick test of all API methods
- **Duration:** ~15 seconds
- **Output:** Console + `log.txt`

**What happens:**
- Tests all API methods sequentially
- Shows automatic system responses (sprinklers, heating, pesticides)

### 3. `run-api.bat`
- **What it does:** Interactive menu to choose API or UI
- **Output:** Console + `log.txt`

### 4. `run-comprehensive-test.bat`
- **What it does:** Comprehensive test with multiple scenarios
- **Duration:** ~30 seconds
- **Output:** Console + `log.txt`

---

## 📁 Log Files Location

### Primary Log File: `log.txt`
- **Location:** Project root directory (`D:\smartGarden\log.txt`)
- **Created when:** `initializeGarden()` is called
- **Contains:** All API events with timestamps
- **Format:** `[YYYY-MM-DD HH:MM:SS] INFO [Category] Message`

**Example log entries:**
```
[2025-12-08 20:48:41] INFO [API] Initializing garden - Day 0 begins
[2025-12-08 20:48:41] INFO [API] Added plant: Strawberry at (1, 1)
[2025-12-08 20:48:41] INFO [API] Rainfall event: 25 units
[2025-12-08 20:48:41] INFO [Watering] Sprinkler deactivated for Zone 1
[2025-12-08 20:48:41] INFO [API] Temperature changed to 60°F (15°C)
[2025-12-08 20:48:41] INFO [Heating] Heating activated. Current temp: 15°C
[2025-12-08 20:48:41] INFO [API] Parasite infestation: Red Mite
[2025-12-08 20:48:41] INFO [PestControl] Threat detected in Zone 1 (HIGH)
[2025-12-08 20:48:44] INFO [PestControl] Applying treatment to Zone 1 - Infestation: 50%
[2025-12-08 20:48:44] INFO [PestControl] Treatment complete for Zone 1. Eliminated: 2, Stock remaining: 49
[2025-12-08 20:48:45] INFO [API] Garden State Report - Day 24
[2025-12-08 20:48:45] INFO [API] Alive: 4, Dead: 0
```

### Secondary Log Files: `logs/garden_*.log`
- **Location:** `D:\smartGarden\logs\garden_YYYYMMDD_HHMMSS.log`
- **Contains:** Detailed system logs (internal events)
- **Note:** These are supplementary - `log.txt` has all API events

---

## 📖 Understanding Logs

### Log Categories

1. **[API]** - API method calls and responses
   - `initializeGarden()` calls
   - `rain()`, `temperature()`, `parasite()` events
   - `getState()` reports

2. **[Watering]** - Sprinkler and watering system
   - Sprinkler activation/deactivation
   - Zone watering events
   - Water supply levels

3. **[PestControl]** - Pest detection and treatment
   - Threat detection
   - Treatment application
   - Pesticide stock levels

4. **[Heating]** - Temperature control system
   - Heating activation/deactivation
   - Temperature changes

5. **[Weather]** - Weather system events
   - Weather changes (RAINY, SUNNY, SNOWY, etc.)

6. **[Plant]** - Individual plant events
   - Plant growth
   - Plant death
   - Health changes

### Key Log Patterns to Look For

**Successful Garden Initialization:**
```
[API] Initializing garden - Day 0 begins
[API] Garden initialized with 4 plants.
```

**Rain Event:**
```
[API] Rainfall event: 25 units
[Watering] Sprinkler deactivated for Zone X
[API] Rain added water to PlantName at (x,y). Current water level: XX
```

**Temperature Change:**
```
[API] Temperature changed to 60°F (15°C)
[Heating] Heating activated. Current temp: 15°C
```

**Pest Attack:**
```
[API] Parasite infestation: Red Mite
[PestControl] Threat detected in Zone X (HIGH)
[PestControl] Applying treatment to Zone X - Infestation: 50%
[PestControl] Treatment complete for Zone X. Eliminated: 2, Stock remaining: 49
```

**Final State:**
```
[API] Garden State Report - Day 24
[API] Alive: 4, Dead: 0
[API]   - Strawberry at (1,1): ALIVE (Health: 85%, Water: 60%)
```

---

## 🧪 Test Scenarios

### Scenario 1: 24-Hour Monitoring Test (Specification Match)

**Run:**
```batch
run-24hour-test.bat
```

**What to check in `log.txt`:**
- ✅ Garden initialized with plants from config
- ✅ 24 API calls (rain/temperature/parasite)
- ✅ Automatic system responses (sprinklers, heating, pesticides)
- ✅ Final state report showing alive/dead plants

**Expected Duration:** ~24 seconds (1 second per day)

### Scenario 2: Quick Verification Test

**Run:**
```batch
run-quick-test.bat
```

**What to check:**
- ✅ All API methods called successfully
- ✅ Automatic sprinkler deactivation during rain
- ✅ Automatic heating activation for cold temperatures
- ✅ Automatic pesticide application for pests

### Scenario 3: Custom Test

**Create your own test:**
```java
import edu.scu.csen275.smartgarden.controller.GardenController;
import edu.scu.csen275.smartgarden.api.GardenSimulationAPI;

public class MyTest {
    public static void main(String[] args) {
        GardenController controller = new GardenController(9, 9);
        GardenSimulationAPI api = new GardenSimulationAPI(controller);
        
        api.initializeGarden();
        api.rain(30);
        api.temperature(50);
        api.parasite("Red Mite");
        api.getState();
        
        GardenSimulationAPI.closeApiLog();
    }
}
```

**Run:**
```batch
mvnw.cmd exec:java -Dexec.mainClass="MyTest"
```

---

## 🔍 How to Read log.txt

### Step 1: Open log.txt
- **Location:** `D:\smartGarden\log.txt`
- **Editor:** Any text editor (Notepad, VS Code, etc.)

### Step 2: Search for Key Events

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

### Step 3: Verify System Responses

**After rain() call, check for:**
- `[Watering] Sprinkler deactivated` - Sprinklers should stop
- `[API] Rain added water to` - Plants should receive water

**After temperature() call, check for:**
- `[Heating] Heating activated` - If temp < 15°C
- `[Heating] Heating deactivated` - If temp > 15°C

**After parasite() call, check for:**
- `[PestControl] Threat detected` - Pests detected
- `[PestControl] Applying treatment` - Pesticide applied (after 3 sec delay)
- `[PestControl] Treatment complete` - Pests eliminated

---

## ⚙️ Manual Run (Command Line)

### Step 1: Build Project
```batch
mvnw.cmd clean compile
```

### Step 2: Run Specific Test

**24-Hour Test:**
```batch
mvnw.cmd exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.GardenSimulator"
```

**Quick Test:**
```batch
mvnw.cmd exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.QuickAPITest"
```

**Basic Example:**
```batch
mvnw.cmd exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.GardenSimulationAPIExample"
```

---

## 📂 File Structure

```
D:\smartGarden\
├── log.txt                    ← MAIN LOG FILE (API events)
├── logs\
│   └── garden_*.log          ← Detailed system logs
├── src\main\resources\
│   ├── garden-config.json    ← Plant initialization config
│   ├── plants.json           ← All plant definitions
│   └── parasites.json        ← All parasite definitions
├── run-24hour-test.bat       ← 24-day simulation script
├── run-quick-test.bat        ← Quick test script
└── run-api.bat               ← API runner script
```

---

## 🐛 Troubleshooting

### Problem: log.txt not created

**Solution:**
1. Check file permissions in project directory
2. Ensure Java has write access
3. Check if `initializeGarden()` was called
4. Look for error messages in console

### Problem: Script fails to run

**Solution:**
1. Check Java is installed: `java -version` (should be Java 21+)
2. Check Maven wrapper exists: `mvnw.cmd` should be in project root
3. Try manual build: `mvnw.cmd clean compile`
4. Check for compilation errors

### Problem: No events in log.txt

**Solution:**
1. Verify `initializeGarden()` was called first
2. Check that API methods are being called
3. Look for error messages in console output
4. Verify `Logger.enableApiLogging()` is called (happens in `initializeGarden()`)

### Problem: Can't find log.txt

**Solution:**
- **Location:** Same directory as `run-24hour-test.bat`
- **Full path:** `D:\smartGarden\log.txt`
- **Note:** File is created when API runs, not before

---

## 📊 Example log.txt Output

```
[2025-12-08 20:48:41] INFO [API] Initializing garden - Day 0 begins
[2025-12-08 20:48:41] INFO [API] Added plant: Strawberry at (1, 1)
[2025-12-08 20:48:41] INFO [API] Added plant: Carrot at (2, 2)
[2025-12-08 20:48:41] INFO [API] Added plant: Tomato at (3, 3)
[2025-12-08 20:48:41] INFO [API] Added plant: Sunflower at (4, 4)
[2025-12-08 20:48:41] INFO [API] Garden initialized with 4 plants.
[2025-12-08 20:48:42] INFO [API] Rainfall event: 25 units
[2025-12-08 20:48:42] INFO [Watering] Sprinkler deactivated for Zone 1
[2025-12-08 20:48:42] INFO [API] Rain added water to Strawberry at (1,1). Current water level: 75
[2025-12-08 20:48:43] INFO [API] Temperature changed to 40°F (4°C)
[2025-12-08 20:48:43] INFO [Heating] Heating activated. Current temp: 4°C
[2025-12-08 20:48:44] INFO [API] Parasite infestation: Red Mite
[2025-12-08 20:48:44] INFO [PestControl] Threat detected in Zone 1 (HIGH)
[2025-12-08 20:48:47] INFO [PestControl] Applying treatment to Zone 1 - Infestation: 50%
[2025-12-08 20:48:47] INFO [PestControl] Treatment complete for Zone 1. Eliminated: 2, Stock remaining: 49
[2025-12-08 20:49:05] INFO [API] Garden State Report - Day 24
[2025-12-08 20:49:05] INFO [API] Alive: 4, Dead: 0
[2025-12-08 20:49:05] INFO [API] Total Plants: 4
[2025-12-08 20:49:05] INFO [API]   - Strawberry at (1,1): ALIVE (Health: 85%, Water: 60%)
[2025-12-08 20:49:05] INFO [API]   - Carrot at (2,2): ALIVE (Health: 90%, Water: 65%)
[2025-12-08 20:49:05] INFO [API]   - Tomato at (3,3): ALIVE (Health: 88%, Water: 62%)
[2025-12-08 20:49:05] INFO [API]   - Sunflower at (4,4): ALIVE (Health: 92%, Water: 58%)
```

---

## ✅ Verification Checklist

After running the API, verify in `log.txt`:

- [ ] `[API] Initializing garden - Day 0 begins` - Garden initialized
- [ ] `[API] Garden initialized with X plants` - Plants loaded from config
- [ ] `[API] Rainfall event` - Rain events logged
- [ ] `[API] Temperature changed` - Temperature events logged
- [ ] `[API] Parasite infestation` - Pest events logged
- [ ] `[Watering] Sprinkler` - Sprinkler responses logged
- [ ] `[Heating] Heating` - Heating system responses logged
- [ ] `[PestControl] Treatment` - Pesticide applications logged
- [ ] `[API] Garden State Report - Day 24` - Final state logged
- [ ] `[API] Alive: X, Dead: Y` - Plant survival status logged

---

## 📝 Summary

**To run the API:**
1. Use `run-24hour-test.bat` for specification-compliant test
2. Check `log.txt` in project root for all events
3. Look for `[API]` tags for API method calls
4. Look for system tags `[Watering]`, `[Heating]`, `[PestControl]` for automatic responses

**Log file location:**
- **Main log:** `D:\smartGarden\log.txt`
- **Detailed logs:** `D:\smartGarden\logs\garden_*.log`

**Key point:** All API events are logged to `log.txt` with timestamps and categories for easy tracking.

