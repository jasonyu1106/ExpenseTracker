package com.example.paymenttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Collections;

import static com.example.paymenttracker.TransactionsFragment.recyclerViewAdapter;
import static com.example.paymenttracker.TransactionsFragment.transactions;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner, getResources().getStringArray(R.array.categories));
        adapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textViewName = (TextView) findViewById(R.id.textViewName);
                System.out.println(i);

                switch(i){
                    case 1:
                        textViewName.setText(R.string.borrower);
                        break;
                    case 2:
                        textViewName.setText(R.string.lender);
                        break;
                    default:
                        textViewName.setText(R.string.recipient);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
                   TransactionsFragment.Transaction transaction = new TransactionsFragment.Transaction();
                   try {
                       transaction.setDate(dateFormat.parse(editDate.getText().toString()));
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }
                   transaction.setName(editTextName.getText().toString());
                   transaction.setAmount(Double.parseDouble(editTextAmount.getText().toString()));
                   transaction.setDescription(editTextDescription.getText().toString());
                   transaction.setCategory(spinner.getSelectedItemPosition());

                   transactions.add(transaction);
                   //TransactionsFragment.recyclerViewAdapter.notifyItemInserted(transactions.size()+1);

                   Collections.sort(transactions);
                   recyclerViewAdapter.notifyDataSetChanged();
                   finish();
               }
           }
       });

    }
}
