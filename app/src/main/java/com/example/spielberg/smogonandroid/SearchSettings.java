package com.example.spielberg.smogonandroid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Spielberg on 2/27/2018.
 */

public class SearchSettings {
    private String pokemon, type1, type2, ability;
    private int statSwitch=6;
    private JSONArray pokelist;
    private int rows[];
    private Handler handler;
    private int threadcount;
    private TableLayout results_table;
    //statSwitch = 0 HP
    //statSwitch = 1 Atk
    //statSwitch = 2 Def
    //statSwitch = 3 Spa
    //statSwitch = 4 Spd
    //statSwitch = 5 Spe
    //statSwitch = 6 None

    public SearchSettings(){
        //empty constructor
        pokemon = "";
        threadcount = 0;
    }
    public void setPokemon(String name){
        pokemon = name.toLowerCase();
        Log.i("Setting Name", pokemon);
    }

    public void setType1(String type){
        type1 = type;
    }

    public void setType2(String type){
        type2 = type;
    }

    public void setAbility(String ability){
        this.ability = ability;
    }

    public void setStatSwitch(int i){
        statSwitch = i;
    }

    public String getPokemon(){
        return pokemon;
    }

    public String getType1(){
        return type1;
    }

    public String getType2(){
        return type2;
    }

    public String getAbility(){
        return ability;
    }

    public int getStatSwitch(){
        return statSwitch;
    }

    public JSONArray getPokelist(){ return pokelist; }

    public void setView(int index, int visibility){
        rows[index] = visibility;
    }

    public void applySettings(TableLayout v){
        results_table = v;
//        for(int i=0;i<v.getChildCount();i++){
//            TableRow row = (TableRow) v.getChildAt(i);
//            applyName(row);
//            applyType1(row);
//            applyType2(row);
//            applyAbility(row);
//        }
        Thread thread1,thread2, thread3;
        int children = v.getChildCount();
        rows = new int[children];
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                String result = (String) message.obj;
                switch(message.what){
                    case 0:
                        break;

                    case 1:
                        threadcount++;
                        //check if all threads are finished
                        if(threadcount ==3 ){
                            setVisibility();
                            threadcount = 0;
                        }
                        break;

                    default:
                        break;
                }
            }
        };
        SearchEngine one = new SearchEngine(handler, v, 0,
                children/3, this);
        SearchEngine two = new SearchEngine(handler, v, children/3,
                (children/3)*2, this);
        SearchEngine three = new SearchEngine(handler, v, (children/3)*2,
                (children/3)*3 + (children%3), this);
        thread1 = new Thread(one);
        thread2 = new Thread(two);
        thread3 = new Thread(three);

        thread1.start();
        thread2.start();
        thread3.start();



    }

    public void setVisibility(){
        for(int i=0;i<results_table.getChildCount();i++){
            TableRow row = (TableRow) results_table.getChildAt(i);
            row.setVisibility(rows[i]);

        }
    }

    public void setPokelist(JSONArray list){
        pokelist = list;
    }
    private void applyAbility(TableRow row) {
        String text =((TextView) row.getChildAt(0)).getText().toString();
        int index = Integer.parseInt(text)-1;
        JSONArray abilitylist;
        String mytype;
        //only apply visible if type1 is an actual type
        try{
            if(!(ability.compareTo("Ability")==0)){
                abilitylist = pokelist.getJSONObject(index).getJSONArray(
                        "alts").getJSONObject(
                        0).getJSONArray("abilities");
                for(int i=0;i<abilitylist.length();i++){
                    mytype = abilitylist.getString(i);
                    if(mytype.contains(ability)){
                        return;
                    }

                }
                row.setVisibility(View.GONE);


            }

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void applyType2(TableRow row) {
        String type =((TextView) row.getChildAt(3)).getText().toString();
        //only apply visible if type1 is an actual type
        if(!(type2.compareTo("Type2")==0)){
            if(!type.contains(type2)){
                row.setVisibility(View.GONE);
            }
        }
    }

    private void applyType1(TableRow row) {
        String type =((TextView) row.getChildAt(3)).getText().toString();
        //only apply visible if type1 is an actual type
        if(!(type1.compareTo("Type1")==0)){
            if(!type.contains(type1)){
                row.setVisibility(View.GONE);
            }
        }
    }

    public void applyName(TableRow row){
        String name =((TextView) row.getChildAt(2)).getText().toString();
        if(pokemon==""){
            row.setVisibility(View.VISIBLE);
        }
        else if(!name.toLowerCase().contains(pokemon) && pokemon.length()>1){
            row.setVisibility(View.GONE);
        }
        else if(name.toLowerCase().contains(pokemon)){
            row.setVisibility(View.VISIBLE);
        }
    }



}
