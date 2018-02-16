package com.example.spielberg.smogonandroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Spielberg on 2/13/2018.
 */

public class pokedex extends AppCompatActivity {
    ServerSmogon sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        setContentView(R.layout.pokedex_view);
        populate_pokedex(bundle.getString("gen"));

    }

    private void populate_pokedex(String gen) {
        JSONArray pokemon;
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
                //System.out.println(pokemon.length());
                addpokemon(pokemon);

            }
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }


    }

    private void addpokemon(JSONArray pokemon) {
        TableLayout table = (TableLayout) findViewById(R.id.pokemon_results);
        TableRow.LayoutParams params;
        table.removeAllViews();
        TableRow row;
        TextView tx;
        //add header row
        row = new TableRow(this);
        row.setId(View.generateViewId());
        row.setBackgroundColor(Color.GRAY);
        row.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));

        //add pic column
        tx = new TextView(this);
        tx.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        tx.setText("pic");

        tx.setId(View.generateViewId());

        tx.setTextColor(Color.WHITE);
        tx.setPadding(5,5,5,5);
        row.addView(tx);

        //add name column
        tx = new TextView(this);
        tx.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        tx.setText("Name");

        tx.setId(View.generateViewId());
        tx.setTextColor(Color.WHITE);
        tx.setPadding(5,5,5,5);

        row.addView(tx);

        //add stats column
        tx = new TextView(this);
        tx.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        tx.setText("Stats");

        tx.setId(View.generateViewId());
        tx.setTextColor(Color.WHITE);
        tx.setPadding(5,5,5,5);

        row.addView(tx);
        row.setPadding(5,5,5,5);

        //add row
        table.addView(row);
        //add pokemon entries
        for(int i=0;i<pokemon.length();i++){
            row = new TableRow(this);
            row.setId(View.generateViewId());
            row.setBackgroundColor(Color.BLACK);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
            ImageView profile = new ImageView(this);
            profile.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            profile.setId(View.generateViewId());
            profile.setPadding(5,5,5,5);
            profile.setImageResource(R.mipmap.ic_launcher);

            tx = new TextView(this);
            tx.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            try{
                tx.setText(pokemon.getJSONObject(i).getString("name"));
                System.out.println(pokemon.getJSONObject(i).getString("name"));
            }catch (JSONException e){
                e.printStackTrace();
            }
            params = (TableRow.LayoutParams)tx.getLayoutParams();
            params.span = 2;
            tx.setId(View.generateViewId());
            tx.setLayoutParams(params);
            tx.setTextColor(Color.WHITE);
            tx.setPadding(5,5,5,5);
            row.addView(profile);
            row.addView(tx);
            row.setPadding(5,5,5,5);

            table.addView(row);


        }

    }

    /*setup directory for specified game version
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
        else{
            folder.mkdirs();
            try{
                f.createNewFile();
            } catch(IOException e){
                e.printStackTrace();
            }

        }
        //download stats
        Resources resource = getBaseContext().getResources();
        System.out.println(resource.toString());
        sm = new ServerSmogon(getBaseContext(), gamever, "stats", "none");
        thread = new Thread(sm);

        thread.start();
        try{

            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }




    }

}
