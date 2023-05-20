package gss.server.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class GameState {

    private HashMap<String, Object> stateVariables = new HashMap<>();

    protected boolean stateUpdated = false;

    public void setData(String key, Object value) {
        stateVariables.put(key, value);
        updated(true);
    }

    public Object getData(String key) {
        return stateVariables.get(key);
    }

    public void updated(boolean up) {
        stateUpdated = up;
    }

    public String serialize() {
        return new Gson().toJson(stateVariables);
    }

    public void deserialize(String stateJson) {

        this.stateVariables = new Gson().fromJson(stateJson, new TypeToken<Map<String, Object>>(){}.getType());

    }

}
