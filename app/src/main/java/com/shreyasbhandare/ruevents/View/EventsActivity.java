package com.shreyasbhandare.ruevents.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shreyasbhandare.ruevents.Model.POJO.EventsPerPage;
import com.shreyasbhandare.ruevents.Presenter.Event;
import com.shreyasbhandare.ruevents.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shreyasbhandare.ruevents.Presenter.RetrofitBuilder.ACCESS_TOKEN;
import static com.shreyasbhandare.ruevents.Presenter.RetrofitBuilder.apiInterface;

public class EventsActivity extends AppCompatActivity {

    List<Event> eventList;
    RecyclerView rv2;
    RVEventsAdapter adapter;
    TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        emptyText = (TextView) findViewById(R.id.view_empty);
        rv2 = (RecyclerView) findViewById(R.id.list_events);
        rv2.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv2.setLayoutManager(llm);
        populateEvents();
    }

    public void populateEvents(){
        final Intent intent = getIntent();
        String page_id="";
        String org_name="";
        long unixTime = System.currentTimeMillis() / 1000L;

        if(intent.hasExtra("page_id"))
            page_id = intent.getStringExtra("page_id");
        if(intent.hasExtra("org_name"))
            org_name = intent.getStringExtra("org_name");

        getSupportActionBar().setTitle(org_name);
        eventList = new ArrayList<>();

        final ProgressDialog progressDialog = ProgressDialog.show(EventsActivity.this,null,"Fetching Events...");

        Call<EventsPerPage> call = apiInterface.getEventPerPageList(page_id,"name,cover,start_time",ACCESS_TOKEN,unixTime+"");

        call.enqueue(new Callback<EventsPerPage>() {
            @Override
            public void onResponse(@NonNull Call<EventsPerPage> call, @NonNull Response<EventsPerPage> response) {
                EventsPerPage eventsPerPage = response.body();
                String organization_name = intent.getStringExtra("org_name");

                if (eventsPerPage != null) {
                    for(int i=0;i<eventsPerPage.getData().length;i++){
                        String name = eventsPerPage.getData()[i].getName();
                        String id = eventsPerPage.getData()[i].getId();
                        String date = eventsPerPage.getData()[i].getStart_time().substring(0,10);

                        String photo = "-";
                        if(eventsPerPage.getData()[i].getCover()!=null)
                            photo = eventsPerPage.getData()[i].getCover().getSource();

                        eventList.add(new Event(id,name,organization_name,date,photo));
                    }
                }

                Collections.reverse(eventList);
                adapter = new RVEventsAdapter(getBaseContext(), eventList);
                rv2.setAdapter(adapter);

                if (adapter.getItemCount() == 0) {
                    emptyText.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<EventsPerPage> call, @NonNull Throwable t) {
                Toast.makeText(getBaseContext(),"Failed to Connect!",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
