package com.example.pefpr.kahaniyonkashehar.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.fragments.DatePickerFragment;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.AserDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StudentDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Student;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.pefpr.kahaniyonkashehar.KksApplication.getCurrentDate;

public class RegistrationForm extends AppCompatActivity {

    //    @BindView(R.id.sp_unit)
//    Spinner sp_unit;
//    @BindView(R.id.sp_class)
//    Spinner sp_class;
//    @BindView(R.id.sp_language)
//    Spinner sp_language;
//    @BindView(R.id.sp_numbers)
//    Spinner sp_numbers;
//    @BindView(R.id.sp_examtype)
//    Spinner sp_examtype;
//
//    @BindView(R.id.ch_add)
//    CheckBox ch_add;
//    @BindView(R.id.ch_sub)
//    CheckBox ch_sub;
//    @BindView(R.id.ch_mul)
//    CheckBox ch_mul;
//    @BindView(R.id.ch_div)
//    CheckBox ch_div;
//    @BindView(R.id.ch_addword)
//    CheckBox ch_addword;
//    @BindView(R.id.ch_subword)
//    CheckBox ch_subword;
//
//    @BindView(R.id.tv_wordproblems)
//    TextView tv_wordproblems;
//    @BindView(R.id.tv_operations)
//    TextView tv_operations;
//    @BindView(R.id.tv_ece)
//    TextView tv_ece;
//    @BindView(R.id.tv_stud_cls)
//    TextView tv_stud_cls;

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
    @BindView(R.id.ll_studentform)
    LinearLayout ll_studentform;

//    @BindView(R.id.et_childId)
//    EditText et_childId;


//    @BindView(R.id.ll_endlinedata)
//    LinearLayout ll_endlinedata;
//    @BindView(R.id.ll_language)
//    LinearLayout ll_language;
//    @BindView(R.id.ll_class)
//    LinearLayout ll_class;
//    @BindView(R.id.ll_operations)
//    LinearLayout ll_operations;
//    @BindView(R.id.ll_numbers)
//    LinearLayout ll_numbers;
//    @BindView(R.id.ll_examtype)
//    LinearLayout ll_examtype;


    Typeface font;
    DatePickerFragment datePickerFragment;
    RadioButton radioButton;
    //    boolean eceFlag = false;
    int classPos, examPos, langPos, numPos, oAdd, oSub, oMul, oDiv, wAdd, wSub, unitPos;
    String selectedDate = "", grp_Id = null, sName, fName, sAge, sChildId, selectedId, gender, sClass;
    String examType, langType, numType, randomUUIDStudent;

    StudentDBHelper studentDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_student);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        font = Typeface.createFromAsset(getAssets(), "fonts/tektonpro-bold.otf");

        String currDate = getCurrentDate();
        tv_date.setText("" + currDate);

        rb_female.setTypeface(font);
        rb_male.setTypeface(font);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        studentDBHelper = new StudentDBHelper(this);

//        tv_wordproblems.setVisibility(View.GONE);
//        ch_addword.setVisibility(View.GONE);
//        ch_subword.setVisibility(View.GONE);
//
//        ch_add.setTypeface(font);
//        ch_addword.setTypeface(font);
//        ch_sub.setTypeface(font);
//        ch_subword.setTypeface(font);
//        ch_div.setTypeface(font);
//        ch_mul.setTypeface(font);

/*        eceFlag = true;
        hideShowFormContent(eceFlag);*/

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(getFragmentManager(), "DatePicker");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fName, mName, lName, sGender, sDate, sVillage, studentUID;
                int sAge = 0;

                fName = ""+(et_studentName.getText()).toString();
                mName = "" + et_fatherName.getText();
                lName = ""+(et_LastName.getText()).toString();
                sDate = ""+(tv_date.getText()).toString();
                if(et_age.getText().toString().length()!=0)
                    sAge = Integer.parseInt(et_age.getText().toString());
                sVillage = ""+(et_villageName.getText()).toString();

                studentUID = KksApplication.getUniqueID().toString();

                if (rb_female.isChecked())
                    sGender = "female";
                else
                    sGender = "male";


                StudentDBHelper studentDBHelper = new StudentDBHelper(RegistrationForm.this);
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
                    student.setDeviceId("");

                    studentDBHelper.insertData(student);
                    Toast.makeText(RegistrationForm.this, "Inserted", Toast.LENGTH_SHORT).show();
                    clearForm(ll_studentform);
                } else {
                    Toast.makeText(RegistrationForm.this, "Incorrect Data", Toast.LENGTH_SHORT).show();
                }

            }

        });

