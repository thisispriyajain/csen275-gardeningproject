package edu.scu.csen275.smartgarden.ui;

import edu.scu.csen275.smartgarden.controller.GardenController;
import edu.scu.csen275.smartgarden.model.Plant;
import edu.scu.csen275.smartgarden.model.Position;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import java.util.List;

/**
 * Panel containing the interactive garden grid with animated tiles.
 */
public class GardenGridPanel extends VBox {
    private final GardenController controller;
    private final GridPane gardenGrid;
    private final AnimatedTile[][] tiles;
    private final GrassTile[][] grassTiles; // Grass tiles for empty cells
    private ComboBox<String> plantSelector;
    private Pane animationContainer; // Container for watering animations
    private Pane coinFloatPane; // Pane for coin float animations
    private static final int GRID_SIZE = 9;
    
    /**
     * Gets the animation container.
     */
    public Pane getAnimationContainer() {
        return animationContainer;
    }
    
    /**
     * Sets the pane for coin float animations.
     */
    public void setCoinFloatPane(Pane pane) {
        this.coinFloatPane = pane;
    }
    
    /**
     * Helper to find particle system and trigger sparkles.
     */
    private void findAndTriggerSparkles(javafx.scene.Node node, double x, double y) {
        if (node instanceof ParticleSystem) {
            ((ParticleSystem) node).createSparkleBurst(x, y);
        } else if (node instanceof javafx.scene.layout.Pane) {
            javafx.scene.layout.Pane pane = (javafx.scene.layout.Pane) node;
            for (javafx.scene.Node child : pane.getChildren()) {
                findAndTriggerSparkles(child, x, y);
            }
        }
    }
    
    /**
     * Creates coin float animation at position.
     */
    private void createCoinFloat(double x, double y, int value) {
        if (coinFloatPane != null) {
            javafx.geometry.Bounds paneBounds = coinFloatPane.localToScene(
                coinFloatPane.getBoundsInLocal()
            );
            if (paneBounds != null) {
                double localX = x - paneBounds.getMinX();
                double localY = y - paneBounds.getMinY();
                CoinFloatAnimation.createCoinFloat(coinFloatPane, localX, localY, value);
            }
        }
    }
    
    public GardenGridPanel(GardenController controller) {
        this.controller = controller;
        this.tiles = new AnimatedTile[GRID_SIZE][GRID_SIZE];
        this.grassTiles = new GrassTile[GRID_SIZE][GRID_SIZE];
        this.gardenGrid = new GridPane();
        
        setupPanel();
        setupPlantSelector();
        setupGrid();
    }
    
