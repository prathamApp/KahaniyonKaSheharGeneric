package com.example.pefpr.kahaniyonkashehar.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.adapters.AttendenceAdapter;
import com.example.pefpr.kahaniyonkashehar.interfaces.ItemClicked;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.GroupDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StudentDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.AttendenceView;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Group;
import com.example.pefpr.kahaniyonkashehar.modalclasses.StudentList;

import java.util.ArrayList;
import java.util.List;

public class AttendenceDisplay extends AppCompatActivity implements ItemClicked {

    public static String currentStudentId;
    boolean studentsShownflag = false;
    ImageView img_admin;
    WeakHandler mHandler;
    private RecyclerView recyclerView;
    private AttendenceAdapter adapter;
    private List<AttendenceView> attendenceViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_display);
        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        attendenceViewList = new ArrayList<>();
        adapter = new AttendenceAdapter(this, attendenceViewList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 6);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(6, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        prepareAlbums();
    }

    private void prepareAlbums() {

        GroupDBHelper groupDBHelper = new GroupDBHelper(this);
        List<Group> groups = groupDBHelper.GetAll();
        if (groups!=null && groups.size()>0) {
            for (int i = 0; i < groups.size(); i++) {
                AttendenceView attendenceView = new AttendenceView(groups.get(i).GroupName, groups.get(i).GroupID, R.drawable.avatarmale);
                attendenceViewList.add(attendenceView);
            }
            adapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this, "No groups registered... Register and try! ", Toast.LENGTH_SHORT).show();
            mHandler = new WeakHandler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AttendenceDisplay.this.finish();
                }
            }, 2000);
        }
    }

    @Override
    public void onCardItemClicked(int position, String id) {
        attendenceViewList.clear();

//        if (!studentsShownflag) {
//            studentsShownflag = true;
//            //Get Students of Group ID 'id'
//            StudentDBHelper studentDBHelper = new StudentDBHelper(this);
//            List<StudentList> students = studentDBHelper.GetAllStudentsByGroupID(id);
//            for (int i = 0; i < students.size(); i++) {
//                StudentList student = students.get(i);
//                AttendenceView attendenceView = new AttendenceView(student.getStudentName(), student.getStudentID(), R.drawable.avatar);
//                attendenceViewList.add(attendenceView);
//            }
//
//            adapter.notifyDataSetChanged();
//        } else {
//            currentStudentId = id;
//            startActivity(new Intent(AttendenceDisplay.this, StoriesDisplay.class));
//        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
