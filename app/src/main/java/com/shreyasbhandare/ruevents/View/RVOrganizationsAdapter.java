package com.shreyasbhandare.ruevents.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shreyasbhandare.ruevents.Presenter.Event;
import com.shreyasbhandare.ruevents.Presenter.Organization;
import com.shreyasbhandare.ruevents.R;

import java.util.ArrayList;
import java.util.List;

public class RVOrganizationsAdapter extends RecyclerView.Adapter<RVOrganizationsAdapter.ItemViewHolder> {
    private List<Organization> organizations;
    private Context context;

    public RVOrganizationsAdapter(Context context, List<Organization> organizations){
        this.organizations = organizations;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_organization_thumbnail, parent, false);
        return new ItemViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.Organization.setText(organizations.get(position).getOrgName());
        if(!organizations.get(position).getOrgPhoto().equals("-"))
            Glide.with(context).load(organizations.get(position).getOrgPhoto()).into(holder.OrganizationPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent eventDetailIntent = new Intent(context, EventsActivity.class);
                eventDetailIntent.putExtra("page_id", organizations.get(position).getOrgId());
                eventDetailIntent.putExtra("org_name", organizations.get(position).getOrgName());
                context.startActivity(eventDetailIntent);

                //Toast.makeText(context,"pos:"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return organizations.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView Organization;
        ImageView OrganizationPhoto;

        ItemViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv3);
            Organization = (TextView)itemView.findViewById(R.id.view_orgnization_name);
            OrganizationPhoto = (ImageView)itemView.findViewById(R.id.image_organization);
        }
    }

    public void setFilter(ArrayList<Organization> newList){
        organizations = new ArrayList<>();
        organizations.addAll(newList);
        notifyDataSetChanged();
    }
}
