package com.shreyasbhandare.ruevents.Utils;

import android.content.Context;
import android.content.Intent;

import com.shreyasbhandare.ruevents.Services.TodaysEventsService;

public class MySharedPreferences {

    private android.content.SharedPreferences pref;
    private android.content.SharedPreferences.Editor editor;
    private Context _context;
    private static final String PREF_NAME = "FirstTimeRun";

    // All Shared Preferences Keys Declare as #public
    private static final String KEY_SET_APP_RUN_FIRST_TIME="KEY_SET_APP_RUN_FIRST_TIME";
    private static final String KEY_SET_APP_NOTIFICATION_NUMBER="KEY_SET_APP_NOTIFICATION_NUMBER";
    private boolean flag = true;


    public MySharedPreferences(Context context) // Constructor
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();

    }

    private void setApp_runFirst(String App_runFirst, int notificationNumber) {
        editor.remove(KEY_SET_APP_RUN_FIRST_TIME);
        editor.putString(KEY_SET_APP_RUN_FIRST_TIME, App_runFirst);
        editor.remove(KEY_SET_APP_NOTIFICATION_NUMBER);
        editor.putInt(KEY_SET_APP_NOTIFICATION_NUMBER,notificationNumber);
        editor.commit();
    }

    private String getApp_runFirst() {
        return pref.getString(KEY_SET_APP_RUN_FIRST_TIME, null);
    }

    public void firstTimeLaunchSettings(){
        if(getApp_runFirst()==null)
        {
            // That's mean First Time Launch
            // After your Work , SET Status NO
            //NotificationGenerator.notifyTodaysEvents(_context);
            _context.startService(new Intent(_context, TodaysEventsService.class));
            setApp_runFirst("NO",0);
            flag = false;
        }
        else
        {
            // App is not First Time Launch
            flag = false;
            return;
        }
    }

    public boolean isFirstTimeLaunch(){
        return flag;
    }

    public int getApp_NotificationNumber() {
        return pref.getInt(KEY_SET_APP_NOTIFICATION_NUMBER, 0);
    }

    public void setApp_NotificationNumber(int number){
        editor.remove(KEY_SET_APP_NOTIFICATION_NUMBER);
        editor.putInt(KEY_SET_APP_NOTIFICATION_NUMBER,number);
        editor.commit();
    }

}
