package it.parello.tictactoenodejs.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.activities.MultiPlayer;
import it.parello.tictactoenodejs.activities.SinglePlayer;


/**
 * Created by Parello on 20/04/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public String MessageReceived;
    Intent intent;
    public static final String INTENT_FILTER_ERROR = "it.parello.tictactoenodejs.ERROR";
    public static final String INTENT_FILTER_GAME_END = "it.parello.tictactoenodejs.GAME_END";
    public static final String INTENT_FILTER_DATA = "it.parello.tictactoenodejs.DATA";
    public static final String INTENT_FILTER_WINNER = "it.parello.tictactoenodejs.WINNER";
    public static int instanceId;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        //TODO se arriva un remoteMessage.getData() : gestiscilo in modo appropriato

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if(remoteMessage.getData().containsValue("StartGame") && remoteMessage.getData().containsValue("200")){
                    instanceId = Integer.parseInt(remoteMessage.getData().get("gameInstanceID"));
                    Log.e(TAG, "confirmGameRequest was received, starting the game with id: " + instanceId);
                    intent = new Intent(getApplicationContext(), MultiPlayer.class);
                    String marker = remoteMessage.getData().get("marker");
                    intent.putExtra("playerMarker", marker);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
            }else if(remoteMessage.getData().containsValue("ERROR") && remoteMessage.getData().containsValue("500")){
                    intent = new Intent(INTENT_FILTER_ERROR);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Log.e(TAG, "An error occured on the server, try again");
            }else if(remoteMessage.getData().containsValue("gameEnded") && remoteMessage.getData().containsValue("503")){
                    Log.e(TAG, "game was ended by other player, you Win! =)");
                    intent = new Intent(INTENT_FILTER_GAME_END);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            } else if (remoteMessage.getData().containsKey("opponent_id")) {
                Log.e(TAG, "getting opponent game data");
                String opponentGameData = remoteMessage.getData().get("board_data");
                Log.e(TAG, "game data---> " + opponentGameData);
                intent = new Intent(INTENT_FILTER_DATA);
                intent.putExtra("board_data", opponentGameData);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }else if(remoteMessage.getData().containsKey("winner")){
                String winner = remoteMessage.getData().get("winner");
                intent = new Intent(INTENT_FILTER_WINNER);
                intent.putExtra("winner", winner);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            MessageReceived = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, SinglePlayer.class);
        intent.putExtra("",MessageReceived);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.admiral_ackbar)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
