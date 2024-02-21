package gss.server.manager;

import gss.GssLogger;

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

        if (hook instanceof AuthHook_loginUsingIdPassword) {

            AuthHook_loginUsingIdPassword hook_loginUsingIdPassword = (AuthHook_loginUsingIdPassword) hook;

            Consumer<Hook> function = hooks.get(hook_loginUsingIdPassword.getClass());
            if (function != null) {
                function.accept(hook_loginUsingIdPassword);
            }

        }
    }

    public static abstract class Hook {
    }

    public static class AuthHook_loginUsingIdPassword extends Hook {

        public int playerId;
        public String password;

        public AuthHook_loginUsingIdPassword() {
        }

        public AuthHook_loginUsingIdPassword(int playerId, String password) {
            this.playerId = playerId;
            this.password = password;
        }

    }

}
