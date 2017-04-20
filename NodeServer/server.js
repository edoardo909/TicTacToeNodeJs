var admin = require("firebase-admin");
var bodyParser = require("body-parser")
var express = require("express");
var server = express();
var serviceAccount = require("./TicTacToeNodeJs-da99a621f809.json");
var mongoClient = require("mongodb").MongoClient;

var dbUrl = 'mongodb://localhost:27017/AndroidTokens';

var tokens = [];

function writeTokenToDatabase(request){
	mongoClient.connect(dbUrl, function(error, db){
		var collection = db.collection('token');
		
	if(error) throw error;
	collection.insert(request.body,function (error, result){
		if(error) throw error;
		
		console.log(result);
	});
});
	
}


	mongoClient.connect(dbUrl, function(error, db){
		var collection = db.collection('token');
		if(error) throw error;
		collection.find({} , {"_id":0}).toArray(function(error, items){
			if (error){
				throw error;
			}else {
				tokens = items;
				console.log("Tokens on db: ---> ", tokens);
			}
		});
		
});

/*.forEach(function(obj){
			console.log(obj);
		});*/

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

server.get("/", function(request, response){
	console.log("Request Body: " , request.body);

	response.send('Our first route is working: ' + JSON.stringify(request.body, null, 2));
	admin.messaging().sendToDevice(tokens, payload).then(function(response){
		console.log("Successfully sent message: ",response);
	}).catch(function(error){
		console.log("Error sending message: ", error);
	});
});
		
server.post("/", function(request, response){
	response.send("You sent a post request =] ")
	
	writeTokenToDatabase(request);
	
	console.log("POST INTERCEPTING =]")
	console.log("data: ", request.get('token'))
	console.log("Request Body (post): " , request.body);
});		

server.listen(8888, '192.168.1.220', function(){
	console.log("Server started on http://192.168.1.220:8888/");
});