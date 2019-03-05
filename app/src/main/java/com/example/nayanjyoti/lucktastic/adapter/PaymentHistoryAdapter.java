package com.example.nayanjyoti.lucktastic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nayanjyoti.lucktastic.fragment.FailureFragment;
import com.example.nayanjyoti.lucktastic.fragment.PendingFragment;
import com.example.nayanjyoti.lucktastic.fragment.SuccessFragment;

public class PaymentHistoryAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public PaymentHistoryAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PendingFragment();
        switch (position){
            case 0:
//                fragment = new PendingFragment();
                break;
            case 1:
                fragment = new SuccessFragment();
                break;
            case 2:
                fragment = new FailureFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
