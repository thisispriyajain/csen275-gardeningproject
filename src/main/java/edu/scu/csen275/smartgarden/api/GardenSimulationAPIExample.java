package edu.scu.csen275.smartgarden.api;

import edu.scu.csen275.smartgarden.controller.GardenController;
import java.util.Map;

/**
 * Example usage of GardenSimulationAPI.
 * This demonstrates how to use the API for automated testing and monitoring.
 */
public class GardenSimulationAPIExample {
    
    public static void main(String[] args) {
        // Create a GardenController (9x9 grid)
        GardenController controller = new GardenController(9, 9);
        
        // Create the API wrapper
        GardenSimulationAPI api = new GardenSimulationAPI(controller);
        
        // 1. Initialize the garden with predefined plants
        System.out.println("=== Initializing Garden ===");
        api.initializeGarden();
        
        // 2. Get plant information
        System.out.println("\n=== Getting Plant Information ===");
        Map<String, Object> plants = api.getPlants();
        System.out.println("Plants: " + plants.get("plants"));
        System.out.println("Water Levels: " + plants.get("waterRequirement"));
        System.out.println("Pest Vulnerabilities: " + plants.get("parasites"));
        
        // 3. Simulate rainfall
        System.out.println("\n=== Simulating Rainfall ===");
        api.rain(10);  // Add 10 units of water
        
        // 4. Change temperature
        System.out.println("\n=== Changing Temperature ===");
        api.temperature(77);  // Set to 77°F (approximately 25°C)
        
        // 5. Trigger a pest attack
        System.out.println("\n=== Triggering Pest Attack ===");
        api.parasite("Red Mite");
        
        // 6. Get garden state
        System.out.println("\n=== Getting Garden State ===");
        api.getState();
        
        System.out.println("\n=== Day Count: " + api.getDayCount() + " ===");
        
        // Cleanup: Close API log file
        GardenSimulationAPI.closeApiLog();
        System.out.println("\n✅ API execution complete! Check log.txt for detailed logs.");
    }
}

