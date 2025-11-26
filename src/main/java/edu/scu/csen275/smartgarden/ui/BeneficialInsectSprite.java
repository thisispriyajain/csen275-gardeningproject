package edu.scu.csen275.smartgarden.ui;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.Random;

/**
 * Animated sprite for beneficial insects (Set C: Monarch Butterfly, Honey Bee, Blue Dragonfly).
 * Slow upward float or gentle hovering animations with healing effects.
 * Uses Text node for proper emoji rendering support.
 */
public class BeneficialInsectSprite extends StackPane {
    private final String insectType;
    private final int healingAmount;
    private final Random random;
    private final Text emojiText;
    private Timeline movementAnimation;
    private Glow healingGlow;
    private boolean isAlive;
    
    // Emoji size: 20-28px for visibility
    private static final int EMOJI_SIZE = 24;
    private static final int SPRITE_SIZE = 28;
    
    public BeneficialInsectSprite(String insectType, int healingAmount) {
        this.insectType = insectType;
        this.healingAmount = healingAmount;
        this.random = new Random();
        this.isAlive = true;
        
        // Use Text node for proper emoji rendering
        String emoji = getInsectEmoji(insectType);
        emojiText = new Text(emoji);
        
        // Use system font that supports emojis - smaller size (20-28px)
        try {
            Font emojiFont = Font.font("Segoe UI Emoji", EMOJI_SIZE);
            emojiText.setFont(emojiFont);
        } catch (Exception e) {
            try {
                Font emojiFont = Font.font("Apple Color Emoji", EMOJI_SIZE);
                emojiText.setFont(emojiFont);
            } catch (Exception e2) {
                emojiText.setFont(Font.font(EMOJI_SIZE));
            }
        }
        
        emojiText.setFill(Color.BLACK);
        emojiText.setStyle("-fx-font-weight: normal;");
        
        // Add text to StackPane
        this.getChildren().add(emojiText);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: transparent;");
        
        // Set initial random position within tile bounds
        setLayoutX(15 + random.nextDouble() * 30);
        setLayoutY(15 + random.nextDouble() * 30);
        
        // Make sure sprite is visible and on top
        setVisible(true);
        setOpacity(1.0);
        setMouseTransparent(true);
        
        // Ensure sprite is properly sized (20-28px)
        setMinSize(SPRITE_SIZE, SPRITE_SIZE);
        setMaxSize(SPRITE_SIZE, SPRITE_SIZE);
        setPrefSize(SPRITE_SIZE, SPRITE_SIZE);
        
        // Add tooltip to show insect type
        javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(insectType + " (Beneficial)");
        javafx.scene.control.Tooltip.install(this, tooltip);
        
        setupAnimations();
    }
    
    /**
     * Gets emoji for insect type (Set C) - colorful emoji-based definitions.
     */
    private String getInsectEmoji(String type) {
        return switch (type.toLowerCase()) {
            case "monarch butterfly" -> "🦋";    // Just the butterfly emoji
            case "honey bee" -> "🐝";           // Just the bee emoji
            case "blue dragonfly" -> "🐉";      // Just the dragonfly emoji
            default -> "🐝";                    // Honey Bee (default)
        };
    }
    
    /**
     * Sets up movement animations based on insect type (Set C).
     * Butterflies and dragonflies: slow upward float or gentle hovering.
     */
    private void setupAnimations() {
        switch (insectType.toLowerCase()) {
            case "monarch butterfly" -> setupButterflyFloatAnimation();
            case "honey bee" -> setupBeeHoverAnimation();
            case "blue dragonfly" -> setupDragonflyFloatAnimation();
            default -> setupBeeHoverAnimation();
        }
        
        // Healing glow effect
        healingGlow = new Glow(0.5);
        ColorAdjust greenTint = new ColorAdjust(0, 1.2, 0, 0); // Green tint
        healingGlow.setInput(greenTint);
        this.setEffect(healingGlow);
    }
    
    /**
     * Monarch Butterfly: Slow upward float with gentle hovering.
     */
    private void setupButterflyFloatAnimation() {
        // Slow upward float
        TranslateTransition floatUp = new TranslateTransition(Duration.millis(2500), this);
        floatUp.setFromY(0);
        floatUp.setToY(-10);
        floatUp.setInterpolator(Interpolator.EASE_BOTH);
        floatUp.setCycleCount(Animation.INDEFINITE);
        floatUp.setAutoReverse(true);
        
        // Gentle horizontal hover
        TranslateTransition hover = new TranslateTransition(Duration.millis(2000), this);
        hover.setFromX(0);
        hover.setToX(5);
        hover.setInterpolator(Interpolator.EASE_BOTH);
        hover.setCycleCount(Animation.INDEFINITE);
        hover.setAutoReverse(true);
        
        // Gentle rotation
        RotateTransition rotate = new RotateTransition(Duration.millis(3000), this);
        rotate.setFromAngle(-3);
        rotate.setToAngle(3);
        rotate.setInterpolator(Interpolator.EASE_BOTH);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.setAutoReverse(true);
        
        ParallelTransition floatAnimation = new ParallelTransition(floatUp, hover, rotate);
        floatAnimation.play();
    }
    
    /**
     * Honey Bee: Gentle hovering with slight movement.
     */
    private void setupBeeHoverAnimation() {
        // Gentle hover - slow up and down
        TranslateTransition hover = new TranslateTransition(Duration.millis(2000), this);
        hover.setFromY(0);
        hover.setToY(-6);
        hover.setInterpolator(Interpolator.EASE_BOTH);
        hover.setCycleCount(Animation.INDEFINITE);
        hover.setAutoReverse(true);
        
        // Slight horizontal drift
        TranslateTransition drift = new TranslateTransition(Duration.millis(3000), this);
        drift.setFromX(0);
        drift.setToX(4);
        drift.setInterpolator(Interpolator.EASE_BOTH);
        drift.setCycleCount(Animation.INDEFINITE);
        drift.setAutoReverse(true);
        
        ParallelTransition hoverAnimation = new ParallelTransition(hover, drift);
        hoverAnimation.play();
    }
    
    /**
     * Blue Dragonfly: Slow upward float with gentle hovering.
     */
    private void setupDragonflyFloatAnimation() {
        // Slow upward float
        TranslateTransition floatUp = new TranslateTransition(Duration.millis(2200), this);
        floatUp.setFromY(0);
        floatUp.setToY(-8);
        floatUp.setInterpolator(Interpolator.EASE_BOTH);
        floatUp.setCycleCount(Animation.INDEFINITE);
        floatUp.setAutoReverse(true);
        
        // Gentle horizontal hover
        TranslateTransition hover = new TranslateTransition(Duration.millis(1800), this);
        hover.setFromX(0);
        hover.setToX(4);
        hover.setInterpolator(Interpolator.EASE_BOTH);
        hover.setCycleCount(Animation.INDEFINITE);
        hover.setAutoReverse(true);
        
        ParallelTransition floatAnimation = new ParallelTransition(floatUp, hover);
        floatAnimation.play();
    }
    
    /**
     * Triggers healing effect animation.
     */
    public void triggerHealingEffect() {
        // Pulsing glow
        ScaleTransition pulse = new ScaleTransition(Duration.millis(400), this);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.15);
        pulse.setToY(1.15);
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
