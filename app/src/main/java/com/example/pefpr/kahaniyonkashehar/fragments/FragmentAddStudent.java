package com.example.pefpr.kahaniyonkashehar.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StudentDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Student;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.pefpr.kahaniyonkashehar.KksApplication.getCurrentDate;

/**
 * Created by pefpr on 28/11/2017.
 */


public class FragmentAddStudent extends Fragment {

    @BindView(R.id.et_studentName)
    EditText et_studentName;
    @BindView(R.id.et_fatherName)
    EditText et_fatherName;
    @BindView(R.id.et_LastName)
    EditText et_LastName;
    @BindView(R.id.rg_gender)
    RadioGroup rg_gender;
    @BindView(R.id.rb_male)
    RadioButton rb_male;
    @BindView(R.id.rb_female)
    RadioButton rb_female;
    @BindView(R.id.iv_date)
    ImageView iv_date;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.et_age)
    EditText et_age;
    @BindView(R.id.et_villageName)
    EditText et_villageName;
    @BindView(R.id.btn_submitstudent)
    Button submitBtn;

    Typeface font;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_student, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        String currDate = getCurrentDate();
        tv_date.setText("" + currDate);
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/tektonpro-bold.otf");
        rb_female.setTypeface(font);
        rb_male.setTypeface(font);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fName, mName, lName, sGender, sDate, sVillage, studentUID;
                int sAge = 0;

                fName = (et_studentName.getText()).toString();
                mName = "" + et_fatherName.getText();
                lName = (et_LastName.getText()).toString();
                sDate = (tv_date.getText()).toString();
                sAge = Integer.parseInt(et_age.getText().toString());
                sVillage = (et_villageName.getText()).toString();

                studentUID = KksApplication.getUniqueID().toString();

                if (rb_female.isChecked())
                    sGender = "female";
                else
                    sGender = "male";

                StudentDBHelper studentDBHelper = new StudentDBHelper(getActivity());
                StatusDBHelper statusDBHelper = new StatusDBHelper(getActivity());
                Student student = new Student();

                if (fName.length() != 0 || lName.length() != 0 || sVillage.length() != 0) {

                    student.setStudentID(studentUID);
                    student.setStudentUID(studentUID);
                    student.setFirstName(fName);
                    student.setMiddleName(mName);
                    student.setLastName(lName);
                    student.setGender(sGender);
                    student.setAge(sAge);
                    student.setVillageName(sVillage);
                    student.setRegDate(sDate);
                    student.setNewFlag(1);
                    student.setDeviceId(statusDBHelper.getValue("DeviceID"));

                    studentDBHelper.insertData(student);
                    Toast.makeText(getActivity(), "Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Incorrect Data", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
