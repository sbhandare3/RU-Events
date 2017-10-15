package com.shreyasbhandare.ruevents.View;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.shreyasbhandare.ruevents.R;
import com.shreyasbhandare.ruevents.Utils.MySharedPreferences;
import com.shreyasbhandare.ruevents.Utils.NotificationGenerator;

public class SettingsActivity extends AppCompatActivity {
    Switch switchButton;
    TextView switchText;
    MySharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchButton = (Switch) findViewById(R.id.switch1);
        switchText = (TextView) findViewById(R.id.view_switch_text);

        SharedPreferences sharedPrefs = getSharedPreferences("com.shreyasbhandare.ruevents", MODE_PRIVATE);
        switchButton.setChecked(sharedPrefs.getBoolean("NameOfThingToSave", true));

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("com.shreyasbhandare.ruevents", MODE_PRIVATE).edit();
                    editor.putBoolean("NameOfThingToSave", true);
                    editor.apply();
                    NotificationGenerator.notifyTodaysEvents(SettingsActivity.this);
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("com.shreyasbhandare.ruevents", MODE_PRIVATE).edit();
                    editor.putBoolean("NameOfThingToSave", false);
                    editor.apply();
                    NotificationGenerator.unsetNotifyTasks(SettingsActivity.this);
                }
            }
        });
    }
}

