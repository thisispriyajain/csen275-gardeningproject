package edu.scu.csen275.smartgarden.model;

/**
 * Represents a tree in the garden.
 * Trees have very long lifespans and slow growth but are hardy.
 */
public class Tree extends Plant {
    private static final int DEFAULT_LIFESPAN = 200; // days
    private static final int WATER_REQ = 50;
    private static final int SUNLIGHT_REQ = 75;
    private static final int MIN_TEMP = 5;
    private static final int MAX_TEMP = 35;
    private static final int PEST_RESISTANCE = 6;
    private static final int GROWTH_DURATION = 15; // days per stage
    
    private int height; // in units
    
    public Tree(Position position) {
        super("Tree", position, DEFAULT_LIFESPAN, WATER_REQ, SUNLIGHT_REQ,
              MIN_TEMP, MAX_TEMP, PEST_RESISTANCE);
        this.height = 1;
    }
    
    @Override
    public void advanceDay() {
        super.advanceDay();
        // Trees grow taller over time
        if (getDaysAlive() % 10 == 0 && !isDead()) {
            height++;
        }
    }
    
    @Override
    public int getGrowthDuration() {
        return GROWTH_DURATION;
    }
    
    public int getHeight() {
        return height;
    }
}

