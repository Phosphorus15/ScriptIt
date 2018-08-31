package net.steepout.scriptit.impl;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.steepout.scriptit.ScriptEvent;
import net.steepout.scriptit.ScriptPlugin;
import net.steepout.scriptit.ScriptPluginManager;
import net.steepout.scriptit.events.PluginLoadEvent;
import net.steepout.scriptit.misc.ScriptPluginException;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class JSPluginManager extends ScriptPluginManager {

    ScriptEngine engine;

    String initialBindings;

    Map<String, List<ScriptObjectMirror>> eventBus = new HashMap<>();

    public JSPluginManager() {
        super("javascript", new HashMap<>());
        engine = Objects.requireNonNull(new ScriptEngineManager().getEngineByName("js"));
        InputStream in = JSPluginManager.class.getResourceAsStream("/init.js");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String str;
        StringBuilder buffer = new StringBuilder();
        try {
            while ((str = reader.readLine()) != null) buffer.append(str).append('\n');
            reader.close();
        } catch (IOException e) {
            throw new ScriptPluginException(e);
        }
        initialBindings = buffer.toString();
    }

    @Override
    public Optional<Object> getScriptEngine() {
        return Optional.of(engine);
    }

    @Override
    public void setupEngine() {

    }

    public void subscribe(String type, Object callback) {
        if (callback instanceof ScriptObjectMirror) {
            eventBus.compute(type, (s, list) -> {
                if (list == null) list = new ArrayList<>();
                list.add((ScriptObjectMirror) callback);
                return list;
            });
        }
    }

    @Override
    public String registerPlugin(String source, String script) {
        Bindings bindings = engine.createBindings();
        bindings.put("jjs_pManager", this);
        bindings.put("jjs_bindings", bindings);
        try {
            engine.eval(initialBindings, bindings);
            engine.eval(script, bindings);
        } catch (ScriptException e) {
            throw new ScriptPluginException(e);
        }
        String id = Objects.requireNonNull(bindings.get("jjs_id"), "a plugin script should always invoke plugin(name) for registration").toString();
        ScriptPlugin pluginObject = new JavaxScriptPlugin(bindings, id
                , bindings.get("jjs_name").toString(), bindings.get("jjs_version").toString(),
                "javascript", source, script);
        registeredPlugins.put(id, pluginObject);
        bindings.put("self", pluginObject);
        handleEvent(new PluginLoadEvent(this, pluginObject));
        return id;
    }

    @Override
    public void handleEvent(ScriptEvent event) {
        Optional<String> type = event.get("eventType");
        if (type.isPresent()) {
            List<ScriptObjectMirror> bus = eventBus.get(type.get());
            if (bus != null)
                for (ScriptObjectMirror mirror : bus) {
                    mirror.call(null, event);
                }
        }
    }
}
