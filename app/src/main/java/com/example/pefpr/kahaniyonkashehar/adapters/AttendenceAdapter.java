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
import com.example.pefpr.kahaniyonkashehar.interfaces.ItemClicked;
import com.example.pefpr.kahaniyonkashehar.modalclasses.AttendenceView;

import java.util.List;

/**
 * Created by Ameya on 23-Nov-17.
 */

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.MyViewHolder> {

    private Context mContext;
    private List<AttendenceView> attendenceViewList;
    ItemClicked itemClicked;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.attendencetitle);
            thumbnail = (ImageView) view.findViewById(R.id.attendencethumbnail);
        }
    }

    public AttendenceAdapter(Context mContext, List<AttendenceView> attendenceViewList, ItemClicked itemClicked) {
        this.mContext = mContext;
        this.attendenceViewList = attendenceViewList;
        this.itemClicked = itemClicked;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_card, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AttendenceView attendenceList = attendenceViewList.get(position);
        holder.title.setText(attendenceList.name);

        Glide.with(mContext).load(attendenceList.attendencethumbnail).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked.onCardItemClicked(position, attendenceList.attendenceId);
            }
        });

    }


    @Override
    public int getItemCount() {
        return attendenceViewList.size();
    }
}