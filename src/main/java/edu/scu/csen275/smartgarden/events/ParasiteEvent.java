package edu.scu.csen275.smartgarden.events;

import edu.scu.csen275.smartgarden.model.Position;

/**
 * Event published when a parasite attacks a plant.
 * Contains the parasite type and the position of the attacked plant.
 */
public class ParasiteEvent {
    private final String parasiteType;
    private final Position position;
    
    public ParasiteEvent(String parasiteType, Position position) {
        this.parasiteType = parasiteType;
        this.position = position;
    }
    
    public String getParasiteType() {
        return parasiteType;
    }
    
    public Position getPosition() {
        return position;
    }
}

