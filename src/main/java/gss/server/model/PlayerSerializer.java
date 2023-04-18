package gss.server.model;

import java.io.IOException;
import java.util.ArrayList;

import org.mapdb.*;

public class PlayerSerializer implements Serializer<Player> {
    
    @Override
    public void serialize(DataOutput2 out, Player player) throws IOException {

        out.writeInt(player.getPlayerId());
        out.writeUTF(player.getName());
        out.writeUTF(player.getPassword());
        
        ArrayList<Integer> gameIds = player.getGameIds();
        out.writeInt(gameIds.size());
        for (int gid : gameIds) {
            out.writeInt(gid);
        }

    }

    @Override
    public Player deserialize(DataInput2 input, int available) throws IOException {
        
        int playerId = input.readInt();
        String name = input.readUTF();    
        String password = input.readUTF();
        
        Player player = new Player(playerId);
        player.setName(name);
        player.setPassword(password);

        int gameIdCount = input.readInt();
        ArrayList<Integer> gameIds = new ArrayList<>(gameIdCount);
        for (int i=0;i<gameIdCount;++i) {
            gameIds.add(input.readInt());
        }

        player.setGameIds(gameIds);

        return player;

    }
}