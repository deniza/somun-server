package gss.server.model;

import java.io.IOException;

import org.mapdb.*;

public class PlayerSerializer implements Serializer<Player> {
    
    @Override
    public void serialize(DataOutput2 out, Player player) throws IOException {

        out.writeInt(player.getPlayerId());
        out.writeUTF(player.getName());
        out.writeUTF(player.getPassword());

    }

    @Override
    public Player deserialize(DataInput2 input, int available) throws IOException {
        
        int playerId = input.readInt();
        String name = input.readUTF();    
        String password = input.readUTF();
        
        Player player = new Player(playerId);
        player.setName(name);
        player.setPassword(password);

        return player;

    }
}