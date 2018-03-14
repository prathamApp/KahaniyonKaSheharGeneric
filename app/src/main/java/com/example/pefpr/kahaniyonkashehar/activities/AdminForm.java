package com.example.pefpr.kahaniyonkashehar.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.CrlDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.VillageDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Crl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminForm extends AppCompatActivity {

    @BindView(R.id.login_layout)
    LinearLayout login_layout;

    @BindView(R.id.sp_crlState)
    Spinner sp_crlState;

    @BindView(R.id.et_crlFirstName)
    EditText et_crlName;
    @BindView(R.id.et_crlLastName)
    EditText et_crlLastName;
    @BindView(R.id.et_crlMobNumber)
    EditText et_crlMobNumber;
    @BindView(R.id.et_crlMailId)
    EditText et_crlMailId;
    @BindView(R.id.et_crlUserName)
    EditText et_crlUserName;
    @BindView(R.id.et_crlPassword)
    EditText et_crlPassword;

    Typeface font;
    List<String> statelist;
    VillageDBHelper vDatabase;
    ArrayAdapter<String> dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_form);
        ButterKnife.bind(this);
        font = Typeface.createFromAsset(getAssets(), "fonts/tektonpro-bold.otf");
        getSupportActionBar().hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        statelist = new ArrayList<String>();
        vDatabase = new VillageDBHelper(AdminForm.this);
        statelist = vDatabase.GetState();
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, statelist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        sp_crlState.setAdapter(dataAdapter);

    }

    @OnClick({R.id.btn_submitunit})
    public void validateFormData() {

        String fName, lName, mNumber, mailID , uName, password, state;
        fName = et_crlName.getText().toString();
        lName = et_crlLastName.getText().toString();
        mNumber = et_crlMobNumber.getText().toString();
        mailID = et_crlMailId.getText().toString();
        uName = et_crlUserName.getText().toString();
        password = et_crlPassword.getText().toString();
        state = sp_crlState.getSelectedItem().toString();

        if (!fName.equals("") && !lName.equals("") && !mNumber.equals("") && mNumber.length() == 10  && !uName.equals("") && !password.equals("") && !state.equals("-- Select State --")) {
            Toast.makeText(this, "Submitted!!!", Toast.LENGTH_SHORT).show();
            insertInCrlTable(fName,lName,mNumber,uName,password,state,mailID);
            clearForm(login_layout);
        } else {
            if (mNumber.length() < 10) {
                Toast.makeText(this, "Mobile number should be 10 digits!!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter proper details!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void insertInCrlTable(String fName, String lName, String mNumber, String uName, String password, String state, String mailID) {
        try {
            CrlDBHelper crlDBHelper = new CrlDBHelper(this);
            Crl crl = new Crl();
            crl.setCRLId(UUID.randomUUID().toString());
            crl.setEmail(mailID);
            crl.setFirstName(fName);
            crl.setLastName(lName);
            crl.setMobile(mNumber);
            crl.setUserName(uName);
            crl.setPassword(password);
            crl.setState(state);
            crl.setProgramId(2);
            crl.newCrl = true;
            crl.CreatedBy="";

            crlDBHelper.insertData(crl);
            BackupDatabase.backup(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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