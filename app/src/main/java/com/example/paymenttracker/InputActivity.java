package com.example.paymenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

//        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.transactionRadioGroup);
////        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(RadioGroup radioGroup, int i) {
////                TextView textViewName = (TextView) findViewById(R.id.textViewName);
////
////                switch (i) {
////                    case R.id.paidOnBehalfRadioButton:
////                        textViewName.setText(R.string.borrower);
////                        break;
////                    case R.id.paidForYouRadioButton:
////                        textViewName.setText(R.string.lender);
////                        break;
////                    default:
////                        textViewName.setText(R.string.recipient);
////                }
////            }
////        });

        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView textViewName = (TextView) findViewById(R.id.textViewName);

        final Bundle extras = getIntent().getExtras();

        if (extras != null){
            final int type = extras.getInt("type", TransactionType.SPEND);

            switch (type){
                case TransactionType.LEND:
                    textViewTitle.setText(R.string.input_lend_title);
                    textViewName.setText(R.string.borrower);
                    break;
                default:
                    textViewTitle.setText(R.string.input_spend_title);
                    textViewName.setText(R.string.vendor);
            }

            ImageButton back_button = (ImageButton) findViewById(R.id.imageButton);
            back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent send_data= new Intent();
                    setResult(MainActivity.RESULT_CANCELLED, send_data);
                    finish();
                }
            });

            final TextView editDate = (TextView) findViewById(R.id.editDate);
            final EditText editTextName = (EditText) findViewById(R.id.editTextName);
            final EditText editTextAmount = (EditText) findViewById(R.id.editTextAmount) ;
            final EditText editTextDescription = (EditText) findViewById(R.id.editTextDescription);

            editDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment calendar = new DateSelectorFragment();
                    calendar.show(getSupportFragmentManager(), "dateSelector");
                }
            });

            final Button finishButton = (Button) findViewById(R.id.finishButton);

            final boolean loadExistingTransaction = extras.getBoolean("existing", false);
            if(loadExistingTransaction) {
                editDate.setText(extras.getString("date"));
                editTextName.setText(extras.getString("name"));
                editTextAmount.setText(String.format(java.util.Locale.US,"%.2f", extras.getFloat("amount")));
                editTextDescription.setText(extras.getString("description"));
                spinner.setSelection(extras.getInt("category"));
                finishButton.setText(R.string.input_modify_button);
            } else {
                finishButton.setText(R.string.input_create_button);
                // set default date to current date
                Calendar myCalendar = Calendar.getInstance();
                final SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
                editDate.setText(dateFormat.format(myCalendar.getTime()));
            }

            finishButton.setOnClickListener(new View.OnClickListener() {
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
                        send_data.putExtra("type", type);

                        if(loadExistingTransaction){
                            // pass position of modified transaction through
                            send_data.putExtra("position", extras.getInt("position"));
                        }

                        setResult(MainActivity.RESULT_OK, send_data);
                        finish();
                    }
                }
            });
        }
    }
}
