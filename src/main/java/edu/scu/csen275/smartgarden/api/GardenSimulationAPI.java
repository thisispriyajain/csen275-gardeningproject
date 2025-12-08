package edu.scu.csen275.smartgarden.api;

import edu.scu.csen275.smartgarden.controller.GardenController;
import edu.scu.csen275.smartgarden.model.*;
import edu.scu.csen275.smartgarden.simulation.SimulationEngine;
import edu.scu.csen275.smartgarden.simulation.HeadlessSimulationEngine;
import edu.scu.csen275.smartgarden.simulation.WeatherSystem;
import edu.scu.csen275.smartgarden.system.HeatingSystem;
import edu.scu.csen275.smartgarden.system.HarmfulPest;
import edu.scu.csen275.smartgarden.system.PestControlSystem;
import edu.scu.csen275.smartgarden.system.WateringSystem;
import edu.scu.csen275.smartgarden.util.Logger;
import edu.scu.csen275.smartgarden.util.EventBus;
import edu.scu.csen275.smartgarden.events.*;

import java.nio.file.Paths;
import java.util.*;

/**
 * GardenSimulationAPI provides an interface for automated testing and monitoring 
 * of the smart garden simulation system.
 * It exposes methods to initialize the garden, retrieve plant information, 
 * and simulate environmental conditions.
 * 
 * This API wraps smartGarden's existing systems (GardenController, WateringSystem,
 * HeatingSystem, PestControlSystem) to provide a simple programmatic interface
 * similar to Group9's API, but integrated with smartGarden's architecture.
 */
public class GardenSimulationAPI {
    private final GardenController controller;
    private final Garden garden;
    private final SimulationEngine engine;
    private final HeadlessSimulationEngine headlessEngine;
    private final Logger logger;
    
    // Track day count for API compatibility (from API calls)
    private int dayCount = 0;
    
    
    // Pest vulnerabilities mapping (similar to Group9 API)
    private static final Map<String, List<String>> pestVulnerabilities = new HashMap<>();
    
    static {
        // Initialize pest vulnerabilities for different plant types
        // This maps plant types to pests that can attack them
        pestVulnerabilities.put("Strawberry", Arrays.asList("Red Mite", "Green Leaf Worm"));
        pestVulnerabilities.put("Grapevine", Arrays.asList("Black Beetle", "Red Mite"));
        pestVulnerabilities.put("Apple Sapling", Arrays.asList("Brown Caterpillar", "Green Leaf Worm"));
        pestVulnerabilities.put("Carrot", Arrays.asList("Red Mite", "Brown Caterpillar"));
        pestVulnerabilities.put("Tomato", Arrays.asList("Black Beetle", "Red Mite"));
        pestVulnerabilities.put("Onion", Arrays.asList("Green Leaf Worm"));
        pestVulnerabilities.put("Sunflower", Arrays.asList("Red Mite", "Brown Caterpillar"));
        pestVulnerabilities.put("Tulip", Arrays.asList("Green Leaf Worm"));
        pestVulnerabilities.put("Rose", Arrays.asList("Black Beetle", "Red Mite"));
    }
    
    /**
     * Creates a new GardenSimulationAPI with a GardenController.
     * The API will use this controller to interact with the garden systems.
     * 
     * @param controller The GardenController instance to use
     */
    public GardenSimulationAPI(GardenController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("GardenController cannot be null");
        }
        this.controller = controller;
        this.garden = controller.getGarden();
        this.engine = controller.getSimulationEngine();
        this.logger = Logger.getInstance();
        
        // Create headless engine that shares the same systems as SimulationEngine
        // This allows both UI and API to work with the same garden state
        this.headlessEngine = new HeadlessSimulationEngine(
            garden,
            engine.getWateringSystem(),
            engine.getHeatingSystem(),
            engine.getPestControlSystem(),
            engine.getWeatherSystem()
        );
        