    /**
     * Sets the animation container for watering effects overlay.
     */
    public void setAnimationContainer(Pane container) {
        this.animationContainer = container;
        System.out.println("[GardenGridPanel] Animation container set: " + (container != null ? "SET" : "NULL"));
        
        // Also set animation container on all existing tiles
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (tiles[row][col] != null) {
                    tiles[row][col].setAnimationContainer(container);
                }
            }
        }
        System.out.println("[GardenGridPanel] Animation container propagated to " + (GRID_SIZE * GRID_SIZE) + " tiles");
    }
    
    /**
     * Sets up the main panel.
     */
    private void setupPanel() {
        this.setSpacing(15);
        this.setPadding(new Insets(15));
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("garden-panel");
        // Make panel background transparent so animated background shows through
        this.setStyle("-fx-background-color: transparent;");
    }
    
    /**
     * Sets up plant selector dropdown.
     */
    private void setupPlantSelector() {
        HBox selectorBox = new HBox(10);
        selectorBox.setAlignment(Pos.CENTER);
        selectorBox.getStyleClass().add("plant-selector");
        
        Label selectLabel = new Label("🌱 Select Plant:");
        selectLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");
        
        plantSelector = new ComboBox<>();
        plantSelector.getItems().addAll("Flower", "Tomato", "Tree", "Grass", "Basil");
        plantSelector.setValue("Flower");
        plantSelector.getStyleClass().add("modern-combo");
        plantSelector.setPrefWidth(150);
        
        javafx.scene.control.Button clearBtn = new javafx.scene.control.Button("🗑 Clear All");
        clearBtn.getStyleClass().add("modern-button");
        clearBtn.setOnAction(e -> clearGarden());
        
        selectorBox.getChildren().addAll(selectLabel, plantSelector, clearBtn);
        this.getChildren().add(selectorBox);
    }
    
    /**
     * Sets up the garden grid with animated tiles.
     */
    private void setupGrid() {
        gardenGrid.setHgap(3);
        gardenGrid.setVgap(3);
        gardenGrid.setPadding(new Insets(10));
        gardenGrid.getStyleClass().add("garden-grid");
        gardenGrid.setAlignment(Pos.CENTER);
        
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                // Create grass tile for empty cells
                GrassTile grassTile = new GrassTile();
                grassTiles[row][col] = grassTile;
                
                // Create plant tile (hidden initially)
                AnimatedTile tile = createTile(row, col);
                int tileIndex = (row * GRID_SIZE + col);
                tile.setTileIndex(tileIndex);
                tiles[row][col] = tile;
                tile.setVisible(false); // Hidden until plant is added
                
                // Stack grass and plant tiles so plant appears on top
                StackPane tileStack = new StackPane();
                tileStack.getChildren().addAll(grassTile, tile);
                tileStack.setAlignment(javafx.geometry.Pos.CENTER);
                
                gardenGrid.add(tileStack, col, row);
            }
        }
        
        this.getChildren().add(gardenGrid);
    }
    
    /**
     * Creates an animated tile for a grid cell.
     */
    private AnimatedTile createTile(int row, int col) {
        AnimatedTile tile = new AnimatedTile();
        Position position = new Position(row, col);
        GrassTile grassTile = grassTiles[row][col];
        
        // Set animation container on tile if available
        if (animationContainer != null) {
            tile.setAnimationContainer(animationContainer);
        }
        
        // Click handler - attach to grass tile (which is always visible for empty cells)
        grassTile.setOnMouseClicked(e -> {
            Plant existing = controller.getGarden().getPlant(position);
            
            // Create sparkle burst at click location
            javafx.geometry.Bounds bounds = grassTile.localToScene(grassTile.getBoundsInLocal());
            if (bounds != null && getAnimationContainer() != null) {
                Pane container = getAnimationContainer();
                if (container instanceof javafx.scene.layout.StackPane) {
                    javafx.geometry.Bounds containerBounds = container.localToScene(
                        container.getBoundsInLocal()
                    );
                    if (containerBounds != null) {
                        double x = bounds.getCenterX() - containerBounds.getMinX();
                        double y = bounds.getCenterY() - containerBounds.getMinY();
                        javafx.scene.Node node = container.getScene().getRoot();
                        findAndTriggerSparkles(node, x, y);
                    }
                }
            }
            
            if (existing == null) {
                String plantType = plantSelector.getValue();
                if (controller.plantSeed(plantType, position)) {
                    tile.animateGrowth();
                    updateTile(row, col);
                    
                    // Coin float on successful plant
                    if (bounds != null) {
                        createCoinFloat(bounds.getCenterX(), bounds.getCenterY(), 5);
                    }
                    
                    // Float petals from grass when planting (only if there was a flower)
                    if (grassTile.hasFlower()) {
                        grassTile.floatPetals();
                    }
                }
                // No auto-bloom - keep empty tiles icon-free
            } else {
                showPlantTooltip(tile, existing);
            }
        });
        
        // Also attach click handler to plant tile when visible
        tile.setOnMouseClicked(e -> {
            Plant existing = controller.getGarden().getPlant(position);
            if (existing != null) {
                showPlantTooltip(tile, existing);
            }
        });
        
        // Hover effects on grass tile
        grassTile.setOnMouseEntered(e -> {
            if (tile.isVisible()) {
                tile.applyHoverEffect();
                Plant plant = controller.getGarden().getPlant(position);
                if (plant != null) {
                    showHoverTooltip(tile, plant);
                }
            } else {
                // Hover effect on grass - make it glow slightly
                grassTile.setEffect(new javafx.scene.effect.Glow(0.2));
            }
        });
        
        grassTile.setOnMouseExited(e -> {
            if (tile.isVisible()) {
                tile.removeHoverEffect();
            } else {
                grassTile.setEffect(null);
            }
        });
        
        // Hover effects on plant tile
        tile.setOnMouseEntered(e -> {
            if (tile.isVisible()) {
                tile.applyHoverEffect();
                Plant plant = controller.getGarden().getPlant(position);
                if (plant != null) {
                    showHoverTooltip(tile, plant);
                }
            }
        });
        
        tile.setOnMouseExited(e -> {
            if (tile.isVisible()) {
                tile.removeHoverEffect();
            }
        });
        
        return tile;
    }
    
    /**
     * Shows tooltip on hover with plant information.
     */
    private void showHoverTooltip(AnimatedTile tile, Plant plant) {
        String tooltipText = String.format(
            "Plant: %s\nHealth: %d%%\nWater: %d%%\nAge: %d days\nStage: %s",
            plant.getPlantType(),
            plant.getHealthLevel(),
            plant.getWaterLevel(),
            plant.getDaysAlive(),
            plant.getGrowthStage().getDisplayName()
        );
        
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(tile, tooltip);
    }
    
    /**
     * Shows detailed plant information dialog.
     */
    private void showPlantTooltip(AnimatedTile tile, Plant plant) {
        // Get active pest count at this plant's position
        int activePestCount = controller.getSimulationEngine()
            .getPestControlSystem()
            .getActivePestCountAtPosition(plant.getPosition());
        
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.INFORMATION
        );
        alert.setTitle("Plant Information");
        alert.setHeaderText(plant.getPlantType() + " at " + plant.getPosition());
        alert.setContentText(String.format(
            "Growth Stage: %s\nHealth: %d%% (%s)\nWater Level: %d%% (Requires: %d%%)\n" +
            "Days Alive: %d / %d\n" +
            "Total Pest Attacks: %d (Lifetime)\n" +
            "Active Pests: %d (Currently Attacking)\n" +
            "Status: %s",
            plant.getGrowthStage().getDisplayName(),
            plant.getHealthLevel(),
            plant.getHealthStatus(),
            plant.getWaterLevel(),
            plant.getWaterRequirement(),
            plant.getDaysAlive(),
            plant.getMaxLifespan(),
            plant.getTotalPestAttacks(),
            activePestCount,
            plant.getHealthStatus()
        ));
        alert.showAndWait();
    }
    
    /**
     * Updates a specific tile.
     */
    public void updateTile(int row, int col) {
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            Position position = new Position(row, col);
            Plant plant = controller.getGarden().getPlant(position);
            
            if (plant == null) {
                // Show grass tile, hide plant tile
                grassTiles[row][col].setVisible(true);
                tiles[row][col].setVisible(false);
                // No icons on empty grass - just plain grass
            } else {
                // Show plant tile, hide grass tile
                grassTiles[row][col].setVisible(false);
                // Remove any flower icon from grass when plant is shown
                grassTiles[row][col].removeFlower();
                tiles[row][col].setVisible(true);
                tiles[row][col].update(plant);
            }
        }
    }
    
    /**
     * Updates all tiles in the grid.
     */
    public void updateAllTiles() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                updateTile(row, col);
            }
        }
    }
    
    /**
     * Animates watering effect on a zone.
     */
    public void animateWatering(int zoneId) {
        // Calculate zone boundaries (3x3 grid of zones)
        int zoneRow = (zoneId - 1) / 3;
        int zoneCol = (zoneId - 1) % 3;
        int tilesPerZone = 3;
        
        int startRow = zoneRow * tilesPerZone;
        int endRow = startRow + tilesPerZone;
        int startCol = zoneCol * tilesPerZone;
        int endCol = startCol + tilesPerZone;
        
        List<AnimatedTile> zoneTiles = new java.util.ArrayList<>();
        for (int row = startRow; row < endRow && row < GRID_SIZE; row++) {
            for (int col = startCol; col < endCol && col < GRID_SIZE; col++) {
                if (tiles[row][col] != null) {
                    zoneTiles.add(tiles[row][col]);
                }
            }
        }
        
        // Use animation container if set, otherwise try to find parent Pane
        Pane container = animationContainer;
        if (container == null) {
            javafx.scene.Node parent = getParent();
            while (parent != null && !(parent instanceof Pane)) {
                parent = parent.getParent();
            }
            if (parent instanceof Pane) {
                container = (Pane) parent;
            }
        }
        
        if (container != null) {
            // Show sprinkler animation
            SprinklerAnimationEngine.animateSprinkler(zoneId, zoneTiles, container);
            // Also show water droplets/ripples
            WaterAnimationEngine.animateZoneWatering(zoneTiles, container);
        }
    }
    
    /**
     * Animates watering effect on all tiles with full visual effects.
     */
    public void animateAllTilesWatering() {
        List<AnimatedTile> allTiles = new java.util.ArrayList<>();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (tiles[row][col] != null) {
                    allTiles.add(tiles[row][col]);
                }
            }
        }
        
        // Find parent Pane container for animation overlay
        Pane container = null;
        javafx.scene.Node parent = this.getParent();
        
        // Traverse up to find a suitable container
        while (parent != null) {
            if (parent instanceof Pane) {
                container = (Pane) parent;
                break;
            }
            parent = parent.getParent();
        }
        
        // If we found a container, start the animation
        if (container != null && !allTiles.isEmpty()) {
            // Start watering animation on all tiles (soil darkening)
            for (AnimatedTile tile : allTiles) {
                tile.startWateringAnimation();
            }
            
            // Show sprinkler animation for all zones
            SprinklerAnimationEngine.animateSprinkler(0, allTiles, container);
            // Also show water droplets/ripples
            WaterAnimationEngine.animateAllTilesWatering(allTiles, container);
        }
    }
    
    /**
     * Animates pesticide effect on a zone.
     */
    public void animatePesticide(int zoneId) {
        int zoneRow = (zoneId - 1) / 3;
        int zoneCol = (zoneId - 1) % 3;
        int tilesPerZone = 3;
        
        int startRow = zoneRow * tilesPerZone;
        int endRow = startRow + tilesPerZone;
        int startCol = zoneCol * tilesPerZone;
        int endCol = startCol + tilesPerZone;
        
        for (int row = startRow; row < endRow && row < GRID_SIZE; row++) {
            for (int col = startCol; col < endCol && col < GRID_SIZE; col++) {
                if (tiles[row][col] != null) {
                    tiles[row][col].animatePesticide();
                }
            }
        }
    }
    
    /**
     * Clears all plants from the garden.
     */
    private void clearGarden() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                controller.removePlant(new Position(row, col));
            }
        }
        updateAllTiles();
    }
    
    public GridPane getGardenGrid() {
        return gardenGrid;
    }
    
    // ============ PEST EVENT HANDLERS ============
    
    /**
     * Handles pest spawn event - spawns pest sprite on tile.
     */
    public void onPestSpawned(Position position, String pestType, boolean isHarmful) {
        System.out.println("[GardenGridPanel] onPestSpawned: " + pestType + 
                         " at (" + position.row() + ", " + position.column() + ")" +
                         " | Harmful: " + isHarmful);
        
        if (position.row() >= 0 && position.row() < GRID_SIZE && 
            position.column() >= 0 && position.column() < GRID_SIZE) {
            
            AnimatedTile tile = tiles[position.row()][position.column()];
            System.out.println("[GardenGridPanel] Tile found: " + (tile != null ? "YES" : "NULL") +
                             " | Visible: " + (tile != null && tile.isVisible()));
            
            if (tile != null && tile.isVisible()) {
                if (isHarmful) {
                    System.out.println("[GardenGridPanel] Spawning harmful pest: " + pestType);
                    tile.spawnPest(pestType);
                } else {
                    // Beneficial insect - determine healing amount
                    int healing = switch (pestType.toLowerCase()) {
                        case "bee" -> 3;
                        case "ladybug" -> 2;
                        case "butterfly" -> 2;
                        default -> 2;
                    };
                    System.out.println("[GardenGridPanel] Spawning beneficial insect: " + pestType + " (healing: " + healing + ")");
                    tile.spawnBeneficial(pestType, healing);
                }
            } else {
                System.err.println("[GardenGridPanel] ERROR: Cannot spawn pest - tile is null or not visible");
            }
        } else {
            System.err.println("[GardenGridPanel] ERROR: Invalid position: (" + position.row() + ", " + position.column() + ")");
        }
    }
    
    /**
     * Handles pest attack event - shows damage visual.
     */
    public void onPestAttack(Position position, int damage) {
        if (position.row() >= 0 && position.row() < GRID_SIZE && 
            position.column() >= 0 && position.column() < GRID_SIZE) {
            
            AnimatedTile tile = tiles[position.row()][position.column()];
            if (tile != null && tile.isVisible()) {
                tile.showDamageVisual(damage);
            }
        }
    }
    
    /**
     * Handles pesticide application - animates spray effect.
     */
    public void onPesticideApplied(edu.scu.csen275.smartgarden.model.Zone zone) {
        // Apply to all plants in the zone
        for (var plant : zone.getPlants()) {
            if (!plant.isDead()) {
                Position pos = plant.getPosition();
                if (pos.row() >= 0 && pos.row() < GRID_SIZE && 
                    pos.column() >= 0 && pos.column() < GRID_SIZE) {
                    
                    AnimatedTile tile = tiles[pos.row()][pos.column()];
                    if (tile != null && tile.isVisible() && tile.hasPests()) {
                        tile.applyPesticide();
                    }
                }
            }
        }
    }
    
    /**
     * Handles pesticide application at a specific position.
     */
    public void onPesticideApplied(Position position) {
        if (position.row() >= 0 && position.row() < GRID_SIZE && 
            position.column() >= 0 && position.column() < GRID_SIZE) {
            
            AnimatedTile tile = tiles[position.row()][position.column()];
            if (tile != null && tile.isVisible() && tile.hasPests()) {
                tile.applyPesticide();
            }
        }
    }
    
    /**
     * Handles beneficial insect healing event.
     */
    public void onBeneficialInsectHealing(Position position, int healing) {
        if (position.row() >= 0 && position.row() < GRID_SIZE && 
            position.column() >= 0 && position.column() < GRID_SIZE) {
            
            AnimatedTile tile = tiles[position.row()][position.column()];
            if (tile != null && tile.isVisible()) {
                tile.showHealingVisual(healing);
            }
        }
    }
    
    /**
     * Gets the tile at a specific position.
     */
    public AnimatedTile getTileAt(int row, int col) {
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            return tiles[row][col];
        }
        return null;
    }
}

