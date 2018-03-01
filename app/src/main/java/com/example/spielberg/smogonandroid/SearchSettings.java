package com.example.spielberg.smogonandroid;

import android.util.Log;
import android.view.View;

/**
 * Created by Spielberg on 2/27/2018.
 */

public class SearchSettings {
    private String pokemon, type1, type2, ability;
    private int statSwitch;
    public SearchSettings(){
        //empty constructor
    }
    public void setPokemon(String name){
        pokemon = name;
        Log.i("Setting Name", pokemon);
    }

    public void setType1(String type){
        type1 = type;
    }

    public void setType2(String type){
        type2 = type2;
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

    public void applySettings(View v){

    }



}
