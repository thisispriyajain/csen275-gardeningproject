package edu.scu.csen275.smartgarden.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

/**
 * Modern info panel with card-based layout.
 */
public class InfoPanel extends VBox {
    private final InfoCard simulationCard;
    private final InfoCard resourcesCard;
    private final InfoCard controlsCard;
    
    private final Label timeLabel;
    private final Label statsLabel;
    private final Label heatingStatusLabel;
    private final WeatherDisplay weatherDisplay;
    private final ProgressBar waterBar;
    private final ProgressBar tempBar;
    private final ProgressBar pesticideBar;
    private final Button refillWaterBtn;
    private final Button refillPesticideBtn;
    private final Button waterAllBtn;
    
    public InfoPanel() {
        this.setSpacing(15);
        this.setPadding(new Insets(15));
        this.setMinWidth(320);
        this.getStyleClass().add("info-panel");
        
        // Simulation Info Card
        simulationCard = new InfoCard("📊 Simulation Info");
        timeLabel = simulationCard.addLabel("Time: --", false);
        statsLabel = simulationCard.addLabel("Plants: 0", false);
        heatingStatusLabel = simulationCard.addLabel("🔥 Heating: Off", false);
        weatherDisplay = new WeatherDisplay();
        simulationCard.getChildren().add(weatherDisplay);
        
        // Resources Card
        resourcesCard = new InfoCard("💧 Resources");
        waterBar = resourcesCard.addProgressBar("Water Supply", 1.0);
        tempBar = resourcesCard.addProgressBar("Temperature", 0.5);
        pesticideBar = resourcesCard.addProgressBar("Pesticide Stock", 1.0);
        
        // Controls Card
        controlsCard = new InfoCard("🎮 Manual Controls");
        
        refillWaterBtn = new Button("💧 Refill Water");
        refillWaterBtn.getStyleClass().add("resource-button");
        refillWaterBtn.setPrefWidth(200);
        
        refillPesticideBtn = new Button("🧪 Refill Pesticide");
        refillPesticideBtn.getStyleClass().add("resource-button");
        refillPesticideBtn.setPrefWidth(200);
        
        waterAllBtn = new Button("🌊 Water All Zones");
        waterAllBtn.getStyleClass().add("resource-button");
        waterAllBtn.setPrefWidth(200);
        
        controlsCard.getChildren().addAll(refillWaterBtn, refillPesticideBtn, waterAllBtn);
        
        // Add all cards
        this.getChildren().addAll(simulationCard, resourcesCard, controlsCard);
    }
    
    /**
     * Updates progress bars with animation.
     */
    public void updateProgressBars(double waterProgress, double tempProgress, double pesticideProgress) {
        animateProgressBar(waterBar, waterProgress);
        animateProgressBar(tempBar, tempProgress);
        animateProgressBar(pesticideBar, pesticideProgress);
        
        // Update styles based on values
        InfoCard.updateProgressBarStyle(waterBar, waterProgress);
        InfoCard.updateProgressBarStyle(pesticideBar, pesticideProgress);
    }
    
    /**
     * Animates progress bar to new value smoothly.
     */
    private void animateProgressBar(ProgressBar bar, double targetValue) {
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(300),
                e -> bar.setProgress(targetValue)
            )
        );
        timeline.play();
    }
    
    // Getters
    public Label getTimeLabel() { return timeLabel; }
    public Label getStatsLabel() { return statsLabel; }
    public Label getHeatingStatusLabel() { return heatingStatusLabel; }
    public WeatherDisplay getWeatherDisplay() { return weatherDisplay; }
    public ProgressBar getWaterBar() { return waterBar; }
    public ProgressBar getTempBar() { return tempBar; }
    public ProgressBar getPesticideBar() { return pesticideBar; }
    public Button getRefillWaterBtn() { return refillWaterBtn; }
    public Button getRefillPesticideBtn() { return refillPesticideBtn; }
    public Button getWaterAllBtn() { return waterAllBtn; }
}