        logger.info("API", "GardenSimulationAPI initialized with headless simulation engine");
    }
    
    /**
     * Initializes the garden with a predefined set of plants.
     * This method marks the beginning of the simulation clock.
     * 
     * Creates plants at fixed positions:
     * - Strawberry at (1, 1)
     * - Carrot at (2, 2)
     * - Tomato at (3, 3)
     * - Sunflower at (4, 4)
     */
    public void initializeGarden() {
        // Enable dual logging - all logs will go to both logs/garden_*.log AND log.txt
        Logger.enableApiLogging(Paths.get("log.txt"));
        
        logger.info("API", "Initializing garden - Day 0 begins");
        dayCount = 0;
        
        // Create predefined plants for simulation
        // Using smartGarden's PlantType enum
        addPlant(PlantType.STRAWBERRY, new Position(1, 1));
        addPlant(PlantType.CARROT, new Position(2, 2));
        addPlant(PlantType.TOMATO, new Position(3, 3));
        addPlant(PlantType.SUNFLOWER, new Position(4, 4));
        
        logger.info("API", "Garden initialized with " + garden.getTotalPlants() + " plants.");
        
        // Enable API mode - disable automatic pest spawning and weather changes
        // Pests and weather will only be triggered via API calls
        // All other automatic systems (pesticide, heating, sprinklers, water decrease) continue to work
        engine.getPestControlSystem().setApiModeEnabled(true);
        engine.getWeatherSystem().setApiModeEnabled(true);
        logger.info("API", "API mode enabled - automatic pest spawning and weather changes disabled");
        logger.info("API", "Pesticide, heating, sprinklers, and water monitoring remain automatic");
        
        // Start headless simulation for continuous plant updates
        // This enables true 24-hour survival testing
        startHeadlessSimulation();
        
        // Publish event for UI
        EventBus.publish("InitializeGardenEvent", new InitializeGardenEvent());
    }
    
    /**
     * Helper method to add a plant to the garden.
     */
    private void addPlant(PlantType plantType, Position position) {
        if (controller.plantSeed(plantType, position)) {
            logger.info("API", "Added plant: " + plantType.getDisplayName() + " at " + position);
        } else {
            logger.warning("API", "Failed to add plant: " + plantType.getDisplayName() + " at " + position);
        }
    }
    
    /**
     * Retrieves plant information including names, water requirements, and pest vulnerabilities.
     * 
     * @return Map containing:
     *         - "plants": List of plant names
     *         - "waterRequirement": List of current water levels
     *         - "parasites": List of lists of pest types that can attack each plant
     */
    public Map<String, Object> getPlants() {
        Map<String, Object> plantInfo = new HashMap<>();
        List<String> plantNames = new ArrayList<>();
        List<Integer> waterLevels = new ArrayList<>();
        List<List<String>> parasiteList = new ArrayList<>();
        
        for (Plant plant : garden.getAllPlants()) {
            String plantType = plant.getPlantType();
            plantNames.add(plantType);
            waterLevels.add(plant.getWaterLevel());
            parasiteList.add(pestVulnerabilities.getOrDefault(plantType, new ArrayList<>()));
        }
        
        plantInfo.put("plants", plantNames);
        plantInfo.put("waterRequirement", waterLevels);
        plantInfo.put("parasites", parasiteList);
        
        logger.info("API", "Retrieved plant information for " + plantNames.size() + " plants.");
        return plantInfo;
    }
    
    /**
     * Simulates rainfall in the garden by adjusting water levels for all plants.
     * This uses smartGarden's WateringSystem and WeatherSystem.
     * 
     * @param amount Amount of water units to add
     */
    public void rain(int amount) {
        logger.info("API", "Rainfall event: " + amount + " units");
        
        // Set weather to rainy
        WeatherSystem weatherSystem = engine.getWeatherSystem();
        weatherSystem.setWeather(WeatherSystem.Weather.RAINY);
        
        // Add water to all plants and apply weather effects
        for (Plant plant : garden.getAllPlants()) {
            if (!plant.isDead()) {
                plant.water(amount);
                plant.applyWeatherEffect("RAINY");
                logger.info("API", "Rain added water to " + plant.getPlantType() + 
                           " at " + plant.getPosition() + ". Current water level: " + plant.getWaterLevel());
            }
        }
        
        // Trigger system updates (sprinklers will stop due to rain listener)
        triggerSystemUpdates();
        
        dayCount++;
        
        // Publish event for UI
        EventBus.publish("RainEvent", new RainEvent(amount));
    }
    
    /**
     * Simulates temperature changes in the garden.
     * This uses smartGarden's HeatingSystem.
     * 
     * @param temp Temperature in Fahrenheit (specification requirement: 40-120 F)
     */
    public void temperature(int temp) {
        // Convert Fahrenheit to Celsius (spec expects Fahrenheit input)
        double tempCelsius = (temp - 32) * 5.0 / 9.0;
        int tempCelsiusInt = (int) Math.round(tempCelsius);
        
        logger.info("API", "Temperature changed to " + temp + "°F (" + tempCelsiusInt + "°C)");
        
        HeatingSystem heatingSystem = engine.getHeatingSystem();
        
        // Set ambient temperature - this updates all zones
        heatingSystem.setAmbientTemperature(tempCelsiusInt);
        
        // Apply temperature effects to all plants (using Celsius internally)
        // NOTE: Weather is NOT changed by temperature - weather only changes via api.rain()
        // In API mode, weather changes must be explicit API calls, not automatic based on temperature
        for (Plant plant : garden.getAllPlants()) {
            if (!plant.isDead()) {
                plant.applyTemperatureEffect(tempCelsiusInt);
                // Don't apply weather effects - weather only changes via explicit api.rain() call
                logger.info("API", plant.getPlantType() + " temperature adjusted to " + temp + "°F (" + tempCelsiusInt + "°C)");
            }
        }
        
        // Trigger system updates (heating will activate if temp is low)
        triggerSystemUpdates();
        
        dayCount++;
        
        // Publish event for UI
        EventBus.publish("TemperatureEvent", new TemperatureEvent(temp));
    }
    
    /**
     * Triggers a parasite (pest) infestation based on plant vulnerabilities.
     * This uses smartGarden's PestControlSystem.
     * 
     * @param parasiteType Type of pest (e.g., "Red Mite", "Green Leaf Worm", etc.)
     */
    public void parasite(String parasiteType) {
        logger.info("API", "Parasite infestation: " + parasiteType);
        
        // Find all plants that are vulnerable to this pest type
        for (Plant plant : garden.getAllPlants()) {
            if (plant.isDead()) {
                continue;
            }
            
            String plantType = plant.getPlantType();
            List<String> vulnerabilities = pestVulnerabilities.getOrDefault(plantType, new ArrayList<>());
            
            if (vulnerabilities.contains(parasiteType)) {
                // Create a pest at the plant's position and attack it
                HarmfulPest pest = new HarmfulPest(parasiteType, plant.getPosition());
                pest.causeDamage(plant);
                
                // Register pest with PestControlSystem so it gets automatically treated
                PestControlSystem pestSystem = engine.getPestControlSystem();
                pestSystem.registerPest(pest);
                
                logger.info("API", plantType + " at " + plant.getPosition() + 
                           " attacked by " + parasiteType);
                
                // Publish event for UI (one per plant attacked)
                EventBus.publish("ParasiteEvent", new ParasiteEvent(parasiteType, plant.getPosition()));
            }
        }
        
        // Trigger system updates (pesticide will be applied automatically)
        triggerSystemUpdates();
        
        dayCount++;
    }
    
    /**
     * Logs details about the garden's current state, including plant health and status.
     * Uses smartGarden's Garden.getStatistics() method.
     */
    public void getState() {
        logger.info("API", "Garden State Report - Day " + dayCount);
        
        Map<String, Integer> stats = garden.getStatistics();
        int alive = stats.getOrDefault("livingPlants", 0);
        int dead = stats.getOrDefault("deadPlants", 0);
        
        logger.info("API", "Alive: " + alive + ", Dead: " + dead);
        logger.info("API", "Total Plants: " + stats.getOrDefault("totalPlants", 0));
        logger.info("API", "Zones: " + stats.getOrDefault("zones", 0));
        
        // Log individual plant status
        for (Plant plant : garden.getAllPlants()) {
            String status = plant.isDead() ? "DEAD" : "ALIVE";
            String plantStatus = "  - " + plant.getPlantType() + " at " + plant.getPosition() + 
                       ": " + status + " (Health: " + plant.getHealthLevel() + "%, Water: " + 
                       plant.getWaterLevel() + "%)";
            logger.info("API", plantStatus);
        }
    }
    
    /**
     * Gets the GardenController instance (for advanced use).
     */
    public GardenController getController() {
        return controller;
    }
    
    /**
     * Gets the Garden instance (for advanced use).
     */
    public Garden getGarden() {
        return garden;
    }
    
    /**
     * Gets the current day count (from API calls).
     */
    public int getDayCount() {
        return dayCount;
    }
    
    /**
     * Starts the headless simulation loop for continuous plant updates.
     * This enables true 24-hour survival testing where plants update automatically,
     * water decreases over time, and systems respond continuously.
     * 
     * Called automatically by initializeGarden(), but can be called manually if needed.
     */
    public void startHeadlessSimulation() {
        if (headlessEngine.isRunning()) {
            logger.warning("API", "Headless simulation already running");
            return;
        }
        
        if (garden.getLivingPlants().isEmpty()) {
            logger.warning("API", "Cannot start headless simulation - no plants in garden");
            return;
        }
        
        headlessEngine.start();
        logger.info("API", "Headless simulation loop started - plants will update continuously, water will decrease automatically");
    }
    
    /**
     * Stops the headless simulation loop.
     * Useful for cleanup or when switching to UI mode.
     */
    public void stopHeadlessSimulation() {
        if (!headlessEngine.isRunning()) {
            return;
        }
        
        headlessEngine.stop();
        logger.info("API", "Headless simulation loop stopped");
    }
    
    /**
     * Checks if headless simulation is currently running.
     */
    public boolean isHeadlessSimulationRunning() {
        return headlessEngine.isRunning();
    }
    
    /**
     * Gets the headless simulation engine's day counter.
     * This tracks days from continuous simulation (different from API dayCount).
     */
    public int getHeadlessDayCount() {
        return headlessEngine.getDayCounter();
    }
    
    /**
     * Triggers all system updates to respond to API state changes.
     * This ensures automatic systems (watering, heating, pest control) work after API calls.
     * Systems are idempotent and safe to call multiple times.
     * All system responses are logged automatically by the systems themselves.
     */
    private void triggerSystemUpdates() {
        HeatingSystem heatingSystem = engine.getHeatingSystem();
        WateringSystem wateringSystem = engine.getWateringSystem();
        PestControlSystem pestSystem = engine.getPestControlSystem();
        
        // Systems automatically log all their actions - just call update()
        heatingSystem.update();
        wateringSystem.checkAndWater();
        pestSystem.update();
    }
    
    /**
     * Closes the API log file writer. Should be called when done using the API.
     */
    public static void closeApiLog() {
        Logger.disableApiLogging();
    }
}