/*        tv_ece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eceFlag = true;
                hideShowFormContent(eceFlag);
            }
        });

        tv_stud_cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eceFlag = false;
                hideShowFormContent(eceFlag);
            }
        });

        ch_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch_add.isChecked()) {
                    tv_wordproblems.setVisibility(View.VISIBLE);
                    ch_addword.setVisibility(View.VISIBLE);
                } else {
                    if (ch_sub.isChecked()) {
                        ch_addword.setChecked(false);
                        ch_addword.setVisibility(View.GONE);
                    } else {
                        ch_addword.setChecked(false);
                        tv_wordproblems.setVisibility(View.GONE);
                        ch_addword.setVisibility(View.GONE);
                    }
                }
            }
        });

        ch_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch_sub.isChecked()) {
                    tv_wordproblems.setVisibility(View.VISIBLE);
                    ch_subword.setVisibility(View.VISIBLE);
                } else {
                    if (ch_add.isChecked()) {
                        ch_subword.setChecked(false);
                        ch_subword.setVisibility(View.GONE);
                    } else {
                        tv_wordproblems.setVisibility(View.GONE);
                        ch_subword.setChecked(false);
                        ch_subword.setVisibility(View.GONE);
                    }
                }
            }
        });*/


/*        GroupDBHelper groupDBHelper = new GroupDBHelper(this);
        final List<Group> AllGroups = groupDBHelper.getAll();*/

/***********************///Unit
/*        List<String> unitlist = new ArrayList<>();
        unitlist.add("- Select Unit -");

        for (int i = 0; i < AllGroups.size(); i++)
            unitlist.add(AllGroups.get(i).GroupName);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_layout, unitlist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        sp_unit.setAdapter(dataAdapter);*/
/***********************///Unit

/***********************///Exam
/*        List<String> examlist = new ArrayList<>();
        examlist.add("- Select Exam -");
        examlist.add("Baseline");
        examlist.add("Endline 1");
        examlist.add("Endline 2");
        examlist.add("Endline 3");
        examlist.add("Endline 4");
        dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, examlist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        sp_examtype.setAdapter(dataAdapter);*/
/***********************///Exam

/***********************///Class
/*        List<String> classlist = new ArrayList<>();
        classlist.add("- Select Class -");
        classlist.add("3");
        classlist.add("4");
        classlist.add("5");
        dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, classlist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        sp_class.setAdapter(dataAdapter);*/
/***********************///Class

/***********************///Num
/*        List<String> numlist = new ArrayList<>();
        numlist.add("- Number Recog -");
        numlist.add("Beginer");
        numlist.add("0-9");
        numlist.add("10-99");
        numlist.add("100-999");
        dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, numlist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        sp_numbers.setAdapter(dataAdapter);*/
/***********************///Num

/***********************///Lang
/*        List<String> langlist = new ArrayList<>();
        langlist.add("- Select Lang -");
        langlist.add("Beginer");
        langlist.add("Letter");
        langlist.add("Word");
        langlist.add("Para");
        langlist.add("Story");
        dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, langlist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        sp_language.setAdapter(dataAdapter);*/
/***********************///Lang

