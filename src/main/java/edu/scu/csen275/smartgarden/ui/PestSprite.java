package edu.scu.csen275.smartgarden.ui;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Random;

/**
 * Animated sprite for harmful pests (Aphid, Caterpillar, Beetle, Spider Mite).
 * Crawls and moves around the plant tile with periodic damage animations.
 * Uses Text node for proper emoji rendering support (avoids surrogate pair issues).
 */
public class PestSprite extends StackPane {
    private final String pestType;
    private final Random random;
    private final Text emojiText;
    private TranslateTransition crawlAnimation;
    private RotateTransition wiggleAnimation;
    private Timeline damageAnimation;
    private boolean isAlive;
    
    public PestSprite(String pestType) {
        this.pestType = pestType;
        this.random = new Random();
        this.isAlive = true;
        
        // Use Text node for proper emoji rendering (handles surrogate pairs correctly)
        String emoji = getPestEmoji(pestType);
        emojiText = new Text(emoji);
        
        // Use system font that supports emojis
        try {
            Font emojiFont = Font.font("Segoe UI Emoji", 36);
            emojiText.setFont(emojiFont);
        } catch (Exception e) {
            // Try alternative emoji fonts
            try {
                Font emojiFont = Font.font("Apple Color Emoji", 36);
                emojiText.setFont(emojiFont);
            } catch (Exception e2) {
                // Fallback to default with larger size
                emojiText.setFont(Font.font(36));
            }
        }
        
        emojiText.setFill(javafx.scene.paint.Color.BLACK);
        emojiText.setStyle("-fx-font-weight: normal;");
        
        // Add text to StackPane
        this.getChildren().add(emojiText);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: transparent;");
        
        // Set initial random position within tile bounds
        setLayoutX(10 + random.nextDouble() * 20);
        setLayoutY(10 + random.nextDouble() * 20);
        
        // Make sure sprite is visible and on top
        setVisible(true);
        setOpacity(1.0);
        setMouseTransparent(true);
        
        // Ensure sprite is properly sized
        setMinSize(40, 40);
        setMaxSize(40, 40);
        setPrefSize(40, 40);
        
        // Add tooltip to show pest type
        javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(pestType + " (Harmful Pest)");
        javafx.scene.control.Tooltip.install(this, tooltip);
        
        // DEBUG: Log emoji being set with proper Unicode
        System.out.println("[PestSprite] Created: " + pestType + 
                         " | Emoji: " + emoji + 
                         " | Length: " + emoji.length() + 
                         " | CodePoints: " + emoji.codePointCount(0, emoji.length()) +
                         " | Font: " + emojiText.getFont().getName() +
                         " | Visible: " + isVisible() +
                         " | Opacity: " + getOpacity());
        
        // Verify emoji is actually set
        if (emojiText.getText() == null || emojiText.getText().isEmpty()) {
            System.err.println("[PestSprite] ERROR: Emoji text is null or empty for " + pestType);
        }
        
        setupAnimations();
    }
    
    /**
     * Gets emoji for pest type using Unicode escape sequences to avoid encoding issues.
     * Also tries direct emoji characters as fallback.
     */
    private String getPestEmoji(String type) {
        String emoji = switch (type.toLowerCase()) {
            case "aphid" -> "\uD83D\uDC1C";      // 🐜 Ant
            case "caterpillar" -> "\uD83D\uDC1B"; // 🐛 Bug
            case "beetle" -> "\uD83E\uDD32";      // 🪲 Beetle
            case "spider mite" -> "\uD83D\uDD77\uFE0F"; // 🕷️ Spider
            default -> "\uD83D\uDC1B";           // 🐛 Bug (default)
        };
        
        // Verify emoji is valid
        if (emoji == null || emoji.isEmpty()) {
            System.err.println("[PestSprite] ERROR: Emoji is null/empty for " + type + ", using fallback");
            emoji = "🐛"; // Direct emoji as ultimate fallback
        }
        
        // Try direct emoji if Unicode escape fails (check if first char is valid emoji range)
        int firstCodePoint = emoji.codePointAt(0);
        if (firstCodePoint < 0x1F000 || firstCodePoint > 0x1FAFF) {
            System.out.println("[PestSprite] Unicode escape may have failed (codePoint: " + firstCodePoint + 
                             "), trying direct emoji for " + type);
            emoji = switch (type.toLowerCase()) {
                case "aphid" -> "🐜";
                case "caterpillar" -> "🐛";
                case "beetle" -> "🪲";
                case "spider mite" -> "🕷️";
                default -> "🐛";
            };
        }
        
        return emoji;
    }
    
    /**
     * Sets up continuous crawling animation (sine wave movement).
     */
    private void setupAnimations() {
        // Crawl animation - moves pest in small circular/sine pattern
        crawlAnimation = new TranslateTransition(Duration.millis(2000 + random.nextInt(1000)), this);
        double offsetX = -5 - random.nextDouble() * 5;
        double offsetY = -5 - random.nextDouble() * 5;
        crawlAnimation.setFromX(0);
        crawlAnimation.setToX(offsetX);
        crawlAnimation.setFromY(0);
        crawlAnimation.setToY(offsetY);
        crawlAnimation.setInterpolator(Interpolator.EASE_BOTH);
        crawlAnimation.setCycleCount(Animation.INDEFINITE);
        crawlAnimation.setAutoReverse(true);
        crawlAnimation.play();
        
        // Periodic wiggle animation (every 2-3 seconds)
        wiggleAnimation = new RotateTransition(Duration.millis(200), this);
        wiggleAnimation.setFromAngle(-10);
        wiggleAnimation.setToAngle(10);
        wiggleAnimation.setCycleCount(2);
        wiggleAnimation.setAutoReverse(true);
        
        // Damage tick animation - wiggle every 2-3 seconds
        damageAnimation = new Timeline(
            new KeyFrame(Duration.seconds(2 + random.nextDouble()), e -> {
                if (isAlive) {
                    wiggleAnimation.playFromStart();
                }
            })
        );
        damageAnimation.setCycleCount(Animation.INDEFINITE);
        damageAnimation.play();
    }
    
    /**
     * Animates pest death when pesticide is applied (pop animation).
     */
    public void animateDeath(Runnable onComplete) {
        if (!isAlive) return;
        isAlive = false;
        
        // Stop all animations
        crawlAnimation.stop();
        damageAnimation.stop();
        
        // Pop animation - scale down and fade out - SLOWED DOWN for visibility
        ScaleTransition pop = new ScaleTransition(Duration.millis(800), this); // 800ms instead of 300ms
        pop.setFromX(1.0);
        pop.setFromY(1.0);
        pop.setToX(0);
        pop.setToY(0);
        pop.setInterpolator(Interpolator.EASE_OUT);
        
        FadeTransition fade = new FadeTransition(Duration.millis(800), this); // 800ms instead of 300ms
        fade.setFromValue(1.0);
        fade.setToValue(0);
        
        ParallelTransition death = new ParallelTransition(pop, fade);
        death.setOnFinished(e -> {
            if (onComplete != null) {
                onComplete.run();
            }
        });
        death.play();
    }
    
    public String getPestType() {
        return pestType;
    }
    
    public boolean isAlive() {
        return isAlive;
    }
    
    /**
     * Triggers attack animation (wiggle).
     */
    public void animateAttack() {
        if (wiggleAnimation != null) {
            wiggleAnimation.playFromStart();
        }
    }
    
    public void stopAnimations() {
        if (crawlAnimation != null) crawlAnimation.stop();
        if (damageAnimation != null) damageAnimation.stop();
        if (wiggleAnimation != null) wiggleAnimation.stop();
    }
}
