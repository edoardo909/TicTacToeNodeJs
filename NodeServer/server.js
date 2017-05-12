var admin = require("firebase-admin");
var bodyParser = require("body-parser")
var express = require("express");
var server = express();
var serviceAccount = require("./TicTacToeNodeJs-da99a621f809.json");
var mongoClient = require("mongodb").MongoClient;
var tokenController = require("./tokenController.js");
var gameController = require("./gameController.js");

var dbUrl = 'mongodb://localhost:27017/AndroidTokens';

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://tictactoenodejs.firebaseio.com/"
});

server.use(bodyParser.json());
server.use(bodyParser.urlencoded({
  extended: true
}));

mongoClient.connect(dbUrl, function(error, db){
    var collection = db.collection("gameInstances");
    if (error) throw error;
    collection.insertOne({_id:1,player1:"playerone",player2:"playertwo"},function(error, result){
        if(error) return;
    });
});

 server.get("/home", function(request, response){
	 console.log("HOMEPAGE")
	 response.send("HomePage.<br />Still haven't decided what to do with it. <br />-try going to '/token' for now")
 });
 
 server.post("/async/game", function(request, response){
	console.log("intercepting asyncTask");
	console.log("Request Body (post): " , request.body);
	var gameData = request.body.GameData;
    gameController.passGameDataToPlayers(request, response, gameData);
    response.send("Node Server intercepting asyncTask ");
 });
 
 server.get("/async/game", function(request, response){
	console.log("getting async");
	console.log("Request Body: " , request.body);
	response.send("Getting asyncTask");
});

server.get("/async/gamerequest", function(request, response){
	console.log("getting async gamerequest");
	console.log("Request Body: " , request.body);
	response.send("Getting gamerequest");
});

server.post("/async/gamerequest", function(request, response){
	console.log("intercepting gamerequest");
	var requestAddress = request.connection.remoteAddress;
	console.log("request from: ", requestAddress);
	gameController.getPlayersIDs(request, response)
	response.send("intercepting gamerequest");
});
 
server.get("/async/forfeit", function(request, response){
    console.log("getting game forfeit");
    response.send("Getting forfeit");
});

server.post("/async/forfeit", function(request, response){
    console.log("intercepting game forfeit");
    console.log("forfeit body: ", request.body)
    gameController.deleteGameInstance(request, response, request.body.ForfeitRequest)
    response.send("gameCancelled");
});

server.get("/token", function(request, response){
	tokenController.readTokenFromDatabase(request, response);
	console.log("Request Body: " , request.body);
	response.send("got your token");
});
		
server.post("/token", function(request, response){
	tokenController.writeTokenToDatabase(request);
	console.log("data: ", request.get('token'))
	console.log("Request Body (post): " , request.body);
	response.send("You sent a post request =] ")
});		

server.listen(8888,'192.168.1.220', function(){
	console.log("Server started on http://192.168.1.220:8888/");
});