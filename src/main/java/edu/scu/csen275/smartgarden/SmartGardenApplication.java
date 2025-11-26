package edu.scu.csen275.smartgarden;

import edu.scu.csen275.smartgarden.controller.GardenController;
import edu.scu.csen275.smartgarden.model.Garden;
import edu.scu.csen275.smartgarden.model.Position;
import edu.scu.csen275.smartgarden.simulation.SimulationEngine;
import edu.scu.csen275.smartgarden.simulation.WeatherSystem;
import edu.scu.csen275.smartgarden.ui.*;
import edu.scu.csen275.smartgarden.util.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/**
 * Main JavaFX application for the Smart Garden Simulation.
 * Modern UI with animations, gradients, and garden-themed design.
 */
public class SmartGardenApplication extends Application {
    private GardenController controller;
    private GardenGridPanel gardenPanel;
    private ModernToolbar toolbar;
    private InfoPanel infoPanel;
    private ListView<String> logListView;
    private Pane decorativePane;
    private AnimatedBackgroundPane animatedBackground;
    private ParticleSystem particleSystem;
    private PestEventBridge pestEventBridge;
    private Scene scene;
    
    // Track displayed pests to avoid duplicates
    private java.util.Set<String> displayedPestIds;
    private java.util.Map<Position, Integer> lastPestCounts;
    
    // Track previous weather to detect changes
    private WeatherSystem.Weather previousWeather = null;
    
    private static final int GRID_SIZE = 9;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            controller = new GardenController(GRID_SIZE, GRID_SIZE);
            
            // Initialize pest event bridge and connect to PestControlSystem
            pestEventBridge = new PestEventBridge();
            controller.getSimulationEngine().getPestControlSystem().setPestEventBridge(pestEventBridge);
            
            // Initialize pest tracking
            displayedPestIds = new java.util.HashSet<>();
            lastPestCounts = new java.util.HashMap<>();
            
            // Load CSS styles
            scene = createScene();
            scene.getStylesheets().add(
                getClass().getResource("/styles/garden-theme.css").toExternalForm()
            );
            
