package com.jsla.tasbeeh;

import static com.jsla.tasbeeh.App.CHANNEL_1_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class masbahaReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getBooleanExtra("showMasbaha",true)) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!Settings.canDrawOverlays(context)){

                    } else {
                        context.startForegroundService(new Intent(context, MasbahaFloatingService.class));
                    }
                } else {
                    context.startService(new Intent(context, MasbahaFloatingService.class));
                }
            } catch (Exception e) {
                Toast.makeText(context.getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context.getApplicationContext());
            String title = "المسبحة";
            String message = "اللهم صل على سيدنا محمد";

            Intent activityIntent = new Intent(context.getApplicationContext(), MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(),
                    0, activityIntent, 0);

            Intent broadcastIntent = new Intent(context.getApplicationContext(), NotificationReceiver.class);

//            PendingIntent actionIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
//                    0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.empty_app_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(Color.parseColor("#936ED3"))
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setSilent(false)
//                    .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                    .build();
            notificationManager.notify(1, notification);

        }
    }

}
