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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
    Switch switcher[];
    JSONArray types, abilities, pokemon;
    List<String> typeList, abilityList, type2List;
    TableLayout table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = new SearchSettings();
        typeList = new ArrayList<>();
        type2List = new ArrayList<>();
        abilityList = new ArrayList<>();
        switcher = new Switch[6];
        typeList.add("Type1");
        type2List.add("Type2");
        abilityList.add("Ability");
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
                settings.setPokelist(pokemon);
                types = obj.getJSONArray("types");
                for(int i=0;i<types.length();i++){//add all types to typeList
                    typeList.add(types.getJSONObject(i).getString("name"));
                    type2List.add(types.getJSONObject(i).getString("name"));
                }

                abilities = obj.getJSONArray("abilities");
                for(int i=0;i<abilities.length();i++){//add all abilities to abilityList
                    abilityList.add(abilities.getJSONObject(i).getString("name"));
                }
                addpokemon(pokemon);



            }
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }


    }


    private void addpokemon(JSONArray pokemon) {
        table = (TableLayout) findViewById(R.id.pokemon_results);
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

        //setup popupView
        Spinner spin1 = (Spinner) popupView.findViewById(R.id.spinner);
        Spinner spin2 = (Spinner) popupView.findViewById(R.id.spinner2);
        Spinner spin3 = (Spinner) popupView.findViewById(R.id.spinner3);


        switcher[0] = (Switch) popupView.findViewById(R.id.switch1);
        switcher[1] = (Switch) popupView.findViewById(R.id.switch2);
        switcher[2] = (Switch) popupView.findViewById(R.id.switch3);
        switcher[3] = (Switch) popupView.findViewById(R.id.switch4);
        switcher[4] = (Switch) popupView.findViewById(R.id.switch5);
        switcher[5] = (Switch) popupView.findViewById(R.id.switch6);

        //set previous settings

        if(settings.getStatSwitch()<6){
            switcher[settings.getStatSwitch()].setChecked(true);
        }

        if(!(settings.getPokemon().compareTo("")==0)){
            TextView text = (TextView) popupView.findViewById(R.id.search_name);
            text.setText(settings.getPokemon());
        }

        //end of previous settings
        // create the popup window
        int width = (mainLayout.getWidth()/4)*3;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        setupType1spinner(spin1);
        setupType2spinner(spin2);
        setupAbilitySpinner(spin3);
        setupSwitchs();
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

    private void setupSwitchs() {
        CompoundButton.OnCheckedChangeListener changeChecker = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    for(int i=0;i<switcher.length;i++){
                        if(compoundButton != switcher[i]){
                            switcher[i].setChecked(false);
                        }
                    }
                }
            }
        };
        for(int i=0;i<switcher.length;i++){
            switcher[i].setOnCheckedChangeListener(changeChecker);
        }
    }

    /**
     * This function sets up the types for the first spinner
     * @param spin The spinner will be given the types for type1
     */
    private void setupType1spinner(Spinner spin) {

        ArrayAdapter<String> spinnerAdapter = arrayAdapt(typeList);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spin.setAdapter(spinnerAdapter);
        spin.setOnItemSelectedListener(AdapterListener());
        if(settings.getType1() != null){
            spin.setSelection(spinnerAdapter.getPosition(settings.getType1()));
        }


    }

    private void setupType2spinner(Spinner spin){
        ArrayAdapter<String> spinnerAdapter = arrayAdapt(type2List);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spin.setAdapter(spinnerAdapter);
        spin.setOnItemSelectedListener(AdapterListener());
        if(settings.getType2() != null){
            spin.setSelection(spinnerAdapter.getPosition(settings.getType2()));
        }
    }

    private void setupAbilitySpinner(Spinner spin){
        ArrayAdapter<String> spinnerAdapter = arrayAdapt(abilityList);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spin.setAdapter(spinnerAdapter);
        spin.setOnItemSelectedListener(AdapterListener());
        if(settings.getAbility() != null){
            spin.setSelection(spinnerAdapter.getPosition(settings.getAbility()));
        }
    }

    /**
     * Creates an arrayAdapter for the specified List<string> variable
     * @param list List variable with strings
     * @return new arrayadapter with hint slot
     */
    private ArrayAdapter<String> arrayAdapt(List<String> list){
         return new ArrayAdapter<String>(
                this, R.layout.spinner_item, list){
            @Override
            public boolean isEnabled(int position){
                if(position ==0){
                    // disable first item
                    //first item will be for hint
                    return true;
                }
                else{
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else{
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
    }

    public AdapterView.OnItemSelectedListener AdapterListener(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }


    public void populateSearch(View v){
        TextView text = (TextView) v.findViewById(R.id.search_name);
        Spinner spin1 = (Spinner) v.findViewById(R.id.spinner);
        Spinner spin2 = (Spinner) v.findViewById(R.id.spinner2);
        Spinner spin3 = (Spinner) v.findViewById(R.id.spinner3);
        Boolean flag = false;
        //get the switch that is true
        for(int i=0;i<switcher.length;i++){
            if(switcher[i].isChecked()){
                settings.setStatSwitch(i);
                flag = true;//check if a stat switch was turned on
                break;
            }
        }
        //if no switch was turned on
        if(!flag){
            settings.setStatSwitch(6);
        }

        settings.setPokemon(text.getText().toString());
        settings.setType1((String) spin1.getSelectedItem());
        settings.setType2((String) spin2.getSelectedItem());
        settings.setAbility((String) spin3.getSelectedItem());
        settings.applySettings(table);
    }

}