/*        sp_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener
                () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    grp_Id = AllGroups.get(position - 1).GroupID;
                    unitPos = position;
                    Toast.makeText(RegistrationForm.this, grp_Id + " " + unitPos, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener
                () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    classPos = position + 2;
                    sClass = (String) sp_class.getSelectedItem();
                    Toast.makeText(RegistrationForm.this, sp_class.getSelectedItem() + " Class " + classPos, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_examtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener
                () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position < 2) {
                    examPos = position;
                    ll_endlinedata.setVisibility(View.GONE);
                } else {
                    examPos = position;
                    ll_endlinedata.setVisibility(View.VISIBLE);
                }

                ch_addword.setChecked(false);
                ch_subword.setChecked(false);
                ch_div.setChecked(false);
                ch_add.setChecked(false);
                ch_sub.setChecked(false);
                ch_mul.setChecked(false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener
                () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    langPos = position;
                    Toast.makeText(RegistrationForm.this, "language" + langPos, Toast.LENGTH_SHORT).show();
                } else
                    langPos = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_numbers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener
                () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    numPos = position;
                    Toast.makeText(RegistrationForm.this, "numPos" + numPos, Toast.LENGTH_SHORT).show();
                } else
                    numPos = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        /*btn_submitstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID uuStdid = UUID.randomUUID();
                randomUUIDStudent = uuStdid.toString();

                String sName = String.valueOf(et_studentName.getText());
                String fName = String.valueOf(et_fatherName.getText());
                String sAge = String.valueOf(et_age.getText());
                String sVillage = String.valueOf(et_villageName.getText());
                String sSchool = String.valueOf(et_schoolName.getText());
                String sUserName = String.valueOf(et_userName.getText());
                String sPassword = String.valueOf(et_password.getText());

//                String sChildId = String.valueOf(et_childId.getText());
                String sChildId = "";

                int selectedId = rg_gender.getCheckedRadioButtonId();
//                radioButton.getId();
                radioButton = (RadioButton) findViewById(selectedId);
                String gender = (String) radioButton.getText();

                Student std = new Student();
                Aser aser = new Aser();
                String deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

//                if (eceFlag) {

*//*                    if (unitPos == 0)
                        Toast.makeText(RegistrationForm.this, "Spinners", Toast.LENGTH_SHORT).show();*//*
                    if (sName.length() == 0 || fName.length() == 0 || sAge.length() == 0 || sVillage.length()==0 || sUserName.length()==0 || sPassword.length()==0 *//*|| sChildId.length() != 4*//*) {
                        Toast.makeText(RegistrationForm.this, "Please enter all data correctly", Toast.LENGTH_SHORT).show();
                    } else {
                        //Insert In DB
                        oAdd = 0;
                        oSub = 0;
                        oMul = 0;
                        oDiv = 0;
                        wAdd = 0;
                        wSub = 0;
                        examPos = 0;
                        langPos = 0;
                        numPos = 0;

                        std.StudentID = randomUUIDStudent;
                        std.FirstName = sName;
                        std.MiddleName = fName;
                        std.LastName = sVillage;
                        std.Class = 0;
                        std.Age = Integer.parseInt(sAge);
                        std.Gender = gender;
                        std.UpdatedDate = selectedDate;
                        std.GroupID = sPassword;
                        std.newStudent = true;
                        std.CreatedBy = sSchool;
                        std.StudentUID = sUserName;
                        std.IsSelected = false;

                        studentDBHelper.insertUniversalChildData(std);
                        Toast.makeText(RegistrationForm.this, "In Student"+studentDBHelper.Get(randomUUIDStudent).FirstName, Toast.LENGTH_SHORT).show();
                        BackupDatabase.backup(RegistrationForm.this);

                    }
*//*                } else {

                    String idFirstChar = String.valueOf(sChildId.charAt(0));
                    if (unitPos == 0)
                        Toast.makeText(RegistrationForm.this, "Spinners", Toast.LENGTH_SHORT).show();
                    else if (sName.length() == 0 || fName.length() == 0 || sAge.length() == 0 || sChildId.length() != 4) {
                        Toast.makeText(RegistrationForm.this, "Please enter all data correctly", Toast.LENGTH_SHORT).show();
                    } else if (!idFirstChar.equalsIgnoreCase(sClass))
                        Toast.makeText(RegistrationForm.this, "Child Id should start with the same class", Toast.LENGTH_SHORT).show();
                    else if (unitPos == 0 || examPos == 0 || langPos == 0 || numPos == 0)
                        Toast.makeText(RegistrationForm.this, "Spinners", Toast.LENGTH_SHORT).show();
                    else {

                        if (ch_add.isChecked())
                            oAdd = 1;
                        else
                            oAdd = 0;
                        if (ch_sub.isChecked())
                            oSub = 1;
                        else
                            oSub = 0;
                        if (ch_mul.isChecked())
                            oMul = 1;
                        else
                            oMul = 0;
                        if (ch_div.isChecked())
                            oDiv = 1;
                        else
                            oDiv = 0;
                        if (ch_add.isChecked() && ch_addword.isChecked())
                            wAdd = 1;
                        else
                            wAdd = 0;
                        if (ch_sub.isChecked() && ch_subword.isChecked())
                            wSub = 1;
                        else
                            wSub = 0;

                        std.StudentID = randomUUIDStudent;
                        std.FirstName = sName;
                        std.MiddleName = fName;
                        std.LastName = "";
                        std.Class = 0;
                        std.Age = Integer.parseInt(sAge);
                        std.Gender = gender;
                        std.UpdatedDate = selectedDate;
                        std.GroupID = grp_Id;
                        std.newStudent = true;
                        std.CreatedBy = "CRL";
                        std.StudentUID = "kks 0";
                        std.IsSelected = false;

                        studentDBHelper.insertUniversalChildData(std);

                        aser.StudentId = randomUUIDStudent;
                        aser.GroupID = grp_Id;
                        aser.ChildID = sChildId;
                        aser.TestType = examPos;
                        aser.TestDate = selectedDate;
                        aser.Lang = langPos;
                        aser.Num = numPos;
                        aser.CreatedBy = "CRL";
                        aser.CreatedDate = selectedDate;
                        aser.DeviceId = deviceID.equals(null) ? "0000" : deviceID;
                        aser.FLAG = 0;
                        aser.OAdd = oAdd;
                        aser.OSub = oSub;
                        aser.OMul = oMul;
                        aser.ODiv = oMul;
                        aser.WAdd = wAdd;
                        aser.WSub = wSub;

                        aserDBHelper.insertData(aser);
                        BackupDatabase.backup(RegistrationForm.this);

                    }*//*
            }
        });

    }*/

