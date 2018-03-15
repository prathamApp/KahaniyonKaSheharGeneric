package com.example.pefpr.kahaniyonkashehar.activities;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.GroupDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.VillageDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Group;
import com.example.pefpr.kahaniyonkashehar.util.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UnitForm extends BaseActivity {

    ArrayAdapter<String> unitFormAdapter;

    @BindView(R.id.sp_block)
    Spinner sp_block;
    @BindView(R.id.sp_state)
    Spinner sp_state;
    @BindView(R.id.tv_village)
    EditText tv_village;
    @BindView(R.id.tv_unitname)
    EditText tv_unitname;
    @BindView(R.id.tv_school)
    EditText tv_school;
    @BindView(R.id.btn_submitunit)
    Button btn_submitunit;

    ArrayList<String> stateData = new ArrayList<>();
    List<String> statelist;
    List<String> blocklist;
    ArrayAdapter<String> dataAdapter;
    VillageDBHelper Vdatabase;
    String selectedState, selectedBlock;
    GroupDBHelper groupDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_form);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        statelist = new ArrayList<String>();

        Vdatabase = new VillageDBHelper(UnitForm.this);
        statelist = Vdatabase.GetState();
        groupDBHelper = new GroupDBHelper(UnitForm.this);

        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, statelist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        sp_state.setAdapter(dataAdapter);

        blocklist = new ArrayList<String>();

        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                selectedState = statelist.get(position);
                if (!selectedState.contains("Select State")) {
                    Toast.makeText(UnitForm.this, "" + selectedState, Toast.LENGTH_SHORT).show();
                    ShowBlocks(selectedState);
                } else {
                    blocklist.clear();

                    blocklist.add("- Select Block -");
                    dataAdapter = new ArrayAdapter<String>(UnitForm.this, R.layout.spinner_layout, blocklist);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                    sp_block.setAdapter(dataAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        sp_block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (!blocklist.get(position).contains("Select Block")) {
                    Toast.makeText(UnitForm.this, "" + blocklist.get(position), Toast.LENGTH_SHORT).show();
                    selectedBlock = blocklist.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void ShowBlocks(String selectedState) {

        blocklist = Vdatabase.GetStatewiseBlock(selectedState);
        dataAdapter = new ArrayAdapter<String>(UnitForm.this, R.layout.spinner_layout, blocklist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        sp_block.setAdapter(dataAdapter);
    }

    private void initializeAdapter(AutoCompleteTextView autoCompleteTextView, ArrayList<String> formData) {
        unitFormAdapter = new ArrayAdapter<String>(UnitForm.this, android.R.layout.simple_dropdown_item_1line, formData);
        autoCompleteTextView.setAdapter(unitFormAdapter);
    }

    @OnClick({R.id.btn_submitunit})
    public void validateFormData() {

        String str_village, str_block, str_state, str_unitname, str_school, deviceID;
        str_village = String.valueOf(tv_village.getText());
        str_school = String.valueOf(tv_school.getText());
        str_unitname = String.valueOf(tv_unitname.getText());

        if (str_unitname.length() == 11) {

            if ((str_village.matches("[a-zA-Z0-9 ]*")) && (str_school.matches("[a-zA-Z0-9 ]*"))
                    && (str_unitname.matches("[a-zA-Z0-9]*"))) {

                int vilID = Vdatabase.GetVillageIDByBlock(selectedBlock);

                deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                Group grpobj = new Group();

                grpobj.GroupID = String.valueOf(UUID.randomUUID());
                grpobj.GroupCode = "";
                grpobj.GroupName = str_unitname;
                grpobj.UnitNumber = "";
                grpobj.VillageName = str_village;
                grpobj.DeviceID = deviceID.equals(null) ? "0000" : deviceID;
                grpobj.SchoolName = str_school;
                grpobj.Responsible = "";
                grpobj.ResponsibleMobile = "";
                grpobj.CreatedBy = "NA";
                grpobj.newGroup = true;
                grpobj.VillageID = vilID;
                grpobj.ProgramID = 2;

                groupDBHelper.insertData(grpobj);
                BackupDatabase.backup(UnitForm.this);
                clearForm();
            }
        }

        sp_state.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(UnitForm.this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(sp_state, InputMethodManager.SHOW_IMPLICIT);

    }

    private void clearForm() {
        blocklist.clear();
        sp_state.setSelection(0);
        tv_unitname.setText("");
        tv_school.setText("");
        tv_village.setText("");

        blocklist.add("- Select Block -");

    }

    private void getViewFocus(Spinner spinner) {
        spinner.requestFocus();
    }

}
