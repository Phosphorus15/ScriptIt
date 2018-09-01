package net.steepout.scriptit;

import java.util.Map;
import java.util.Optional;

public interface ScriptEvent {

    Map<String, Object> getContext();

    <T> Optional<T> get(String key);

    String getEventType();

    boolean hasKey(String key);

}
