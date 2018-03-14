package com.example.pefpr.kahaniyonkashehar.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.GamesDisplay;
import com.example.pefpr.kahaniyonkashehar.activities.StoriesDisplay;
import com.example.pefpr.kahaniyonkashehar.interfaces.ItemClicked;
import com.example.pefpr.kahaniyonkashehar.interfaces.StoryClicked;
import com.example.pefpr.kahaniyonkashehar.modalclasses.AttendenceView;
import com.example.pefpr.kahaniyonkashehar.modalclasses.StoriesView;

import java.util.List;

/**
 * Created by Ameya on 23-Nov-17.
 */

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.MyViewHolder> {

    private Context mContext;
    private List<StoriesView> storiesViewList;
    StoryClicked storyClicked;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout story_card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.story_title);
            thumbnail = (ImageView) view.findViewById(R.id.story_thumbnail);
            story_card_view = (LinearLayout) view.findViewById(R.id.story_card_view);
        }
    }

    public StoriesAdapter(Context mContext, List<StoriesView> storiesViewList, StoryClicked storyClicked) {
        this.mContext = mContext;
        this.storiesViewList = storiesViewList;
        this.storyClicked = storyClicked;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stories_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final StoriesView storiesList = storiesViewList.get(position);

        if (StoriesDisplay.storiesDispLang.equalsIgnoreCase("pun")) {
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/raavi_punjabi.ttf");
            holder.title.setTypeface(custom_font, Typeface.BOLD);
        } else if (StoriesDisplay.storiesDispLang.equalsIgnoreCase("odiya")) {
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/lohit_oriya.ttf");
            holder.title.setTypeface(custom_font, Typeface.BOLD);
        } else if (StoriesDisplay.storiesDispLang.equalsIgnoreCase("guj")) {
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/muktavaani_gujarati.ttf");
            holder.title.setTypeface(custom_font, Typeface.BOLD);
        }

        holder.title.setText(storiesList.storyName);

        Glide.with(mContext).load(storiesList.storyThumbnail).into(holder.thumbnail);
        holder.story_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyClicked.onStoryClicked(position, storiesList.storyId, storiesList.storyData, storiesList.storyId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storiesViewList.size();
    }
}