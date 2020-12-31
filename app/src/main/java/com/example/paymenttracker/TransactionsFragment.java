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

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

//        FloatingActionButton fab = (FloatingActionButton) fragment_view.findViewById(R.id.fab_main);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent start_input = new Intent(getActivity(), InputActivity.class);
//                startActivityForResult(start_input, 1);
//            }
//        });

        final Intent transaction_input = new Intent(getActivity(), InputActivity.class);
        FloatingActionButton addPurchase = (FloatingActionButton) fragment_view.findViewById(R.id.fab_purchase);
        final FloatingActionsMenu addTransactionMenu = (FloatingActionsMenu) fragment_view.findViewById(R.id.add_transaction);
        addPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transaction_input.putExtra("type", TransactionType.PURCHASE);
                    addTransactionMenu.collapse();
                    startActivityForResult(transaction_input, 1);
                }
            });
        FloatingActionButton addReceivable = (FloatingActionButton) fragment_view.findViewById(R.id.fab_receivable);
            addReceivable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transaction_input.putExtra("type", TransactionType.RECEIVABLE);
                    addTransactionMenu.collapse();
                    startActivityForResult(transaction_input, 1);
                }
            });
        FloatingActionButton addDebt = (FloatingActionButton) fragment_view.findViewById(R.id.fab_debt);
        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction_input.putExtra("type", TransactionType.DEBT);
                addTransactionMenu.collapse();
                startActivityForResult(transaction_input, 1);
            }
        });
        //actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

        return fragment_view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == MainActivity.RESULT_OK){
            String name = data.getStringExtra("name");
            float amount = data.getFloatExtra("amount", 0);
            String description = data.getStringExtra("description");
            int category = data.getIntExtra("category", 8);
            String date = data.getStringExtra("date");
            int type = data.getIntExtra("type", TransactionType.PURCHASE);

            Transaction transaction = new Transaction();
            transaction.setName(name);
            transaction.setAmount(amount);
            transaction.setDescription(description);
            transaction.setCategory(category);
            transaction.setType(type);

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

