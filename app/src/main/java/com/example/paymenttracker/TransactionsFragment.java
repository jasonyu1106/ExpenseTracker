package com.example.paymenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class TransactionsFragment extends Fragment {

    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragment_view = inflater.inflate(R.layout.fragment_transactions, container, false);
        final RecyclerView recyclerView = (RecyclerView) fragment_view.findViewById(R.id.recyclerView);


        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), MainActivity.transactions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton fab = (FloatingActionButton) fragment_view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start_input = new Intent(getActivity(), InputActivity.class);
                startActivityForResult(start_input, MainActivity.REQUEST_CODE);
            }
        });
        return fragment_view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.REQUEST_CODE){

            String name = data.getStringExtra("name");
            float amount = data.getFloatExtra("amount", 0);
            String description = data.getStringExtra("description");
            int category = data.getIntExtra("category", 8);
            String date = data.getStringExtra("date");
            boolean isExpense = data.getBooleanExtra("isExpense", true);

            Transaction transaction = new Transaction();
            transaction.setName(name);
            transaction.setAmount(amount);
            transaction.setDescription(description);
            transaction.setCategory(category);
            transaction.setExpense(isExpense);
            System.out.println("THIS BETTER ME F: " + isExpense);

            SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
            try {
                transaction.setDate(dateFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            MainActivity.transactions.add(transaction);
            Collections.sort(MainActivity.transactions);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}

