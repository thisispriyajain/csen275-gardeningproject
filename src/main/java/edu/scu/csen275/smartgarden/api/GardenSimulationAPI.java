package edu.scu.csen275.smartgarden.api;

import edu.scu.csen275.smartgarden.controller.GardenController;
import edu.scu.csen275.smartgarden.model.*;
import edu.scu.csen275.smartgarden.simulation.SimulationEngine;
import edu.scu.csen275.smartgarden.simulation.WeatherSystem;
import edu.scu.csen275.smartgarden.system.HeatingSystem;
import edu.scu.csen275.smartgarden.system.HarmfulPest;
import edu.scu.csen275.smartgarden.util.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final Logger logger;
    
    // Track day count for API compatibility
    private int dayCount = 0;
    
    // API-specific log.txt file writer (per specification requirement)
    private static BufferedWriter apiLogWriter;
    private static final Object apiLogLock = new Object();
    private static final DateTimeFormatter LOG_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Initialize API log.txt file on first use
    static {
        try {
            Path logFile = Paths.get("log.txt");
            boolean fileExists = Files.exists(logFile);
            apiLogWriter = Files.newBufferedWriter(logFile, 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            // Write header if file is new (empty or doesn't exist)
            if (!fileExists || Files.size(logFile) == 0) {
                apiLogWriter.write("=====================================");
                apiLogWriter.newLine();
                apiLogWriter.write("Smart Garden Simulation API Log");
                apiLogWriter.newLine();
                apiLogWriter.write("Started: " + LocalDateTime.now().format(LOG_TIME_FORMATTER));
                apiLogWriter.newLine();
                apiLogWriter.write("=====================================");
                apiLogWriter.newLine();
                apiLogWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Warning: Failed to initialize API log.txt: " + e.getMessage());
            apiLogWriter = null; // Ensure it's null if initialization fails
        }
    }
    
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
        
        logger.info("API", "GardenSimulationAPI initialized");
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
        logger.info("API", "Initializing garden - Day 0 begins");
        writeToApiLog("Initializing garden - Day 0 begins");
        dayCount = 0;
        
        // Create predefined plants for simulation
        // Using smartGarden's PlantType enum
        addPlant(PlantType.STRAWBERRY, new Position(1, 1));
        addPlant(PlantType.CARROT, new Position(2, 2));
        addPlant(PlantType.TOMATO, new Position(3, 3));
        addPlant(PlantType.SUNFLOWER, new Position(4, 4));
        
        logger.info("API", "Garden initialized with " + garden.getTotalPlants() + " plants.");
        writeToApiLog("Garden initialized with " + garden.getTotalPlants() + " plants.");
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
        writeToApiLog("Rainfall event: " + amount + " units");
        
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
                writeToApiLog("Rain added water to " + plant.getPlantType() + ". Current water level: " + plant.getWaterLevel());
            }
        }
        
        dayCount++;
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
        writeToApiLog("Temperature changed to " + temp + "°F");
        
        HeatingSystem heatingSystem = engine.getHeatingSystem();
        heatingSystem.setAmbientTemperature(tempCelsiusInt);
        
        // Determine weather condition based on temperature (using Fahrenheit thresholds)
        WeatherSystem.Weather weatherCondition;
        String weatherString;
        if (temp > 100) { // > 100°F = very hot
            weatherCondition = WeatherSystem.Weather.SUNNY;
            weatherString = "SUNNY";
        } else if (temp < 45) { // < 45°F = cold
            weatherCondition = WeatherSystem.Weather.SNOWY;
            weatherString = "SNOWY";
        } else {
            weatherCondition = WeatherSystem.Weather.CLOUDY;
            weatherString = "CLOUDY";
        }
        
        // Set weather to match temperature
        WeatherSystem weatherSystem = engine.getWeatherSystem();
        weatherSystem.setWeather(weatherCondition);
        
        // Apply temperature and weather effects to all plants (using Celsius internally)
        for (Plant plant : garden.getAllPlants()) {
            if (!plant.isDead()) {
                plant.applyTemperatureEffect(tempCelsiusInt);
                plant.applyWeatherEffect(weatherString);
                logger.info("API", plant.getPlantType() + " temperature adjusted to " + temp + "°F (" + tempCelsiusInt + "°C)");
            }
        }
        
        dayCount++;
    }
    
    /**
     * Triggers a parasite (pest) infestation based on plant vulnerabilities.
     * This uses smartGarden's PestControlSystem.
     * 
     * @param parasiteType Type of pest (e.g., "Red Mite", "Green Leaf Worm", etc.)
     */
    public void parasite(String parasiteType) {
        logger.info("API", "Parasite infestation: " + parasiteType);
        writeToApiLog("Parasite infestation: " + parasiteType);
        
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
                
                logger.info("API", plantType + " at " + plant.getPosition() + 
                           " attacked by " + parasiteType);
                writeToApiLog(plantType + " attacked by " + parasiteType);
            }
        }
        
        dayCount++;
    }
    
    /**
     * Logs details about the garden's current state, including plant health and status.
     * Uses smartGarden's Garden.getStatistics() method.
     */
    public void getState() {
        logger.info("API", "Garden State Report - Day " + dayCount);
        writeToApiLog("Garden State Report - Day " + dayCount);
        
        Map<String, Integer> stats = garden.getStatistics();
        int alive = stats.getOrDefault("livingPlants", 0);
        int dead = stats.getOrDefault("deadPlants", 0);
        
        logger.info("API", "Alive: " + alive + ", Dead: " + dead);
        writeToApiLog("Alive: " + alive + ", Dead: " + dead);
        logger.info("API", "Total Plants: " + stats.getOrDefault("totalPlants", 0));
        logger.info("API", "Zones: " + stats.getOrDefault("zones", 0));
        
        // Log individual plant status
        for (Plant plant : garden.getAllPlants()) {
            String status = plant.isDead() ? "DEAD" : "ALIVE";
            String plantStatus = "  - " + plant.getPlantType() + " at " + plant.getPosition() + 
                       ": " + status + " (Health: " + plant.getHealthLevel() + "%, Water: " + 
                       plant.getWaterLevel() + "%)";
            logger.info("API", plantStatus);
            writeToApiLog(plantStatus);
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
     * Gets the current day count.
     */
    public int getDayCount() {
        return dayCount;
    }
    
    /**
     * Helper method to write to API-specific log.txt file (per specification requirement).
     * This ensures all API events are logged to a single log.txt file.
     */
    private void writeToApiLog(String message) {
        synchronized (apiLogLock) {
            try {
                if (apiLogWriter != null) {
                    String logEntry = "[" + LocalDateTime.now().format(LOG_TIME_FORMATTER) + "] " + message;
                    apiLogWriter.write(logEntry);
                    apiLogWriter.newLine();
                    apiLogWriter.flush();
                }
            } catch (IOException e) {
                // Silently fail - don't disrupt API execution if log.txt write fails
                System.err.println("Warning: Failed to write to log.txt: " + e.getMessage());
            }
        }
    }
    
    /**
     * Closes the API log file writer. Should be called when done using the API.
     */
    public static void closeApiLog() {
        synchronized (apiLogLock) {
            try {
                if (apiLogWriter != null) {
                    apiLogWriter.close();
                    apiLogWriter = null;
                }
            } catch (IOException e) {
                System.err.println("Error closing API log.txt: " + e.getMessage());
            }
        }
    }
}

