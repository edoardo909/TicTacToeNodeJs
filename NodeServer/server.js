var admin = require("firebase-admin");
var bodyParser = require("body-parser")
var express = require("express");
var server = express();
var serviceAccount = require("./TicTacToeNodeJs-da99a621f809.json");
var mongoClient = require("mongodb").MongoClient;
var autoIncrement = require("mongodb-autoincrement");
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
})

 server.get("/home", function(request, response){
	 console.log("HOMEPAGE")
	 response.send("HomePage.<br />Still haven't decided what to do with it. <br />-try going to '/token' for now")
 });
 
 server.post("/async", function(request, response){
	console.log("intercepting asyncTask");
	response.send("Node Server intercepting asyncTask");
//	console.log("Request Body (post): " , request.body);
 });
 
 server.get("/async", function(request, response){
	console.log("getting async");
	response.send("Getting asyncTask");
	console.log("Request Body: " , request.body);
	
});

server.get("/async/gamerequest", function(request, response){
	console.log("getting async gamerequest");
	response.send("Getting gamerequest");
	console.log("Request Body: " , request.body);
});

server.post("/async/gamerequest", function(request, response){
	console.log("intercepting gamerequest");
	var requestAddress = request.connection.remoteAddress;
	console.log("request from: ", requestAddress);
	gameController.getPlayersIDs(request, response)
});
 
server.get("/async/forfeit", function(request, response){
    console.log("getting game forfeit");
    response.send("Getting forfeit");
});

server.post("/async/forfeit", function(request, response){
    console.log("intercepting game forfeit");
    response.send("gameCancelled");
    //TODO deleteGameInstance =)
});

server.get("/token", function(request, response){
	tokenController.readTokenFromDatabase(request, response);
	console.log("Request Body: " , request.body);
});
		
server.post("/token", function(request, response){
	response.send("You sent a post request =] ")
	tokenController.writeTokenToDatabase(request);
	console.log("data: ", request.get('token'))
	console.log("Request Body (post): " , request.body);
});		

server.listen(8888, function(){
	console.log("Server started on http://192.168.1.220:8888/");
});