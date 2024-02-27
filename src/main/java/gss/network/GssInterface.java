package gss.network;

import gss.GssLogger;
import gss.server.manager.PlayerManager;
import gss.server.model.Player;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class GssInterface {

    private final HashMap<String, Method> callableMethods = new HashMap<String, Method>();

    public GssInterface() {
        // register all interface methods that are annotated with @GssCallable
        // so that they can be called by the clients
        registerAnnotatedCallableFunctions();
    }

    private void registerAnnotatedCallableFunctions() {
        Class<?> clazz = this.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(GssCallable.class)) {
                String functionName = method.getName();
                callableMethods.put(functionName, method);
            }
            else {

                Class<?> superclass = clazz.getSuperclass();
                if (superclass != null) {
                    try {
                        // try to find the method in the superclass
                        // if it is not found, it will throw a NoSuchMethodException
                        // and we will log a warning
                        superclass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                    }
                    catch (NoSuchMethodException e) {
                        GssLogger.info("GssInterface: method %s in class %s is not annotated with @GssCallable", method, clazz);
                    }
                }

            }
        }
    }

    public Method getCallableMethod(String name) {
        return callableMethods.get(name);
    }

    public void call(GssConnection con, String module, String function, Object... args) {
        if (con != null) {
            con.invokeMethod(module + "_" + function, args);
        }
    }

    public void clientDisconnected(GssConnection con) {
    }

    public void clientConnected(GssConnection con) {
    }

    protected Player getPlayer(GssConnection con) {
        return PlayerManager.get().getPlayer(con);
    }

}
