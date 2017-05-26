package com.example.m1762.testvr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by m1762 on 2017/5/25.
 */

public class VertViewAdapter extends FragmentStatePagerAdapter {
    String Data;

    public VertViewAdapter(FragmentManager fm, String Data) {
        super(fm);
        this.Data = Data;
    }

    @Override
    public Fragment getItem(int position) {
        BlankFragment blankFragment = new BlankFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Data",Data);
        blankFragment.setArguments(bundle);
        return blankFragment;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
