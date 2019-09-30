package com.example.paymenttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.security.acl.Group;
import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Collections;

/*import static com.example.paymenttracker.TransactionsFragment.recyclerViewAdapter;
import static com.example.paymenttracker.TransactionsFragment.transactions;*/

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner, getResources().getStringArray(R.array.categories));
        adapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(adapter);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.transactionRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                TextView textViewName = (TextView) findViewById(R.id.textViewName);

                switch (i) {
                    case R.id.paidOnBehalfRadioButton:
                        textViewName.setText(R.string.borrower);
                        break;
                    case R.id.paidForYouRadioButton:
                        textViewName.setText(R.string.lender);
                        break;
                    default:
                        textViewName.setText(R.string.recipient);
                }
            }
        });

        ImageButton back_button = (ImageButton) findViewById(R.id.imageButton);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final TextView editDate = (TextView) findViewById(R.id.editDate);
        final EditText editTextName = (EditText) findViewById(R.id.editTextName);
        final EditText editTextAmount = (EditText) findViewById(R.id.editTextAmount) ;
        final EditText editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        Calendar myCalendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
        editDate.setText(dateFormat.format(myCalendar.getTime()));

       editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment calendar = new DateSelectorFragment();
                calendar.show(getSupportFragmentManager(), "dateSelector");
            }
        });

       Button createButton = (Button) findViewById(R.id.createButton);
       createButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (TextUtils.isEmpty(editTextName.getText()) && TextUtils.isEmpty(editTextAmount.getText())){
                   editTextName.setError("Required Field");
                   editTextAmount.setError("Required Field");
               }
               else{
                   Intent send_data = new Intent();
                   send_data.putExtra("date", editDate.getText().toString());
                   send_data.putExtra("name", editTextName.getText().toString());
                   send_data.putExtra("amount", Float.valueOf(editTextAmount.getText().toString()));
                   send_data.putExtra("description", editTextDescription.getText().toString());
                   send_data.putExtra("category", spinner.getSelectedItemPosition());
                   if (radioGroup.getCheckedRadioButtonId() == R.id.paidOnBehalfRadioButton) {
                       send_data.putExtra("isExpense", false);
                       System.out.println("LOL GOT EMM");
                   }

                   setResult(MainActivity.REQUEST_CODE, send_data);
                   finish();
               }
           }
       });

    }
}
