package gss.server.model;

public abstract class GameState {
    
    protected boolean stateUpdated = false;

    public void updated(boolean up) {
        stateUpdated = up;
    }

    public String saveState() {
        return "";        
    }

    public void loadState(String state) {        
    }

}
