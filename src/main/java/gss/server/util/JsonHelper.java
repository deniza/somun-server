package gss.server.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class JsonHelper {

    public static String hashmapToJson(HashMap<String, Object> map) {
        return new Gson().toJson(map);
    }

    public static HashMap<String, Object> jsonToHashmap(String json) {
        return new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>(){}.getType());
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }
    public static <T> T fromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

}
