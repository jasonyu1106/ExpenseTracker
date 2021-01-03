package com.example.paymenttracker;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FixedTabsPagerAdapter extends FragmentPagerAdapter {
    Resources res;
    private Fragment mTransactionFragment;
    private Fragment mOverviewFragment;
    private Fragment mJournalFragment;

    public FixedTabsPagerAdapter(Context c, FragmentManager fm) {
        super(fm);
        res = c.getResources();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TransactionsFragment();
            case 1:
                return new OverviewFragment();
            case 2:
                return new JournalFragment();
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save reference to fragments depending on position
        switch (position) {
            case 0:
                mTransactionFragment = (TransactionsFragment) createdFragment;
                break;
            case 1:
                mOverviewFragment = (OverviewFragment) createdFragment;
                break;
            case 2:
                mJournalFragment = (JournalFragment) createdFragment;
        }
        return createdFragment;
    }

    @Override
    public int getCount(){
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return res.getString(R.string.transactions_page_title);
            case 1:
                return res.getString(R.string.overview_page_title);
            case 2:
                return res.getString(R.string.journal_page_title);
            default:
                return null;
        }
    }

    public Fragment getTransactionFragment(){
        return mTransactionFragment;
    }

    public Fragment getOverviewFragment(){
        return mOverviewFragment;
    }

    public Fragment getJournalFragment(){
        return mJournalFragment;
    }
}

