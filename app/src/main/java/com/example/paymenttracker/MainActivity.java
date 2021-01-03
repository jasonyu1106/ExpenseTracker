package com.example.paymenttracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TransactionsFragment.onTransactionsFragmentListener {

    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCELLED = 0;
    public static ArrayList<Transaction> transactions = new ArrayList<Transaction>();

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

    public ArrayList getTransactions (){
        return transactions;
    }

    @Override
    public void onTransactionModifyEvent() {
        OverviewFragment overviewFragment = (OverviewFragment) pagerAdapter.getOverviewFragment();
        if (overviewFragment != null) {
            overviewFragment.updateChart();
        }
    }
}
