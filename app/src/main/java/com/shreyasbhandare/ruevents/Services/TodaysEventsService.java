package com.shreyasbhandare.ruevents.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.PeriodicSync;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.shreyasbhandare.ruevents.Model.POJO.EventsPerPage;
import com.shreyasbhandare.ruevents.Model.POJO.PageList;
import com.shreyasbhandare.ruevents.Presenter.Event;
import com.shreyasbhandare.ruevents.Presenter.Organization;
import com.shreyasbhandare.ruevents.Presenter.RetrofitBuilder;
import com.shreyasbhandare.ruevents.Utils.NetworkStatus;
import com.shreyasbhandare.ruevents.Utils.SQLiteDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodaysEventsService extends Service implements RetrofitBuilder {
    // constant
    public static final long NOTIFY_INTERVAL = 60000; //3600*1000
    ArrayList<Organization> organizationList = new ArrayList<>();
    ArrayList<Event> todaysEventList = new ArrayList<>();

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    private Runnable runnable;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        runnable = new Runnable() {
            @Override
            public void run() {
                /* do what you need to do */
                Toast.makeText(getApplicationContext(), "updated!", Toast.LENGTH_SHORT).show();
                populateTodaysEvents(getApplicationContext());
                /* and here comes the "trick" */
                mHandler.postDelayed(this, NOTIFY_INTERVAL);
            }
        };
        mHandler.postDelayed(runnable, NOTIFY_INTERVAL);

    }

    private void populateTodaysEvents(final Context context){
        final SQLiteDatabaseHelper helper = new SQLiteDatabaseHelper(context);
        helper.deleteEventEntry();

        if(!NetworkStatus.checkNetworkStatus(context).equals("noNetwork")) {
            Call<PageList> call = apiInterface.getPageList("id,name,cover", ACCESS_TOKEN, "400", "rutgers", "page");

            call.enqueue(new Callback<PageList>() {
                @Override
                public void onResponse(@NonNull Call<PageList> call, @NonNull Response<PageList> response) {
                    PageList pageList = response.body();
                    if(pageList.getData()==null)
                        SQLiteLoad(context);
                    else if (pageList != null) {
                        for (int i = 0; i < pageList.getData().length; i++) {
                            String name = pageList.getData()[i].getName();
                            String id = pageList.getData()[i].getId();
                            String photo = "-";
                            if(pageList.getData()[i].getCover()!=null) {
                                try {
                                    photo = pageList.getData()[i].getCover().getSource();
                                    organizationList.add(new Organization(name, id, photo));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                organizationList.add(new Organization(name, id, photo));
                            }

                        }
                    }

                    for(int j=0; j<organizationList.size();j++) {
                        final String org_name = organizationList.get(j).getOrgName();
                        long unixTime = System.currentTimeMillis() / 1000L;

                        Call<EventsPerPage> call2 = apiInterface.getEventPerPageList(organizationList.get(j).getOrgId(), "name,cover,start_time", ACCESS_TOKEN, unixTime + "");

                        call2.enqueue(new Callback<EventsPerPage>() {
                            @Override
                            public void onResponse(@NonNull Call<EventsPerPage> call, @NonNull Response<EventsPerPage> response) {
                                EventsPerPage eventsPerPage = response.body();
                                Date date = new Date();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                String todayDate = df.format(date);

                                if (eventsPerPage != null) {
                                    for (int i = 0; i < eventsPerPage.getData().length; i++) {
                                        String name = eventsPerPage.getData()[i].getName();
                                        String id = eventsPerPage.getData()[i].getId();
                                        String dateCur = eventsPerPage.getData()[i].getStart_time().substring(0, 10);
                                        String photo = "-";
                                        if(eventsPerPage.getData()[i].getCover()!=null)
                                            photo = eventsPerPage.getData()[i].getCover().getSource();

                                        if(dateCur.equals(todayDate)) {
                                            helper.insertEventEntry(id, name, org_name, dateCur, photo);
                                            todaysEventList.add(new Event(id, name, org_name, dateCur, photo));
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<EventsPerPage> call, @NonNull Throwable t) {
                            }
                        });

                    }
                }
                @Override
                public void onFailure(@NonNull Call<PageList> call, @NonNull Throwable t) {
                }
            });
        }
        else {
            SQLiteLoad(context);
        }
    }

    private void SQLiteLoad(Context context){
        SQLiteDatabaseHelper helper = new SQLiteDatabaseHelper(context);
        Cursor cursor = helper.getEntry();
        while (cursor.moveToNext()) {
            organizationList.add(new Organization(cursor.getString(1), cursor.getString(0), cursor.getString(2)));
        }
    }
}