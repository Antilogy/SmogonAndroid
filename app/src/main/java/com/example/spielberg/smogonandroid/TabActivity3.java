package com.example.spielberg.smogonandroid;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Spielberg on 7/25/2017.
 */

public class TabActivity3 extends Fragment {
    JSONArray strategy;
    LinearLayout list;
    String pokemon, gen, format;
    public TabActivity3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Bundle bundle = getArguments();
            if(strategy==null){
                strategy = new JSONArray(bundle.getString("movesets"));
            }
            pokemon = bundle.getString("pokemon");
            gen = bundle.getString("gen");
            if(format==null){
                format = bundle.getString("format");
            }

        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.articles, container, false);
        this.list = (LinearLayout)v.findViewById(R.id.list_articles);
        setupArticles();
        return v;
    }

    private void setupArticles() {
        String info;
        JSONObject obj;
        list.removeAllViews();
        if(strategy.length()>0){
            for(int i=0;i<strategy.length();i++){
                try{
                    obj = strategy.getJSONObject(i);
                    info = obj.getString("name");
                    info = info.concat("\n" + obj.getString("description"));
                    addView(list, Html.fromHtml(info));
                } catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }
    }

    private void addView(LinearLayout list, CharSequence name){
        TextView tx;
        //add pic column
        tx = new TextView(getActivity());
        tx.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tx.getLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;
        //params.setMargins(0,1,0,1);
        tx.setLayoutParams(params);
        tx.setText(name);

        tx.setId(View.generateViewId());

        tx.setTextColor(Color.WHITE);
        tx.setPadding(20,20,20,20);
        tx.setMaxLines(4);
        tx.setEllipsize(TextUtils.TruncateAt.END);
        //if the textview has text give it a border
        tx.setBackground(getResources().getDrawable(R.drawable.border));

        //setup onclickviewlistener
        tx.setOnClickListener(new View.OnClickListener(){
            //textview is calling onClick
            public void onClick(View v){
                TextView textview = (TextView) v;
                int index = ((LinearLayout) textview.getParent()).indexOfChild(textview);
                Intent intent = new Intent(getActivity(), readArticle.class);
                Bundle bundle = new Bundle();

                bundle.putString("article", strategy.toString());
                bundle.putInt("index", index);
                bundle.putString("pokemon", pokemon);
                bundle.putString("gen", gen);
                bundle.putString("format", format);

                intent.putExtras(bundle);
                startActivity(intent);


            }
        });
        list.addView(tx);
    }
    public static TabActivity3 newInstance(String text, String pokemon, String gen, String format){
        TabActivity3 tab = new TabActivity3();
        Bundle args = new Bundle();
        args.putString("movesets", text);
        args.putString("pokemon", pokemon);
        args.putString("gen", gen);
        args.putString("format", format);
        tab.setArguments(args);
        return tab;
    }

    public void newMoveset(String text, String format){
        try{
            strategy = new JSONArray(text);
            this.format = format;
            if(list!=null){
                setupArticles();
            }


        } catch(JSONException e){
            e.printStackTrace();
        }

    }
}
