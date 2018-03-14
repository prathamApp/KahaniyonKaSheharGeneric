package com.example.pefpr.kahaniyonkashehar.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.pefpr.kahaniyonkashehar.R;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    int year;
    int month;
    int day;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);

        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        return dialog;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthofyear, int dayOfMonth) {
        this.year = year;
        this.month = monthofyear + 1;
        this.day = dayOfMonth;

        TextView tv_date = (TextView) getActivity().findViewById(R.id.tv_date);

        if (this.month < 10 && this.day < 10) {
            tv_date.setText("0" + dayOfMonth + "-0" + month + "-" + year);
        } else if (this.month < 10) {
            tv_date.setText("" + dayOfMonth + "-0" + month + "-" + year);
        } else if (this.day < 10) {
            tv_date.setText("0" + dayOfMonth + "-" + month + "-" + year);
        } else {
            tv_date.setText("" + dayOfMonth + "-" + month + "-" + year);
        }
    }
}
