package edu.scu.csen275.smartgarden.api;

import java.util.Map;

/**
 * Comprehensive Garden Test - Full System Testing
 * 
 * This script tests ALL systems in 2 hours:
 * - Temperature changes (heating & cooling)
 * - Rain events
 * - Pest infestations
 * - Automatic systems (watering, heating, cooling, pest control)
 * - Auto-refill systems
 * 
 * Each hour tests multiple events to see all systems in action.
 * 
 * Usage:
 *   mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.ComprehensiveGardenTest"
 */
public class ComprehensiveGardenTest {
    private static final long HOUR_IN_MILLIS = 3600000; // 1 hour = 3600000 milliseconds
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Comprehensive Garden Test - 2 Hours");
        System.out.println("Testing ALL Systems: Rain, Temperature, Pests");
        System.out.println("========================================\n");
        
        // Create the API wrapper
        GardenSimulationAPI gardenAPI = new GardenSimulationAPI();
        
        // Beginning of the simulation
        System.out.println("=== Beginning of Simulation ===");
        gardenAPI.initializeGarden(); // This marks the beginning of the clock
        
        // Verify headless simulation is running
        if (gardenAPI.isHeadlessSimulationRunning()) {
            System.out.println("✓ Headless simulation loop started");
            System.out.println("  - Plants update continuously");
            System.out.println("  - Water decreases automatically");
            System.out.println("  - Systems respond automatically\n");
        }
        
        // Get initial plant details
        Map<String, Object> initialPlantDetails = gardenAPI.getPlants();
        System.out.println("Initial Plants: " + initialPlantDetails.get("plants"));
        System.out.println("Water Requirements: " + initialPlantDetails.get("waterRequirement"));
        System.out.println("Pest Vulnerabilities: " + initialPlantDetails.get("parasites"));
        System.out.println();
        
        // ========================================
        // HOUR 1: Test Multiple Events
        // ========================================
        System.out.println("========================================");
        System.out.println("=== HOUR 1: Multiple Events ===");
        System.out.println("========================================\n");
        
        // Event 1: Low Temperature (should trigger HEATING)
        System.out.println("--- Event 1: Low Temperature (Testing HEATING System) ---");
        int lowTemp = 45; // 45°F = ~7°C (below 15°C threshold)
        System.out.println("Setting temperature to " + lowTemp + "°F (~" + 
                         (int)((lowTemp - 32) * 5.0 / 9.0) + "°C)");
        System.out.println("Expected: HEATING system should activate (temp < 15°C)");
        gardenAPI.temperature(lowTemp);
        System.out.println();
        
