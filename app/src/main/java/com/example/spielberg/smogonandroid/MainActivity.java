package com.example.spielberg.smogonandroid;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    URL url;
    HttpURLConnection urlConnection ;
    ServerSmogon smogon;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonxy = (Button) findViewById(R.id.xandy);
        buttonxy.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updatesmogon(6);
            }
        });
        final Button buttonsunandmoon = (Button) findViewById(R.id.sunandmoon);
        buttonsunandmoon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updatesmogon(7);
            }
        });




    }
    public void updatesmogon(int x){

        Thread thread;
        TextView stats,overview, article;
        setContentView(R.layout.pokearticle);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


//        stats = (TextView) findViewById(R.id.editText2);
//        stats.setText("stat1");
        //tab1.setContent(new Intent(this,TabActivity1.class));


        //tab2.setContent(new Intent(this, TabActivity2.class));


        //tab3.setContent(new Intent(this, TabActivity3.class));

        switch (x){
            case 6:

                smogon = new ServerSmogon(this.getBaseContext(), "xy");
                thread = new Thread(smogon);
                thread.start();
                break;
            case 7:
                smogon = new ServerSmogon(this.getBaseContext(), "sm");
                thread = new Thread(smogon);
                thread.start();
                break;
            default:
                break;
        }

    }
    private void setupViewPager(ViewPager viewpager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabActivity1(), "ONE");
        adapter.addFragment(new TabActivity2(), "TWO");
        adapter.addFragment(new TabActivity3(), "THREE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
