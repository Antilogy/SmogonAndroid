package com.example.spielberg.smogonandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spielberg on 3/2/2018.
 */

public class SelectGen extends AppCompatActivity {
    URL url;
    HttpURLConnection urlConnection ;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private final String gen1="rb",gen2="gs",gen3="rs",gen4="dp",gen5="bw",gen6="xy",gen7="sm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonrb = (Button) findViewById(R.id.redandblue);
        buttonrb.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setuppokedex(gen1);
            }
        });


        final Button buttongs = (Button) findViewById(R.id.goldandsilver);
        buttongs.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setuppokedex(gen2);
            }
        });
        final Button buttonrs = (Button) findViewById(R.id.rubyandsapphire);
        buttonrs.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setuppokedex(gen3);
            }
        });

        final Button buttondp = (Button) findViewById(R.id.diamondandpearl);
        buttondp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setuppokedex(gen4);
            }
        });
        final Button buttonbw = (Button) findViewById(R.id.blackandwhite);
        buttonbw.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setuppokedex(gen5);
            }
        });

        final Button buttonxy = (Button) findViewById(R.id.xandy);
        buttonxy.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setuppokedex(gen6);
            }
        });
        final Button buttonsunandmoon = (Button) findViewById(R.id.sunandmoon);
        buttonsunandmoon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setuppokedex(gen7);
            }
        });




    }



    private void setuppokedex(String gen){
        Intent intent = new Intent(SelectGen.this, pokedex.class);
        Bundle bundle = new Bundle();
        bundle.putString("gen", gen);
        intent.putExtras(bundle);
        startActivity(intent);
    }



    private void setupPopWindow(){
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.activity_main_layout);

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
}
