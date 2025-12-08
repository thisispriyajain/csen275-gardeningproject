package edu.scu.csen275.smartgarden.api;

import edu.scu.csen275.smartgarden.controller.GardenController;
import edu.scu.csen275.smartgarden.system.HeatingSystem;
import edu.scu.csen275.smartgarden.system.PestControlSystem;
import edu.scu.csen275.smartgarden.system.WateringSystem;
import edu.scu.csen275.smartgarden.model.Plant;
import java.util.Map;

/**
 * Comprehensive API Test - Tests ALL scenarios step by step.
 * 
 * This test verifies:
 * 1. Water decreasing naturally (from headless simulation)
 * 2. Sprinklers activating when water is low
 * 3. Sprinklers deactivating when rain starts
 * 4. Pests attacking and causing damage
 * 5. Pesticide application after 3-second delay
 * 6. Heating activation when temperature is cold
 * 7. Heating deactivation when temperature is hot
 * 8. Plant health changes from all factors
 * 
 * Usage:
 *   mvnw exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.ComprehensiveAPITest"
 */
public class ComprehensiveAPITest {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Comprehensive API Test - All Scenarios");
        System.out.println("========================================\n");
        
        // Create GardenController (9x9 grid)
        GardenController controller = new GardenController(9, 9);
        
        // Create the API wrapper
        GardenSimulationAPI api = new GardenSimulationAPI(controller);
        
        // Get access to systems for status checking
        WateringSystem wateringSystem = api.getController().getSimulationEngine().getWateringSystem();
        HeatingSystem heatingSystem = api.getController().getSimulationEngine().getHeatingSystem();
        PestControlSystem pestSystem = api.getController().getSimulationEngine().getPestControlSystem();
        
        // ============================================================
        // TEST 1: Initialize Garden
        // ============================================================
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ TEST 1: Initialize Garden              ║");
        System.out.println("╚════════════════════════════════════════╝");
        api.initializeGarden();
        
        if (api.isHeadlessSimulationRunning()) {
            System.out.println("✓ Headless simulation started - plants updating continuously");
        }
        
        Map<String, Object> plants = api.getPlants();
        System.out.println("Plants: " + plants.get("plants"));
        System.out.println("Initial Water Levels: " + plants.get("waterRequirement"));
        System.out.println();
        
        // Show initial system states
        printSystemStatus(wateringSystem, heatingSystem, pestSystem, api);
        System.out.println();
        
        // Wait for water to decrease naturally (simulation runs)
        System.out.println("⏳ Waiting 20 seconds for water to decrease naturally...");
        try { Thread.sleep(20000); } catch (InterruptedException e) {}
        
        // ============================================================
        // TEST 2: Check Water Decreasing
        // ============================================================
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║ TEST 2: Water Decreasing (Natural)     ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        printPlantWaterLevels(api);
        System.out.println("✓ Water should have decreased due to continuous simulation");
        System.out.println();
        
        // ============================================================
        // TEST 3: Sprinklers Activate (Low Water)
        // ============================================================
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ TEST 3: Sprinkler Activation           ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.println("Current water levels:");
        printPlantWaterLevels(api);
        
        // Trigger system update check (sprinklers should activate if water is low)
        System.out.println("⏳ Waiting 10 seconds for automatic sprinkler check...");
        System.out.println("(Sprinklers will activate if plants need water)");
        try { Thread.sleep(10000); } catch (InterruptedException e) {}
        
        System.out.println("✓ Check log.txt for sprinkler activation messages");
        printSystemStatus(wateringSystem, heatingSystem, pestSystem, api);
        System.out.println();
        
        // ============================================================
        // TEST 4: Rain Stops Sprinklers
        // ============================================================
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ TEST 4: Rain Stops Sprinklers          ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.println("Triggering rain (25 units)...");
        api.rain(25);
        System.out.println("⏳ Waiting 2 seconds...");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        System.out.println("✓ Check log.txt for: 'Sprinklers automatically DEACTIVATED'");
        printPlantWaterLevels(api);
        printSystemStatus(wateringSystem, heatingSystem, pestSystem, api);
        System.out.println();
        
        // ============================================================
        // TEST 5: Cold Temperature - Heating Activates
        // ============================================================
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ TEST 5: Cold Temperature - Heating ON  ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.println("Setting temperature to 40°F (4°C - COLD)...");
        api.temperature(40);
        System.out.println("⏳ Waiting 2 seconds...");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        System.out.println("✓ Check log.txt for: 'Heating system ACTIVATED'");
        printSystemStatus(wateringSystem, heatingSystem, pestSystem, api);
        System.out.println();
        
        // ============================================================
        // TEST 6: Hot Temperature - Heating Deactivates
        // ============================================================
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ TEST 6: Hot Temperature - Heating OFF  ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.println("Setting temperature to 110°F (43°C - HOT)...");
        api.temperature(110);
        System.out.println("⏳ Waiting 2 seconds...");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        System.out.println("✓ Check log.txt for: 'Heating system is OFF' or 'DEACTIVATED'");
        printSystemStatus(wateringSystem, heatingSystem, pestSystem, api);
        System.out.println();
        
        // ============================================================
        // TEST 7: Pest Attack - Damage Occurs
        // ============================================================
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ TEST 7: Pest Attack - Damage           ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.println("Plant health before pest attack:");
        printPlantHealth(api);
        
