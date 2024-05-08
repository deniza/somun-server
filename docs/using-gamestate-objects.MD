# Using GameState Objects in Game Flow

The `GameState` object is a crucial part of every project. It is used to manage the state of a game session, including both public and private data.
Public state is shared with all players, while private state is only visible to server-side code. The public state is typically used to store information that all players need to know, such as the game board, while the private state is used to store information that only the server needs to know, such as secret information or player hands.
We use a sample class that extends GameHandler which is called `SampleGameHandler` to demonstrate how to use `GameState` objects in the context of game flow.

## Initialization

At the start of a game session, `GameState` objects for both the public and private states are already created and initialized to an empty state. This is done when a `GameSession` object is created.
You can access the `GameState` objects using the `getPublicState` and `getPrivateState` methods of the `GameSession` object. Here is an example of how you can initialize the `GameState` objects in the `SampleGameHandler`'s `onGameCreated` method: 

```java
@Override
public void onGameCreated(GameSession session) {

    GameState privateState = session.getPrivateState();
    privateState.setData("game-secret-data", new Random().nextInt(100) + 1);
    session.setPrivateState(privateState);

}
```

## Updating the State

During the game, the `GameState` objects are updated to reflect the changes in the game. This is typically done in the `SampleGameHandler`'s `onPlayerMakeMove` method:

```java
@Override
public void onPlayerMakeMove(GameSession session, String jsonData) {
    // Update the game state based on the player's move
    // This will depend on your game's rules
    GameState publicState = session.getPublicState();
    GameState privateState = session.getPrivateState();
    // Update the states...
    publicState.setData("some-player-data", "some-value");
    privateState.setData("some-private-game-data", "some-private-value");

    // After each turn, the updated GameState objects are serialized and stored in the database automatically.
    // This ensures that the game state is preserved even if the game session is interrupted.
    // public state is sent to all players when the turn owner changes.
    // This ensures that all players are aware of the current state of the game as it progresses.

}
```

This method also includes the serialization of the `GameState` objects after each turn. The updated `GameState` objects are automatically serialized and stored in the database, ensuring that the game state is preserved even if the game session is interrupted.

When the turn owner changes, the public state is sent to all players. This ensures that all players are aware of the current state of the game as it progresses.

## Game Completion

At the end of a game session, the final `GameState` objects are typically stored for record-keeping. This is done in the `SampleGameHandler`'s `onGameFinished` method:

```java
@Override
public void onGameFinished(GameSession session) {
    // Change the final state of the game
    GameState privateState = session.getPrivateState();
    privateState.setData("game-finished-private-data", "some-value");
}
```

Remember that the exact usage of `GameState` objects will depend on the rules of your game. The above examples are just a general guide.