package gss.server.samplegame;

import com.google.gson.Gson;

public class Move {    
    
    public int number;

    public Move(int number) {
        this.number = number;
    }

    public static Move fromJson(String json) {
        return new Gson().fromJson(json, Move.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
