package edu.scu.csen275.smartgarden.ui;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates spray mist animation when pesticide is applied to a tile.
 * Shows expanding white/blue fog, pest death animations, and healing glow.
 */
public class PesticideSprayEngine {
    
    /**
     * Animates pesticide spray on a tile with all effects.
     */
    public static void animateSpray(StackPane tile, List<PestSprite> pests, 
                                   List<BeneficialInsectSprite> beneficialInsects,
                                   Pane particleContainer, double centerX, double centerY) {
        double tileWidth = 60;
        double tileHeight = 60;
        
        // Check if mist canvas already exists (prevent multiple animations)
        Canvas existingMist = null;
        for (var child : tile.getChildren()) {
            if (child instanceof Canvas && child.getUserData() != null && child.getUserData().equals("mist")) {
                existingMist = (Canvas) child;
                break;
            }
        }
        if (existingMist != null) {
            return; // Already animating, skip
        }
        
        // Create spray mist canvas overlay
        Canvas mistCanvas = new Canvas(tileWidth, tileHeight);
        mistCanvas.setUserData("mist"); // Mark as mist canvas
        GraphicsContext gc = mistCanvas.getGraphicsContext2D();
        mistCanvas.setMouseTransparent(true);
        
        // Add mist canvas to tile temporarily (bring to front)
        tile.getChildren().add(mistCanvas);
        mistCanvas.toFront();
        
        // List of active mist particles - centered in tile
        List<MistParticle> mistParticles = new ArrayList<>();
        double tileCenterX = tileWidth / 2;
        double tileCenterY = tileHeight / 2;
        for (int i = 0; i < 20; i++) {
            MistParticle p = new MistParticle();
            p.x = tileCenterX + (Math.random() - 0.5) * 10;
            p.y = tileCenterY + (Math.random() - 0.5) * 10;
            p.radius = 2 + Math.random() * 3;
            p.vx = (Math.random() - 0.5) * 3;
            p.vy = (Math.random() - 0.5) * 3;
            p.alpha = 0.8;
            mistParticles.add(p);
        }
        
        // Animate expanding mist - SLOWED DOWN: 150 cycles = 2.4 seconds for better visibility
        Timeline mistAnimation = new Timeline(
            new KeyFrame(Duration.millis(16), e -> {
                gc.clearRect(0, 0, tileWidth, tileHeight);
                
                // Update and draw mist particles
                for (MistParticle p : mistParticles) {
                    p.x += p.vx;
                    p.y += p.vy;
                    p.radius += 0.3; // Slower expansion
                    p.alpha *= 0.97; // Slower fade
                    p.vx *= 0.99; // Slower velocity decay
                    p.vy *= 0.99;
                    
                    // Draw mist particle with more visible color
                    gc.setFill(Color.color(0.9, 0.95, 1.0, p.alpha * 0.8)); // Slightly blue-tinted white
                    gc.fillOval(p.x - p.radius, p.y - p.radius, p.radius * 2, p.radius * 2);
                }
            })
        );
        mistAnimation.setCycleCount(150); // ~2.4 seconds at 16ms per frame - MUCH LONGER
        mistAnimation.setOnFinished(e -> {
            tile.getChildren().remove(mistCanvas);
        });
        mistAnimation.play();
        
        // Animate pest death (shrink animation)
        List<Runnable> completionCallbacks = new ArrayList<>();
        for (PestSprite pest : new ArrayList<>(pests)) {
            // Delay each pest death slightly for staggered effect
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(
                Duration.millis(200 + (int)(Math.random() * 300)) // 200-500ms delay
            );
            delay.setOnFinished(e -> {
                pest.animateDeath(() -> {
                    // Pest death animation completed
                });
            });
            delay.play();
        }
        
        // 70% chance to save beneficial insects (animate them escaping)
        for (BeneficialInsectSprite insect : new ArrayList<>(beneficialInsects)) {
            if (Math.random() > 0.3) {
                // Escape animation - fly away
                TranslateTransition escape = new TranslateTransition(Duration.millis(500), insect);
                escape.setByX((Math.random() - 0.5) * 50);
                escape.setByY(-30 - Math.random() * 20);
                escape.setOnFinished(e -> {
                    // Insect saved, removed by AnimatedTile
                });
                escape.play();
            } else {
                // Insect dies too
                ScaleTransition pop = new ScaleTransition(Duration.millis(300), insect);
                pop.setFromX(1.0);
                pop.setFromY(1.0);
                pop.setToX(0);
                pop.setToY(0);
                FadeTransition fade = new FadeTransition(Duration.millis(300), insect);
                fade.setToValue(0);
                ParallelTransition death = new ParallelTransition(pop, fade);
                death.play();
            }
        }
        
        // Show "Plant Saved!" text - FIXED: Use correct coordinates and ensure visibility
        if (particleContainer != null) {
            javafx.geometry.Bounds tileBounds = tile.localToScene(tile.getBoundsInLocal());
            javafx.geometry.Bounds containerBounds = particleContainer.sceneToLocal(tileBounds);
            double textX = containerBounds.getMinX() + containerBounds.getWidth() / 2;
            double textY = containerBounds.getMinY() + containerBounds.getHeight() / 2;
            DamageTextAnimation.createText(particleContainer, "Plant Saved!", 
                                          Color.rgb(51, 255, 51), 
                                          textX, 
                                          textY);
        } else {
            System.err.println("[PesticideSprayEngine] WARNING: particleContainer is null - cannot show 'Plant Saved!' message");
        }
        
        // Green glow on tile - LONGER DURATION (2.5 seconds)
        Glow healGlow = new Glow(0.6);
        javafx.scene.effect.ColorAdjust greenTint = new javafx.scene.effect.ColorAdjust(0, 1.2, 0, 0);
        healGlow.setInput(greenTint);
        tile.setEffect(healGlow);
        
        FadeTransition glowFade = new FadeTransition(Duration.millis(2500), tile); // 2.5 seconds
        glowFade.setFromValue(1.0);
        glowFade.setToValue(1.0); // Don't fade, just remove effect
        glowFade.setOnFinished(e -> {
            tile.setEffect(null);
        });
        glowFade.play();
    }
    
    /**
     * Helper class for mist particles.
     */
    private static class MistParticle {
        double x, y, radius, vx, vy, alpha;
    }
}
