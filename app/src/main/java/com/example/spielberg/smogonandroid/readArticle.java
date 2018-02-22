package com.example.spielberg.smogonandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spielberg on 2/20/2018.
 */

public class readArticle extends AppCompatActivity {
    JSONArray article;
    int current_tab;
    ViewPager viewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_article);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try{
            article = new JSONArray(bundle.getString("article"));

        } catch(JSONException e){
            e.printStackTrace();
        }
        current_tab = bundle.getInt("index");
        setupArticle();


    }

    private void setupArticle() {
        viewpager = (ViewPager) findViewById(R.id.viewpager2);
        setupViewPager(viewpager);
        TabLayout tablayout = (TabLayout) findViewById(R.id.tab_strategy);
        tablayout.setupWithViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewpager){
        ReadTab tab;
        readArticle.ViewPagerAdapter adapter = new readArticle.ViewPagerAdapter(
                getSupportFragmentManager());
        JSONObject obj = new JSONObject();
        for(int i=0;i<article.length();i++){
            try{
                obj = article.getJSONObject(i);
                tab = ReadTab.newInstance(obj.toString());
                adapter.addFragment(tab, obj.getString("name"));
            } catch(JSONException e){
                e.printStackTrace();
            }


        }




        viewpager.setAdapter(adapter);
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
