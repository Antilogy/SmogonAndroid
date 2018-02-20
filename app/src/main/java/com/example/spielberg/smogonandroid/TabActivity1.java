package com.example.spielberg.smogonandroid;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Spielberg on 7/25/2017.
 */

public class TabActivity1 extends Fragment {
    JSONArray stats;
    String name;
    public TabActivity1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Bundle bundle = getArguments();
            stats = new JSONArray(bundle.getString("stats"));
            name = bundle.getString("name");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.stats, container, false);
        setupStats(v);
        return v;
    }

    private void setupStats(View v) {
        TableRow row, row2, row3, row4, row5, row6;
        TextView text = (TextView)v.findViewById(R.id.pokemon_name);
        text.setText(name);
        text.setTextColor(Color.WHITE);
        TableLayout table = (TableLayout)v.findViewById(R.id.stats_table);
        row = createRow();
        addView(row, "");//Add empty column
        addView(row, "HP");//Add HP row

        row2 = createRow();
        addView(row2, "");//Add empty column
        addView(row2, "ATK");//Add ATK row
        row3 = createRow();
        addView(row3, "");//Add empty column
        addView(row3, "DEF");//Add DEF row
        row4 = createRow();
        addView(row4, "");//Add empty column
        addView(row4, "SPA");////Add SPA row
        row5 = createRow();
        addView(row5, "");//Add empty column
        addView(row5, "SPD");//Add SPD row
        row6 = createRow();
        addView(row6, "");//Add empty column
        addView(row6, "SPE");//Add SPE row
        try{
            addView(row, stats.getJSONObject(0).getString("hp"));
            addView(row2, stats.getJSONObject(0).getString("atk"));
            addView(row3, stats.getJSONObject(0).getString("def"));
            addView(row4, stats.getJSONObject(0).getString("spa"));
            addView(row5, stats.getJSONObject(0).getString("spd"));
            addView(row6, stats.getJSONObject(0).getString("spe"));
        } catch(JSONException e){
            e.printStackTrace();
        }
        addView(row, "");
        addView(row2, "");//Add empty column
        addView(row3, "");//Add empty column
        addView(row4, "");//Add empty column
        addView(row5, "");//Add empty column
        addView(row6, "");//Add empty column

        table.addView(row);
        table.addView(row2);
        table.addView(row3);
        table.addView(row4);
        table.addView(row5);
        table.addView(row6);



    }

    public static TabActivity1 newInstance(String text, String name){
        TabActivity1 tab = new TabActivity1();
        Bundle args = new Bundle();
        args.putString("stats", text);
        args.putString("name", name);

        tab.setArguments(args);
        return tab;
    }

    /**
     *
     */
    private TableRow createRow(){
        TableRow row = new TableRow(getActivity());
        row.setId(View.generateViewId());
        row.setBackgroundColor(Color.BLACK);
        row.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
        return row;
    }

    /**
     * generic addview function for tablerow.
     * Adds a textview with name to a tablerow.
     */
    private void addView(TableRow row, CharSequence name){
        TextView tx;
        //add pic column
        tx = new TextView(getActivity());
        tx.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams params = (TableRow.LayoutParams)tx.getLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;
        //params.setMargins(0,1,0,1);
        tx.setLayoutParams(params);
        tx.setText(name);

        tx.setId(View.generateViewId());

        tx.setTextColor(Color.WHITE);
        tx.setPadding(20,20,20,20);
        //if the textview has text give it a border
        if(name.length()>0){
            tx.setBackground(getResources().getDrawable(R.drawable.border));
        }
        row.addView(tx);
    }
}
