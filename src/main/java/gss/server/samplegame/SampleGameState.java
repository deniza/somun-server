package gss.server.samplegame;

import gss.server.model.GameState;
import com.google.gson.Gson;

public class SampleGameState extends GameState {
    
    protected int numberToFind;

    @Override
    public void loadState(String state) {

        Gson gson = new Gson();
        SampleGameState savedState = gson.fromJson(state, SampleGameState.class);
        
        this.numberToFind = savedState.numberToFind;

    }

    @Override
    public String saveState() {

        Gson gson = new Gson();
        return gson.toJson(this);

    }

}
