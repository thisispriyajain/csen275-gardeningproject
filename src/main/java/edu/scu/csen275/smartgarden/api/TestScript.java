package edu.scu.csen275.smartgarden.api;

import java.util.Map;
import java.util.Random;

/**
 * Test script matching the specification example pattern.
 * Simulates garden over multiple days with sleepOneHour() between days.
 * 
 * Pattern matches specification:
 * - Initialize garden
 * - Get plants
 * - Day 1: rain, sleepOneHour()
 * - Day 2: temperature and parasite, sleepOneHour()
 * - Continue for 24 days
 * - After 24 days: getState()
 */
public class TestScript {
    private static final long HOUR_IN_MILLIS = 1000; // 1 second = 1 hour for testing
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Garden Simulation API - Test Script");
        System.out.println("========================================\n");
        
        // Create API instance (no controller needed - matches specification)
        GardenSimulationAPI gardenAPI = new GardenSimulationAPI();
        
        // Beginning of the simulation
        System.out.println("// beginning of the simulation");
        gardenAPI.initializeGarden(); // this marks the beginning of the clock
        
        // Get initial plant details
        Map<String, Object> initialPlantDetails = gardenAPI.getPlants();
        System.out.println("Initial Plants: " + initialPlantDetails.get("plants"));
        System.out.println("Water Requirements: " + initialPlantDetails.get("waterRequirement"));
        System.out.println("Pest Vulnerabilities: " + initialPlantDetails.get("parasites"));
        System.out.println();
        
        Random random = new Random();
        String[] parasiteTypes = {"Red Mite", "Green Leaf Worm", "Black Beetle", "Brown Caterpillar"};
        
        // Simulate 24 days
        for (int day = 1; day <= 24; day++) {
            System.out.println("=== Beginning of Day " + day + " ===");
            
            // Randomly choose an event for this day
            int eventType = random.nextInt(3); // 0=rain, 1=temperature, 2=parasite
            
            switch (eventType) {
                case 0: // Rain
                    int rainAmount = 10 + random.nextInt(30); // 10-40 units
                    gardenAPI.rain(rainAmount);
                    System.out.println("Rainfall: " + rainAmount + " units");
                    break;
                    
                case 1: // Temperature
                    int temperature = 40 + random.nextInt(80); // 40-120°F
                    gardenAPI.temperature(temperature);
                    System.out.println("Temperature: " + temperature + "°F");
                    break;
                    
                case 2: // Parasite
                    String parasiteType = parasiteTypes[random.nextInt(parasiteTypes.length)];
                    gardenAPI.parasite(parasiteType);
                    System.out.println("Parasite: " + parasiteType);
                    break;
            }
            
            // End of day - sleep one hour (simulated)
            System.out.println("// end of day " + day);
            sleepOneHour();
            System.out.println();
        }
        
        // After 24 days - get final state
        System.out.println("//.... after 24 days");
        System.out.println("========================================");
        System.out.println("=== Final Garden State ===");
        System.out.println("========================================");
        gardenAPI.getState();
        
        System.out.println("\n=== Test Complete ===");
        System.out.println("Total Days Simulated: " + gardenAPI.getDayCount());
        System.out.println("\nCheck log.txt for detailed event logs.");
        System.out.println("Location: D:\\smartGarden\\log.txt");
        
        // Cleanup
        gardenAPI.stopHeadlessSimulation();
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

