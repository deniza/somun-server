package gss.server.model;

import gss.server.util.JsonHelper;
import java.util.HashMap;

public class GameState {

    private HashMap<String, Object> stateVariables = new HashMap<>();

    protected boolean stateUpdated = false;

    public GameState() {
    }
    public GameState(String stateJson) {
        deserialize(stateJson);
    }

    public void setData(String key, Object value) {
        stateVariables.put(key, value);
        updated(true);
    }

    public Object getData(String key) {
        return stateVariables.get(key);
    }
    public Integer getInteger(String key) {
        Object obj = getData(key);
        if (obj instanceof Integer) {
            return (int) obj;
        }
        else if (obj instanceof Double) {
            return (int) Math.round((Double) obj);
        }
        else {
            return null;
        }
    }

    public String getString(String key) {
        return String.valueOf(getData(key));
    }
    public HashMap<String, Object> getDataMap() {
        return stateVariables;
    }

    public void updated(boolean up) {
        stateUpdated = up;
    }

    public String serialize() {
        return JsonHelper.hashmapToJson(stateVariables);
    }

    public void deserialize(String stateJson) {

        this.stateVariables = JsonHelper.jsonToHashmap(stateJson);

    }

}
