# Testing API Mode Implementation

This guide explains how to test that API mode correctly disables automatic pest spawning and weather changes, while keeping all other automatic systems working.

## Test 1: Verify API Mode Disables Automatic Pests/Weather

### Step 1: Run Quick API Test
```batch
run-quick-test.bat
```

Or manually:
```bash
mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.QuickAPITest"
```

### Step 2: Check `log.txt` for API Mode Messages

Look for these log entries that confirm API mode is enabled:

```
[timestamp] INFO [API] API mode enabled - automatic pest spawning and weather changes disabled
[timestamp] INFO [API] Pesticide, heating, sprinklers, and water monitoring remain automatic
[timestamp] INFO [PestControl] API mode enabled - automatic pest spawning disabled
[timestamp] INFO [Weather] API mode enabled - automatic weather changes disabled
```

### Step 3: Verify No Automatic Pest Spawning

**What to check:**
- Look through the entire log for messages like "Red Mite appeared at" or "Green Leaf Worm appeared at"
- These should **ONLY** appear after `api.parasite()` is called
- You should **NOT** see random pest spawns between API calls

**Expected:** Only pests spawned via `api.parasite()` calls should appear in logs.

### Step 4: Verify No Automatic Weather Changes

**What to check:**
- Look for "Weather changed from X to Y" messages
- These should **NOT** appear during the test (unless triggered by API)
- Weather should stay constant unless `api.rain()` or `api.temperature()` is called

**Expected:** Weather only changes when API methods are explicitly called.

### Step 5: Verify Automatic Systems Still Work

**Check logs for:**
1. **Pesticide Application** (should be automatic):
   ```
   [timestamp] INFO [PestControl] Threat detected in Zone X
   [timestamp] INFO [PestControl] Treatment complete for Zone X
   ```

2. **Heating Activation** (should be automatic when temperature is cold):
   ```
   [timestamp] INFO [Heating] Heating activated. Current temp: X°C
   ```

3. **Sprinkler Activation** (should be automatic when water is low):
   ```
   [timestamp] INFO [Watering] Auto-watered Zone X
   [timestamp] INFO [Watering] Sprinkler activated for Zone X
   ```

4. **Water Decrease** (should be continuous):
   - Plants should gradually lose water over time
   - Check plant water levels in logs

---

## Test 2: Verify Normal Simulation is Unaffected

### Step 1: Run Normal UI Simulation

```batch
run-ui.bat
```

### Step 2: Verify Normal Behavior

1. **Automatic Pest Spawning:** 
   - Wait 1-2 minutes in the simulation
   - You should see pests appearing randomly on plants
   - Check logs for random pest spawns

2. **Automatic Weather Changes:**
   - Weather should rotate automatically (if rotation mode enabled)
   - Or weather should change periodically based on duration

**Expected:** Normal simulation works exactly as before - automatic pests and weather should appear.

---

## Test 3: Comprehensive Test (All Scenarios)

### Run Comprehensive Test Script

```batch
run-comprehensive-test.bat
```

Or manually:
```bash
mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.ComprehensiveAPITest"
```

### What This Test Verifies:

1. ✅ Water decreasing naturally (from headless simulation)
2. ✅ Sprinklers activating when water is low
3. ✅ Sprinklers deactivating when rain starts
4. ✅ Pests attacking and causing damage (API-triggered)
5. ✅ Pesticide application after 3-second delay (automatic)
6. ✅ Heating activation when temperature is cold (automatic)
7. ✅ Heating deactivation when temperature is hot (automatic)
8. ✅ Plant health changes from all factors

### Check Logs For:

- **API Mode Enabled Messages**
- **No Random Pest Spawns** (only API-triggered)
- **No Random Weather Changes** (only API-triggered)
- **All Automatic Responses** (pesticide, heating, sprinklers)

---

## What to Look For in Logs

### ✅ GOOD (API Mode Working Correctly):

```
[timestamp] INFO [API] API mode enabled - automatic pest spawning and weather changes disabled
[timestamp] INFO [PestControl] API mode enabled - automatic pest spawning disabled
[timestamp] INFO [Weather] API mode enabled - automatic weather changes disabled
[timestamp] INFO [API] Parasite infestation: Red Mite
[timestamp] WARNING [PestControl] Red Mite appeared at (X,Y)  ← Only after API call
[timestamp] INFO [PestControl] Treatment complete for Zone X  ← Automatic pesticide
[timestamp] INFO [Watering] Auto-watered Zone X  ← Automatic sprinklers
[timestamp] INFO [Heating] Heating activated  ← Automatic heating
```

### ❌ BAD (API Mode Not Working):

```
[timestamp] WARNING [PestControl] Red Mite appeared at (X,Y)  ← Random spawn (shouldn't happen)
[timestamp] INFO [Weather] Weather changed from SUNNY to RAINY  ← Random change (shouldn't happen)
```

### ✅ GOOD (Normal Simulation):

```
[timestamp] WARNING [PestControl] Red Mite appeared at (X,Y)  ← Random spawn (OK in normal mode)
[timestamp] INFO [Weather] Weather changed from SUNNY to RAINY  ← Random change (OK in normal mode)
```

---

## Quick Verification Checklist

**API Mode Test:**
- [ ] API mode enabled messages appear in logs
- [ ] No pests spawn except after `api.parasite()` calls
- [ ] No weather changes except after API calls
- [ ] Pesticide applies automatically when pests detected
- [ ] Heating activates automatically when temperature is cold
- [ ] Sprinklers activate automatically when water is low
- [ ] Water decreases continuously

**Normal Simulation Test:**
- [ ] Pests spawn randomly (normal behavior)
- [ ] Weather changes automatically (normal behavior)
- [ ] All systems work as before

---

## Troubleshooting

### If automatic pests still appear in API mode:

1. Check that `api.initializeGarden()` was called
2. Verify log shows "API mode enabled" messages
3. Check `PestControlSystem.update()` - should skip spawning when `apiModeEnabled == true`

### If automatic weather changes still occur in API mode:

1. Check that `api.initializeGarden()` was called
2. Verify log shows "API mode enabled" messages
3. Check `WeatherSystem.update()` - should skip weather changes when `apiModeEnabled == true`

### If normal simulation doesn't work:

1. Make sure you're running normal UI (not API)
2. No API mode should be enabled (flags remain false)
3. Check that automatic spawning/changes work normally

