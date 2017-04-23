var admin = require("firebase-admin");
var bodyParser = require("body-parser")
var express = require("express");
var server = express();
var serviceAccount = require("./TicTacToeNodeJs-da99a621f809.json");
var mongoClient = require("mongodb").MongoClient;

var dbUrl = 'mongodb://localhost:27017/androidTokens';
var players = [];

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://tictactoenodejs.firebaseio.com/"
});

var payload = {
  notification: {
    title: "Hello World",
    body: "Hello World! this is a push notification! =)"
  }
};

server.use(bodyParser.json());
server.use(bodyParser.urlencoded({
  extended: true
}));

function writeTokenToDatabase(request){
	mongoClient.connect(dbUrl, function(error, db){
		var collection = db.collection('token');
		if(error) throw error;
		var requestBodyObject = eval(request.body);
		for (key in requestBodyObject) {
				collection.insert({"token":key},function (error, result){
				if(error) throw error;
				console.log(result);
			});
		}
});
}

function readTokenFromDatabase(request, response){
	mongoClient.connect(dbUrl, function(error, db){
		var collection = db.collection('token');
		if(error) throw error;
		collection.find({} , {"_id":0}).toArray(function(e, dataArray) {
			var tokens = new Array;
				for (var token in dataArray){
					tokens[token] = dataArray[token].token;
				}
			sendNotification(request, response, tokens);
		});
});
}

function sendNotification(request, response, tokens){
	 response.send('Our first route is working: ' + tokens);
	admin.messaging().sendToDevice(tokens, payload).then(function(response){
		console.log("Successfully sent message: ",response);
	}).catch(function(error){
		console.log("Error sending message: ", error);
	});
}
 
 server.get("/home", function(request, response){
	 console.log("HOMEPAGE")
	 response.send("HomePage.<br />Still haven't decided what to do with it. <br />-try going to '/token' for now")
 });
 
 server.post("/async", function(request, response){
	console.log("intercepting asyncTask");
	response.send("Node Server intercepting asyncTask");
	console.log("Request Body (post): " , request.body);
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
	
	players[0] = requestAddress;
	
	/*for(var i = 0; i < numberOfGameRequests; i++){
		players[i] = request.body.GameRequest;
	}
	if(players.length == 2){
	console.log(players.length)
	for(var i = 0; i < 2; i++){
		console.log(players)
	}
	}*/
	response.send("ok");
	console.log("Request Body (post): " , request.body);
});
 

server.get("/", function(request, response){
	readTokenFromDatabase(request, response);
	console.log("Request Body: " , request.body);
	
});
		
server.post("/", function(request, response){
	response.send("You sent a post request =] ")
	
	writeTokenToDatabase(request);
	
	console.log("POST INTERCEPTING =]")
	console.log("data: ", request.get('token'))
	console.log("Request Body (post): " , request.body);
});		

server.listen(8888,'192.168.10.21', function(){
	console.log("Server started on http://192.168.10.21:8888/");
});