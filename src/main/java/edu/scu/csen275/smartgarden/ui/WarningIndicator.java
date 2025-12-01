package edu.scu.csen275.smartgarden.ui;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Warning indicator that shows a red exclamation bubble above tiles under pest attack.
 */
public class WarningIndicator extends Label {
    private Timeline pulseAnimation;
    
    public WarningIndicator() {
        setText("!");
        setStyle(
            "-fx-background-color: #FF4444; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 50%; " +
            "-fx-min-width: 20px; " +
            "-fx-min-height: 20px; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 14px; " +
            "-fx-alignment: center;"
        );
        setAlignment(Pos.CENTER);
        
        // Glowing red effect
        Glow glow = new Glow(0.8);
        glow.setInput(new javafx.scene.effect.ColorAdjust(0, 0, 0, 0));
        setEffect(glow);
        
        setupPulseAnimation();
    }
    
    /**
     * Sets up pulsing glow animation.
     */
    private void setupPulseAnimation() {
        // Scale pulse
        ScaleTransition scalePulse = new ScaleTransition(Duration.millis(1000), this);
        scalePulse.setFromX(1.0);
        scalePulse.setFromY(1.0);
        scalePulse.setToX(1.2);
        scalePulse.setToY(1.2);
        scalePulse.setInterpolator(Interpolator.EASE_BOTH);
        scalePulse.setCycleCount(Animation.INDEFINITE);
        scalePulse.setAutoReverse(true);
        scalePulse.play();
    }
    
    /**
     * Shows warning with bounce-in animation.
     */
    public void show() {
        setVisible(true);
        setOpacity(1.0);
        
        // Bounce-in animation
        ScaleTransition bounceIn = new ScaleTransition(Duration.millis(300), this);
        bounceIn.setFromX(0);
        bounceIn.setFromY(0);
        bounceIn.setToX(1.0);
        bounceIn.setToY(1.0);
        bounceIn.setInterpolator(Interpolator.EASE_OUT);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1.0);
        
        ParallelTransition appear = new ParallelTransition(bounceIn, fadeIn);
        appear.play();
    }
    
    /**
     * Hides warning with fade-out.
     */
    public void hide() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> setVisible(false));
        fadeOut.play();
    }
}
