package net.steepout.scriptit;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class ScriptEvent {

    private Map<String, Object> eventContext;

    protected static Map<String, Object> createMap(Pair<String, Object>... attrs) {
        Map<String, Object> map = new HashMap<>();
        for (Pair<String, Object> p : attrs) {
            map.put(p.key, p.value);
        }
        return map;
    }

    protected static class Pair<K, V> {
        K key;

        V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    /**
     * Construct a naive event
     *
     * @param type the type of event, must be class-unique
     */
    protected ScriptEvent(String type) {
        this(type, new HashMap<>());
    }

    protected ScriptEvent(String type, Map<String, Object> context) {
        eventContext = context;
        eventContext.put("eventType", type);
    }

    public Map<String, Object> getContext() {
        return Collections.unmodifiableMap(eventContext);
    }

    @SuppressWarnings("all")
    public <T> Optional<T> get(String key) {
        try {
            return Optional.ofNullable((T) eventContext.get(key));
        } catch (ClassCastException | NullPointerException e) {
            return Optional.empty();
        }
    }

    public boolean hasKey(String key) {
        return get(key).isPresent();
    }

}
