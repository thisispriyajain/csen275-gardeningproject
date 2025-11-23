package edu.scu.csen275.smartgarden.model;

/**
 * Represents an aromatic herb in the garden.
 * Herbs are low-maintenance and pest-resistant.
 */
public class Herb extends Plant {
    private static final int DEFAULT_LIFESPAN = 75; // days
    private static final int WATER_REQ = 35;
    private static final int SUNLIGHT_REQ = 60;
    private static final int MIN_TEMP = 12;
    private static final int MAX_TEMP = 30;
    private static final int PEST_RESISTANCE = 7; // Very resistant
    private static final int GROWTH_DURATION = 6; // days per stage
    
    private final String herbType;
    private int aromaStrength;
    
    public Herb(Position position, String herbType) {
        super("Herb", position, DEFAULT_LIFESPAN, WATER_REQ, SUNLIGHT_REQ,
              MIN_TEMP, MAX_TEMP, PEST_RESISTANCE);
        this.herbType = herbType;
        this.aromaStrength = 50;
    }
    
    public Herb(Position position) {
        this(position, "Basil");
    }
    
    @Override
    public void advanceDay() {
        super.advanceDay();
        // Aroma strength increases with maturity
        if (!isDead() && getGrowthStage().getStageNumber() >= 2) {
            aromaStrength = Math.min(100, aromaStrength + 1);
        }
    }
    
    @Override
    public int getGrowthDuration() {
        return GROWTH_DURATION;
    }
    
    public String getHerbType() {
        return herbType;
    }
    
    public int getAromaStrength() {
        return aromaStrength;
    }
    
    @Override
    public String getPlantType() {
        return herbType;
    }
}

