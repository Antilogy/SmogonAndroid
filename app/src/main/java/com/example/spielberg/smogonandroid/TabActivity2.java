package com.example.spielberg.smogonandroid;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Spielberg on 7/25/2017.
 */

public class TabActivity2 extends Fragment {
    TextView textView;
    String overview;
    public TabActivity2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overview = getArguments().getString("text", "no text");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.overview, container, false);
        textView = (TextView)v.findViewById(R.id.overview_text);
        textView.setText(Html.fromHtml(overview));
        textView.setTextColor(Color.WHITE);
        return v;
    }

    public static TabActivity2 newInstance(String text){
        TabActivity2 tab = new TabActivity2();
        Bundle args = new Bundle();
        args.putString("text", text);
        tab.setArguments(args);
        return tab;
    }

    public void newOverview(String text){
        overview = text;
        if(textView!=null){
            textView.setText(Html.fromHtml(overview));
        }
    }

//    public void setText(String text){
//        textView.setText(text);
//    }
}
