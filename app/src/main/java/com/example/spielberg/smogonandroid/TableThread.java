package com.example.spielberg.smogonandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
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

/**
 * Created by Spielberg on 3/4/2018.
 */

public class TableThread implements Runnable {
    Handler hand;
    Context context;
    int[] range;
    ArrayList<RowStats> result_rows;
    JSONArray pokemon;
    int threadID;
    String gen;

    public TableThread(Handler handler, Context context,
                       int start, int end, JSONArray pokemon, int id, String gen){
        hand = handler;
        this.context = context;
        this.range = new int[2];
        range[0] = start;
        range[1] = end;
        this.pokemon = pokemon;
        threadID = id;
        this.gen = gen;
        result_rows = new ArrayList<>();
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
        int[] pokestat = new int[6];
        JSONObject stats;
        JSONArray types;
        RowStats rowstat;
        TableRow.LayoutParams imageparams;
        for(int i=range[0];i<range[1];i++){
            row = new TableRow(context);
            row.setId(i);
            row.setBackgroundColor(Color.BLACK);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
            row.setWeightSum(1f);
            profile = new ImageView(context);
            profile.setLayoutParams(new TableRow.LayoutParams(
                    0, TableRow.LayoutParams.WRAP_CONTENT));
            imageparams = (TableRow.LayoutParams)profile.getLayoutParams();
            profile.setId(View.generateViewId());
            profile.setPadding(5,5,5,5);
            profile.setImageResource(R.mipmap.ic_launcher);
            imageparams.weight = 0.08f;
            profile.setLayoutParams(imageparams);
            profile.setAdjustViewBounds(true);

//            addView(row, Integer.toString(i+1), 0.06f);
            row.addView(profile);

            try{
                //add name
                addView(row,  pokemon.getJSONObject(i).getString("name"), 0.28f);
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
                addView(row, typeString, 0.28f);
                //add stats
                statString = "";
                stats = pokemon.getJSONObject(i).getJSONArray("alts").getJSONObject(
                        0);

                pokestat = new int[6];
                pokestat[0] = stats.getInt("hp");
                pokestat[1] = stats.getInt("atk");
                pokestat[2] = stats.getInt("def");
                pokestat[3] = stats.getInt("spa");
                pokestat[4] = stats.getInt("spd");
                pokestat[5] = stats.getInt("spe");
                statString = (String.format("%-4d",pokestat[0]));//get hp
//                addView(row, Integer.toString(pokestat[0]), 0.06f);
//                addView(row, Integer.toString(pokestat[1]), 0.06f);
//                addView(row, Integer.toString(pokestat[2]), 0.06f);
//                addView(row, Integer.toString(pokestat[3]), 0.06f);
//                addView(row, Integer.toString(pokestat[4]), 0.06f);
//                addView(row, Integer.toString(pokestat[5]), 0.06f);
                statString = statString + (String.format("%-4d",pokestat[1]));//get atk
                statString = statString + (String.format("%-4d",pokestat[2]));//get def
                statString = statString + (String.format("%-4d",pokestat[3]));//get spa
                statString = statString + (String.format("%-4d",pokestat[4]));//get spd
                statString = statString + (String.format("%-4d",pokestat[5]));//get spe
                addView(row, statString, 0.36f);


            }catch (JSONException e){
                e.printStackTrace();
            }
            row.setPadding(5,5,5,5);

            //setup for onclick view
            row.setOnClickListener(new View.OnClickListener(){
                //TableRow is calling onClick()
                public void onClick(View v){
                    TableRow littlerow = (TableRow) v;
                    TextView text = (TextView) littlerow.getChildAt(1);
                    //TextView number = (TextView) littlerow.getChildAt(0);
                    //Get original index for pokedex
                    int index;
                    index = littlerow.getId();
                    Intent intent = new Intent(context, pokearticle.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", index);
                    bundle.putString("gen", gen);
//                    Log.i("tablerow",number.getText().toString()+
//                            text.getText().toString());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
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
    private void addView(TableRow row, CharSequence name, float weight){
        TextView tx;
        //add pic column
        tx = new TextView(context);
        tx.setLayoutParams(new TableRow.LayoutParams(
                0, TableRow.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams params = (TableRow.LayoutParams)tx.getLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;
        params.weight = weight;
        tx.setLayoutParams(params);
        tx.setText(name);
        tx.setTypeface(Typeface.MONOSPACE);

        //tx.setId(View.generateViewId());

        tx.setTextColor(Color.WHITE);
        tx.setPadding(5,5,5,5);

        row.addView(tx);
    }
}
