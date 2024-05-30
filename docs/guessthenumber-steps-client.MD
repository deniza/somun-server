# How to Create Guess The Number Game Step by Step (Client)

This guide covers the client side of our sample "Guess the Number" game written using the open-source game framework Solar2D. It utilizes the `somun-solar2d` client library to handle core functionalities. Most of the source code deals with UI and state management, which is not directly related to understanding the client library. Therefore, we will focus on the code fragments related to the `somun` client implementation and explain how to use them in your projects.

Various states of the game UI are rendered inside the `renderUI` function. The `setState` function is used to change the current state of the game and render the UI accordingly. All functionality is triggered by event handlers of various button widgets.

## Step 1: Import the somun-solar2d Client Library

Download the necessary `somun-solar2d` client library source code Lua files and place them inside a directory called `somun`. Then, import the somun library:

```lua
require("somun.somun")
```

## Step 2: Register Necessary Callback Functions

The `Somun.registerCallback` function is used to register callback handler functions to the `somun` client system. These callback functions are like event handlers called from the server to inform us about the state of the game. The callback mechanism is used extensively in the somun server.

For this sample game project, we need to handle four callback functions:

```lua
Somun.registerCallback("Play_gameCreated", function(...arguments)
    -- function body...
end)

Somun.registerCallback("Play_gameStateUpdated", function(...arguments)
    -- function body...
end)

Somun.registerCallback("Play_turnOwnerChanged", function(...arguments)
    -- function body...
end)

Somun.registerCallback("Play_gameFinished", function(...arguments)
    -- function body...
end)
```

## Step 3: Connect to the Server

Our application starts in the `DISCONNECTED` state. When the user presses the connect button, we try to connect to the somun server using the following `Somun.start` function:

```lua
Somun.start("localhost", 16666, function()
    -- connection successful!
end)
```

We assume that the Somun server is running locally and using the default port, which is 16666. After the connection succeeds, we can do various things such as creating a guest account, logging into an account, and disconnecting from the server. First, let's create a guest account.

## Step 4: Create a Guest Account

The easiest way to create an account on the Somun server is by creating a guest account. We use the `Somun.account.createGuestAccount` function:

```lua
Somun.account.createGuestAccount(function(_playerId, _playerName, _password)
    -- account created
end)
```

Every user on the Somun server has a unique ID called `playerId`. The `createGuestAccount` function calls a callback function upon success, providing `playerId`, `playerName` (randomly generated), and `password` (also randomly generated). We will need this `playerId` and `password` to log into our account next time. The Somun server automatically logs in after creating a new account.

## Step 5: Create a Random Game

Now we are connected to the Somun server. There are plenty of things to do in this state, but for simplicity, we will create a game. Creating a random game is one of the many game creation options in the Somun server. Somun will match random players who request to create random games and start the game session automatically. You can also create games by invitation, but that is for another tutorial. Let's call the `Somun.play.createRandomGame` function to create a random game:

```lua
Somun.play.createRandomGame(0, function(status)
    if status == 1 then
        -- done! waiting for a matching player now...
    end
end)
```

The first parameter of the function denotes `gametype`. There can be many types of games running on the same Somun server. The `gametype` parameter is used to differentiate between different games. In our instance, we only have one type of game, which is the "Guess the Number" game, so we use 0 as `gametype`.

It will take some time for a game to be created. There should be at least one other player who is also registered to create a random game. Somun will match those players, create the game session, and inform us using one of the callback functions mentioned in Step 2. To proceed, run another instance of the client sample, create another guest account, and create a random game. This way, Somun will be able to match two players and create a game successfully.

Remember that we registered a `Play_gameCreated` callback function. That function will be called right after the game session is created. If we want to play the game, we should enter that game first.

## Step 6: Enter a Game

There can be many active games in progress for our player on the Somun server. We must enter a game to make a move. Entering a game tells the Somun server that we are actively playing that game right now. Like the unique `playerId`, every game on the Somun server has a unique identifier called `gameId`. We use that `gameId` as a parameter in almost every Somun function related to gameplay. Let's call `Somun.play.enterGame` to activate the game:

```lua
Somun.play.enterGame(gameId, function(status, isCompleted, turnOwnerId, winnerId, stateJson)
    if status == 1 then
        -- we are in the game now!
    end
end)
```

There are some important parameters passed to us from the Somun server. `isCompleted` flag shows if a game is finished or still ongoing. `turnOwnerId` is the `playerId` of the player who owns the turn in a turn-based game. `winnerId` is set if the game is completed. `stateJson` is the state of the game in a JSON format. We will talk about this parameter later.

## Step 7: Understanding Game State

We have learned that on the Somun server, each game has a private and a public state. The private state is kept secret from the players, and the public state is shared between players. You can use the private state to store and update private game information in the database, such as the number we are guessing in our game. The public state can be used to share the current state of the game with players. In our game, we use the public state to tell players the status of the last move (whether it was higher or lower than the number to be guessed).

You can use the public state to share key-value data or as an alternative remote procedure call (RPC) mechanism.

## Step 8: Making a Move (Making a Guess)

We use the `Somun.play.makeMove` function to make a move in a turn-based game. The most important parameter of this function is `moveData`, which is a JSON string. This can be anything that is formatted as JSON. Here is the function signature:

```lua
Somun.play.makeMove(gameId, moveData, function(status)
end)
```

For our purposes, we use `number` as a key and set it to the number we guess. The Somun server will pass this to the relevant game handler class, and our specific game handler class will process this information as explained in the server side of this tutorial.

## Step 9: Handling a Game State Change

The Somun server will call our `Play_gameStateUpdated` callback handler to update the state of our game.

```lua
Somun.registerCallback("Play_gameStateUpdated", function(gameId, stateJson)
    -- handle updated stateJson here
end)
```

The game will continue from here. We can make another move if we are the turn owner.

## Step 10: Game Finish

If any of the players guess the secret number, the game will end. This information will be passed to us using the `Play_gameFinished` callback.

```lua
Somun.registerCallback("Play_gameFinished", function(gameId, winnerId, stateJson)
    -- game is complete here!
end)
```

`winnerId` is the `playerId` who wins the game. `stateJson` is the final state of the game. You cannot make any more moves in a finished game.

## Final Thoughts

That's it! You learned the key points of the "Guess the Number" client-side implementation. Please study the code of this sample project more thoroughly to gain better insights into how to handle various state changes and the flow of the game logic.

Looking for server implementation? Follow: [How to Create Guess The Number Game Step by Step (Server)](docs/guessthenumber-steps-server.MD)