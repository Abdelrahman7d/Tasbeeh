package com.jsla.tasbeeh;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class timePicker extends DialogFragment {
    @NonNull
        @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int hour = 5;
        int minute = 0;
        return new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener)getActivity(),hour,minute, DateFormat.is24HourFormat(getActivity()));
    }
}
