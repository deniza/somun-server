package gss.server.manager.hooks;

import java.util.HashMap;
import java.util.function.Consumer;

public class HookManager {

    private static HookManager instance;

    private final HashMap<Class, Consumer<Hook>> hooks = new HashMap<>();

    private HookManager() {
    }

    public static HookManager get() {
        if (instance == null) {
            instance = new HookManager();
        }
        return instance;
    }

    public void addHook(Class hookClass, Consumer<Hook> function) {
        hooks.put(hookClass, function);
    }

    public void removeHook(Hook hook) {
        hooks.remove(hook);
    }

    public void processHook(Hook hook) {

        Consumer<Hook> function = hooks.get(hook.getClass());
        if (function != null) {
            function.accept(hook);
        }

    }

}
