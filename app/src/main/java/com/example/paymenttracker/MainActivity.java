package com.example.paymenttracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements TransactionsFragment.onTransactionsFragmentListener {

    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCELLED = 0;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private float[] categoryTotals = new float[9];

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
    }

    @Override
    public void onAddTransactionEvent(Transaction transaction) {
        transactions.add(0, transaction);
        if (transaction.getType() == TransactionType.SPEND) {
            categoryTotals[transaction.getCategory()] += transaction.getAmount();
        }
        updateOverviewChart(categoryTotals);
        Collections.sort(transactions);
    }

    @Override
    public void onTransactionModifyEvent(int position, boolean isRemove) {
        if (isRemove){
            Transaction removedTransaction = transactions.get(position);
            categoryTotals[removedTransaction.getCategory()] -= removedTransaction.getAmount();
            transactions.remove(removedTransaction);
        }
        updateOverviewChart(categoryTotals);
    }

    @Override
    public ArrayList<Transaction> getTransactions (){
        return transactions;
    }

    private void updateOverviewChart(float[] categoryTotals) {
        OverviewFragment overviewFragment = (OverviewFragment) pagerAdapter.getOverviewFragment();
        if (overviewFragment != null) {
            overviewFragment.updateChart(categoryTotals);
        }
    }
}