/*    private void hideShowFormContent(boolean studtype) {

        if (studtype) {
            tv_ece.setBackgroundResource(R.drawable.loginbackground);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tv_stud_cls.setBackground(null);
            }
            ll_examtype.setVisibility(View.GONE);
            ll_class.setVisibility(View.GONE);
            ll_language.setVisibility(View.GONE);
            ll_numbers.setVisibility(View.GONE);
            ll_operations.setVisibility(View.GONE);
            sp_language.setVisibility(View.GONE);
            sp_numbers.setVisibility(View.GONE);
            tv_operations.setVisibility(View.GONE);
            tv_operations.setVisibility(View.GONE);
            tv_wordproblems.setVisibility(View.GONE);
            sp_class.setVisibility(View.GONE);
            ch_add.setVisibility(View.GONE);
            ch_div.setVisibility(View.GONE);
            ch_mul.setVisibility(View.GONE);
            ch_sub.setVisibility(View.GONE);
            ch_subword.setVisibility(View.GONE);
            ch_addword.setVisibility(View.GONE);

        } else {
            tv_stud_cls.setBackgroundResource(R.drawable.loginbackground);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tv_ece.setBackground(null);
            }
            ll_examtype.setVisibility(View.VISIBLE);
            ll_class.setVisibility(View.VISIBLE);
            ll_language.setVisibility(View.VISIBLE);
            ll_numbers.setVisibility(View.VISIBLE);
            ll_operations.setVisibility(View.VISIBLE);
            sp_examtype.setVisibility(View.VISIBLE);
            sp_language.setVisibility(View.VISIBLE);
            sp_numbers.setVisibility(View.VISIBLE);
            tv_operations.setVisibility(View.VISIBLE);
            sp_class.setVisibility(View.VISIBLE);
            tv_operations.setVisibility(View.VISIBLE);
            //tv_wordproblems.setVisibility(View.VISIBLE);
            ch_add.setVisibility(View.VISIBLE);
            ch_div.setVisibility(View.VISIBLE);
            ch_mul.setVisibility(View.VISIBLE);
            ch_sub.setVisibility(View.VISIBLE);
        }

    }*/
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
            if (view instanceof Spinner) {
                ((Spinner) view).setSelection(0, true);
            }
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }
}