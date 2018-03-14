package com.example.pefpr.kahaniyonkashehar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.modalclasses.ResourceView;
import com.example.pefpr.kahaniyonkashehar.interfaces.ItemClicked;

import java.util.List;

/**
 * Created by Ameya on 23-Nov-17.
 */

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.MyViewHolder> {

    private Context mContext;
    private List<ResourceView> resourceViewList;
    ItemClicked itemClicked;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public ResourceAdapter(Context mContext, List<ResourceView> resourceViewList, ItemClicked itemClicked) {
        this.mContext = mContext;
        this.resourceViewList = resourceViewList;
        this.itemClicked = itemClicked;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resource_card, parent, false);

        /*final Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.5, 15);
        animation.setInterpolator(interpolator);
        itemView.startAnimation(animation);*/
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ResourceView resource = resourceViewList.get(position);
        holder.title.setText(resource.getName());
        holder.count.setText(resource.getResourceID() + " songs");
        Glide.with(mContext).load(resource.getThumbnail()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked.onCardItemClicked(position, resource.getResourceID());
            }
        });


    }


    @Override
    public int getItemCount() {
        return resourceViewList.size();
    }
}