var admin = require("firebase-admin");

exports.sendTestNotification = sendTestNotification;
exports.confirmGameRequestNotification = confirmGameRequestNotification;
exports.sendErrorNotification = sendErrorNotification;
exports.sendGameEndedNotification = sendGameEndedNotification;

var payload = {
  notification: {
    title: "Hello World",
    body: "Hello World! this is a push notification! =)"
  }
};

function sendTestNotification(request, response, tokens){
	 response.send('Our first route is working: ' + tokens);
	admin.messaging().sendToDevice(tokens, payload).then(function(response){
		console.log("Successfully sent message: ",response);
	}).catch(function(error){
		console.log("Error sending message: ", error);
	});
}

function confirmGameRequestNotification(request, response, playersIDs, gameConfirmation){
	admin.messaging().sendToDevice(playersIDs, gameConfirmation).then(function(response){
		console.log("Successfully sent message: ",response);
	}).catch(function(error){
		console.log("Error sending message: ", error);
	});
}

function sendErrorNotification(request, response, playersIDs, gameErrorMessage){
    admin.messaging().sendToDevice(playersIDs, gameErrorMessage).then(function(response){
    		console.log("Successfully sent message: ",response);
    	}).catch(function(error){
    		console.log("Error sending message: ", error);
    	});
}

function sendGameEndedNotification(request, response, playersIDs, gameEndedMessage){
    admin.messaging().sendToDevice(playersIDs, gameErrorMessage).then(function(response){
        console.log("Successfully sent message: ",response);
    }).catch(function(error){
        console.log("Error sending message: ", error);
    });
}