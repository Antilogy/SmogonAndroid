package com.example.spielberg.smogonandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Spielberg on 3/4/2018.
 */

public class TableThread implements Runnable {
    Handler hand;
    //Context context;
    pokedex poke_view;
    int[] range;
    ArrayList<RowStats> result_rows;
    JSONArray pokemon;
    int threadID;
    String gen;
    HelperClass help;

    public TableThread(Handler handler, pokedex view,
                       int start, int end, JSONArray pokemon, int id, String gen){
        hand = handler;
        poke_view = view;
        this.range = new int[2];
        range[0] = start;
        range[1] = end;
        this.pokemon = pokemon;
        threadID = id;
        this.gen = gen;
        result_rows = new ArrayList<>();
        help = new HelperClass();

    }

    @Override
    public void run(){
        // Moves the current Thread into the Background
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        //add pokemon entries
        makeTableRows();
        Message message = hand.obtainMessage(threadID, result_rows);
        message.sendToTarget();


    }

    private void makeTableRows() {
        //add pokemon entries
        TableRow row;
        ImageView profile;
        String typeString, statString;
        SpannableStringBuilder buildString;
        int[] pokestat = new int[6];
        JSONObject stats;
        JSONArray types;
        RowStats rowstat;
        TableRow.LayoutParams imageparams;
        for(int i=range[0];i<range[1];i++){
            row = new TableRow(poke_view);
            row.setId(i);
            row.setBackgroundColor(Color.BLACK);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
            row.setWeightSum(1f);
//            profile = new ImageView(poke_view);
//            profile.setLayoutParams(new TableRow.LayoutParams(
//                    0, TableRow.LayoutParams.WRAP_CONTENT));
//            imageparams = (TableRow.LayoutParams)profile.getLayoutParams();
//            profile.setId(View.generateViewId());
//            profile.setPadding(5,5,5,5);
//            profile.setImageResource(R.mipmap.ic_launcher);
//            imageparams.weight = 0.08f;
//            profile.setLayoutParams(imageparams);
//            profile.setAdjustViewBounds(true);

//            addView(row, Integer.toString(i+1), 0.06f);
            //row.addView(profile);

            try{
                //add name
                buildString = new SpannableStringBuilder();
                buildString.append(pokemon.getJSONObject(i).getString("name"));
                addView(row,  buildString, 0.36f);
                buildString.clear();
                //add type1/type2
                types = pokemon.getJSONObject(i).getJSONArray("alts").getJSONObject(
                        0).getJSONArray("types");
                for(int g=0;g<types.length();g++){
                    //build the string for types
                    buildString.append(String.format("%-9s",types.getString(g)));
                    if(types.length() == 1){
                        buildString.append(String.format("%-9s",""));
                    }
                }
//                addView(row, typeString, 0.28f);
                //add stats
                stats = pokemon.getJSONObject(i).getJSONArray("alts").getJSONObject(
                        0);

                pokestat = new int[6];
                pokestat[0] = stats.getInt("hp");
                pokestat[1] = stats.getInt("atk");
                pokestat[2] = stats.getInt("def");
                pokestat[3] = stats.getInt("spa");
                pokestat[4] = stats.getInt("spd");
                pokestat[5] = stats.getInt("spe");
                buildString.append(String.format(Locale.US,"%-4d",pokestat[0]));//get hp
                buildString.append(String.format(Locale.US,"%-4d",pokestat[1]));//get atk
                buildString.append(String.format(Locale.US,"%-4d",pokestat[2]));//get def
                buildString.append(String.format(Locale.US,"%-4d",pokestat[3]));//get spa
                buildString.append(String.format(Locale.US,"%-4d",pokestat[4]));//get spd
                buildString.append(String.format(Locale.US,"%-4d",pokestat[5]));//get spe


                help.ColorCodeType(buildString);
                addView(row, buildString, 0.64f);


            }catch (JSONException e){
                e.printStackTrace();
            }
            row.setPadding(5,5,5,5);

            //setup for onclick view
            row.setOnClickListener(new View.OnClickListener(){
                //TableRow is calling onClick()
                public void onClick(View v){
                    TableRow littlerow = (TableRow) v;
                    //TextView number = (TextView) littlerow.getChildAt(0);
                    //Get original index for pokedex
                    int index;
                    index = littlerow.getId();
                    Intent intent = new Intent(poke_view, pokearticle.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", index);
                    bundle.putString("gen", gen);
//                    Log.i("tablerow",number.getText().toString()+
//                            text.getText().toString());
                    intent.putExtras(bundle);
                    poke_view.startActivity(intent);
                }
            });

            //add rowstat to arraylist
            rowstat = new RowStats(row, pokestat);
            result_rows.add(rowstat);


        }
    }

    /**
     * generic addview function for tablerow.
     * Adds a textview with name to a tablerow.
     */
    private void addView(TableRow row, SpannableStringBuilder name, float weight){
        TextView tx;
        //add pic column
        tx = new TextView(poke_view);
        tx.setLayoutParams(new TableRow.LayoutParams(
                0, TableRow.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams params = (TableRow.LayoutParams)tx.getLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;
        params.weight = weight;

        tx.setLayoutParams(params);
        tx.setText(name);
        //set textsize depending on screen resolution 720, 1080, 1440


        tx.setTextSize(poke_view.getResources().getDimension(poke_view.width));
        tx.setTypeface(Typeface.MONOSPACE);

        //tx.setId(View.generateViewId());

        tx.setTextColor(Color.WHITE);
        tx.setPadding(5,5,5,5);

        row.addView(tx);
    }
}
