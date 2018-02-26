package com.example.spielberg.smogonandroid;

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
import android.widget.LinearLayout;
import android.widget.PopupWindow;

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
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                String result = (String) message.obj;
                switch(message.what){
                    case 0:
                        //setup pop window
                        setupPopWindow();
                        break;

                    case 1:
                        //file was updated
                        break;

                    default:
                        break;
                }
            }
        };
        setupArticle();


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
                tab = ReadTab.newInstance(obj.toString(), pokemon, gen, format, mHandler);
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

    private void setupPopWindow(){
        CoordinatorLayout mainLayout = (CoordinatorLayout) findViewById(R.id.read_article);

        //inflate the layout of the popupwindow
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_checkarticles, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

}
