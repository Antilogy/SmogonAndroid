package com.example.spielberg.smogonandroid;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

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
    String pokemon, gen, format;
    Handler mHandler;

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
        this.pokemon = bundle.getString("pokemon");
        this.gen = bundle.getString("gen");
        this.format = bundle.getString("format");
        Button button = (Button) findViewById(R.id.copy);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                CopyToClipBoard();
            }
        });
        setupArticle();


    }

    /**
     * Copies current moveset to clipboard with pokemon name
     */
    public void CopyToClipBoard(){
        TabLayout tabs = (TabLayout) findViewById(R.id.tab_strategy);
        ReadTab current = (ReadTab) getSupportFragmentManager().getFragments(
        ).get(tabs.getSelectedTabPosition());
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text = current.getClipboard();

        ViewPagerAdapter adapter = ((ViewPagerAdapter) viewpager.getAdapter());
        current = (ReadTab) adapter.getItem(tabs.getSelectedTabPosition());
        text = current.getClipboard();

        ClipData clip = ClipData.newPlainText("text", text);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(getApplicationContext(),
                "Copied Moveset: "+pokemon, Toast.LENGTH_SHORT).show();


    }

    private void setupArticle() {
        viewpager = (ViewPager) findViewById(R.id.viewpager2);
        setupViewPager(viewpager);
        TabLayout tablayout = (TabLayout) findViewById(R.id.tab_strategy);
        tablayout.setupWithViewPager(viewpager);
        ChosenTab(tablayout);
    }

    /**
     * Select the current tab that was clicked
     */
    private void ChosenTab(TabLayout tablayout) {
        tablayout.getTabAt(current_tab).select();
    }

    private void setupViewPager(ViewPager viewpager){
        ReadTab tab;
        readArticle.ViewPagerAdapter adapter = new readArticle.ViewPagerAdapter(
                getSupportFragmentManager());
        JSONObject obj = new JSONObject();
        for(int i=0;i<article.length();i++){
            try{
                obj = article.getJSONObject(i);
                tab = ReadTab.newInstance(obj.toString(), pokemon, gen,
                        obj.getString("name"), mHandler);
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
