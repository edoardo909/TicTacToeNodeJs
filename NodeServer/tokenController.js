var mongoClient = require("mongodb").MongoClient;
var notifications = require("./notifications.js");

var dbUrl = 'mongodb://localhost:27017/AndroidTokens';

exports.writeTokenToDatabase = writeTokenToDatabase;
exports.readTokenFromDatabase = readTokenFromDatabase;

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
			notifications.sendTestNotification(request, response, tokens);
		});
});
}

