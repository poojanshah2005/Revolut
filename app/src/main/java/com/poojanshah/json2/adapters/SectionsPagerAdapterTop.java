package com.poojanshah.json2.adapters;

/**
 * Created by shahp on 22/08/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.poojanshah.json2.MainActivity;
import com.poojanshah.json2.variables.CURRENCIES;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterTop extends FragmentPagerAdapter {

    public SectionsPagerAdapterTop(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return MainActivity.placeholderFragmentTop.newInstance(CURRENCIES.getCURRENCIES(position).toString());
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return CURRENCIES.values().length;
    }
}
