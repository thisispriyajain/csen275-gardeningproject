package edu.scu.csen275.smartgarden.ui;

import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sprinkler animation engine that shows water being sprinkled when watering zones.
 * Creates water droplets spraying from sprinkler positions.
 */
public class SprinklerAnimationEngine {
    private static final Random random = new Random();
    private static final double ANIMATION_DURATION = 3000; // 3 seconds
    private static final double FRAME_RATE = 60.0; // 60 FPS
    private static final double FRAME_INTERVAL = 1000.0 / FRAME_RATE; // ~16.67ms
    
    /**
     * Animates sprinkler water for a specific zone.
     */
    public static void animateSprinkler(int zoneId, List<AnimatedTile> tiles, Pane container) {
        if (tiles.isEmpty() || container == null) {
            return;
        }
        
        
        // Create animation canvas overlay
        Canvas animationCanvas = new Canvas();
        animationCanvas.setMouseTransparent(true);
        container.getChildren().add(animationCanvas);
        
        // Bind canvas size to container
        animationCanvas.widthProperty().bind(container.widthProperty());
        animationCanvas.heightProperty().bind(container.heightProperty());
        
        // Create sprinkler water particles for each tile
        List<SprinklerParticle> particles = new ArrayList<>();
        for (AnimatedTile tile : tiles) {
            if (tile.getScene() == null) continue;
            
            Bounds tileBounds = tile.localToScene(tile.getBoundsInLocal());
            if (tileBounds == null || tileBounds.isEmpty()) continue;
            
            Bounds containerBounds = container.localToScene(container.getBoundsInLocal());
            if (containerBounds == null || containerBounds.isEmpty()) continue;
            
            // Convert to canvas coordinates
            double centerX = (tileBounds.getMinX() - containerBounds.getMinX()) + tileBounds.getWidth() / 2;
            double centerY = (tileBounds.getMinY() - containerBounds.getMinY()) + tileBounds.getHeight() / 2;
            
            // Create multiple water particles per tile (sprinkler effect)
            int particleCount = 8 + random.nextInt(5); // 8-12 particles per tile
            for (int i = 0; i < particleCount; i++) {
                SprinklerParticle particle = new SprinklerParticle();
                particle.startX = centerX;
                particle.startY = centerY;
                particle.x = centerX;
                particle.y = centerY;
                
                // Random angle for sprinkler spray (arc pattern)
                double angle = (Math.PI / 4) + (random.nextDouble() * Math.PI / 2); // 45-135 degrees
                double velocity = 2 + random.nextDouble() * 3; // 2-5 pixels per frame
                particle.vx = Math.cos(angle) * velocity;
                particle.vy = Math.sin(angle) * velocity;
                
                particle.size = 3 + random.nextDouble() * 2; // 3-5 pixels
                particle.opacity = 0.7 + random.nextDouble() * 0.3; // 0.7-1.0
                particle.lifetime = 0;
                particle.maxLifetime = 30 + random.nextInt(20); // 30-50 frames
                
                particles.add(particle);
            }
        }
        
        
        // Create animation timeline (60 FPS)
        Timeline animationTimeline = new Timeline(
            new KeyFrame(Duration.millis(FRAME_INTERVAL), e -> {
                GraphicsContext gc = animationCanvas.getGraphicsContext2D();
                double width = animationCanvas.getWidth();
                double height = animationCanvas.getHeight();
                
                if (width <= 0 || height <= 0) {
                    return;
                }
                
                gc.clearRect(0, 0, width, height);
                
                // Update and draw particles
                boolean hasActiveParticles = false;
                for (SprinklerParticle particle : new ArrayList<>(particles)) {
                    particle.lifetime++;
                    
                    if (particle.lifetime < particle.maxLifetime) {
                        hasActiveParticles = true;
                        
                        // Update position
                        particle.x += particle.vx;
                        particle.y += particle.vy;
                        
                        // Apply gravity
                        particle.vy += 0.3;
                        
                        // Fade out over time
                        double progress = (double)particle.lifetime / particle.maxLifetime;
                        double currentOpacity = particle.opacity * (1.0 - progress);
                        
                        // Draw water droplet
                        gc.setFill(Color.rgb(135, 206, 250, currentOpacity)); // Light blue
                        gc.fillOval(
                            particle.x - particle.size / 2,
                            particle.y - particle.size / 2,
                            particle.size,
                            particle.size
                        );
                    } else {
                        particles.remove(particle);
                    }
                }
                
                // Remove canvas when all particles are gone
                if (!hasActiveParticles) {
                    container.getChildren().remove(animationCanvas);
                }
            })
        );
        animationTimeline.setCycleCount(Animation.INDEFINITE);
        
        // Stop after animation duration
        Timeline stopTimeline = new Timeline(
            new KeyFrame(Duration.millis(ANIMATION_DURATION), e -> {
                animationTimeline.stop();
                container.getChildren().remove(animationCanvas);
            })
        );
        
        animationTimeline.play();
        stopTimeline.play();
    }
    
    /**
     * Data class for a sprinkler water particle.
     */
    private static class SprinklerParticle {
        double startX, startY; // Starting position
        double x, y; // Current position
        double vx, vy; // Velocity
        double size; // Particle size
        double opacity; // Initial opacity
        int lifetime; // Current lifetime in frames
        int maxLifetime; // Maximum lifetime in frames
    }
}

