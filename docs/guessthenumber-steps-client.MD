# How to Create Guess The Number Game Step by Step (Client)

In this tutorial, we will create a simple "Guess The Number" game using the Somun open source game server Solar2d client library.
The game will be played between two players, where one player thinks of a number between 1 and 100, and the other player tries to guess it.

Most of the source code of the game is doing UI and state management stuff that is not directly releated to understand the client library. Therefore we inspect code fragments that are releated to the somun client implementation and try to explain how to use that in your own projects.

Various states of the game UI is rendered inside renderUI function. setState function is used to change the current state of the game and renderUI accordingly. All of the functionality is triggered by from the event handlers of various button widget.

## Step 1: Import somun-solar2d Client Library
Download and the necessary somun-solar2d client library source code lua files and put them inside a directory called somun. Then import the somun library:
```lua
require("somun.somun")
```

## Step 2: Register Necessary Callback Functions
```Somun.registerCallback``` function is used to register callback handler functions to somun client system. These callback functions are like event handlers called from the server to tell us the state of the game. Callback mechanism is used extensively in somun server.

For this sample game project we require to handle only 4 callback functions such as:

```lua
Somun.registerCallback("Play_gameCreated", function(...arguments)
	--- function body...
end)
```
```lua
Somun.registerCallback("Play_gameStateUpdated", function(...arguments)
	--- function body...
end)
```
```lua
Somun.registerCallback("Play_turnOwnerChanged", function(...arguments)
	--- function body...
end)
```
```lua
Somun.registerCallback("Play_gameFinished", function(...arguments)
	--- function body...
end)
```

## Step 3: Connect to Server
Our application is in ```DISCONNECTED``` state. When the user presses connect button we try to connect to somun server by using following ```Somun.start``` function:
```lua
Somun.start("localhost", 16666, function()
	--- connection successful!
end)
```
We assume that the Somun server is running locally and using default port which is 16666. After the connection succeeded we can do various things such as; creating a guest account, login to an account and disconnect from server. We should create a guest account first, lets dig in.

## Step 4: Create Guest Account
The easiest way top create an account in Somun server is creating a guest account. We use ```Somun.account.createGuestAccount``` function.
```lua
Somun.account.createGuestAccount(function(_playerId, _playerName, _password)
	--- account created
end)
```
Every user in Somun server have a unique ID called playerId. Create guest account function calls a function when its succedded with playerId, playerName (which is randomly generated) and password (randomly generated too). We will need this playerId and password parameter when we login to our account next time. Somun server automatically log-in after creating a new account.

## Step 5: Create a Random Game
Now we are in Somun server. There are plenty of thing to to in this state. We choose to create a game for simplicity. Creating a random game is one of the many game creation options in Somun server. Somun will match random players who requested to create random games and start the game session automatically. You can also create games by invitation, but this is for another tutorial. Let's call ```Somun.play.createRandomGame``` function to create a random game.
```lua
Somun.play.createRandomGame(0, function(status)
	if status == 1 then
		-- done! waiting for a matching player now...
	end
end)
```
The first parameter of the function denotes ```gametype```. There can be many type of games running on the same Somun server. gametype parameter is used to differantiate between different games. In our instance we only have 1 type of game which is Guess the Number game. So we use 0 as gametype.

It will take some time for a game to be created. Firstly there should be at last one another player who is also registered to create random game. Somun will match those players, create the game session and tell us whats happening by using one of the callback functions we mentioned at Step 1 of this tutorial. To proceed run another instance of the client sample, create another guest account and create a random game. This way somun will be able to match 2 players and create a game succesfully.

Remember that we registered a ```"Play_gameCreated"``` callback function. That function will be called right after the game session is created. If we want to play the game, we should enter that game first though.

## Step 6: Enter a Game
There can be many active games in progress for our player in Somun server. We must enter a game to make a move. Entering a game tells Somun server that we are actively playing that game right now. Like unique playerId, every game in Somun server have a unique identifier called ```gameId```. We use that gameId as a parameter in almost every Somun function releated to gameplay. Let's call ```Somun.play.enterGame``` to activate the game.
```lua
Somun.play.enterGame(gameId, function(status, isCompleted, turnOwnerId, winnerId, stateJson)
	if status == 1 then
		--- we are in game now!
	end
end)
```
There are some important parameters passed to us from Somun server. ```isCompleted``` flag show if a game is finished or still ongoing. ```turnOwnerId``` is the playerId who owns the turn in a turn based game. ```winnerId``` is set if game is completed.  ```stateJson``` is the state of the game in a json format. We will talk about this parameter later.

## Step 7: Understanding Game State
We have learned that in Somun server each game has a private and a public state. Private state kept secret from the players and public state is shared between players. You can use private state to store and update private game information in database such as the number we are guessing in our game. Public state can be used to share current state of the game with players. In our game, we use public state to tell players the status of the last move (was it higher than the number to be guessed or lower).

You can use public state to share key, value data or as an alternative remote procedure call (RPC) mechanism.

## Step 8: Making a Move (Making a Guess)
We use ```Somun.play.makeMove``` function to make a move in a turn based game. Most important parameter of this function is moveData which is a json string. This can be anything that is formatted as json. Here is the function signature:
```lua
Somun.play.makeMove(gameId, moveData, function(status)
end)
```
For our purposes we use ```number``` as a key and set it to the number we guess. Somun server will pass this to the relevant game handler class and our specific game handler class will process this information as explained in the server side of this tutorial.

## Step 9: Handling a Game State Change
Somun server will call our ```"Play_gameStateUpdated"``` callback handler to update the state of our game.
```lua
Somun.registerCallback("Play_gameStateUpdated", function(gameId, stateJson)
	--- handle updated statJson here
end)
```
The game will continue from here. We can make another move is we are the turn owner.

## Step 10: Game Finish
If any of the players guess the secret number the game will end. This information will be passed to us by using ```"Play_gameFinished"``` callback.
```lua
Somun.registerCallback("Play_gameFinished", function(gameId, winnerId, stateJson)
	--- game is complete here!
end)
```
winnerId is the playerId who wins the game. stateJson is the final state of the game. You can not make any more moves in a finished game.

## Final Thoughts
That's it! You learned key points of the Guess the Number client side implementation. Please study more throughly the code of this sample project to get better insights how to handle various state changes and flow of the game logic.

Looking for server implementation? Follow: [How to Create Guess The Number Game Step by Step (Server)](docs/guessthenumber-steps-server.MD)