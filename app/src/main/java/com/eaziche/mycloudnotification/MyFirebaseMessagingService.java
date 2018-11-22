package com.eaziche.mycloudnotification;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import static com.eaziche.mycloudnotification.ListActivity.online;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    static boolean firstTime = true;
    NotificationCompat.Builder builder;
    NotificationManager manager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            showNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String message, String title) {

        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri notification = Uri.parse("android.resource://"
                + this.getPackageName() + "/" + R.raw.notification);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        if (firstTime) {
            builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setSound(notification)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent);
            firstTime = false;
        } else {
            builder.setContentText(message);
        }
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (online.equals((Object) false)) {
            manager.notify(0, builder.build());
        }
    }


}