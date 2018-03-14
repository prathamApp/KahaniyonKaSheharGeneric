package com.example.pefpr.kahaniyonkashehar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.AttendenceDisplay;
import com.example.pefpr.kahaniyonkashehar.animations.MyBounceInterpolator;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.CrlDBHelper;
import com.example.pefpr.kahaniyonkashehar.util.PD_Utility;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pravin on 23/11/2017.
 */

public class FragmentLogin extends Fragment {

    @BindView(R.id.login_layout)
    LinearLayout login_layout;
    @BindView(R.id.et_userName)
    EditText et_userName;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.btn_skip)
    Button btn_skip;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        animateScreen();
    }

    @OnClick(R.id.btn_login)
    public void validateLogin() {
        CrlDBHelper crlDBHelper = new CrlDBHelper(getActivity());
        String uName = et_userName.getText().toString(), pwd = et_password.getText().toString();
        if (!(uName.equals("") || (pwd.equals("")))) {
            if (crlDBHelper.crlExist(uName)) {
                //Valid Username
                if (crlDBHelper.CrlLogin(uName, pwd)) {
                    //Valid User
                    clearForm(login_layout);
                    Intent main = new Intent(getActivity(), AttendenceDisplay.class);
                    startActivity(main);
                } else {
                    Toast.makeText(getActivity(), "Invalid Password!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Invalid Username!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Fill Proper Details!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_skip)
    public void skipLogin() {
        clearForm(login_layout);
        Intent main = new Intent(getActivity(), AttendenceDisplay.class);
        startActivity(main);
    }

    @OnClick(R.id.btn_sync)
    public void syncData() {
        clearForm(login_layout);
        //TODO sync data operations
    }

        public void animateScreen() {
        Animation rubber = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 15);
        rubber.setInterpolator(interpolator);
        login_layout.startAnimation(rubber);
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }
}
