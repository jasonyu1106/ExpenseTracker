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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TransactionsFragment extends Fragment {

    class RequestCode {
        static final int ADD = 100;
        static final int MODIFY = 101;
    }

    private RecyclerViewAdapter recyclerViewAdapter;

    public interface onTransactionsFragmentListener{
        void onAddTransactionEvent(Transaction transaction);
        void onRemoveTransactionEvent(int position);
        void onModifyTransactionEvent(int position, Transaction modifiedTransaction);
        Transaction getTransactionData(int position);
        ArrayList<Transaction> getTransactions();
    }
    private onTransactionsFragmentListener mActivityCallback;

    private RecyclerViewAdapter.onRecyclerViewAdapterListener mFragmentCallback = new onRecyclerViewAdapterListener() {
        @Override
        public void removeTransactionRequest(int position) {
            mActivityCallback.onRemoveTransactionEvent(position);
        }

        @Override
        public void modifyTransactionRequest(int position) {
            Transaction currentTransaction = mActivityCallback.getTransactionData(position);

            Intent transaction_modify = new Intent(getActivity(), InputActivity.class);
            final SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
            transaction_modify.putExtra("existing", true);
            transaction_modify.putExtra("position", position);
            transaction_modify.putExtra("date", dateFormat.format(currentTransaction.getDate()));
            transaction_modify.putExtra("name", currentTransaction.getRecipient());
            transaction_modify.putExtra("amount", currentTransaction.getAmount().toString());
            transaction_modify.putExtra("description", currentTransaction.getDescription());
            transaction_modify.putExtra("category", currentTransaction.getCategory());
            transaction_modify.putExtra("type", currentTransaction.getType());

            startActivityForResult(transaction_modify, RequestCode.MODIFY);
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
                    startActivityForResult(transaction_input, RequestCode.ADD);
                }
            });
        FloatingActionButton addReceivable = (FloatingActionButton) fragment_view.findViewById(R.id.fab_lend);
            addReceivable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transaction_input.putExtra("type", TransactionType.LEND);
                    addTransactionMenu.collapse();
                    startActivityForResult(transaction_input, RequestCode.ADD);
                }
            });

        //actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

        return fragment_view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //call mActivityCallback.onModifyTransactionEvent(position, transaction) on RequestCode.MODIFY, RESULT_OK
        if (resultCode == MainActivity.RESULT_OK){
            String name = data.getStringExtra("name");
            String stringAmount = data.getStringExtra("amount");
            String description = data.getStringExtra("description");
            int category = data.getIntExtra("category", 8);
            String date = data.getStringExtra("date");
            int type = data.getIntExtra("type", TransactionType.SPEND);
            int position = data.getIntExtra("position", 0);

            BigDecimal amount = new BigDecimal(stringAmount);

            Date formattedDate;
            SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
            try {
                formattedDate = dateFormat.parse(date);
            } catch (ParseException e) {
                formattedDate = new Date(0);
                e.printStackTrace();
            }

            switch(requestCode){
                case RequestCode.ADD:
                    Transaction newTransaction = new Transaction(name, description, formattedDate, amount, category, type);
                    mActivityCallback.onAddTransactionEvent(newTransaction);
                    break;
                case RequestCode.MODIFY:
                    Transaction modifiedTransaction = new Transaction(name, description, formattedDate, amount, category, type);
                    mActivityCallback.onModifyTransactionEvent(position, modifiedTransaction);
                    break;
            }

            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}

