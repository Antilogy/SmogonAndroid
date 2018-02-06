package com.example.spielberg.smogonandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    URL url;
    HttpURLConnection urlConnection ;
    ServerSmogon smogon;
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
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabSpec tab1 = tabHost.newTabSpec("tag1");
        TabSpec tab2 = tabHost.newTabSpec("tag2");
        TabSpec tab3 = tabHost.newTabSpec("tag3");

        tab1.setIndicator("stats");
        tab1.setContent(R.id.stats);

//        stats = (TextView) findViewById(R.id.editText2);
//        stats.setText("stat1");
        //tab1.setContent(new Intent(this,TabActivity1.class));

        tab2.setIndicator("overview");
        tab2.setContent(R.id.overview);
        //tab2.setContent(new Intent(this, TabActivity2.class));

        tab3.setIndicator("articles");
        tab3.setContent(R.id.articles);
        //tab3.setContent(new Intent(this, TabActivity3.class));

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
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
}
