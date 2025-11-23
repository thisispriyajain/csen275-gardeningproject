package edu.scu.csen275.smartgarden.ui;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.Random;

/**
 * Animated sprite for beneficial insects (Bee, Ladybug, Butterfly).
 * Provides friendly animations and healing effects.
 */
public class BeneficialInsectSprite extends Label {
    private final String insectType;
    private final int healingAmount;
    private final Random random;
    private Timeline movementAnimation;
    private Glow healingGlow;
    private boolean isAlive;
    
    public BeneficialInsectSprite(String insectType, int healingAmount) {
        this.insectType = insectType;
        this.healingAmount = healingAmount;
        this.random = new Random();
        this.isAlive = true;
        
        // Set emoji based on insect type using Unicode escape sequences
        String emoji = getInsectEmoji(insectType);
        setText(emoji);
        
        // Use system font that supports emojis
        try {
            javafx.scene.text.Font emojiFont = javafx.scene.text.Font.font("Segoe UI Emoji", 36);
            setFont(emojiFont);
        } catch (Exception e) {
            try {
                javafx.scene.text.Font emojiFont = javafx.scene.text.Font.font("Apple Color Emoji", 36);
                setFont(emojiFont);
            } catch (Exception e2) {
                setFont(javafx.scene.text.Font.font(36));
            }
        }
        
        setAlignment(Pos.CENTER);
        setStyle(
            "-fx-padding: 0px; " +
            "-fx-background-color: transparent; " +
            "-fx-text-fill: black; " +
            "-fx-font-weight: normal;"
        );
        
        // Set initial random position within tile bounds
        setLayoutX(15 + random.nextDouble() * 30);
        setLayoutY(15 + random.nextDouble() * 30);
        
        // Ensure sprite is properly sized
        setMinSize(32, 32);
        setMaxSize(32, 32);
        setPrefSize(32, 32);
        
        // Add tooltip to show insect type
        javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(insectType + " (Beneficial)");
        javafx.scene.control.Tooltip.install(this, tooltip);
        
        setupAnimations();
    }
    
    /**
     * Gets emoji for insect type using Unicode escape sequences.
     */
    private String getInsectEmoji(String type) {
        return switch (type.toLowerCase()) {
            case "bee" -> "\uD83D\uDC1D";      // 🐝 Bee
            case "ladybug" -> "\uD83D\uDC1E";  // 🐞 Ladybug
            case "butterfly" -> "\uD83E\uDD8B"; // 🦋 Butterfly
            default -> "\uD83D\uDC1D";         // 🐝 Bee (default)
        };
    }
    
    /**
     * Sets up movement animations based on insect type.
     */
    private void setupAnimations() {
        switch (insectType.toLowerCase()) {
            case "bee" -> setupBeeBuzzAnimation();
            case "ladybug" -> setupLadybugCrawlAnimation();
            case "butterfly" -> setupButterflyFlutterAnimation();
        }
        
        // Healing glow effect
        healingGlow = new Glow(0.5);
        ColorAdjust greenTint = new ColorAdjust(0, 1.2, 0, 0); // Green tint
        healingGlow.setInput(greenTint);
        this.setEffect(healingGlow);
    }
    
    /**
     * Bee: Fast buzzing movement (jittery).
     */
    private void setupBeeBuzzAnimation() {
        movementAnimation = new Timeline(
            new KeyFrame(Duration.millis(40), e -> {
                if (isAlive) {
                    setTranslateX(getTranslateX() + (random.nextDouble() - 0.5) * 3);
                    setTranslateY(getTranslateY() + (random.nextDouble() - 0.5) * 3);
                }
            })
        );
        movementAnimation.setCycleCount(Animation.INDEFINITE);
        movementAnimation.play();
    }
    
    /**
     * Ladybug: Slow crawling animation.
     */
    private void setupLadybugCrawlAnimation() {
        TranslateTransition crawl = new TranslateTransition(Duration.millis(3000), this);
        crawl.setFromX(0);
        crawl.setToX(10);
        crawl.setFromY(0);
        crawl.setToY(-5);
        crawl.setInterpolator(Interpolator.EASE_BOTH);
        crawl.setCycleCount(Animation.INDEFINITE);
        crawl.setAutoReverse(true);
        crawl.play();
    }
    
    /**
     * Butterfly: Smooth fluttering up and down.
     */
    private void setupButterflyFlutterAnimation() {
        TranslateTransition flutter = new TranslateTransition(Duration.millis(1500), this);
        flutter.setFromY(0);
        flutter.setToY(-8);
        flutter.setInterpolator(Interpolator.EASE_BOTH);
        flutter.setCycleCount(Animation.INDEFINITE);
        flutter.setAutoReverse(true);
        
        RotateTransition flutterRotate = new RotateTransition(Duration.millis(1500), this);
        flutterRotate.setFromAngle(-5);
        flutterRotate.setToAngle(5);
        flutterRotate.setInterpolator(Interpolator.EASE_BOTH);
        flutterRotate.setCycleCount(Animation.INDEFINITE);
        flutterRotate.setAutoReverse(true);
        
        ParallelTransition flutterAnimation = new ParallelTransition(flutter, flutterRotate);
        flutterAnimation.play();
    }
    
    /**
     * Triggers healing effect animation.
     */
    public void triggerHealingEffect() {
        // Pulsing glow
        ScaleTransition pulse = new ScaleTransition(Duration.millis(300), this);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(2);
        pulse.play();
    }
    
    public String getInsectType() {
        return insectType;
    }
    
    public int getHealingAmount() {
        return healingAmount;
    }
    
    public boolean isAlive() {
        return isAlive;
    }
    
    /**
     * Triggers healing animation.
     */
    public void animateHealing() {
        triggerHealingEffect();
    }
    
    public void stopAnimations() {
        if (movementAnimation != null) movementAnimation.stop();
    }
}
