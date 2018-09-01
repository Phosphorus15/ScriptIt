package net.steepout.scriptit.events;

import net.steepout.scriptit.ScriptEvent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <b>This class is under construction, any invocation would be returned with null</b>
 * <p>
 * Considered that most of the script languages are duck-typing
 * <p>
 * We have enough reason to 'create' a custom event without a explicit hosted class (but an interface)
 * <p>
 * Any type of event created round here would be automatically submitted to event systems (hell yeah)
 */
class CustomEventsFactory {

    public static Class<? extends ScriptEvent> createEventType(String typeName, String alias, Class<?> interfaze, InvocationHandler redundantHandler) {
        Proxy.newProxyInstance(ClassLoader.getSystemClassLoader()
                , new Class<?>[]{interfaze, ScriptEvent.class}
                , new MirrorHandler(interfaze, redundantHandler));
        return null;
    }

    private static class MirrorHandler implements InvocationHandler {

        Class<?> mirrorObject;

        InvocationHandler redundantHandler;

        public MirrorHandler(Class<?> mirrorObject, InvocationHandler redundantHandler) {
            this.mirrorObject = mirrorObject;
            this.redundantHandler = redundantHandler;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return null;
        }
    }

}
