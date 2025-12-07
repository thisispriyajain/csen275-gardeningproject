package edu.scu.csen275.smartgarden.ui;

import javafx.animation.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Snow animation engine that shows falling snowflakes when weather is SNOWY.
 * Creates continuous snowflakes falling across the entire garden.
 */
public class SnowAnimationEngine {
    private static final Random random = new Random();
    private static Canvas snowCanvas;
    private static Timeline snowTimeline;
    private static List<Snowflake> snowflakes;
    private static boolean isSnowing = false;
    
    /**
     * Starts snow animation across the entire garden.
     */
    public static void startSnow(Pane container) {
        if (isSnowing) {
            return; // Already snowing
        }
        
        isSnowing = true;
        
        // Create snow canvas overlay
        snowCanvas = new Canvas();
        snowCanvas.setMouseTransparent(true);
        container.getChildren().add(snowCanvas);
        
        // Bind canvas size to container
        snowCanvas.widthProperty().bind(container.widthProperty());
        snowCanvas.heightProperty().bind(container.heightProperty());
        
        // Initialize snowflakes
        snowflakes = new ArrayList<>();
        
        // Wait a bit for container to size properly, then initialize
        javafx.animation.PauseTransition initDelay = new javafx.animation.PauseTransition(Duration.millis(100));
        initDelay.setOnFinished(e -> {
            initializeSnowflakes(container);
        });
        initDelay.play();
        
        // Create continuous snow animation (60 FPS)
        snowTimeline = new Timeline(
            new KeyFrame(Duration.millis(16), e -> {
                if (snowCanvas != null && container != null && snowflakes != null) {
                    updateAndDrawSnow(container);
                }
            })
        );
        snowTimeline.setCycleCount(Animation.INDEFINITE);
        snowTimeline.play();
    }
    
    /**
     * Stops snow animation.
     */
    public static void stopSnow(Pane container) {
        if (!isSnowing) {
            return;
        }
        
        isSnowing = false;
        
        if (snowTimeline != null) {
            snowTimeline.stop();
            snowTimeline = null;
        }
        
        if (snowCanvas != null && container != null) {
            container.getChildren().remove(snowCanvas);
            snowCanvas = null;
        }
        
        if (snowflakes != null) {
            snowflakes.clear();
        }
    }
    
    /**
     * Initializes snowflakes across the canvas.
     */
    private static void initializeSnowflakes(Pane container) {
        if (container == null || snowCanvas == null) {
            return;
        }
        
        // Wait for container to have valid size
        double width = container.getWidth();
        double height = container.getHeight();
        
        if (width <= 0 || height <= 0) {
            // Use default size if container not sized yet
            width = 800;
            height = 600;
        }
        
        // Create snowflakes (fewer than rain drops, slower)
        int flakeCount = (int)(width * height / 4000); // Lower density than rain
        flakeCount = Math.max(30, Math.min(150, flakeCount)); // Clamp between 30-150
        
        if (snowflakes == null) {
            snowflakes = new ArrayList<>();
        }
        snowflakes.clear();
        
        for (int i = 0; i < flakeCount; i++) {
            Snowflake flake = new Snowflake();
            flake.x = random.nextDouble() * width;
            flake.y = random.nextDouble() * height; // Start at random Y
            flake.speed = 0.5 + random.nextDouble() * 1.5; // 0.5-2 pixels per frame (slower than rain)
            flake.size = 2 + random.nextDouble() * 4; // 2-6 pixels size
            flake.opacity = 0.6 + random.nextDouble() * 0.4; // 0.6-1.0 opacity
            flake.wobble = random.nextDouble() * Math.PI * 2; // Random starting wobble phase
            flake.wobbleSpeed = 0.02 + random.nextDouble() * 0.03; // Slow horizontal drift
            flake.wobbleAmount = 0.5 + random.nextDouble() * 1.5; // Amount of horizontal movement
            snowflakes.add(flake);
        }
    }
    
    /**
     * Updates and draws all snowflakes.
     */
    private static void updateAndDrawSnow(Pane container) {
        if (snowCanvas == null || container == null || snowflakes == null) {
            return;
        }
        
        GraphicsContext gc = snowCanvas.getGraphicsContext2D();
        double width = snowCanvas.getWidth();
        double height = snowCanvas.getHeight();
        
        if (width <= 0 || height <= 0) {
            return;
        }
        
        // Clear canvas
        gc.clearRect(0, 0, width, height);
        
        // Update and draw each snowflake
        for (Snowflake flake : snowflakes) {
            // Update wobble (horizontal drift)
            flake.wobble += flake.wobbleSpeed;
            
            // Move flake down and add horizontal drift
            flake.y += flake.speed;
            flake.x += Math.sin(flake.wobble) * flake.wobbleAmount;
            
            // Reset if flake goes off screen
            if (flake.y > height) {
                flake.y = -flake.size; // Start from top
                flake.x = random.nextDouble() * width; // Random X position
            }
            
            // Wrap around horizontally
            if (flake.x < 0) {
                flake.x = width;
            } else if (flake.x > width) {
                flake.x = 0;
            }
            
            // Draw snowflake (circle)
            gc.setFill(Color.rgb(255, 255, 255, flake.opacity)); // White
            gc.fillOval(
                flake.x - flake.size / 2, 
                flake.y - flake.size / 2,
                flake.size, 
                flake.size
            );
        }
        
        // Reinitialize if container size changed significantly
        if (Math.abs(width - (snowflakes.isEmpty() ? 0 : width)) > 100) {
            initializeSnowflakes(container);
        }
    }
    
    /**
     * Checks if snow is currently active.
     */
    public static boolean isSnowing() {
        return isSnowing;
    }
    
    /**
     * Data class for a single snowflake.
     */
    private static class Snowflake {
        double x, y; // Position
        double speed; // Pixels per frame (slower than rain)
        double size; // Size of snowflake
        double opacity; // 0.0 to 1.0
        double wobble; // Phase for horizontal wobble
        double wobbleSpeed; // Speed of wobble
        double wobbleAmount; // Amount of horizontal movement
    }
}

