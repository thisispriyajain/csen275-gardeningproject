package edu.scu.csen275.smartgarden.api;

import edu.scu.csen275.smartgarden.controller.GardenController;
import java.util.Map;
import java.util.Random;

/**
 * Garden Simulator - 24-hour automated testing script.
 * 
 * This script simulates the professor's 24-hour monitoring scenario:
 * - Initializes garden
 * - Calls API methods (rain, temperature, parasite) every hour for 24 hours
 * - Each hour represents one simulated day
 * - After 24 days, checks final state
 * 
 * Usage:
 *   mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.GardenSimulator"
 */
public class GardenSimulator {
    private static final int SIMULATION_DAYS = 24;
    private static final long HOUR_IN_MILLIS = 1000; // 1 second = 1 hour for testing (adjust as needed)
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Garden Simulator - 24 Day Test");
        System.out.println("========================================\n");
        
        // Create GardenController (9x9 grid)
        GardenController controller = new GardenController(9, 9);
        
        // Create the API wrapper
        GardenSimulationAPI gardenAPI = new GardenSimulationAPI(controller);
        
        // Beginning of the simulation
        System.out.println("=== Beginning of Simulation ===");
        gardenAPI.initializeGarden(); // This marks the beginning of the clock and starts headless simulation
        
        // Verify headless simulation is running
        if (gardenAPI.isHeadlessSimulationRunning()) {
            System.out.println("✓ Headless simulation loop started - plants will update continuously");
            System.out.println("  (Water decreases automatically, systems respond continuously)\n");
        }
        
        // Get initial plant details
        Map<String, Object> initialPlantDetails = gardenAPI.getPlants();
        System.out.println("Initial Plants: " + initialPlantDetails.get("plants"));
        System.out.println("Initial Water Requirements: " + initialPlantDetails.get("waterRequirement"));
        System.out.println("Pest Vulnerabilities: " + initialPlantDetails.get("parasites"));
        System.out.println();
        
        Random random = new Random();
        String[] parasiteTypes = {"Red Mite", "Green Leaf Worm", "Black Beetle", "Brown Caterpillar"};
        
        // Simulate 24 days (24 hours)
        for (int day = 1; day <= SIMULATION_DAYS; day++) {
            System.out.println("=== Beginning of Day " + day + " ===");
            
            // Randomly choose an environmental event for this day
            int eventType = random.nextInt(3); // 0=rain, 1=temperature, 2=parasite
            
            switch (eventType) {
                case 0: // Rain
                    int rainAmount = 10 + random.nextInt(30); // 10-40 units
                    System.out.println("Day " + day + ": Rainfall event - " + rainAmount + " units");
                    gardenAPI.rain(rainAmount);
                    break;
                    
                case 1: // Temperature change
                    int temperature = 40 + random.nextInt(80); // 40-120°F (spec range)
                    System.out.println("Day " + day + ": Temperature change - " + temperature + "°F");
                    gardenAPI.temperature(temperature);
                    break;
                    
                case 2: // Parasite infestation
                    String parasiteType = parasiteTypes[random.nextInt(parasiteTypes.length)];
                    System.out.println("Day " + day + ": Parasite infestation - " + parasiteType);
                    gardenAPI.parasite(parasiteType);
                    break;
            }
            
            // End of day - sleep one hour (simulated)
            System.out.println("Day " + day + " complete.\n");
            sleepOneHour();
        }
        
        // After 24 days - get final state
        System.out.println("========================================");
        System.out.println("=== After 24 Days - Final State ===");
        System.out.println("========================================");
        gardenAPI.getState();
        
        System.out.println("\n=== Simulation Complete ===");
        System.out.println("API Days (from API calls): " + gardenAPI.getDayCount());
        System.out.println("Headless Simulation Days: " + gardenAPI.getHeadlessDayCount());
        System.out.println("\nCheck log.txt for detailed event logs.");
        
        // Cleanup: Stop headless simulation and close API log file
        gardenAPI.stopHeadlessSimulation();
        System.out.println("✓ Headless simulation stopped");
        GardenSimulationAPI.closeApiLog();
    }
    
    /**
     * Simulates one hour passing (one day in simulation time).
     * In real testing, this would be 1 hour = 3600000 milliseconds.
     * For faster testing, using 1 second = 1 hour.
     */
    private static void sleepOneHour() {
        try {
            Thread.sleep(HOUR_IN_MILLIS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
    }
}

