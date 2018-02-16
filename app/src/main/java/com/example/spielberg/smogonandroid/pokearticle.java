package com.example.spielberg.smogonandroid;


import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spielberg on 2/9/2018.
 */

public class pokearticle extends AppCompatActivity {
    ServerSmogon smogon;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String pokemon, gen;
    private final String gen1="rb",gen2="gs",gen3="rs",gen4="dp",gen5="bw",gen6="xy",gen7="sm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pokemon = savedInstanceState.getString("pokemon").toLowerCase();
        this.gen = savedInstanceState.getString("gen");
        setContentView(R.layout.pokearticle);

        final Button buttonrb = (Button) findViewById(R.id.redandblue);
        buttonrb.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updatesmogon(1);
            }
        });


        final Button buttongs = (Button) findViewById(R.id.goldandsilver);
        buttongs.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updatesmogon(2);
            }
        });
        final Button buttonrs = (Button) findViewById(R.id.rubyandsapphire);
        buttonrs.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updatesmogon(3);
            }
        });

        final Button buttondp = (Button) findViewById(R.id.diamondandpearl);
        buttondp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updatesmogon(4);
            }
        });
        final Button buttonbw = (Button) findViewById(R.id.blackandwhite);
        buttonbw.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updatesmogon(5);
            }
        });

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
        TabLayout tablayout2;
        Thread thread;
        TextView stats,overview, article;
        Boolean directory;
        setContentView(R.layout.pokearticle);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tablayout2 = (TabLayout) findViewById(R.id.format);
        tablayout2.addTab(tablayout2.newTab().setText("format1"));
        tablayout2.addTab(tablayout2.newTab().setText("format2"));




        switch (x){
            case 1:
                smogon = new ServerSmogon(this.getBaseContext(), "rb", "articles", pokemon);
                //check if directory for articles is created
                directory = setupDirectory("rb");
                if(directory){
                    break;
                }
                else{
                    thread = new Thread(smogon);
                    thread.start();
                }
                thread = new Thread(smogon);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                smogon = new ServerSmogon(this.getBaseContext(), "gs", "articles", pokemon);
                //check if directory for articles is created
                directory = setupDirectory("gs");
                if(directory){
                    break;
                }
                else{
                    thread = new Thread(smogon);
                    thread.start();
                }
                thread = new Thread(smogon);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case 3:
                smogon = new ServerSmogon(this.getBaseContext(), "rs", "articles", pokemon);
                //check if directory for articles is created
                directory = setupDirectory("rs");
                if(directory){
                    break;
                }
                else{
                    thread = new Thread(smogon);
                    thread.start();
                }
                thread = new Thread(smogon);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case 4:
                smogon = new ServerSmogon(this.getBaseContext(), "dp","articles", pokemon);
                //check if directory for articles is created
                directory = setupDirectory("dp");
                if(directory){
                    break;
                }
                else{
                    thread = new Thread(smogon);
                    thread.start();
                }
                thread = new Thread(smogon);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case 5:
                smogon = new ServerSmogon(this.getBaseContext(), "bw", "articles", pokemon);
                //check if directory for articles is created
                directory = setupDirectory("bw");
                if(directory){
                    break;
                }
                else{
                    thread = new Thread(smogon);
                    thread.start();
                }
                thread = new Thread(smogon);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;


            case 6:

                smogon = new ServerSmogon(this.getBaseContext(), "xy", "articles", pokemon);
                //check if directory for articles is created
                directory = setupDirectory("xy");
                if(directory){
                    break;
                }
                else{
                    thread = new Thread(smogon);
                    thread.start();
                }
                thread = new Thread(smogon);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                smogon = new ServerSmogon(this.getBaseContext(), "sm", "articles", pokemon);
                //check if directory for articles is created
                directory = setupDirectory("sm");
                if(directory){
                    break;
                }
                else{
                    thread = new Thread(smogon);
                    thread.start();
                }
                thread = new Thread(smogon);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

    private void setupViewPager(ViewPager viewpager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabActivity1(), "Stats");
        adapter.addFragment(new TabActivity2(), "Overview");
        adapter.addFragment(new TabActivity3(), "Articles");
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

    /*setup directory for specified game version
    * Returns true if directory already exists
    * Returns false if directory needs to be created*/
    private boolean setupDirectory(String gamever){
        File f = new File(Environment.getExternalStorageDirectory() + gamever);
        if(f.exists() && f.isDirectory()){
            return true;
        }
        //if not download articles
        else{
            f.mkdirs();
            return false;
        }
    }
}
