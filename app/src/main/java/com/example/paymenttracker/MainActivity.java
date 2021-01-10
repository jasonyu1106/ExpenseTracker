package com.example.paymenttracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements TransactionsFragment.onTransactionsFragmentListener {
    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCELLED = 0;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private BigDecimal[] categoryTotals = new BigDecimal[9];

    private FixedTabsPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new FixedTabsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        Arrays.fill(categoryTotals, BigDecimal.ZERO);
    }

    @Override
    public void onAddTransactionEvent(Transaction newTransaction) {
        transactions.add(0, newTransaction);
        if (newTransaction.getType() == TransactionType.SPEND) {
            categoryTotals[newTransaction.getCategory()]
                    = categoryTotals[newTransaction.getCategory()].add(newTransaction.getAmount());
        }
        updateOverviewChart(categoryTotals);
        Collections.sort(transactions);
    }

    @Override
    public void onRemoveTransactionEvent(int position) {
        Transaction removedTransaction = transactions.get(position);
        categoryTotals[removedTransaction.getCategory()]
                = categoryTotals[removedTransaction.getCategory()].subtract(removedTransaction.getAmount());
        transactions.remove(removedTransaction);
        updateOverviewChart(categoryTotals);
    }

    @Override
    public void onModifyTransactionEvent(int position, Transaction modifiedTransaction) {
        Transaction transactionToModify = transactions.get(position);
        if (modifiedTransaction.getType() == TransactionType.SPEND) {
            categoryTotals[transactionToModify.getCategory()]
                    = categoryTotals[transactionToModify.getCategory()].subtract(transactionToModify.getAmount());
            categoryTotals[modifiedTransaction.getCategory()]
                    = categoryTotals[modifiedTransaction.getCategory()].add(modifiedTransaction.getAmount());
        }
        transactions.set(position, modifiedTransaction);
        updateOverviewChart(categoryTotals);
        Collections.sort(transactions);
    }

    @Override
    public Transaction getTransactionData(int position) {
        return transactions.get(position);
    }

    @Override
    public ArrayList<Transaction> getTransactions (){
        return transactions;
    }

    private void updateOverviewChart(BigDecimal[] categoryTotals) {
        OverviewFragment overviewFragment = (OverviewFragment) pagerAdapter.getOverviewFragment();
        if (overviewFragment != null) {
            overviewFragment.updateChart(categoryTotals);
        }
    }
}
