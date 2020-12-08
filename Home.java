package com.example.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.weatherapp.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private ViewPager mPager;
    static PagerAdapter pagerAdapter;
    private SharedPreferences preferences;
    private TabLayout tabs;
    private ArrayList<Fragment> fragmentList;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager2);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

//        System.out.println("length: " + preferences.getAll().size());

//        fragmentManager = getSupportFragmentManager();
//        if(findViewById(R.id.fragment) != null){
//            if(savedInstanceState != null){
//                return;
//            }
//        }
//
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        currentHome addedHome = new currentHome();
//        getSupportActionBar().hide();
//        getActionBar().hide();

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mPager = (ViewPager) findViewById(R.id.view_pager2);

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),preferences.getAll().size() + 1);
//        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //call sharepreference
        //traverse it and create the fragement here
        //add each fragment into arraylist
//        Map<String,?> keys = preferences.getAll();
//        for(Map.Entry<String,?> entry : keys.entrySet()) {
////            Log.d("map values", entry.getKey() + ": " +
////                    entry.getValue().toString());
//              String[] coord = entry.getValue().toString().split(",");
//              String loc = entry.getKey();
//              fragmentList.add(new favortite_fragment(coord[0], coord[1],loc));
//        }

//        Fragment fragment = new Search_Swipe();
//        Bundle bundle = new Bundle();
//        bundle.putString("placeInfo", storedInfo);
//        fragment.setArguments(bundle);
//        MainActivity.swipeAdapter.addItem(fragment);


        mPager.setAdapter(pagerAdapter);
    }

    public void check() {
        System.out.print("activity method called!!!");

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),preferences.getAll().size() + 1);
        mPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
    }

    public void clear(int position) {
        System.out.print("activity method called!!!");
        tabs.removeTabAt(position);
        //mPager.removeTabPage(position);
    }
}