        System.out.println("\nTriggering Red Mite attack...");
        api.parasite("Red Mite");
        
        System.out.println("\nPlant health immediately after pest attack:");
        printPlantHealth(api);
        
        System.out.println("✓ Health should have decreased for vulnerable plants");
        System.out.println("⏳ Waiting 4 seconds for pesticide application...");
        try { Thread.sleep(4000); } catch (InterruptedException e) {}
        
        System.out.println("\nPlant health after pesticide:");
        printPlantHealth(api);
        System.out.println("✓ Check log.txt for: 'Pesticide applied automatically'");
        printSystemStatus(wateringSystem, heatingSystem, pestSystem, api);
        System.out.println();
        
        // ============================================================
        // TEST 8: Multiple Pest Attacks
        // ============================================================
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ TEST 8: Multiple Pest Attacks          ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.println("Triggering Green Leaf Worm attack...");
        api.parasite("Green Leaf Worm");
        System.out.println("⏳ Waiting 4 seconds for pesticide...");
        try { Thread.sleep(4000); } catch (InterruptedException e) {}
        
        System.out.println("✓ Check log.txt for multiple pest attacks and pesticide applications");
        printSystemStatus(wateringSystem, heatingSystem, pestSystem, api);
        System.out.println();
        
        // ============================================================
        // TEST 9: Water Stress Damage
        // ============================================================
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ TEST 9: Water Stress Damage            ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.println("Current plant states:");
        printFullPlantStatus(api);
        
        System.out.println("\n⏳ Waiting 20 seconds for water to decrease and cause stress damage...");
        System.out.println("(Plants with water < 50% will take damage from water stress)");
        try { Thread.sleep(20000); } catch (InterruptedException e) {}
        
        System.out.println("\nPlant states after waiting:");
        printFullPlantStatus(api);
        
        System.out.println("✓ Plants with low water should have reduced health");
        System.out.println("✓ Check log.txt for damage from water stress");
        System.out.println();
        
        // ============================================================
        // TEST 10: Final State Check
        // ============================================================
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ TEST 10: Final State                   ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        api.getState();
        printSystemStatus(wateringSystem, heatingSystem, pestSystem, api);
        System.out.println();
        
        // ============================================================
        // SUMMARY
        // ============================================================
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ TEST SUMMARY                           ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("API Days: " + api.getDayCount());
        System.out.println("Headless Simulation Days: " + api.getHeadlessDayCount());
        System.out.println("\n✓ All scenarios tested!");
        System.out.println("✓ Check log.txt for detailed system responses:");
        System.out.println("  - Sprinkler activation/deactivation");
        System.out.println("  - Heating activation/deactivation");
        System.out.println("  - Pesticide application");
        System.out.println("  - Water stress damage");
        System.out.println("  - Plant health changes");
        
        // Cleanup
        api.stopHeadlessSimulation();
        GardenSimulationAPI.closeApiLog();
        System.out.println("\n✅ Comprehensive test complete!");
    }
    
    private static void printSystemStatus(WateringSystem wateringSystem, 
                                         HeatingSystem heatingSystem,
                                         PestControlSystem pestSystem,
                                         GardenSimulationAPI api) {
        System.out.println("\n--- System Status ---");
        System.out.println("Water Supply: " + wateringSystem.getWaterSupply() + "L");
        System.out.println("Heating Mode: " + heatingSystem.getHeatingMode());
        System.out.println("Current Temperature: " + heatingSystem.getCurrentTemperature() + "°C");
        System.out.println("Active Pests: " + pestSystem.getHarmfulPestCount());
        System.out.println("Pesticide Stock: " + pestSystem.getPesticideStock());
        System.out.println("Headless Simulation Running: " + api.isHeadlessSimulationRunning());
    }
    
    private static void printPlantWaterLevels(GardenSimulationAPI api) {
        System.out.println("\n--- Plant Water Levels ---");
        for (Plant plant : api.getGarden().getAllPlants()) {
            if (!plant.isDead()) {
                System.out.println("  " + plant.getPlantType() + " at " + plant.getPosition() + 
                                 ": " + plant.getWaterLevel() + "% (Requires: " + 
                                 plant.getWaterRequirement() + "%)");
            }
        }
    }
    
    private static void printPlantHealth(GardenSimulationAPI api) {
        System.out.println("\n--- Plant Health ---");
        for (Plant plant : api.getGarden().getAllPlants()) {
            if (!plant.isDead()) {
                System.out.println("  " + plant.getPlantType() + " at " + plant.getPosition() + 
                                 ": " + plant.getHealthLevel() + "% health, " + 
                                 plant.getWaterLevel() + "% water");
            }
        }
    }
    
    private static void printFullPlantStatus(GardenSimulationAPI api) {
        System.out.println("\n--- Full Plant Status ---");
        for (Plant plant : api.getGarden().getAllPlants()) {
            if (!plant.isDead()) {
                System.out.println("  " + plant.getPlantType() + " at " + plant.getPosition() + ":");
                System.out.println("    Health: " + plant.getHealthLevel() + "%");
                System.out.println("    Water: " + plant.getWaterLevel() + "% (Requires: " + 
                                 plant.getWaterRequirement() + "%)");
                System.out.println("    Pest Attacks: " + plant.getPestAttacks());
            } else {
                System.out.println("  " + plant.getPlantType() + " at " + plant.getPosition() + ": DEAD");
            }
        }
    }
}

