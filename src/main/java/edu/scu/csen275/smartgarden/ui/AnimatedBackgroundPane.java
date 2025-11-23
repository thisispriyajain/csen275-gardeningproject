package edu.scu.csen275.smartgarden.ui;

import javafx.animation.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Animated background with moving clouds and sunlight rays.
 * Gardenscapes-inspired cheerful sky background.
 */
public class AnimatedBackgroundPane extends Pane {
    private final Canvas backgroundCanvas;
    private final List<Cloud> clouds;
    private final List<SunRay> sunRays;
    private final Random random;
    private Timeline animationTimeline;
    
    public AnimatedBackgroundPane() {
        this.backgroundCanvas = new Canvas();
        this.clouds = new ArrayList<>();
        this.sunRays = new ArrayList<>();
        this.random = new Random();
        
        // Make canvas fill the entire pane
        backgroundCanvas.widthProperty().bind(this.widthProperty());
        backgroundCanvas.heightProperty().bind(this.heightProperty());
        
        // Initialize background elements
        initializeClouds();
        initializeSunRays();
        
        this.getChildren().add(backgroundCanvas);
        
        // Start animation loop (60 FPS)
        startAnimation();
    }
    
    /**
     * Initializes floating clouds.
     */
    private void initializeClouds() {
        for (int i = 0; i < 4; i++) {
            Cloud cloud = new Cloud();
            cloud.x = random.nextDouble() * 2000 - 200;
            cloud.y = 50 + random.nextDouble() * 150;
            cloud.size = 80 + random.nextDouble() * 60;
            cloud.speed = 0.2 + random.nextDouble() * 0.3;
            cloud.opacity = 0.6 + random.nextDouble() * 0.3;
            clouds.add(cloud);
        }
    }
    
    /**
     * Initializes sunlight rays.
     */
    private void initializeSunRays() {
        for (int i = 0; i < 3; i++) {
            SunRay ray = new SunRay();
            ray.angle = -45 + i * 15; // Spread rays
            ray.opacity = 0.15 + random.nextDouble() * 0.1;
            ray.width = 30 + random.nextDouble() * 20;
            sunRays.add(ray);
        }
    }
    
    /**
     * Starts the animation loop.
     */
    private void startAnimation() {
        animationTimeline = new Timeline(
            new KeyFrame(Duration.millis(1000.0 / 60.0), e -> {
                updateAndDraw();
            })
        );
        animationTimeline.setCycleCount(Animation.INDEFINITE);
        animationTimeline.play();
    }
    
    /**
     * Updates cloud positions and draws everything.
     */
    private void updateAndDraw() {
        GraphicsContext gc = backgroundCanvas.getGraphicsContext2D();
        double width = backgroundCanvas.getWidth();
        double height = backgroundCanvas.getHeight();
        
        // Skip if canvas is not ready
        if (width <= 0 || height <= 0) {
            return;
        }
        
        // Clear canvas
        gc.clearRect(0, 0, width, height);
        
        // Draw gradient sky background
        drawSkyGradient(gc, width, height);
        
        // Draw sunlight rays
        drawSunRays(gc, width, height);
        
        // Update and draw clouds
        for (Cloud cloud : clouds) {
            cloud.x += cloud.speed;
            
            // Loop cloud when it goes off screen
            if (cloud.x > width + 100) {
                cloud.x = -cloud.size - 100;
                cloud.y = 50 + random.nextDouble() * 150;
            }
            
            // Only draw clouds that are on screen or about to enter
            if (cloud.x > -cloud.size - 100 && cloud.x < width + 100) {
                drawCloud(gc, cloud);
            }
        }
    }
    
    /**
     * Draws cheerful gradient sky background.
     */
    private void drawSkyGradient(GraphicsContext gc, double width, double height) {
        // Bright sky gradient (light blue to light green) - make it vibrant and visible
        LinearGradient skyGradient = new LinearGradient(
            0, 0, 0, height,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(135, 206, 235)), // Sky blue - more vibrant
            new Stop(0.4, Color.rgb(176, 224, 230)), // Light sky blue
            new Stop(0.7, Color.rgb(173, 255, 173)), // Light green
            new Stop(1, Color.rgb(144, 238, 144)) // Light green - brighter
        );
        
        gc.setFill(skyGradient);
        gc.fillRect(0, 0, width, height);
    }
    
    /**
     * Draws sunlight rays from top-right.
     */
    private void drawSunRays(GraphicsContext gc, double width, double height) {
        double centerX = width * 0.9;
        double centerY = height * 0.1;
        
        for (SunRay ray : sunRays) {
            gc.save();
            gc.setGlobalAlpha(ray.opacity + Math.sin(System.currentTimeMillis() / 2000.0) * 0.05);
            gc.translate(centerX, centerY);
            gc.rotate(ray.angle);
            
            // Draw ray as a gradient
            LinearGradient rayGradient = new LinearGradient(
                0, 0, ray.width, 0,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 200, 0.3)),
                new Stop(1, Color.rgb(255, 255, 200, 0.0))
            );
            
            gc.setFill(rayGradient);
            gc.fillRect(0, -5, height * 1.5, 10);
            gc.restore();
        }
    }
    
    /**
     * Draws a fluffy cloud - make clouds more visible.
     */
    private void drawCloud(GraphicsContext gc, Cloud cloud) {
        // Make clouds more visible with higher opacity
        double cloudOpacity = Math.max(0.7, cloud.opacity);
        gc.setGlobalAlpha(cloudOpacity);
        gc.setFill(Color.rgb(255, 255, 255, 0.95)); // More opaque white
        
        // Draw cloud as overlapping circles for fluffy effect
        double size = cloud.size;
        // Main cloud body
        gc.fillOval(cloud.x, cloud.y, size * 0.6, size * 0.6);
        gc.fillOval(cloud.x + size * 0.3, cloud.y, size * 0.7, size * 0.7);
        gc.fillOval(cloud.x + size * 0.6, cloud.y, size * 0.6, size * 0.6);
        // Bottom layers
        gc.fillOval(cloud.x + size * 0.2, cloud.y + size * 0.2, size * 0.5, size * 0.5);
        gc.fillOval(cloud.x + size * 0.5, cloud.y + size * 0.2, size * 0.5, size * 0.5);
        
        gc.setGlobalAlpha(1.0);
    }
    
    /**
     * Cloud data structure.
     */
    private static class Cloud {
        double x, y, size, speed, opacity;
    }
    
    /**
     * Sun ray data structure.
     */
    private static class SunRay {
        double angle, opacity, width;
    }
    
    /**
     * Stops animation when pane is removed.
     */
    public void stop() {
        if (animationTimeline != null) {
            animationTimeline.stop();
        }
    }
}

