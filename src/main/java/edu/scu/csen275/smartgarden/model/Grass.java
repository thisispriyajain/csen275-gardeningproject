package edu.scu.csen275.smartgarden.model;

/**
 * Represents grass ground cover in the garden.
 * Grass spreads quickly and has minimal requirements.
 */
public class Grass extends Plant {
    private static final int DEFAULT_LIFESPAN = 90; // days
    private static final int WATER_REQ = 40;
    private static final int SUNLIGHT_REQ = 50;
    private static final int MIN_TEMP = 8;
    private static final int MAX_TEMP = 32;
    private static final int PEST_RESISTANCE = 4;
    private static final int GROWTH_DURATION = 4; // days per stage
    
    private int coverage; // percentage
    
    public Grass(Position position) {
        super("Grass", position, DEFAULT_LIFESPAN, WATER_REQ, SUNLIGHT_REQ,
              MIN_TEMP, MAX_TEMP, PEST_RESISTANCE);
        this.coverage = 10;
    }
    
    @Override
    public void advanceDay() {
        super.advanceDay();
        // Grass coverage increases over time
        if (!isDead() && coverage < 100) {
            coverage = Math.min(100, coverage + 2);
        }
    }
    
    @Override
    public int getGrowthDuration() {
        return GROWTH_DURATION;
    }
    
    public int getCoverage() {
        return coverage;
    }
}

