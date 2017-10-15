package com.shreyasbhandare.ruevents.View;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.shreyasbhandare.ruevents.Presenter.Event;
import com.shreyasbhandare.ruevents.Presenter.Organization;
import com.shreyasbhandare.ruevents.R;
import com.shreyasbhandare.ruevents.Utils.MySharedPreferences;
import com.shreyasbhandare.ruevents.Utils.SQLiteDatabaseHelper;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    SQLiteDatabaseHelper helper;
    ArrayList<Event> todaysEventList;
    RecyclerView rv3;
    RVEventsAdapter adapter;
    TextView emptyText;
    MySharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mySharedPreferences = new MySharedPreferences(this);
        mySharedPreferences.setApp_NotificationNumber(0);
        todaysEventList = new ArrayList<>();
        helper = new SQLiteDatabaseHelper(this);
        adapter = new RVEventsAdapter(this, todaysEventList);
        emptyText = (TextView) findViewById(R.id.view_empty);
        rv3 = (RecyclerView) findViewById(R.id.list_events);
        rv3.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv3.setLayoutManager(llm);
        rv3.setAdapter(adapter);
        SQLiteLoad();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void SQLiteLoad(){
        Cursor cursor = helper.getEventEntry();
        while (cursor.moveToNext()) {
            todaysEventList.add(new Event(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
        }
        adapter.notifyDataSetChanged();
    }
}
