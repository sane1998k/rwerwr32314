package com.automat.manager.services;

import static com.automat.manager.global.GlobalValue.topics;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.automat.manager.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Collections;

public class MyFCMService extends FirebaseMessagingService {

    private static final String TAG = "TAG";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        String topic = Objects.requireNonNull(remoteMessage.getFrom()).replace("/topics/", "");
//        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
//        String body = remoteMessage.getNotification().getBody();
//
//        PreferenceUtils utils = new PreferenceUtils(getApplicationContext());
//        String activityTopic = utils.getPrefrence(TOPIC, "");
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getFrom());
//        if (topic.equals(activityTopic)) showNotifcation(title, body, this);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            showNotifcation(title, body, this);
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.



        //      super.onMessageReceived(remoteMessage);
        //      showNotifcation(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), MyFCMService.this);
    }

    private void showNotifcation(String title, String body, Context context) {
        //Este método muestra notificaciones compatibles con Android Api Level < 26 o >=26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Mostrar notificacion en Android Api level >=26
            final String CHANNEL_ID = "HEADS_UP_NOTIFICATIONS";
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "MyNotification",
                    NotificationManager.IMPORTANCE_HIGH);

            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
            NotificationManagerCompat.from(this).notify(1, notification.build());

        }else{
            //Mostrar notificación para Android Api Level Menor a 26
            String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(/*notification id*/1, notificationBuilder.build());

        }

    }

    public static void foo(String str) {
        int lastIndex = topics.size() - 1;
        topics.stream()
                .filter(i -> !i.equals(str))
                .forEach(i -> {
                    Log.e("QQQQ", "foo: " + i);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(i);
                });
        FirebaseMessaging.getInstance().subscribeToTopic(str);

//        Collections.reverse(topics.subList(0, topics.size()));

        //topics.forEach(i -> Log.e("QQQQ", "foo: " + i));
    }
}
