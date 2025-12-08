package edu.scu.csen275.smartgarden.events;

/**
 * Event published when temperature changes.
 * Contains temperature in Fahrenheit.
 */
public class TemperatureEvent {
    private final int temperatureFahrenheit;
    
    public TemperatureEvent(int temperatureFahrenheit) {
        this.temperatureFahrenheit = temperatureFahrenheit;
    }
    
    public int getTemperatureFahrenheit() {
        return temperatureFahrenheit;
    }
}