        // Wait a bit for systems to respond
        try {
            Thread.sleep(5000); // 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Event 2: Rain Event (should stop sprinklers, water plants)
        System.out.println("--- Event 2: Rainfall (Testing WATERING System) ---");
        int rainAmount = 30;
        System.out.println("Rainfall: " + rainAmount + " units");
        System.out.println("Expected: Sprinklers should stop, plants get watered");
        gardenAPI.rain(rainAmount);
        System.out.println();
        
        // Wait a bit
        try {
            Thread.sleep(5000); // 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Event 3: High Temperature (should trigger COOLING)
        System.out.println("--- Event 3: High Temperature (Testing COOLING System) ---");
        int highTemp = 110; // 110°F = ~43°C (above max temp for most plants ~30°C)
        System.out.println("Setting temperature to " + highTemp + "°F (~" + 
                         (int)((highTemp - 32) * 5.0 / 9.0) + "°C)");
        System.out.println("Expected: COOLING system should activate (temp > plant max)");
        gardenAPI.temperature(highTemp);
        System.out.println();
        
        // Wait a bit
        try {
            Thread.sleep(5000); // 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Event 4: Pest Infestation (should trigger PEST CONTROL)
        System.out.println("--- Event 4: Pest Infestation (Testing PEST CONTROL System) ---");
        String pestType = "Red Mite";
        System.out.println("Parasite: " + pestType);
        System.out.println("Expected: Pest control should detect and treat infestation");
        gardenAPI.parasite(pestType);
        System.out.println();
        
        System.out.println("=== Hour 1 Complete - Sleeping for 1 hour ===\n");
        sleepOneHour();
        
        // ========================================
        // HOUR 2: More Events
        // ========================================
        System.out.println("========================================");
        System.out.println("=== HOUR 2: More Events ===");
        System.out.println("========================================\n");
        
        // Event 1: Moderate Temperature (optimal range)
        System.out.println("--- Event 1: Optimal Temperature ---");
        int optimalTemp = 70; // 70°F = ~21°C (optimal range)
        System.out.println("Setting temperature to " + optimalTemp + "°F (~" + 
                         (int)((optimalTemp - 32) * 5.0 / 9.0) + "°C)");
        System.out.println("Expected: Both heating and cooling should be OFF");
        gardenAPI.temperature(optimalTemp);
        System.out.println();
        
        // Wait a bit
        try {
            Thread.sleep(5000); // 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Event 2: Another Rain Event
        System.out.println("--- Event 2: Heavy Rainfall ---");
        int heavyRain = 40;
        System.out.println("Rainfall: " + heavyRain + " units");
        System.out.println("Expected: Plants get more water, sprinklers remain off");
        gardenAPI.rain(heavyRain);
        System.out.println();
        
        // Wait a bit
        try {
            Thread.sleep(5000); // 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Event 3: Different Pest Type
        System.out.println("--- Event 3: Different Pest Infestation ---");
        String pestType2 = "Green Leaf Worm";
        System.out.println("Parasite: " + pestType2);
        System.out.println("Expected: Pest control should handle different pest type");
        gardenAPI.parasite(pestType2);
        System.out.println();
        
        // Wait a bit
        try {
            Thread.sleep(5000); // 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Event 4: Extreme High Temperature
        System.out.println("--- Event 4: Extreme High Temperature (Testing COOLING) ---");
        int extremeTemp = 120; // 120°F = ~49°C (maximum allowed, way above plant max)
        System.out.println("Setting temperature to " + extremeTemp + "°F (~" + 
                         (int)((extremeTemp - 32) * 5.0 / 9.0) + "°C)");
        System.out.println("Expected: COOLING system should activate at HIGH mode");
        gardenAPI.temperature(extremeTemp);
        System.out.println();
        
        System.out.println("=== Hour 2 Complete - Sleeping for 1 hour ===\n");
        sleepOneHour();
        
        // ========================================
        // FINAL STATE
        // ========================================
        System.out.println("========================================");
        System.out.println("=== After 2 Hours - Final State ===");
        System.out.println("========================================");
        gardenAPI.getState();
        
        System.out.println("\n========================================");
        System.out.println("=== Test Complete ===");
        System.out.println("========================================");
        System.out.println("API Days (from API calls): " + gardenAPI.getDayCount());
        System.out.println("Headless Simulation Days: " + gardenAPI.getHeadlessDayCount());
        System.out.println("\n=== Check log.txt for detailed logs ===");
        System.out.println("Look for these log categories:");
        System.out.println("  [API]          - All API method calls");
        System.out.println("  [Heating]      - Heating system activation/deactivation");
        System.out.println("  [Cooling]      - Cooling system activation/deactivation");
        System.out.println("  [Watering]     - Sprinkler activities");
        System.out.println("  [PestControl] - Pest detection and treatment");
        System.out.println("  [Simulation]   - Auto-refill messages");
        System.out.println("  [Weather]      - Weather changes");
        System.out.println("  [Plant]        - Plant health updates");
        
        // Cleanup
        gardenAPI.stopHeadlessSimulation();
        System.out.println("\n✓ Headless simulation stopped");
        GardenSimulationAPI.closeApiLog();
        System.out.println("✓ API log file closed");
    }
    
    /**
     * Sleeps for one hour (3600000 milliseconds).
     */
    private static void sleepOneHour() {
        try {
            System.out.println("Sleeping for 1 hour (3600000 ms)...");
            System.out.println("During this time:");
            System.out.println("  - Plants continue to update");
            System.out.println("  - Water levels decrease");
            System.out.println("  - Systems respond automatically");
            System.out.println("  - Auto-refill may occur if thresholds are reached\n");
            Thread.sleep(HOUR_IN_MILLIS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
    }
}

