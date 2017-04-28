var admin = require("firebase-admin");
var bodyParser = require("body-parser")
var express = require("express");
var server = express();
var serviceAccount = require("./TicTacToeNodeJs-da99a621f809.json");
var mongoClient = require("mongodb").MongoClient;
var autoIncrement = require("mongodb-autoincrement");

var dbUrl = 'mongodb://localhost:27017/AndroidTokens';


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

var gameConfirmation = {
	data: {
		message: "ok",
		responseCode: "200"
	}
}

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
			sendTestNotification(request, response, tokens);
		});
});
}

function sendTestNotification(request, response, tokens){
	 response.send('Our first route is working: ' + tokens);
	admin.messaging().sendToDevice(tokens, payload).then(function(response){
		console.log("Successfully sent message: ",response);
	}).catch(function(error){
		console.log("Error sending message: ", error);
	});
}

function getPlayersIDsAndConfirm(request, response){
	mongoClient.connect(dbUrl, function(error, db){
		var collection = db.collection('token');
		if(error) throw error;
		var requestingPlayerID = request.body.GameRequest;
        console.log("GameRequest: ",requestingPlayerID);
		//Controllo se requestingPlayerID esiste fra i token su db: se si ritorna 200(ok) se no ritorna 500
		collection.find({token: { $exists: true, $eq: requestingPlayerID}},{"_id":0}).toArray(function(err, dataArray){
		    var playersIDs = new Array;
		    if (dataArray.length == 0 || err){
		     response.send("500");
		     console.log("player does not exist")
		     return err;
		    }else {
                for (var playerID in dataArray){
                    playersIDs[playerID] = dataArray[playerID].token;
                }
                console.log("playersIDs", playersIDs);
                isAnyPlayerWaitingForGame(request, response, playersIDs);
                confirmGameRequestNotification(request, response, playersIDs);
                response.send("200");
            }
		});

    });
}

function confirmGameRequestNotification(request, response, playersIDs){
	admin.messaging().sendToDevice(playersIDs, gameConfirmation).then(function(response){
		console.log("Successfully sent message: ",response);
	}).catch(function(error){
		console.log("Error sending message: ", error);
	});
}
function isAnyPlayerWaitingForGame(request, response, playersIDs){
    //TODO controllo se esiste gia una gameInstance e se è gia piena di giocatori(.length>2)
    //TODO poi se esiste e non è piena, inserisci giocatore; se esiste ed è piena creane un'altra; se non esiste, crea gameInstance
    /**
    *   Nella collection 'gameInstances' il primo record deve esistere prima di fare la prima chiamata:
    *   ovvero: se la collection è vuota e mando una gameRequest , il db non restituisce nulla perchè l'array 'data' è di lunghezza 0
    */
    mongoClient.connect(dbUrl, function(error,db){
        var collection = db.collection("gameInstances");
        if (error) throw error;
        collection.find().toArray(function(error, data){
            if (error) return error;
            var arrayP1 = new Array;
            var arrayP2 = new Array;
            for (var i = 1;i < data.length; i++ ){
                arrayP1[i] = data[i].player1;
                arrayP2[i] = data[i].player2;
            }
            console.log("last dataArray: ", data[data.length-1]);
            console.log("last player1: ", arrayP1[data.length-1]);
            console.log("last player2: ", arrayP2[data.length-1]);
            if ((arrayP2[data.length-1] == "" || arrayP2[data.length-1] == null) && arrayP1[data.length-1] != "playerone"  ){
                createNewGameInstance(request, response, playersIDs);
                return true;
            } else {                        //TODO FIX STA MERDA DEL CAZZO CHE NON VUOLE FUNZIONARE
                createGameInstance(request, response, playersIDs);
                return false;
            }

        });
    });
}

function createGameInstance(request, response, playersIDs){
    //TODO visto che esiste una gameInstance con un giocatore in attesa, ti butto li dentro
    mongoClient.connect(dbUrl, function(error,db){
        var collection = db.collection("gameInstances");
        if (error) throw error;
        collection.find().toArray(function(error, data){
                    if (error) return error;
                    var arrayP1 = new Array;
                    var arrayP2 = new Array;
                    var IDArray = new Array;
                    for (var i = 1;i < data.length; i++ ){
                        arrayP1[i] = data[i].player1;
                        arrayP2[i] = data[i].player2;
                        IDArray[i] = data[i]._id;
                    }
                    console.log("Updating game instance with ID: ", IDArray[data.length-1]);
                    if(arrayP1[data.length-1] != "playerone" || arrayP2[data.length-1] == null){ //c'è un record con ID=1, player1='playerone', player2='playertwo' che deve esistere, altrimenti non funziona
                        collection.updateOne({_id: IDArray[data.length-1]},{$set: {player2: playersIDs[1]}},function(error, result){
                        if(error) throw error;
                        console.log("update outcome: ok");
                        })
                    }
        });
    });
}

function createNewGameInstance(request, response, playersIDs){
    mongoClient.connect(dbUrl, function(error,db){
        if (error) throw error;
        autoIncrement.getNextSequence(db, "gameInstances", function(error, index){
            var collection = db.collection("gameInstances");

            collection.insert({_id: index ,player1: playersIDs[0],function (error, result){
                if(error) throw error;
                console.log("insert outcome: ",result)
                }
            })
            //TODO waitForOtherPlayer()
        })
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

	getPlayersIDsAndConfirm(request, response)

	console.log("Request Body (post): " , request.body);
});
 


server.get("/token", function(request, response){
	readTokenFromDatabase(request, response);
	console.log("Request Body: " , request.body);
	
});
		
server.post("/token", function(request, response){
	response.send("You sent a post request =] ")
	
	writeTokenToDatabase(request);
	
	console.log("POST INTERCEPTING =]")
	console.log("data: ", request.get('token'))
	console.log("Request Body (post): " , request.body);
});		


server.listen(8888,'192.168.1.220', function(){
	console.log("Server started on http://192.168.1.220:8888/");
});