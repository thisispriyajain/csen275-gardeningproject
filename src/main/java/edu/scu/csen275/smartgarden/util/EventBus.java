package edu.scu.csen275.smartgarden.util;

import javafx.application.Platform;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Event bus for decoupled communication between API and UI.
 * Thread-safe pub/sub pattern implementation.
 * 
 * Allows API to publish events that UI can subscribe to, enabling
 * real-time visual updates when API methods are called.
 */
public class EventBus {
    private static final Map<String, List<Consumer<Object>>> listeners = new ConcurrentHashMap<>();
    
    /**
     * Subscribe to an event type.
     * @param eventType The event type to listen for (e.g., "RainEvent")
     * @param listener The handler function
     */
    public static void subscribe(String eventType, Consumer<Object> listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
    }
    
    /**
     * Unsubscribe from an event type.
     */
    public static void unsubscribe(String eventType, Consumer<Object> listener) {
        List<Consumer<Object>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }
    
    /**
     * Publish an event to all subscribers.
     * UI events are dispatched on JavaFX thread automatically.
     * @param eventType The event type
     * @param event The event object
     */
    public static void publish(String eventType, Object event) {
        List<Consumer<Object>> eventListeners = listeners.getOrDefault(eventType, Collections.emptyList());
        for (Consumer<Object> listener : eventListeners) {
            // Check if we're on JavaFX thread
            if (Platform.isFxApplicationThread()) {
                listener.accept(event);
            } else {
                // Dispatch to JavaFX thread for UI updates
                Platform.runLater(() -> listener.accept(event));
            }
        }
    }
    
    /**
     * Clear all subscribers (useful for testing).
     */
    public static void clear() {
        listeners.clear();
    }
}

