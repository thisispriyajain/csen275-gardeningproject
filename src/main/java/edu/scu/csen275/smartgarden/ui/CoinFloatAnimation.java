package edu.scu.csen275.smartgarden.ui;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Creates floating coin animation when points are earned.
 * Gardenscapes-style coin float-up effect.
 */
public class CoinFloatAnimation {
    
    /**
     * Creates a floating coin animation at the specified position.
     */
    public static void createCoinFloat(Pane container, double startX, double startY, int value) {
        Label coinLabel = new Label("💰 +" + value);
        coinLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        coinLabel.setTextFill(Color.rgb(255, 193, 7));
        coinLabel.setStyle(
            "-fx-background-color: rgba(255,255,255,0.9); " +
            "-fx-background-radius: 15; " +
            "-fx-padding: 5 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(255,193,7,0.6), 10, 0.8, 0, 0);"
        );
        
        coinLabel.setLayoutX(startX);
        coinLabel.setLayoutY(startY);
        
        container.getChildren().add(coinLabel);
        
        // Float up animation
        TranslateTransition floatUp = new TranslateTransition(Duration.seconds(1.5), coinLabel);
        floatUp.setByY(-80);
        floatUp.setByX((Math.random() - 0.5) * 40); // Slight horizontal drift
        floatUp.setInterpolator(Interpolator.EASE_OUT);
        
        // Fade out
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), coinLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setInterpolator(Interpolator.EASE_OUT);
        
        // Scale pulse
        ScaleTransition pulse = new ScaleTransition(Duration.millis(500), coinLabel);
        pulse.setFromX(0.5);
        pulse.setFromY(0.5);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(2);
        
        // Remove when done
        floatUp.setOnFinished(e -> {
            container.getChildren().remove(coinLabel);
        });
        
        // Start animations
        pulse.play();
        ParallelTransition parallel = new ParallelTransition(floatUp, fadeOut);
        parallel.play();
    }
    
    /**
     * Creates a floating star animation.
     */
    public static void createStarFloat(Pane container, double startX, double startY) {
        Label starLabel = new Label("⭐");
        starLabel.setFont(Font.font(24));
        
        starLabel.setLayoutX(startX);
        starLabel.setLayoutY(startY);
        
        container.getChildren().add(starLabel);
        
        // Float up with rotation
        TranslateTransition floatUp = new TranslateTransition(Duration.seconds(1.5), starLabel);
        floatUp.setByY(-100);
        floatUp.setByX((Math.random() - 0.5) * 50);
        floatUp.setInterpolator(Interpolator.EASE_OUT);
        
        RotateTransition rotate = new RotateTransition(Duration.seconds(1.5), starLabel);
        rotate.setFromAngle(0);
        rotate.setToAngle(360);
        rotate.setInterpolator(Interpolator.LINEAR);
        
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), starLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        ScaleTransition scale = new ScaleTransition(Duration.millis(300), starLabel);
        scale.setFromX(0.3);
        scale.setFromY(0.3);
        scale.setToX(1.5);
        scale.setToY(1.5);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        
        floatUp.setOnFinished(e -> {
            container.getChildren().remove(starLabel);
        });
        
        scale.play();
        ParallelTransition parallel = new ParallelTransition(floatUp, rotate, fadeOut);
        parallel.play();
    }
}

