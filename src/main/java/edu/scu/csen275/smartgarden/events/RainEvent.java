package edu.scu.csen275.smartgarden.events;

/**
 * Event published when rain occurs.
 * Contains the amount of rain in water units.
 */
public class RainEvent {
    private final int amount;
    
    public RainEvent(int amount) {
        this.amount = amount;
    }
    
    public int getAmount() {
        return amount;
    }
}

