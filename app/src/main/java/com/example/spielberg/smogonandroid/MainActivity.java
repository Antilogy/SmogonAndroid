package com.example.spielberg.smogonandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
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
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private final String gen1="rb",gen2="gs",gen3="rs",gen4="dp",gen5="bw",gen6="xy",gen7="sm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity);

        //setup button input
        TextView pokedex = (TextView) findViewById(R.id.select_pokedex);
        pokedex.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                CallPokedex();
            }
        });





    }

    public void CallPokedex(){
        Intent intent = new Intent(MainActivity.this, SelectGen.class);
        startActivity(intent);
    }









}