            // Setup stage
            primaryStage.setTitle("🌿 Smart Garden Simulation - CSEN 275");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1400);
            primaryStage.setMinHeight(900);
            primaryStage.setOnCloseRequest(e -> {
                controller.shutdown();
                Platform.exit();
            });
            
            // Setup pest event handlers
            setupPestEventHandlers();
            
            // ROTATION MODE: Rotate between sunny and rainy every 1 minute
            controller.getSimulationEngine().getWeatherSystem().enableSunnyRainyRotation();
            System.out.println("[SmartGardenApplication] ROTATION MODE: Weather rotating between SUNNY and RAINY every 1 minute");
            
            // Add initial test log entry to verify log display
            controller.getLogger().info("System", "Smart Garden Simulation started");
            controller.getLogger().info("System", "Rain test mode enabled - rain will occur every 1 minute");
            
            // Add initial test log entries to list view
            if (logListView != null) {
                logListView.getItems().clear();
                logListView.getItems().add("[00:00:00] System: Smart Garden Simulation started");
                logListView.getItems().add("[00:00:00] System: Rain test mode enabled - rain will occur every 1 minute");
            }
            
            // Start UI update timer
            startUIUpdateTimer();
            
            primaryStage.show();
            
            // Add decorative elements after scene is shown
            Platform.runLater(() -> {
                // Bind decorative pane to scene size
                Scene currentScene = primaryStage.getScene();
                if (currentScene != null && decorativePane != null) {
                    decorativePane.prefWidthProperty().bind(currentScene.widthProperty());
                    decorativePane.prefHeightProperty().bind(currentScene.heightProperty().subtract(200));
                    addDecorativeElements();
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getInstance().logException("Application", "Failed to start", e);
        }
    }
    
    /**
     * Creates the main scene with all UI components.
     */
    private Scene createScene() {
        // Create animated background with clouds and sunlight rays - fills entire scene
        animatedBackground = new AnimatedBackgroundPane();
        
        // Create decorative overlay pane
        decorativePane = new Pane();
        decorativePane.setPickOnBounds(false);
        decorativePane.setMouseTransparent(true);
        
        // Create particle system for sparkles and pollen
        particleSystem = new ParticleSystem();
        particleSystem.setPickOnBounds(false);
        particleSystem.setMouseTransparent(true);
        
        // Create UI components
        toolbar = new ModernToolbar();
        gardenPanel = new GardenGridPanel(controller);
        infoPanel = new InfoPanel();
        
        // Container for center (garden + decorations + particles)
        StackPane centerContainer = new StackPane();
        centerContainer.setStyle("-fx-background-color: transparent;");
        centerContainer.getChildren().addAll(
            gardenPanel,
            decorativePane,
            particleSystem
        );
        
        // Set animation container for garden panel (for watering animations)
        gardenPanel.setAnimationContainer(centerContainer);
        
        // Set coin float pane
        gardenPanel.setCoinFloatPane(decorativePane);
        
        // Setup toolbar actions
        setupToolbarActions();
        
        // Setup info panel actions with coin animations
        setupInfoPanelActions();
        
        // Create log panel
        VBox logPanel = createLogPanel();
        
        // Root BorderPane with transparent background
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: transparent;");
        
        // Make toolbar semi-transparent so clouds show through
        toolbar.setStyle("-fx-background-color: rgba(46, 125, 50, 0.85);");
        
        // Layout UI on top
        root.setTop(toolbar);
        root.setCenter(centerContainer);
        root.setRight(infoPanel);
        root.setBottom(logPanel);
        
        // Stack everything: animated background behind (fills entire scene), UI on top
        StackPane rootStack = new StackPane();
        rootStack.getChildren().addAll(animatedBackground, root);
        
        // Make background fill entire stack and be visible
        StackPane.setAlignment(animatedBackground, javafx.geometry.Pos.TOP_LEFT);
        animatedBackground.prefWidthProperty().bind(rootStack.widthProperty());
        animatedBackground.prefHeightProperty().bind(rootStack.heightProperty());
        animatedBackground.minWidthProperty().bind(rootStack.widthProperty());
        animatedBackground.minHeightProperty().bind(rootStack.heightProperty());
        animatedBackground.maxWidthProperty().bind(rootStack.widthProperty());
        animatedBackground.maxHeightProperty().bind(rootStack.heightProperty());
        
        // Ensure background is behind everything and visible
        animatedBackground.toBack();
        
        Scene scene = new Scene(rootStack, 1400, 900);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT); // Transparent scene fill so background shows
        
        return scene;
    }
    
    /**
     * Sets up toolbar button actions.
     */
    private void setupToolbarActions() {
        toolbar.getStartButton().setOnAction(e -> {
            try {
                controller.startSimulation();
                toolbar.getStartButton().setDisable(true);
                toolbar.getPauseButton().setDisable(false);
            } catch (IllegalStateException ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        
        toolbar.getPauseButton().setOnAction(e -> {
            if (toolbar.getPauseButton().getText().contains("Pause")) {
                controller.pauseSimulation();
                toolbar.getPauseButton().setText("▶️ Resume");
            } else {
                controller.resumeSimulation();
                toolbar.getPauseButton().setText("⏸ Pause");
            }
        });
        
        toolbar.getStopButton().setOnAction(e -> {
            controller.stopSimulation();
            toolbar.getStartButton().setDisable(false);
            toolbar.getPauseButton().setDisable(true);
            toolbar.getPauseButton().setText("⏸ Pause");
        });
        
        toolbar.getSpeedBox().setOnAction(e -> {
            String speed = toolbar.getSpeedBox().getValue().replace("x", "");
            controller.setSimulationSpeed(Integer.parseInt(speed));
        });
    }
    
    /**
     * Sets up info panel button actions.
     */
    private void setupInfoPanelActions() {
        infoPanel.getRefillWaterBtn().setOnAction(e -> {
            controller.refillWater();
            animateButtonClick(infoPanel.getRefillWaterBtn());
        });
        
        infoPanel.getRefillPesticideBtn().setOnAction(e -> {
            controller.refillPesticide();
            animateButtonClick(infoPanel.getRefillPesticideBtn());
        });
        
        infoPanel.getWaterAllBtn().setOnAction(e -> {
            // Trigger watering on all zones
            for (int i = 1; i <= 9; i++) {
                controller.manualWaterZone(i);
            }
            
            // Trigger combined watering animation on all tiles
            gardenPanel.animateAllTilesWatering();
            
            // Create sparkle burst at button location
            javafx.geometry.Bounds bounds = infoPanel.getWaterAllBtn().localToScene(
                infoPanel.getWaterAllBtn().getBoundsInLocal()
            );
            if (particleSystem != null && bounds != null) {
                javafx.geometry.Bounds sceneBounds = animatedBackground.localToScene(
                    animatedBackground.getBoundsInLocal()
                );
                if (sceneBounds != null) {
                    double x = bounds.getCenterX() - sceneBounds.getMinX();
                    double y = bounds.getCenterY() - sceneBounds.getMinY();
                    particleSystem.createSparkleBurst(x, y);
                }
            }
            
            animateButtonClick(infoPanel.getWaterAllBtn());
        });
    }
    
    /**
     * Creates the log panel.
     */
    private VBox createLogPanel() {
        VBox logPanel = new VBox(8);
        logPanel.setPadding(new Insets(10));
        logPanel.setPrefHeight(200); // Increased from maxHeight
        logPanel.setMinHeight(150);
        logPanel.setMaxHeight(250);
        logPanel.getStyleClass().add("log-panel");
        logPanel.setVisible(true); // Ensure visible
        logPanel.setManaged(true); // Ensure managed
        
        Label logTitle = new Label("📋 Recent Events");
        logTitle.getStyleClass().add("log-title");
        
        logListView = new ListView<>();
        logListView.setPrefHeight(150); // Increased height
        logListView.setMinHeight(100);
        logListView.getStyleClass().add("list-view");
        logListView.setVisible(true); // Ensure visible
        logListView.setManaged(true); // Ensure managed
        logListView.setEditable(false);
        logListView.setFocusTraversable(false);
        
        // Add initial test message to verify list view works
        logListView.getItems().add("Log panel initialized - waiting for events...");
        
        logPanel.getChildren().addAll(logTitle, logListView);
        return logPanel;
    }
    
    /**
     * Adds decorative animated elements.
     */
    private void addDecorativeElements() {
        // Add 3-5 colorful butterflies with sinusoidal flight
        int butterflyCount = 3 + (int)(Math.random() * 3); // 3-5 butterflies
        DecorativeElements.createButterflies(decorativePane, butterflyCount);
        
        // Add 2-3 bees with fast buzzing movement
        int beeCount = 2 + (int)(Math.random() * 2); // 2-3 bees
        DecorativeElements.createBees(decorativePane, beeCount);
        
        // Add 1-2 birds flying across
        int birdCount = 1 + (int)(Math.random() * 2); // 1-2 birds
        DecorativeElements.createBirds(decorativePane, birdCount);
        
        // Periodically add floating leaves
        javafx.animation.Timeline leafTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(
                javafx.util.Duration.seconds(5),
                e -> DecorativeElements.createLeaf(decorativePane)
            )
        );
        leafTimeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        leafTimeline.play();
    }
    
    /**
     * Animates button click.
     */
    private void animateButtonClick(javafx.scene.control.Button button) {
        javafx.animation.ScaleTransition scale = new javafx.animation.ScaleTransition(
            javafx.util.Duration.millis(100), button
        );
        scale.setToX(0.9);
        scale.setToY(0.9);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }
    
    /**
     * Sets up pest event handlers to connect PestControlSystem to UI animations.
     */
    private void setupPestEventHandlers() {
        pestEventBridge.setHandler(new PestEventBridge.PestAnimationHandler() {
            @Override
            public void onPestSpawned(Position position, String pestType, boolean isHarmful) {
                gardenPanel.onPestSpawned(position, pestType, isHarmful);
            }

            @Override
            public void onPestAttack(Position position, int damage) {
                gardenPanel.onPestAttack(position, damage);
            }

            @Override
            public void onPesticideApplied(Position position) {
                gardenPanel.onPesticideApplied(position);
            }

            @Override
            public void onPestRemoved(Position position, String pestType) {
                // Not explicitly handled in UI yet, but can be added
            }
        });
    }
    
    /**
     * Starts the UI update timer.
     */
    private void startUIUpdateTimer() {
        javafx.animation.Timeline uiTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.5), e -> updateUI())
        );
        uiTimeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        uiTimeline.play();
    }
    
    /**
     * Updates the entire UI.
     */
    private void updateUI() {
        if (controller == null) {
            System.err.println("[SmartGardenApplication] ERROR: Controller is null in updateUI()!");
            return;
        }
        
        try {
            SimulationEngine engine = controller.getSimulationEngine();
            if (engine == null) {
                System.err.println("[SmartGardenApplication] ERROR: SimulationEngine is null!");
                return;
            }
            Garden garden = controller.getGarden();
            if (garden == null) {
                System.err.println("[SmartGardenApplication] ERROR: Garden is null!");
                return;
            }
            
            // Update garden tiles
            gardenPanel.updateAllTiles();
            
            // Update toolbar status
            String state = engine.getState().toString();
            toolbar.updateStatus(state);
            
            // Update simulation info
            infoPanel.getTimeLabel().setText(
                "⏰ Day " + engine.getDayCounter() + " | " + engine.getFormattedTime()
            );
            infoPanel.getStatsLabel().setText(
                String.format("🌱 Plants: %d alive / %d total",
                    garden.getLivingPlants().size(), garden.getTotalPlants())
            );
            
            // Update weather display and rain animation
            WeatherSystem.Weather weather = engine.getWeatherSystem().getCurrentWeather();
            infoPanel.getWeatherDisplay().updateWeather(weather);
            
            // Update background brightness based on weather
            if (animatedBackground != null) {
                animatedBackground.setWeather(weather == WeatherSystem.Weather.SUNNY);
            }
            
            // Show/hide rain animation based on weather changes
            if (previousWeather != weather) {
                // Get center container - root is StackPane containing BorderPane
                Pane centerContainer = null;
                if (scene != null && scene.getRoot() != null) {
                    if (scene.getRoot() instanceof StackPane) {
                        StackPane rootStack = (StackPane) scene.getRoot();
                        // Find BorderPane in StackPane children
                        for (javafx.scene.Node child : rootStack.getChildren()) {
                            if (child instanceof BorderPane) {
                                BorderPane borderPane = (BorderPane) child;
                                javafx.scene.Node center = borderPane.getCenter();
                                if (center instanceof Pane) {
                                    centerContainer = (Pane) center;
                                    break;
                                }
                            }
                        }
                    } else if (scene.getRoot() instanceof BorderPane) {
                        BorderPane borderPane = (BorderPane) scene.getRoot();
                        javafx.scene.Node center = borderPane.getCenter();
                        if (center instanceof Pane) {
                            centerContainer = (Pane) center;
                        }
                    }
                }
                
                if (centerContainer != null) {
                    if (weather == WeatherSystem.Weather.RAINY) {
                        System.out.println("[SmartGardenApplication] Weather changed to RAINY - starting rain animation");
                        RainAnimationEngine.startRain(centerContainer);
                    } else if (previousWeather == WeatherSystem.Weather.RAINY) {
                        System.out.println("[SmartGardenApplication] Weather changed from RAINY to " + weather + " - stopping rain animation");
                        RainAnimationEngine.stopRain(centerContainer);
                    }
                } else {
                    System.err.println("[SmartGardenApplication] WARNING: Could not find center container for rain animation");
                }
                previousWeather = weather;
            }
            
            // Update resource bars with animation
            double waterProgress = Math.max(0, Math.min(1, 
                engine.getWateringSystem().getWaterSupply() / 10000.0));
            double tempProgress = Math.max(0, Math.min(1,
                engine.getHeatingSystem().getCurrentTemperature() / 40.0));
            double pesticideProgress = Math.max(0, Math.min(1,
                engine.getPestControlSystem().getPesticideStock() / 50.0));
            
            infoPanel.updateProgressBars(waterProgress, tempProgress, pesticideProgress);
            
            // Update logs and detect automatic watering
            List<String> recentLogs = new ArrayList<>();
            
            try {
                // Get logs - try both controller logger and singleton instance
                edu.scu.csen275.smartgarden.util.Logger logger = null;
                
                if (controller != null) {
                    logger = controller.getLogger();
                }
                
                // Fallback to singleton if controller logger is null
                if (logger == null) {
                    logger = edu.scu.csen275.smartgarden.util.Logger.getInstance();
                }
                
                if (logger != null) {
                    List<edu.scu.csen275.smartgarden.util.Logger.LogEntry> logEntries = 
                        logger.getRecentLogs(20);
                    
                    for (edu.scu.csen275.smartgarden.util.Logger.LogEntry entry : logEntries) {
                        try {
                            String logEntry = entry.toDisplayFormat();
                            if (logEntry != null && !logEntry.trim().isEmpty()) {
                                recentLogs.add(logEntry);
                                
                                // Detect automatic watering from logs
                                if (logEntry.contains("Auto-watered Zone")) {
                                    // Extract zone ID from log message
                                    try {
                                        int zoneId = Integer.parseInt(logEntry.substring(
                                            logEntry.indexOf("Zone ") + 5,
                                            logEntry.indexOf(" ", logEntry.indexOf("Zone ") + 5)
                                        ));
                                        System.out.println("[SmartGardenApplication] Detected automatic watering for Zone " + zoneId);
                                        gardenPanel.animateWatering(zoneId);
                                    } catch (Exception e) {
                                        // If parsing fails, just animate all tiles
                                        System.out.println("[SmartGardenApplication] Detected automatic watering - animating all tiles");
                                        gardenPanel.animateAllTilesWatering();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("[SmartGardenApplication] Error formatting log entry: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.err.println("[SmartGardenApplication] ERROR: Could not get logger instance!");
                }
            } catch (Exception e) {
                System.err.println("[SmartGardenApplication] ERROR getting logs: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Update list view (already on JavaFX thread from Timeline)
            try {
                if (logListView != null) {
                    // Clear and update on JavaFX thread (we're already on it, but be safe)
                    javafx.application.Platform.runLater(() -> {
                        try {
                            logListView.getItems().clear();
                            
                            if (!recentLogs.isEmpty()) {
                                logListView.getItems().addAll(recentLogs);
                                
                                // Auto-scroll to bottom of log
                                if (!logListView.getItems().isEmpty()) {
                                    logListView.scrollTo(logListView.getItems().size() - 1);
                                }
                            } else {
                                // Keep the initial message if no logs yet
                                if (logListView.getItems().isEmpty()) {
                                    logListView.getItems().add("No events yet...");
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("[SmartGardenApplication] ERROR updating list view items: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                } else {
                    System.err.println("[SmartGardenApplication] ERROR: logListView is null! Cannot display logs.");
                }
            } catch (Exception e) {
                System.err.println("[SmartGardenApplication] ERROR updating list view: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Monitor pest state and update UI animations
            updatePestAnimations(engine);
            
        } catch (Exception e) {
            // Log UI update errors instead of silently ignoring
            System.err.println("[SmartGardenApplication] ERROR in updateUI(): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Monitors pest state and updates UI animations.
     */
    private void updatePestAnimations(SimulationEngine engine) {
        try {
            var pestSystem = engine.getPestControlSystem();
            var garden = controller.getGarden();
            
            // Get all current pests from PestControlSystem
            var allPests = pestSystem.getPests();
            
            // Track current pest state per position
            java.util.Map<Position, java.util.List<edu.scu.csen275.smartgarden.system.Pest>> currentPests = new java.util.HashMap<>();
            
            // Group pests by position
            for (var pest : allPests) {
                if (pest.isAlive()) {
                    Position pos = pest.getPosition();
                    currentPests.computeIfAbsent(pos, k -> new java.util.ArrayList<>()).add(pest);
                }
            }
            
            // Check ALL plant positions (not just ones with pests) to detect removal
            for (var plant : garden.getLivingPlants()) {
                Position pos = plant.getPosition();
                var tile = gardenPanel.getTileAt(pos.row(), pos.column());
                
                if (tile != null && tile.isVisible()) {
                    // Get pest counts
                    var pestsAtPos = currentPests.getOrDefault(pos, new java.util.ArrayList<>());
                    int previousCount = lastPestCounts.getOrDefault(pos, 0);
                    int currentCount = pestsAtPos.size();
                    int uiPestCount = tile.getActivePestCount();
                    
                    // If PestControlSystem has pests but UI doesn't, spawn them
                    if (currentCount > 0 && uiPestCount == 0) {
                        // Spawn all pests at this position
                        for (var pest : pestsAtPos) {
                            String pestType = pest.getPestType();
                            
                            // Spawn in UI (all pests are harmful now)
                            gardenPanel.onPestSpawned(pos, pestType, true);
                        }
                    } else if (currentCount > uiPestCount && uiPestCount > 0) {
                        // More pests than UI shows - spawn additional ones
                        int toSpawn = currentCount - uiPestCount;
                        int spawned = 0;
                        for (var pest : pestsAtPos) {
                            if (spawned >= toSpawn) break;
                            String pestType = pest.getPestType();
                            // All pests are harmful now
                            gardenPanel.onPestSpawned(pos, pestType, true);
                            spawned++;
                        }
                    }
                    
                    // If PestControlSystem has no pests but UI does, remove them (pesticide applied)
                    if (currentCount == 0 && uiPestCount > 0) {
                        // Pests were removed - trigger pesticide animation
                        tile.applyPesticide();
                        // Clean up displayed pests for this position
                        displayedPestIds.removeIf(id -> id.contains("," + pos.row() + "," + pos.column()));
                    }
                    
                    // Update animations
                    tile.updatePestAnimations();
                    
                    // Update last count
                    lastPestCounts.put(pos, currentCount);
                }
            }
            
            // Clean up positions that no longer have plants
            lastPestCounts.keySet().removeIf(pos -> {
                var plant = garden.getPlant(pos);
                return plant == null || plant.isDead();
            });
            
            // Check for pest attacks and show damage
            for (var plant : garden.getLivingPlants()) {
                if (plant.getTotalPestAttacks() > 0) {
                    Position pos = plant.getPosition();
                    var tile = gardenPanel.getTileAt(pos.row(), pos.column());
                    
                    if (tile != null && tile.isVisible() && tile.hasPests()) {
                        // Show damage visual periodically when under attack
                        // (We'll show damage when pest attacks happen)
                    }
                }
            }
            
            // Check for pesticide application (when treatment happens)
            for (var zone : garden.getZones()) {
                if (pestSystem.getActivePestCountAtPosition(zone.getBoundaries().get(0)) == 0) {
                    // Check if there were pests before (simplified check)
                    // Better approach: track when treatment is applied
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            // Log error but continue
        }
    }
    
    /**
     * Shows an alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
