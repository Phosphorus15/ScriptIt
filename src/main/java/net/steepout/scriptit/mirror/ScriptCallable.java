package net.steepout.scriptit.mirror;

public interface ScriptCallable {

    Object call(Object... args);

    boolean isMethod();

}
