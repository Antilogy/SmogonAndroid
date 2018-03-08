package com.example.spielberg.smogonandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
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
    int width;
    Switch switcher[];
    JSONArray types, abilities, pokemon;
    List<String> typeList, abilityList, type2List;
    TableLayout table;
    int statOrder;//current order of rows by stat
    ArrayList<RowStats> myList;
    ArrayList<RowStats>[] result_list;
    Handler handler;//used to save results of thread rows
    int threadcount;//number of tablethreads that are finished
    ProgressDialog dialog;
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
        statOrder = 6;
        threadcount = 0;
        myList = new ArrayList<>();
        result_list = new ArrayList[2];
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

    @Override
    public void onPause(){
        super.onPause();

        //dismiss dialog
        if(dialog != null){
            dialog.dismiss();
        }
    }

    private void populate_pokedex(String gen) {
        //setup width
        int screen_width = Resources.getSystem().getDisplayMetrics().widthPixels;
        Log.i("width "+screen_width, "hello");
        if(screen_width<=1080){
            width = R.dimen.NanoText;
        }
        else if(screen_width<=1280){
            width = R.dimen.SmallestText;
        }
        else if(screen_width<=1440){
            width = R.dimen.TinyText;
        }
        else if(screen_width<=1920){
            width = R.dimen.TinyText;
        }
        else{
            //screen is really wide
            width = R.dimen.SmallestText;
        }
        generation = gen;
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                ArrayList<RowStats> results = (ArrayList<RowStats>) message.obj;
                threadcount++;
                switch(message.what){
                    case 0:
                        result_list[message.what] = results;
                        refreshTable(message.what);
                        break;

                    case 1:
                        result_list[message.what] = results;
                        refreshTable(message.what);
                        break;
                }
            }
        };

        dialog=new ProgressDialog(this);
        dialog.setMessage("Loading Pokedex Results");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();


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
                //test threadrows
                TableThread table1 = new TableThread(handler, this, 0,
                        pokemon.length()/2, pokemon, 0, generation);
                TableThread table2 = new TableThread(handler, this, pokemon.length()/2,
                        (pokemon.length()/2)*2 +pokemon.length()%2, pokemon, 1, generation);

                Thread thread = new Thread(table1);
                Thread thread1 = new Thread(table2);

                thread.start();
                thread1.start();



            }
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Function to refresh table
     * @param id
     */
    private void refreshTable(int id){
//        String type = new String();
//        String name = new String();
        if(threadcount == 2){
            table.removeAllViews();
            for(int i=0; i<result_list.length;i++){
                myList.addAll(result_list[i]);
                //refresh tablelayout
            }
            for(int i=0;i<myList.size();i++){
                table.addView(myList.get(i).row);
//                String sample = ((TextView)myList.get(i).row.getChildAt(2)).getText().toString();
//                if(sample.length()>type.length()){
//                    type = sample;
//                }
//                sample = ((TextView)myList.get(i).row.getChildAt(1)).getText().toString();
//                if(sample.length()>name.length()){
//                    name = sample;
//                }
            }
            //print name and type
//            Log.i("name %d"+name.length(), name);
//            Log.i("type %d"+type.length(), type);
            threadcount = 0;

            if(dialog.isShowing()){
                dialog.dismiss();
            }

        }


    }

    private void addpokemon(JSONArray pokemon) {
        table = (TableLayout) findViewById(R.id.pokemon_results);
        TableLayout header_table = (TableLayout) findViewById(R.id.pokemon_header_row);
        table.removeAllViews();
        header_table.removeAllViews();
        SpannableStringBuilder build= new SpannableStringBuilder();
        TableRow row;
        //add header row
        row = new TableRow(this);
        row.setId(View.generateViewId());
        row.setBackgroundColor(Color.GRAY);
        row.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
        row.setWeightSum(1f);

//        addView(row,getText(R.string.number), 0.06f);//add index
        addView(row,"pic", 0.08f);//add pic column
        addView(row,"Name", 0.28f);//add name column
        addView(row,getText(R.string.type), 0.28f);//add type column
        //addView(row,getText(R.string.stats));//add stats column
        addView(row, getResources().getString(R.string.stats), 0.360f);
//        addView(row, "ATK",0.060f);
//        addView(row, "DEF",0.060f);
//        addView(row, "SPA",0.060f);
//        addView(row, "SPD",0.060f);
//        addView(row, "SPE",0.060f);

        row.setPadding(5,5,5,5);
        row.setGravity(Gravity.CENTER_HORIZONTAL);



        //add row
        header_table.addView(row);


    }


    /**
     * generic addview function for tablerow.
     * Adds a textview with name to a tablerow.
     */
    private void addView(TableRow row, CharSequence name, float weight){
        TextView tx;
        //add pic column
        tx = new TextView(this);
        tx.setLayoutParams(new TableRow.LayoutParams(
                0, TableRow.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams params = (TableRow.LayoutParams)tx.getLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;
        params.weight = weight;
        tx.setLayoutParams(params);
        tx.setText(name);

        //set text size
        tx.setTextSize(getResources().getDimension(width));
        tx.setTypeface(Typeface.MONOSPACE);
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
        settings.setResults(myList);
        settings.applySettings(table);
    }

}
