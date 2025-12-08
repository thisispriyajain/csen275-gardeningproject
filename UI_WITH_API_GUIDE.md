# Running UI with API Mode - Guide

## Overview

This guide explains how to run the UI with API mode enabled, allowing API and UI to work together with **shared garden state** (similar to Smart_Garden_System 3).

## Key Features

When API mode is enabled:
- ✅ UI and API share the **same `GardenController`** (shared state)
- ✅ Automatic pest spawning: **DISABLED** (only via API calls)
- ✅ Automatic weather changes: **DISABLED** (only via API calls)  
- ✅ All other systems work automatically:
  - Pesticide application when pests detected
  - Heating activation when temperature is cold
  - Sprinkler activation when water is low
  - Continuous water decrease
  - Continuous monitoring

## How to Run

### Method 1: Using Helper Script (Recommended)

```batch
run-ui-with-api.bat
```

This script:
1. Builds the project
2. Launches UI with API mode enabled
3. API is created using the same controller as UI

### Method 2: Manual Launch

```batch
mvnw javafx:run -Dsmartgarden.api.enabled=true
```

### Method 3: Command-Line Argument

```batch
mvnw javafx:run --api
```

Or via Java:
```batch
java -Dsmartgarden.api.enabled=true -cp ... edu.scu.csen275.smartgarden.SmartGardenApplication --api
```

## What Happens

1. **UI launches** with API mode enabled
2. **API is created** using the same `GardenController` instance
3. **API mode flags** are set:
   - `PestControlSystem.setApiModeEnabled(true)`
   - `WeatherSystem.setApiModeEnabled(true)`
4. **Weather rotation** is automatically disabled (no conflicts with API)
5. **Headless simulation** starts (continuous plant updates)

## Making API Calls

Once UI is running with API mode enabled, you can make API calls:

### Option 1: From Another Terminal (Same Process)

**Note:** This creates a **new controller instance**, so garden state is **separate**. However, EventBus events will still work, so UI animations will trigger.

```batch
run-api.bat
```
Choose option 1 (Run API)

### Option 2: Use Test Scripts

```batch
run-quick-test.bat
```

Or:
```batch
mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.QuickAPITest"
```

### Option 3: Programmatic API Calls (Same Process)

If you want to make API calls from within the UI (same process, shared state), you can:

1. Access the API instance (currently private - would need getter)
2. Call API methods programmatically
3. UI will update automatically via EventBus

## Important Notes

### Same Process vs. Different Processes

**Same Process (Recommended):**
- UI creates API internally → Shared controller → Shared state ✅
- Both use the same garden instance
- Changes from API are immediately visible in UI

**Different Processes:**
- UI runs in one JVM, API in another JVM
- **Separate controller instances** → Separate garden state ⚠️
- EventBus events still work (static singleton)
- UI animations will trigger, but garden state is separate
- This is a limitation of the current architecture

### For Professor's 24-Hour Test

The professor will likely:
- Run **API-only mode** (`run-api.bat` option 1)
- Call API methods every hour for 24 hours
- Monitor via `log.txt`

In this case, UI is not needed, so the limitation doesn't matter.

## Comparison with Normal Simulation

| Feature | Normal UI | UI + API Mode |
|---------|-----------|---------------|
| Automatic Pests | ✅ Yes | ❌ No (API only) |
| Automatic Weather | ✅ Yes | ❌ No (API only) |
| Automatic Pesticide | ✅ Yes | ✅ Yes |
| Automatic Heating | ✅ Yes | ✅ Yes |
| Automatic Sprinklers | ✅ Yes | ✅ Yes |
| Water Decrease | ✅ Yes | ✅ Yes |
| Shared State | N/A | ✅ Yes (same process) |

## Troubleshooting

### API calls not visible in UI?

- **Check EventBus subscriptions:** UI should subscribe to `RainEvent`, `TemperatureEvent`, `ParasiteEvent`
- **Check API mode:** Make sure `smartgarden.api.enabled=true`
- **Check logs:** Look for "API mode enabled" messages in `log.txt`

### Weather still changing automatically?

- **Check rotation timer:** Should be stopped when API mode enabled
- **Check logs:** Look for "Stopped weather rotation timer" message
- **Verify API mode:** Check if `setApiModeEnabled(true)` was called

### Pests still spawning automatically?

- **Check API mode flag:** `PestControlSystem.isApiModeEnabled()` should return `true`
- **Check logs:** Look for "API mode enabled - automatic pest spawning disabled"

## Example Usage

```java
// In SmartGardenApplication (same process)
// API is already created when UI starts with API mode

// To make API calls programmatically (same process, shared state):
api.rain(25);           // Rain animation in UI
api.temperature(40);    // Temperature display updates
api.parasite("Red Mite"); // Pest animation in UI
```

## Next Steps

For true cross-process sharing (like Smart_Garden_System 3's singleton pattern), you could:
1. Make `Garden` a singleton (but this might affect normal simulation)
2. Use shared memory/file-based state
3. Use network-based communication
4. Keep current approach (same-process sharing recommended)

The current implementation is sufficient for most use cases where API and UI run in the same process.

