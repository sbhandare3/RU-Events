package com.shreyasbhandare.ruevents.View;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shreyasbhandare.ruevents.Presenter.Event;
import com.shreyasbhandare.ruevents.R;

import java.util.List;

public class RVEventsAdapter extends RecyclerView.Adapter<RVEventsAdapter.ItemViewHolder> {
    private List<Event> events;
    private Context context;


    public RVEventsAdapter(Context context, List<Event> events){
        this.events = events;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_thumbnail, parent, false);
        return new ItemViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.EventName.setText(events.get(position).getEventName());
        holder.Organization.setText(events.get(position).getOrganization());
        holder.Date.setText(events.get(position).getDate());
        if(!events.get(position).getPhoto().equals("-"))
            Glide.with(context).load(events.get(position).getPhoto()).into(holder.EventPhoto);
        //holder.EventPhoto.setImageResource(events.get(position).photo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"clicked"+position,Toast.LENGTH_SHORT).show();
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String facebookUrl = getFacebookPageURL(context, events.get(position).getEventId());
                facebookIntent.setData(Uri.parse(facebookUrl));
                context.startActivity(facebookIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView EventName;
        TextView Organization;
        TextView Date;
        ImageView EventPhoto;

        ItemViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv2);
            EventName = (TextView)itemView.findViewById(R.id.view_event_name);
            Organization = (TextView)itemView.findViewById(R.id.view_event_by);
            Date = (TextView)itemView.findViewById(R.id.view_event_date);
            EventPhoto = (ImageView)itemView.findViewById(R.id.image_event);
        }
    }



    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context, String event_id) {
        String FACEBOOK_URL = "https://www.facebook.com/events/"+event_id;
        String FACEBOOK_PAGE_ID = "events/"+event_id;

        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
}
