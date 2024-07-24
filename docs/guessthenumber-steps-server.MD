# How to Create Guess The Number Game Step by Step (Server)

## Step 1: Create the Game Handler Class

First, create the `GuessTheNumberGameHandler` class extending the `GameHandler` class.

```java
public class GuessTheNumberGameHandler extends GameHandler {
}
```

## Step 2: Override Necessary Methods

Override the most needed methods to run our game logic.

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

## Step 3: Initialize the Game State

We select a random number between 1 and 100 for each game started. We do this inside the `onGameCreated` function. We use the private state of the `GameSession` instance to achieve this functionality. The private state is kept secret from the players and will be serialized to the database.

```java
@Override
public void onGameCreated(GameSession session) {
    GameState privateState = session.getPrivateState();
    privateState.setData("numberToFind", new Random().nextInt(100) + 1);
}
```

## Step 4: Handle Player Moves

The `onPlayerMakeMove` function will be called each time one of the clients makes a move. A move is just a simple JSON string containing the data that the client passes to the server. In our case, this JSON string is a simple key-value pair like the following:

```json
{"number": 23}
```

We can use a JSON library such as Gson to parse this JSON string and get the number as an integer.

```java
JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
int number = jsonObject.get("number").getAsInt();
```

Here is the updated `onPlayerMakeMove` function to achieve this.

```java
@Override
public void onPlayerMakeMove(GameSession session, String jsonData) {
    // parse incoming jsonData and get the number that the player just guesses
    JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
    int number = jsonObject.get("number").getAsInt();
}
```

## Step 5: Compare Player Guess with Secret Number

We need to check if the player's guess is correct. To accomplish this, we compare the number that the player sends with the secret number for this game session. Remember, we kept the secret number in the private state of this session. We get this secret number from the private state and compare it with the player's guess.

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
    } else {
        // wrong guess!
    }
}
```

## Step 6: Handle Correct Guess

If the guess is correct, it is an end game situation. We set the required win state on the `GameSession` object to tell the Somun server the game is finished. Here is the updated code fragment:

```java
if (number == numberToFind) {
    // Player wins!

    // Update session data to reflect the winner and that the game is completed
    session.setWinner(session.getTurnOwner());
    session.setCompleted(true);
} else {
    // wrong guess!
}
```

## Step 7: Notify Clients of Game Completion

We should also tell the clients that the game is finished. We use the shared public state to accomplish this. Remember: public state is shared as a JSON object among players. You can use public state to synchronize data between server and clients. We set both `numberToFind` and `winner` state variables to tell the clients who the winner is and what the number was. Here is the updated code fragment.

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
} else {
    // wrong guess!
}
```

## Step 8: Handle Incorrect Guess

If the guess is wrong, the game should continue. We use a public state variable called "target" to tell the clients if the guess is too high or too low. If target is set to 1, that means the player's last guess is too low; if it is -1, the last guess is too high. This is the complete code for the `onPlayerMakeMove` function:

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
        // Player wins!

        // Update session data to reflect the winner and that the game is completed
        session.setWinner(session.getTurnOwner());
        session.setCompleted(true);

        // Update public state
        // Public state is shared with all players
        GameState publicState = session.getPublicState();
        publicState.setData("numberToFind", numberToFind);
        publicState.setData("winner", session.getWinner().getPlayerId());
    } else {
        // Update public state and continue the game.
        // This state will be sent to all players.
        GameState publicState = session.getPublicState();
        publicState.setData("target", number > numberToFind ? -1 : 1);
    }
}
```

## Final Thoughts

This is a complete basic sample of the "Guess the Number" game server-side implementation using the Somun open-source game server. All the client authentication, state synchronization, state persistence to the database, and network communication parts are taken care of by the Somun game server stack.

To have a fully running game, we now have to code the client-side implementation of this game. Follow [How to Create Guess The Number Game Step by Step (Client)](guessthenumber-steps-client.MD) for the next steps.
