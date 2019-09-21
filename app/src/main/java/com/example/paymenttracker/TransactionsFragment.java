package com.example.paymenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class TransactionsFragment extends Fragment {

    protected static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    protected static RecyclerViewAdapter recyclerViewAdapter;


    public static class Transaction implements Comparable<Transaction>{
        private String name, description;
        private Date date;
        private double amount;
        private int category;

        public void setName(String name){
            this.name = name;
        }
        public void setDate (Date date){
            this.date = date;
        }
        public void setDescription (String info){
            description = info;
        }
        public void setAmount (double price){
            amount = price;
        }
        public void setCategory (int position){
            category = position;
        }
        public String getRecipient (){
            return name;
        }
        public Date getDate (){
            return date;
        }
        public String getDescription() {
            return description;
        }
        public double getAmount (){
            return amount;
        }
        public int getCategory() {
            return category;
        }

        @Override
        public int compareTo(Transaction o) {
            if (this.date.after(o.date))
                return -1;
            else if (this.date.before(o.date))
                return 1;
            else
                return 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragment_view = inflater.inflate(R.layout.fragment_transactions, container, false);

        final RecyclerView recyclerView = (RecyclerView) fragment_view.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), transactions);
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
                startActivity(start_input);
            }
        });
        return fragment_view;
    }
}

