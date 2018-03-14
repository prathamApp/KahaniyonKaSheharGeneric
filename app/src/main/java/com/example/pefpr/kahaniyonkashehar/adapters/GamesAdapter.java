package com.example.pefpr.kahaniyonkashehar.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.GamesDisplay;
import com.example.pefpr.kahaniyonkashehar.contentplayer.SdCardPath;
import com.example.pefpr.kahaniyonkashehar.interfaces.GameClicked;
import com.example.pefpr.kahaniyonkashehar.modalclasses.ModalGames;

import java.util.List;

/**
 * Created by Ameya on 23-Nov-17.
 */

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {

    private Context mContext;
    private List<ModalGames> gamesViewList;
    GameClicked gameClicked;
    SdCardPath ex_path;
    String sdCardPathString;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout game_card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.game_title);
            thumbnail = (ImageView) view.findViewById(R.id.game_thumbnail);
            game_card_view = (LinearLayout) view.findViewById(R.id.game_card_view);
        }
    }

    public GamesAdapter(Context mContext, List<ModalGames> gamesViewList, GameClicked gameClicked) {
        this.mContext = mContext;
        this.gamesViewList = gamesViewList;
        this.gameClicked = gameClicked;
    }
    /*
    public GamesAdapter(Context mContext, List<GamesView> gamesViewList, GameClicked gameClicked) {
        this.mContext = mContext;
        this.gamesViewList = gamesViewList;
        this.gameClicked = gameClicked;
    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ModalGames gamesList = gamesViewList.get(position);

        if (GamesDisplay.gameDispLang.equalsIgnoreCase("pun")) {
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/raavi_punjabi.ttf");
            holder.title.setTypeface(custom_font, Typeface.BOLD);
        } else if (GamesDisplay.gameDispLang.equalsIgnoreCase("odiya")) {
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/lohit_oriya.ttf");
            holder.title.setTypeface(custom_font, Typeface.BOLD);
        } else if (GamesDisplay.gameDispLang.equalsIgnoreCase("guj")) {
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/muktavaani_gujarati.ttf");
            holder.title.setTypeface(custom_font, Typeface.BOLD);
        }

        holder.title.setText(gamesList.getNodeTitle());
        ex_path = new SdCardPath(mContext);
        sdCardPathString = ex_path.getSdCardPath();


        Glide.with(mContext).load(sdCardPathString+ "/StoryData/" +gamesList.getNodeImage()).into(holder.thumbnail);
        holder.game_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClicked.onGameClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
            return gamesViewList.size();
    }
}