package com.amberj.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventEmitter {

    private final Map<String, List<Consumer<Object>>> listeners = new HashMap<>();

    public enum EventType {
        PROJECT_CHANGED("project-changed");

        public final String value;
        EventType(String s) {
            this.value = s;
        }
    }

    /**
     * Registers a listener for the specified event type.
     *
     * @param event the event type
     * @param listener the listener to register
     */
    public void on(String event, Consumer<Object> listener) {
        System.out.println(event);
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Registers a listener for the specified event type that will be called only once.
     *
     * @param event the event type
     * @param listener the listener to register
     */
    public void once(String event, Consumer<Object> listener) {
        Consumer<Object> wrapper = new Consumer<>() {
            @Override
            public void accept(Object data) {
                listener.accept(data);
                off(event, this);  // Remove the listener after execution
            }
        };
        on(event, wrapper);
    }

    /**
     * Removes a listener for the specified event type.
     *
     * @param event the event type
     * @param listener the listener to remove
     */
    public void off(String event, Consumer<Object> listener) {
        List<Consumer<Object>> eventListeners = listeners.get(event);
        if (eventListeners != null) {
            eventListeners.remove(listener);
            if (eventListeners.isEmpty()) {
                listeners.remove(event);
            }
        }
    }

    /**
     * Emits an event, calling all listeners registered for the event type.
     *
     * @param event the event type
     * @param data the data to pass to listeners
     */
    public void emit(EventType event, Object data) {
        List<Consumer<Object>> eventListeners = listeners.get(event.value);
        if (eventListeners != null) {
            for (Consumer<Object> listener : new ArrayList<>(eventListeners)) {
                listener.accept(data);
            }
        }
    }

    /**
     * Removes all listeners for a specific event type, or for all events if no type is specified.
     *
     * @param event the event type (optional)
     */
    public void removeAllListeners(String event) {
        if (event == null) {
            listeners.clear();
        } else {
            listeners.remove(event);
        }
    }
}
