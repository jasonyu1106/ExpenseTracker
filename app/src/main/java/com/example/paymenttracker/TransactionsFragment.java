package com.example.paymenttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paymenttracker.RecyclerViewAdapter.onRecyclerViewAdapterListener;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TransactionsFragment extends Fragment {

    private RecyclerViewAdapter recyclerViewAdapter;

    public interface onTransactionsFragmentListener{
        void onAddTransactionEvent(Transaction transaction);
        void onTransactionModifyEvent(int position, boolean isRemove);
        ArrayList<Transaction> getTransactions();
    }
    private onTransactionsFragmentListener mActivityCallback;

    private RecyclerViewAdapter.onRecyclerViewAdapterListener mFragmentCallback = new onRecyclerViewAdapterListener() {
        @Override
        public void onRemoveTransactionEvent(int position) {
            mActivityCallback.onTransactionModifyEvent(position, true);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onTransactionsFragmentListener) {
            mActivityCallback = (onTransactionsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onTransactionsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragment_view = inflater.inflate(R.layout.fragment_transactions, container, false);
        final RecyclerView recyclerView = (RecyclerView) fragment_view.findViewById(R.id.recyclerView);

        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), mActivityCallback.getTransactions(), mFragmentCallback);
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
        FloatingActionButton addPurchase = (FloatingActionButton) fragment_view.findViewById(R.id.fab_spend);
        final FloatingActionsMenu addTransactionMenu = (FloatingActionsMenu) fragment_view.findViewById(R.id.add_transaction);
        addPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transaction_input.putExtra("type", TransactionType.SPEND);
                    addTransactionMenu.collapse();
                    startActivityForResult(transaction_input, 1);
                }
            });
        FloatingActionButton addReceivable = (FloatingActionButton) fragment_view.findViewById(R.id.fab_lend);
            addReceivable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transaction_input.putExtra("type", TransactionType.LEND);
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
            int type = data.getIntExtra("type", TransactionType.SPEND);

            Date formattedDate;
            SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
            try {
                formattedDate = dateFormat.parse(date);
            } catch (ParseException e) {
                formattedDate = new Date(0);
                e.printStackTrace();
            }

            Transaction newTransaction = new Transaction(name, description, formattedDate, amount, category, type);
            mActivityCallback.onAddTransactionEvent(newTransaction);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}

