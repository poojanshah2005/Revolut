package com.poojanshah.json2.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.poojanshah.json2.MainActivity;
import com.poojanshah.json2.variables.CURRENCIES;
import com.poojanshah.json2.variables.CurrencyAmount;

/**
 * Created by shahp on 22/08/2017.
 */

public class SectionsPagerAdapterBottom extends FragmentPagerAdapter {

    public SectionsPagerAdapterBottom(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return MainActivity.placeholderFragmentBottom.newInstance(CURRENCIES.getCURRENCIES(position).toString());
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return CURRENCIES.values().length;
    }
}
