package com.example.pefpr.kahaniyonkashehar.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.adapters.AttendenceAdapter;
import com.example.pefpr.kahaniyonkashehar.interfaces.ItemClicked;
import com.example.pefpr.kahaniyonkashehar.modalclasses.AttendenceView;

import java.util.ArrayList;
import java.util.List;


public class ContentDisplay extends AppCompatActivity implements ItemClicked {

    private RecyclerView recyclerView;
    private AttendenceAdapter attendenceAdapter;
    private List<AttendenceView> attendenceViewList;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);
        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.attendnce_recycler_view);

        attendenceViewList = new ArrayList<>();


        attendenceAdapter = new AttendenceAdapter(this, attendenceViewList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(5, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(attendenceAdapter);

        prepareAlbums();

    }

    private void prepareAlbums() {
        for (int i = 0; i < 8; i++) {
            AttendenceView attendenceListView = new AttendenceView("Naruto", "id" + i, R.drawable.avatar);
            attendenceViewList.add(attendenceListView);
        }
        AttendenceView attendenceListView = new AttendenceView("Naruto Uzumaki", "id", R.drawable.avatarmale);
        attendenceViewList.add(attendenceListView);
    }

    @Override
    public void onCardItemClicked(int position, String id) {
/*
        resourceViewList.clear();
        cardAdapter.notifyDataSetChanged();
*/

    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + avatar) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + avatar) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
