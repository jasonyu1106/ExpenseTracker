package com.example.paymenttracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateSelectorFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));

        SimpleDateFormat initDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String myDate = String.format(Locale.getDefault(),"%d/%d/%d", month+1, day, year);

        Date currDate = Calendar.getInstance().getTime();

        try {
            currDate = initDateFormat.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = dateFormat.format(currDate);

        TextView editDate = (TextView) getActivity().findViewById(R.id.editDate);
        editDate.setText(formattedDate);
    }
}
