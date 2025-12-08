package edu.scu.csen275.smartgarden.api;

import edu.scu.csen275.smartgarden.controller.GardenController;
import java.util.Map;

/**
 * Quick API Test - Calls all methods sequentially for immediate verification.
 * 
 * This script quickly tests all API methods to verify:
 * - Automatic pesticide application when pests attack
 * - Automatic sprinkler activation when water is low
 * - Automatic sprinkler deactivation when raining
 * - Automatic heating activation when temperature is cold
 * 
 * Usage:
 *   mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.QuickAPITest"
 */
public class QuickAPITest {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Quick API Test - All Methods");
        System.out.println("========================================\n");
        
        // Create GardenController (9x9 grid)
        GardenController controller = new GardenController(9, 9);
        
        // Create the API wrapper
        GardenSimulationAPI api = new GardenSimulationAPI(controller);
        
        // 1. Initialize garden
        System.out.println("1. Initializing Garden...");
        api.initializeGarden();
        System.out.println("   ✓ Garden initialized\n");
        
        // 2. Get initial plant information
        System.out.println("2. Getting Plant Information...");
        Map<String, Object> plants = api.getPlants();
        System.out.println("   Plants: " + plants.get("plants"));
        System.out.println("   Water Levels: " + plants.get("waterRequirement"));
        System.out.println("   Pest Vulnerabilities: " + plants.get("parasites"));
        System.out.println("   ✓ Plant info retrieved\n");
        
        // 3. Test RAIN - should stop sprinklers automatically
        System.out.println("3. Testing RAIN (25 units)...");
        System.out.println("   Expected: Sprinklers should stop automatically");
        api.rain(25);
        System.out.println("   ✓ Rain applied - check logs for sprinkler deactivation\n");
        
        // Wait a moment to see effects
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        // 4. Test TEMPERATURE (cold) - should activate heating
        System.out.println("4. Testing TEMPERATURE (40°F - COLD)...");
        System.out.println("   Expected: Heating system should activate automatically");
        api.temperature(40);
        System.out.println("   ✓ Temperature set to 40°F - check logs for heating activation\n");
        
        // Wait a moment
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        // 5. Test TEMPERATURE (hot) - should deactivate heating
        System.out.println("5. Testing TEMPERATURE (110°F - HOT)...");
        System.out.println("   Expected: Heating system should deactivate");
        api.temperature(110);
        System.out.println("   ✓ Temperature set to 110°F - check logs for heating deactivation\n");
        
        // Wait a moment
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        // 6. Test PARASITE - should trigger automatic pesticide
        System.out.println("6. Testing PARASITE (Red Mite)...");
        System.out.println("   Expected: Pesticide should be applied automatically after 3 seconds");
        api.parasite("Red Mite");
        System.out.println("   ✓ Parasite attack triggered - pesticide should apply automatically\n");
        
        // Wait for pesticide to apply (3 second delay + buffer)
        System.out.println("   Waiting 5 seconds for automatic pesticide application...");
        try { Thread.sleep(5000); } catch (InterruptedException e) {}
        System.out.println("   ✓ Check logs for pesticide application\n");
        
        // 7. Test RAIN again - verify sprinklers stay off
        System.out.println("7. Testing RAIN again (15 units)...");
        System.out.println("   Expected: Sprinklers should remain off (already raining)");
        api.rain(15);
        System.out.println("   ✓ Rain applied again\n");
        
        // Wait a moment
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        // 8. Test PARASITE on different pest type
        System.out.println("8. Testing PARASITE (Green Leaf Worm)...");
        System.out.println("   Expected: Pesticide should be applied automatically");
        api.parasite("Green Leaf Worm");
        System.out.println("   ✓ Parasite attack triggered\n");
        
        // Wait for pesticide
        System.out.println("   Waiting 5 seconds for automatic pesticide application...");
        try { Thread.sleep(5000); } catch (InterruptedException e) {}
        System.out.println("   ✓ Check logs for pesticide application\n");
        
        // 9. Get final state
        System.out.println("9. Getting Final Garden State...");
        api.getState();
        System.out.println("   ✓ Final state retrieved\n");
        
        // 10. Summary
        System.out.println("========================================");
        System.out.println("Test Summary");
        System.out.println("========================================");
        System.out.println("Total Days Simulated: " + api.getDayCount());
        System.out.println("\nCheck log.txt for detailed logs showing:");
        System.out.println("  ✓ Automatic sprinkler deactivation during rain");
        System.out.println("  ✓ Automatic heating activation/deactivation");
        System.out.println("  ✓ Automatic pesticide application for pests");
        System.out.println("  ✓ All system responses to API calls");
        
               // Cleanup: Stop headless simulation and close log
               api.stopHeadlessSimulation();
               GardenSimulationAPI.closeApiLog();
               System.out.println("\n✅ Quick test complete! Check log.txt for detailed event logs.");
    }
}

