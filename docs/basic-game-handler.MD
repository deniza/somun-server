# Writing Your Own GameHandler

This guide will walk you through the process of creating your own `GameHandler` for a simple turn-based game. `GameHandler` is a class that handles the game logic for a specific game. It's responsible for managing the game state, processing player moves, and handling game events. A `GameHandler` is used by the server to manage game sessions and enforce the rules of the game. Game sessions are created when players start a new game instance, and they run until the game is finished.

## Step 1: Create a New GameHandler Class

First, you'll need to create a new class that extends `GameHandler`. This class should be in the `gss.server` package or a subpackage. Here's a basic template:

```java
package gss.server.yourgame;

import gss.server.model.GameHandler;
import gss.server.model.GameSession;

public class YourGameHandler extends GameHandler {

    // Your code goes here

}
```

## Step 2: Override Necessary Methods

The `GameHandler` class has several methods that you can override to customize the behavior of your game. Here are the most important ones:

- `start()`: This method is only called once when the game handler starts. You can use it to initialize your game.

- `onGameCreated(GameSession session)`: This method is called when a new game session is created. You can use it to set up the initial state of the game session. There can be multiple game sessions running at the same time. Game sessions are created when a game instance is created between players. 

- `onPlayerMakeMove(GameSession session, String jsonData)`: This method is called when a player makes a move. You can use it to update the game state based on the player's move. jsonData is the data sent by the player in the form of a JSON string representing the move.

- `onGameFinished(GameSession session)`: This method is called when a game session is finished. You can use it to clean up after a game session. You can use it to update player stats, save game results, etc.

- `onRpcCall(String functionName, String jsonData)`: This method is called when a remote procedure call (RPC) is made. You can use it to handle custom RPCs.

Here's an example of how you might override these methods:

```java
@Override
public void start() {
    // Initialize your game here
}

@Override
public void onGameCreated(GameSession session) {
    // Set up the initial state of the game session here
    // You can use the public and private state of the game session to store game data.
    // The public state is shared between players, while the private state is only visible to the server.
    // You can use the public state to store the game board, player scores, etc.
    // You can use the private state to store game state that should not be visible to players. For example, the server's internal game state.
}

@Override
public void onPlayerMakeMove(GameSession session, String jsonData) {
    // Update the game state based on the player's move here.
    // jsonData is the data sent by the player in the form of a JSON string representing the move.
}

@Override
public void onGameFinished(GameSession session) {
    // Clean up after a game session here
}

@Override
public void onRpcCall(String functionName, String jsonData) {
    // Handle custom RPCs here
}
```

## Step 3: Implement Game Logic

The most important part of your `GameHandler` is the game logic, which you'll implement in the `onPlayerMakeMove` method. This method is called whenever a player makes a move, and it's where you'll update the game state based on the player's move.

The `jsonData` parameter contains the data for the player's move, serialized as a JSON string. You'll need to deserialize this data and use it to update the game state.

Here's an example of how you might implement this method. In this example, we assume that the player's move is represented by a `Move` class that has a `fromJson` method to deserialize the JSON data. You'll need to replace this with your own parsing logic.

```java
@Override
public void onPlayerMakeMove(GameSession session, String jsonData) {
    // Deserialize the player's move
    Move move = Move.fromJson(jsonData);

    // Update the game state based on the player's move
    // ...
}
```

Remember to update both the private and public state of the game session as necessary. The public state is shared between players, while the private state is only visible to the server.

## Step 4: Test Your GameHandler

Finally, you'll need to test your `GameHandler` to make sure it works correctly. You can do this by creating a new game session and making moves to see how the game state changes. Use one of the client libraries to connect to the server and interact with your game.

Remember to check both the private and public state of the game session to make sure they're being updated correctly.

That's it! You've now created your own basic `GameHandler`. You can now customize it further to add more game logic, handle different types of moves, and more. Have fun creating your game!