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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        LinearLayout row;
        ImageView profile;
        String typeString, statString;
        SpannableStringBuilder buildString = new SpannableStringBuilder();
        SpannableStringBuilder name = new SpannableStringBuilder();
        int[] pokestat = new int[6];
        JSONObject stats;
        JSONArray types;
        RowStats rowstat;
        LayoutInflater inflater = (LayoutInflater) poke_view.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        for(int i=range[0];i<range[1];i++){


            try{
                name = new SpannableStringBuilder();
                buildString = new SpannableStringBuilder();

                //add name
                name.append(String.format(Locale.US,"%-24s",
                        pokemon.getJSONObject(i).getString("name")));

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


            }catch (JSONException e){
                e.printStackTrace();
            }

            //setup for onclick view


            //add rowstat to arraylist
            rowstat = new RowStats(name, buildString, pokestat, i);
            result_rows.add(rowstat);


        }
    }
}
