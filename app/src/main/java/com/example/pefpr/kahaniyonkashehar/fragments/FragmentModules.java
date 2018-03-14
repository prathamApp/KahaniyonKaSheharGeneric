package com.example.pefpr.kahaniyonkashehar.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.AdminConsole;
import com.example.pefpr.kahaniyonkashehar.activities.GamesDisplay;
import com.example.pefpr.kahaniyonkashehar.activities.RegistrationForm;
import com.example.pefpr.kahaniyonkashehar.contentplayer.WebViewActivity;
import com.example.pefpr.kahaniyonkashehar.interfaces.ViewPagerFragmentReloaded;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.CrlDBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pefpr on 28/11/2017.
 */


public class FragmentModules extends Fragment implements ViewPagerFragmentReloaded {
    /*    @BindView(R.id.btn_add_unit)
        ImageButton btn_add_unit;*/
    @BindView(R.id.btn_add_student)
    ImageButton btn_add_student;
    @BindView(R.id.btn_admin)
    ImageButton btn_admin;
    CrlDBHelper crlDBHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modules, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        crlDBHelper = new CrlDBHelper(getActivity());

    }

    public void animateModuleButtons() {
        // Animation purpose
        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        rotation.setFillAfter(true);
        btn_add_student.startAnimation(rotation);
        btn_admin.startAnimation(rotation);
//        btn_add_unit.startAnimation(rotation);
    }

    @OnClick(R.id.btn_admin)
    public void adminModule() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.crl_login_dialog_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button dLogin = dialog.findViewById(R.id.d_btn_login);
        Button dCancle = dialog.findViewById(R.id.d_btn_cancle);
        final TextView d_uName = dialog.findViewById(R.id.d_uname_crl);
        final TextView d_password = dialog.findViewById(R.id.d_password_crl);


        dCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        dLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = d_uName.getText().toString();
                String uPassword = d_password.getText().toString();
                Boolean crlCheck = crlDBHelper.CrlLogin(uName,uPassword);
                if(crlCheck) {
                    d_uName.setText("");
                    d_password.setText("");
                    startActivity(new Intent(getActivity(), AdminConsole.class));
                    dialog.dismiss();
                }
                else
                    Toast.makeText(getActivity(), "User Name or Password invalid", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick(R.id.btn_add_student)
    public void startLearningModule() {
        Intent main = new Intent(getActivity(), RegistrationForm.class);
        startActivity(main);
    }

    @Override
    public void onViewPagerFragmentReloaded() {
        //todo animations reload
        animateModuleButtons();
    }

/*    @OnClick(R.id.btn_add_unit)
    public void startAssessment() {
        Intent main = new Intent(getActivity(), UnitForm.class);
        startActivity(main);
    }

    @OnClick(R.id.btn_add_admin)
    public void addAdmin() {
        Intent main = new Intent(getActivity(), AdminForm.class);
        startActivity(main);
    }
*/
}