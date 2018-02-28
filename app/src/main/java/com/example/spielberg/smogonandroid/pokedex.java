package com.example.spielberg.smogonandroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Spielberg on 2/13/2018.
 */

public class pokedex extends AppCompatActivity {
    ServerSmogon sm;
    String generation;
    private Button search;
    private SearchSettings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = new SearchSettings();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        setContentView(R.layout.pokedex_view);
        populate_pokedex(bundle.getString("gen"));
        search = (Button) findViewById(R.id.button);
        search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setupPopWindow(0);
            }
        });
    }

    private void populate_pokedex(String gen) {
        JSONArray pokemon;
        generation = gen;
        //download pokemon info on first bootup
        //check if directory is created and has files
        setupDirectory(gen);
        //read stats
        File f = new File(getFilesDir() +
                "/pokedex/"+ gen+".txt");
        try {
            Scanner scan = new Scanner(f, "UTF-8").useDelimiter("/n");
            if(scan.hasNext()){
                JSONObject obj = new JSONObject(scan.nextLine());
                pokemon = obj.getJSONArray("pokemon");
                addpokemon(pokemon);



            }
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }


    }


    private void addpokemon(JSONArray pokemon) {
        TableLayout table = (TableLayout) findViewById(R.id.pokemon_results);
        TableLayout header_table = (TableLayout) findViewById(R.id.pokemon_header_row);
        TableRow.LayoutParams params;
        table.removeAllViews();
        header_table.removeAllViews();
        TableRow row;
        TextView tx;
        JSONArray types;
        JSONObject stats;
        String typeString, statString;
        ImageView profile;
        //add header row
        row = new TableRow(this);
        row.setId(View.generateViewId());
        row.setBackgroundColor(Color.GRAY);
        row.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));

        addView(row,getText(R.string.number));//add index
        addView(row,"pic");//add pic column
        addView(row,"Name");//add name column
        addView(row,getText(R.string.type));//add type column
        addView(row,getText(R.string.stats));//add stats column

        row.setPadding(5,5,5,5);
        row.setGravity(Gravity.CENTER_HORIZONTAL);



        //add row
        header_table.addView(row);
        //table.addView(row);

        //add pokemon entries
        for(int i=0;i<pokemon.length();i++){
            row = new TableRow(this);
            row.setId(View.generateViewId());
            row.setBackgroundColor(Color.BLACK);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
            profile = new ImageView(this);
            profile.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            profile.setId(View.generateViewId());
            profile.setPadding(5,5,5,5);
            profile.setImageResource(R.mipmap.ic_launcher);

            addView(row, Integer.toString(i+1));
            row.addView(profile);

            try{
                //add name
                addView(row,  pokemon.getJSONObject(i).getString("name"));
                //add type1/type2
                types = pokemon.getJSONObject(i).getJSONArray("alts").getJSONObject(
                        0).getJSONArray("types");
                typeString = "";
                for(int g=0;g<types.length();g++){
                    //build the string for types
                    typeString = typeString + types.getString(g);
                    if(g<types.length()-1){
                        typeString = typeString + " / ";
                    }
                }
                addView(row, typeString);
                //add stats
                statString = "";
                stats = pokemon.getJSONObject(i).getJSONArray("alts").getJSONObject(
                        0);

                statString = (Integer.toString(stats.getInt("hp"))+" | ");//get hp
                statString = statString + (Integer.toString(stats.getInt("atk"))+" | ");//get atk
                statString = statString + (Integer.toString(stats.getInt("def"))+" | ");//get def
                statString = statString + (Integer.toString(stats.getInt("spa"))+" | ");//get spa
                statString = statString + (Integer.toString(stats.getInt("spd"))+" | ");//get spd
                statString = statString + (Integer.toString(stats.getInt("spe")));//get spe
                addView(row, statString);


            }catch (JSONException e){
                e.printStackTrace();
            }
            row.setPadding(5,5,5,5);

            //setup for onclick view
            row.setOnClickListener(new View.OnClickListener(){
                //TableRow is calling onClick()
                public void onClick(View v){
                    TableRow littlerow = (TableRow) v;
                    TextView text = (TextView) littlerow.getChildAt(2);
                    TextView number = (TextView) littlerow.getChildAt(0);
                    //Get original index for pokedex
                    int index = Integer.parseInt(number.getText().toString()) - 1;
                    Intent intent = new Intent(pokedex.this, pokearticle.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", index);
                    bundle.putString("gen", generation);
                    Log.i("tablerow",number.getText().toString()+
                            text.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            table.addView(row);


        }

    }


    /**
     * generic addview function for tablerow.
     * Adds a textview with name to a tablerow.
     */
    private void addView(TableRow row, CharSequence name){
        TextView tx;
        //add pic column
        tx = new TextView(this);
        tx.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams params = (TableRow.LayoutParams)tx.getLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;
        tx.setLayoutParams(params);
        tx.setText(name);

        tx.setId(View.generateViewId());

        tx.setTextColor(Color.WHITE);
        tx.setPadding(5,5,5,5);

        row.addView(tx);
    }

    /**
     * setup directory for specified game version
    **/
    private void setupDirectory(String gamever){
        File folder = new File(getFilesDir() +
                "/pokedex/");
        File f = new File(getFilesDir() +
                "/pokedex/"+ gamever+".txt");
        File[] contents = f.listFiles();
        Thread thread;
        //check if directory is made
        if(folder.exists() && folder.isDirectory() && f.exists()){
            return;
        }
        //if not make it

        folder.mkdirs();
        try{
            f.createNewFile();
        } catch(IOException e){
            e.printStackTrace();
        }


        //download stats
        Resources resource = getBaseContext().getResources();
        sm = new ServerSmogon(getBaseContext(), gamever, "stats", "none");
        thread = new Thread(sm);

        thread.start();
        try{

            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }




    }

    private void setupPopWindow(int status){
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.pokedex_view);

        //inflate the layout of the popupwindow
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.search_tab, null);
        Button sear = (Button) popupView.findViewById(R.id.search);

        //choose the popup window to use


        // create the popup window
        int width = (mainLayout.getWidth()/4)*3;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        sear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                populateSearch(popupView);
                popupWindow.dismiss();
            }
        });
        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                populateSearch(v);
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void populateSearch(View v){
        TextView text = (TextView) v.findViewById(R.id.search_name);
        settings.setPokemon(text.getText().toString());
    }

}
