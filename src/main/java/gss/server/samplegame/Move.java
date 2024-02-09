package gss.server.samplegame;

import gss.server.util.JsonHelper;

public class Move {    
    
    public int number;

    public Move(int number) {
        this.number = number;
    }

    public static Move fromJson(String json) {
        return JsonHelper.fromJson(json, Move.class);
    }

    public String toJson() {
        return JsonHelper.toJson(this);
    }

}
