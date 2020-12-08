package com.example.weatherapp.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.weatherapp.R;
import com.example.weatherapp.currentHome;
import com.example.weatherapp.favortite_fragment;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
//public class SectionsPagerAdapter extends FragmentPagerAdapter {
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;
    private SharedPreferences preferences;
    private int position;
    static ArrayList<Fragment> fragmentList =  fragmentList = new ArrayList<>();


    //create the arraylist


    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private Context mContext;


//    public SectionsPagerAdapter(Context context, FragmentManager fm) {
//        super(fm);
////        fragmentList = new ArrayList<>();
//        mContext = context;
//        preferences = PreferenceManager.getDefaultSharedPreferences(context);
//    }

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
//        fragmentList = new ArrayList<>();
        mContext = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SectionsPagerAdapter(FragmentManager supportFragmentManager, int numOfTabs) {
        super(supportFragmentManager);
        this.numOfTabs = numOfTabs;
    }
    public SectionsPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        this.position = position;

        // arraylist.get(position)
//        return fragmentList.get(position);

        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//        return PlaceholderFragment.newInstance(position + 1);

        switch (position) {
            case 0:
                Fragment home = new currentHome();
                return home;
        }
        //return null;
        return new favortite_fragment(position);
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
////        return mContext.getResources().getString(TAB_TITLES[position]);
//    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return numOfTabs;
//        return fragmentList.size();
    }





}