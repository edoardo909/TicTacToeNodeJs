var notifications = require("./notifications.js");
var mongoClient = require("mongodb").MongoClient;

var dbUrl = 'mongodb://localhost:27017/androidTokens';

exports.getPlayersIDs = getPlayersIDs;
exports.deleteGameInstance = deleteGameInstance;

function getPlayersIDs(request, response){
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
                    if(playersIDs.length == 0){
                        playersIDs[playerID] = dataArray[playerID].token;
                    } else {
                        playersIDs[playerID].push(dataArray[playerID].token);
                    }
                }
                console.log("playersIDs", playersIDs);
                isAnyPlayerWaitingForGame(request, response, playersIDs);

            }
		});
    });
}

function isAnyPlayerWaitingForGame(request, response, playersIDs){
    /*   controllo se esiste gia una gameInstance e se è gia piena di giocatori(.length>2)
       poi se esiste e non è piena, inserisci giocatore; se esiste ed è piena creane un'altra; se non esiste, crea gameInstance

       Nella collection 'gameInstances' il primo record deve esistere prima di fare la prima chiamata:
       ovvero: se la collection è vuota e mando una gameRequest , il db non restituisce nulla perchè l'array 'data' è di lunghezza 0
    */
    mongoClient.connect(dbUrl, function(error,db){
        var collection = db.collection("gameInstances");
        if (error) throw error;
        var nGameInstances = collection.count({}, function(error, count){
            nGameInstances = count;
        });

        collection.find().toArray(function(error, data){
            var i = data.length-1;
            console.log("nGameInstances", nGameInstances)
            console.log("last gameId: ", data[i]);
            var gameErrorMessage = {
                data: {
                    message: "ERROR",
                    responseCode: "500",
                    gameInstanceID: '' + data[i]._id
                }
            }

            if((nGameInstances == 1 || (data[i].player1 && data[i].player2)) ){
                createNewGameInstance(request, response, playersIDs);
            }else if(data[i].player1 && !data[i].player2){
                console.log("PLAYERsIDS prima", playersIDs)
                console.log("playerIDS before: ", data[i].player1 + " " + data[i].player2)
                playersIDs.push(data[i].player1);
                updateGameInstance(request, response, playersIDs);
                var gameConfirmation = {
                	data: {
                		message: "StartGame",
                		responseCode: "200",
                		gameInstanceID: '' + data[i]._id
                	}
                }
                notifications.confirmGameRequestNotification(request, response, playersIDs, gameConfirmation);
                console.log("playerIds at confirmation: ", playersIDs)
                response.send("200");
            } else if(!data[i].player1 && !data[i].player2){
                createNewGameInstance(request, response, playersIDs);
                response.send("503");
            }
            if(data[i].player1 == data[i].player2 || playersIDs[0] == playersIDs[1]){
                deleteGameInstance(request, response, data[i]);
                notifications.sendErrorNotification(request, response, playersIDs, gameErrorMessage);
                console.log("same fucking IDs in gameInstance!! =(")
                return;
            }

        });
    });
}

function updateGameInstance(request, response, playersIDs){
    mongoClient.connect(dbUrl, function(error,db){
        var collection = db.collection("gameInstances");
        if (error) throw error;
        collection.find().toArray(function(error, data){
            if (error) return error;
            var i = data.length-1;
            console.log("Updating game instance with ID: ", data[i]._id);
            //c'è un record con ID=1, player1='playerone', player2='playertwo' che viene creato all'avvio del server
            if(data[i].player1 != "playerone" || data[i].player2 != "playertwo"){
                collection.updateOne({_id: data[i]._id},{$set: {player2: playersIDs[0]}},function(error, result){
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
            console.log("Creating New gameInstance");
            collection.insert({_id: index ,player1: playersIDs[0],function (error, result){
                if(error) throw error;
                console.log("insert outcome: ",result)
                }
            })
        })
    });
}

function deleteGameInstance(request, response, instanceID){
    mongoClient.connect(dbUrl, function(error, db){
        if (error) throw error;
        var collection = db.collection("gameInstances");
        console.log("deleting instance with id", instanceID)
        collection.deleteOne({_id: instanceID},function(error, result){
            if (error) throw error;
        })
    })
}