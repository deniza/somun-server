# How to Create Guess The Number Game Step by Step (Server)

First create the GuessTheNumberGameHandler class extending GameHandler class

```java
public class GuessTheNumberGameHandler extends GameHandler {
}
```

Override most needed methods to run our game logic.

```java
public class GuessTheNumberGameHandler extends GameHandler {

    
    @Override
    public void onGameCreated(GameSession session) {
    }    
    
    @Override
    public void onPlayerMakeMove(GameSession session, String jsonData) {
    }
    
    
}
```

We select a random number between 1 and 100 for each game started. We do this inside onGameCreated function. We use private state of the GameSession instance of the created game to achieve this functionality. Private state is kept secret from the players and will be serialized to database.

```java
    @Override
    public void onGameCreated(GameSession session) {

        GameState privateState = session.getPrivateState();
        privateState.setData("numberToFind", new Random().nextInt(100) + 1);

    }
```

onPlayerMakeMove function will be called each time one of the clients makes a move. A move is just a simple json string containing the data that client passes to server. In our case this json string is a simple key, value pair like following:

```json
{"number": 23}
```

We can use a json library such as Gson to parse this json string and get the number as an integer.

```java
JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
int number = jsonObject.get("number").getAsInt();
```

Here is the updated onPlayerMakeMove function to achieve this.

```java
@Override
public void onPlayerMakeMove(GameSession session, String jsonData) {

    // parse incoming jsonData and get the number that the player just guesses
    JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
    int number = jsonObject.get("number").getAsInt();
    
}
```
Now we must check if the players guess is correct. To accomplish this we must compare the number that the player sends with the secret number for this game session. Remember, we kept the secret number in private state of this session. We get this secret number from the private state and compare it with the players's guess.

```java
@Override
public void onPlayerMakeMove(GameSession session, String jsonData) {

    // parse incoming jsonData and get the number that the player just guesses
    JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
    int number = jsonObject.get("number").getAsInt();
    
    // Get the number to find from the private state
    GameState privateState = session.getPrivateState();
    int numberToFind = privateState.getInteger("numberToFind");
    
    if (number == numberToFind) {
        // correct guess!
    }
    else {
        // wrong guess!
    }
    
}
```

If the guess is correct, it is a end game situation. We set required win state on the game session object to tell the Somun server the gam is just finished. Here is the updated code fragment:

```java
    if (number == numberToFind) {
        // Player wins!

        // Update session data to reflect the winner and that the game is completed
        session.setWinner(session.getTurnOwner());
        session.setCompleted(true);
    }
    else {
        // wrong guess!
    }
```

We should also tell the clients that the game is finished. We use the shared public state to accomplish this. Remenber: public state is shared as a json object among players. You can use public state to synchronize data between server and clients. We set both numberToFind and winner state variables to tell the clients who is the winner and what was the number we are guessing. Here is the updated code fragment.

```java
    if (number == numberToFind) {
        // Player wins!

        // Update session data to reflect the winner and that the game is completed
        session.setWinner(session.getTurnOwner());
        session.setCompleted(true);
        
        // Update public state
        // Public state is shared with all players
        GameState publicState = session.getPublicState();
        publicState.setData("numberToFind", numberToFind);
        publicState.setData("winner", session.getWinner().getPlayerId());
        
    }
    else {
        // wrong guess!
    }
```

If the guess is wrong, the game should continue. We use a public state variable called "target" to tell the clients if the guess is too high or too low. If target is set to 1, that means player's last guess is too low, if it is -1, the last guess is too high. This is the complete code for the onPlayerMakeMove function:

```java
@Override
public void onPlayerMakeMove(GameSession session, String jsonData) {

    // parse incoming jsonData and get the number that the player just guesses
    JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
    int number = jsonObject.get("number").getAsInt();
    
    // Get the number to find from the private state
    GameState privateState = session.getPrivateState();
    int numberToFind = privateState.getInteger("numberToFind");

    if (move.number == numberToFind) {

        // Player wins!

        // Update session data to reflect the winner and that the game is completed
        session.setWinner(session.getTurnOwner());
        session.setCompleted(true);

        // Update public state
        // Public state is shared with all players
        GameState publicState = session.getPublicState();
        publicState.setData("numberToFind", numberToFind);
        publicState.setData("winner", session.getWinner().getPlayerId());

    }
    else {

        // Update public state and continue the game.
        // This state will be sent to all players.

        GameState publicState = session.getPublicState();
        publicState.setData("target", move.number > numberToFind ? -1 : 1);

    }
    
}
```

## Final Toughts

So this is a complete basic sample guess the number game server side implementation using Somun open source game server. All the client authentication, state synchronization, state persistence to database and network communication parts are taking care of by Somun game server stack.

To have a fully running game we now have to code client side of the impelemtation of this game. Follow [How to Create Guess The Number Game Step by Step (Client)](docs/guessthenumber-steps-client.MD).