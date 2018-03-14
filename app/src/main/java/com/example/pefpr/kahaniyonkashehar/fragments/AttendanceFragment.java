package com.example.pefpr.kahaniyonkashehar.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.StoriesDisplay;
import com.example.pefpr.kahaniyonkashehar.activities.StoryOrGame;
import com.example.pefpr.kahaniyonkashehar.adapters.AttendenceAdapter;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.interfaces.ItemClicked;
import com.example.pefpr.kahaniyonkashehar.interfaces.ViewPagerFragmentReloaded;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.AttendanceDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.LevelDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.SessionDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StudentDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.AttendenceView;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Student;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceFragment extends Fragment implements ItemClicked, ViewPagerFragmentReloaded {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static String currentStudentId;
    boolean studentsShownflag = false;
    ImageView img_admin;
    WeakHandler mHandler;
    private AttendenceAdapter adapter;
    private List<AttendenceView> attendenceViewList;
    List<Student> students;
    SessionDBHelper sessionDBHelper;
    AttendanceDBHelper attendanceDBHelper;
    StatusDBHelper statusDBHelper;
    StudentDBHelper studentDBHelper;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        sessionDBHelper = new SessionDBHelper(getActivity());
        attendanceDBHelper = new AttendanceDBHelper(getActivity());
        statusDBHelper = new StatusDBHelper(getActivity());
        studentDBHelper = new StudentDBHelper(getActivity());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 5);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(5, dpToPx(20), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        prepareAlbums();

    }

    public void prepareAlbums() {

        attendenceViewList = new ArrayList<>();

        students = new ArrayList<Student>();
        students = studentDBHelper.GetAllNewStudents();

        if (students != null && students.size() > 0) {

            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                int studentAvatar = R.drawable.avatarfemale;
                if (student.getGender().equalsIgnoreCase("male"))
                    studentAvatar = R.drawable.avatarmale;
                AttendenceView attendenceView = new AttendenceView(student.getFirstName(), student.getStudentID(), studentAvatar);
                attendenceViewList.add(attendenceView);
            }
            adapter = new AttendenceAdapter(getActivity(), attendenceViewList, this);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "No Students registered... Register and try! ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCardItemClicked(int position, String id) {

        Intent intent;
        String sessionID = KksApplication.getUniqueID().toString();
        Boolean checkSessionEntry = sessionDBHelper.addToSessionTable(sessionID, KksApplication.getCurrentDateTime(), "NA");

        attendanceDBHelper.add(sessionID, id);
        statusDBHelper.Update("CurrentSession", "" + sessionID);

        LevelDBHelper levelDBHelper = new LevelDBHelper(getActivity());

        if (levelDBHelper.GetStudentLevelByStdID(id) == null) {
            intent = new Intent(getActivity(), StoriesDisplay.class);
            intent.putExtra("StudentID", id);
        } else {
            intent = new Intent(getActivity(), StoryOrGame.class);
            intent.putExtra("StudentID", id);
            intent.putExtra("StudentName", students.get(position).FirstName);
        }

        BackupDatabase.backup(getActivity());
        startActivity(intent);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onViewPagerFragmentReloaded() {
        prepareAlbums();
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
