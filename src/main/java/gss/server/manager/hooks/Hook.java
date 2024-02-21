package gss.server.manager.hooks;

public abstract class Hook {

    public static class AuthHook_loginUsingIdPassword extends Hook {

        public int playerId;
        public String password;

        public AuthHook_loginUsingIdPassword(int playerId, String password) {
            this.playerId = playerId;
            this.password = password;
        }

    }

}
