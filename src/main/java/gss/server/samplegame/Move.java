package gss.server.samplegame;

import com.google.gson.Gson;

public class Move {    
    
    public int number;

    public static Move fromJson(String json) {
        return new Gson().fromJson(json, Move.class);
    }

}
