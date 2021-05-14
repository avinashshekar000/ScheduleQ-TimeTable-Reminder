package com.avinash.scheduleq.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.avinash.scheduleq.R;
import com.avinash.scheduleq.activities.MainActivity;

import java.util.Calendar;

/**
 * Created by Ulan on 28.01.2019.
 */
public class DailyReceiver extends BroadcastReceiver {

    Context context;
    DbHelper db;
    String notificationContent = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        db = new DbHelper(context);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        /*Date dt =new Date();*/

        calendar.add(Calendar.MINUTE,3);

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.HOUR_OF_DAY,1);
        }
        int currHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currMinute = calendar.get(Calendar.MINUTE);

        //currMinute+=3;
        @SuppressLint("DefaultLocale") String currentTime = String.format("%02d:%02d",currHour,currMinute);

        if (isTimerSet(currentTime, day)) {
            //Toast.makeText(context,notificationContent,Toast.LENGTH_SHORT).show();
            notificator();
        }
        /*{
            String message;

            long when = System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            message = getLessons(calendar.get(Calendar.DAY_OF_WEEK));

            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                    context, "tester").setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(context.getString(R.string.notification_title))
                    .setContentText(message).setSound(alarmSound)
                    .setAutoCancel(true).setWhen(when)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

            if (notificationManager != null) {
                notificationManager.notify(5, mNotifyBuilder.build());
            }
        }*/

    }

    private void notificator() {
        int NOTIFICATION_ID = 123;
        String CHANNEL_ID = "Alarm Alert";

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(notificationIntent);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Notification notification = new Notification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String CHANNEL_NAME = "Alarm Alert";
            String CHANNEL_DESCRIPTION = "User Notification";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200, 100, 200, 100, 200, 100});
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setSound(alarmSound,new AudioAttributes.Builder().build());

            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(notificationContent)
                .setSound(alarmSound)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationContent))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{100, 200, 100, 200, 100, 200, 100, 200, 100})
                .setAutoCancel(true);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID,builder.build());
        }

    }

    private boolean isTimerSet(String currTime, int day) {
        //String message;
        String dayText = getCurrentDay(day);
        notificationContent = db.getCurrentNotification(currTime, dayText);

        return !notificationContent.contentEquals("");
    }


    /*@RequiresApi(api = Build.VERSION_CODES.N)
    private String getLessons(int day) {
        StringBuilder lessons = new StringBuilder("");
        String currentDay = getCurrentDay(day);

        db.getWeek(currentDay).forEach(week -> {
            if (week != null) {
                lessons.append(week.getSubject()).append(" ")
                        .append(week.getFromTime())
                        .append(" - ")
                        .append(week.getToTime()).append(" ")
                        .append(week.getRoom())
                        .append("\n");
            }
        });

        return !lessons.toString().equals("") ? lessons.toString() : context.getString(R.string.do_not_have_lessons);
    }*/

    private String getCurrentDay(int day) {
        String currentDay = null;
        switch (day) {
            case 1:
                currentDay = "Sunday";
                break;
            case 2:
                currentDay = "Monday";
                break;
            case 3:
                currentDay = "Tuesday";
                break;
            case 4:
                currentDay = "Wednesday";
                break;
            case 5:
                currentDay = "Thursday";
                break;
            case 6:
                currentDay = "Friday";
                break;
            case 7:
                currentDay = "Saturday";
                break;
        }
        return currentDay;
    }
}