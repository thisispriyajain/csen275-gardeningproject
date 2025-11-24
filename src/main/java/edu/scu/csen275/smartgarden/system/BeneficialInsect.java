package edu.scu.csen275.smartgarden.system;

import edu.scu.csen275.smartgarden.model.Plant;
import edu.scu.csen275.smartgarden.model.Position;

/**
 * Represents a beneficial insect that helps plants (e.g., pollinators).
 */
public class BeneficialInsect extends Pest {
    private final int pollinationBonus;
    
    /**
     * Creates a new BeneficialInsect.
     */
    public BeneficialInsect(String type, Position position, int pollinationBonus) {
        super(type, 0, position); // No damage
        this.pollinationBonus = pollinationBonus;
    }
    
    /**
     * Creates a default beneficial insect (Honey Bee).
     */
    public BeneficialInsect(Position position) {
        this("Honey Bee", position, 2);
    }
    
    @Override
    public void causeDamage(Plant plant) {
        // Beneficial insects don't cause damage
    }
    
    /**
     * Provides pollination benefit to a plant.
     */
    public void pollinate(Plant plant) {
        if (isAlive && !plant.isDead()) {
            plant.heal(pollinationBonus);
        }
    }
    
    @Override
    public boolean isBeneficial() {
        return true;
    }
    
    public int getPollinationBonus() {
        return pollinationBonus;
    }
}

