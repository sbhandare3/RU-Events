package com.shreyasbhandare.ruevents.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.shreyasbhandare.ruevents.Model.POJO.PageList;
import com.shreyasbhandare.ruevents.Presenter.Event;
import com.shreyasbhandare.ruevents.Presenter.Organization;
import com.shreyasbhandare.ruevents.Presenter.RetrofitBuilder;
import com.shreyasbhandare.ruevents.R;
import com.shreyasbhandare.ruevents.Services.TodaysEventsService;
import com.shreyasbhandare.ruevents.Utils.MySharedPreferences;
import com.shreyasbhandare.ruevents.Utils.NetworkStatus;
import com.shreyasbhandare.ruevents.Utils.NotificationGenerator;
import com.shreyasbhandare.ruevents.Utils.SQLiteDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RetrofitBuilder {

    private ArrayList<Organization> organizationList;
    EditText searchBox;
    RecyclerView rv;
    RVOrganizationsAdapter adapter;
    NotificationBadge mBadge;
    ImageView notify;
    private int notifyNumber;
    SQLiteDatabaseHelper helper;
    MySharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
        populateOrganization();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchStr = s.toString().toLowerCase();
                ArrayList<Organization>newList = new ArrayList<Organization>();
                for (Organization org: organizationList) {
                    String name = org.getOrgName().toLowerCase();
                    if(name.contains(searchStr))
                        newList.add(org);
                }
                adapter.setFilter(newList);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void init(){
        searchBox = (EditText) findViewById(R.id.search_org);
        mBadge = (NotificationBadge) findViewById(R.id.badge);
        rv = (RecyclerView) findViewById(R.id.list_organizations);
        notify = (ImageView) findViewById(R.id.icon);
        mySharedPreferences = new MySharedPreferences(this);
        mySharedPreferences.firstTimeLaunchSettings();
        notifyNumber = mySharedPreferences.getApp_NotificationNumber();
        mBadge.setNumber(notifyNumber);
        helper = new SQLiteDatabaseHelper(this);
        organizationList = new ArrayList<>();

        /*
        start notification and todays events service
         */
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        if(sdf.format(new Date()).equals("07:59:00"))
            NotificationGenerator.notifyTodaysEvents(this);
        /*
        setup organizations adapter and view
         */
        adapter = new RVOrganizationsAdapter(this, organizationList);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyNumber = mySharedPreferences.getApp_NotificationNumber();
        mBadge.setNumber(notifyNumber);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            startActivity(new Intent(this,SettingsActivity.class));
            //Toast.makeText(this,"Coming Soon!",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            sendMail();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void populateOrganization(){
        final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this,null,"Initializing...");
        if(!NetworkStatus.checkNetworkStatus(this).equals("noNetwork")) {
            Call<PageList> call = apiInterface.getPageList("id,name,cover", ACCESS_TOKEN, "400", "rutgers", "page");

            call.enqueue(new Callback<PageList>() {
                @Override
                public void onResponse(@NonNull Call<PageList> call, @NonNull Response<PageList> response) {
                    PageList pageList = response.body();
                    if(pageList.getData()==null)
                        SQLiteLoad();
                    else if (pageList != null) {
                        for (int i = 0; i < pageList.getData().length; i++) {
                            String name = pageList.getData()[i].getName();
                            String id = pageList.getData()[i].getId();
                            String photo = "-";
                            if(pageList.getData()[i].getCover()!=null) {
                                try {
                                    photo = pageList.getData()[i].getCover().getSource();
                                    organizationList.add(new Organization(name, id, photo));
                                    helper.insertEntry(id, name, photo);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                organizationList.add(new Organization(name, id, photo));
                                helper.insertEntry(id, name, photo);
                            }

                        }
                    }

                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<PageList> call, @NonNull Throwable t) {
                    Toast.makeText(getBaseContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
        else {
            SQLiteLoad();
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    public void SQLiteLoad(){
        Cursor cursor = helper.getEntry();
        while (cursor.moveToNext()) {
            organizationList.add(new Organization(cursor.getString(1), cursor.getString(0), cursor.getString(2)));
        }
    }

    public void sendMail() {
        String[] toEmail = {"bhandare.shreyas@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, toEmail);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            /// ------------------check------------------
            //Log.i("email sent", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please email to 'bhandare.shreyas@gmail.com'", Toast.LENGTH_LONG).show();
        }
    }

    public void notifyPressed(View view){
        notifyNumber=0;
        startActivity(new Intent(MainActivity.this,NotificationActivity.class));
    }

}
