package com.shreyasbhandare.ruevents.BroadcastReceivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.shreyasbhandare.ruevents.R;
import com.shreyasbhandare.ruevents.Utils.MySharedPreferences;
import com.shreyasbhandare.ruevents.Utils.SQLiteDatabaseHelper;
import com.shreyasbhandare.ruevents.View.EventsActivity;
import com.shreyasbhandare.ruevents.View.NotificationActivity;

public class NotificationReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;
    PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeatingIntent = new Intent(context, NotificationActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(context,100,repeatingIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotifyBuilder(context);
    }


    public void NotifyBuilder(Context context){
        SQLiteDatabaseHelper helper = new SQLiteDatabaseHelper(context);
        int todaysEvents = helper.getNumberOfEvents();
        String notifyTitle;
        if(todaysEvents>1)
            notifyTitle = "You have "+todaysEvents+" events around today";
        else if(todaysEvents==0)
            notifyTitle = "You have no events around today";
        else
            notifyTitle = "You have "+todaysEvents+" event around today";

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        mySharedPreferences.setApp_NotificationNumber(todaysEvents);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_todays_events)
                .setContentTitle(notifyTitle)
                .setAutoCancel(true);

        notificationManager.notify(100,builder.build());
    }


}
