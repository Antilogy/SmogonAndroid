package com.example.spielberg.smogonandroid;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Spielberg on 3/1/2018.
 */

public class SearchEngine implements Runnable {
    private Handler mHandler;
    private TableLayout table;
    private int start, end, threadID;
    private SearchSettings settings;
    private int index[];

    public SearchEngine(Handler hand, TableLayout table,
                        int start, int end, SearchSettings settings, int id){
        mHandler = hand;
        this.table = table;
        this.start = start;
        this.end = end;
        this.settings = settings;
        threadID = id;
        index = new int[2];
        index[0] = start;
        index[1] = end;
    }

    public SearchEngine(Handler hand, SearchSettings settings, int id){
        mHandler = hand;
        this.settings = settings;
        threadID = id;
        index = new int[2];
        index[0] = start;
        index[1] = end;
    }

    @Override
    public void run(){
        // Moves the current Thread into the Background
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        /**
         * Code you want to run on the thread goes here
         */
        ArrayList<RowStats> list = settings.getResults();
        if(threadID == 4){
            //do sorting instead
            findBigg(list, 0, list.size(), settings.getStatSwitch());
            Message message = mHandler.obtainMessage(threadID, index);
            message.sendToTarget();
            return;
        }
        applySettings(list);
        //report when done
        Message message = mHandler.obtainMessage(threadID, index);
        message.sendToTarget();

    }

    /**
     * sort array according to the stat value in descending order
     * @param array
     * @param index
     * @param size
     * @param stat
     */
    public void findBigg(ArrayList<RowStats> array, int index, int size, int stat){
        int handle, hindex = index;
        RowStats rowStats = array.get(index);
        handle =  array.get(index).stats[stat];

        //compare the handle to all values
        if(hindex==array.size()-1){
            array.remove(hindex);
            array.add(0, rowStats);
            return;
        }
        for(int j=index+1;j<(size);j++){
            if(handle >  array.get(j).stats[stat]){
                rowStats = array.get(j);
                handle = array.get(j).stats[stat];
                hindex = j;
            }


        }
        array.remove(hindex);
        array.add(0, rowStats);
        findBigg(array, index+1, size, stat);

    }

    public void applySettings(ArrayList<RowStats> v){

        //perform the fastest search ever
        if(settings.getPokemon() =="" && settings.getType1()=="Type1" &&
                settings.getType2()=="Type2" && settings.getAbility() == "Ability"){
            for(int i=start;i<end;i++){
                settings.setView(i, View.VISIBLE);

            }
            return;
        }
        //otherwise perform regular search
        for(int i=start;i<end;i++){
            TableRow row = v.get(i).row;
            applyName(row, i);
            applyType1(row, i);
            applyType2(row, i);
            applyAbility(row, i);

        }

    }


    private void applyAbility(TableRow row, int rowindex) {
        String text =((TextView) row.getChildAt(0)).getText().toString();
        int index = Integer.parseInt(text)-1;
        JSONArray abilitylist;
        String mytype;
        //only apply visible if type1 is an actual type
        try{
            if(!(settings.getAbility().compareTo("Ability")==0)){
                abilitylist = settings.getPokelist().getJSONObject(index).getJSONArray(
                        "alts").getJSONObject(
                        0).getJSONArray("abilities");
                for(int i=0;i<abilitylist.length();i++){
                    mytype = abilitylist.getString(i);
                    if(mytype.contains(settings.getAbility())){
                        return;
                    }

                }
                settings.setView(rowindex, View.GONE);



            }

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void applyType2(TableRow row, int rowindex) {
        String type =((TextView) row.getChildAt(3)).getText().toString();
        //only apply visible if type1 is an actual type
        if(!(settings.getType2().compareTo("Type2")==0)){
            if(!type.contains(settings.getType2())){
                settings.setView(rowindex, View.GONE);
            }
        }
    }

    private void applyType1(TableRow row, int rowindex) {
        String type =((TextView) row.getChildAt(3)).getText().toString();
        //only apply visible if type1 is an actual type
        if(!(settings.getType1().compareTo("Type1")==0)){
            if(!type.contains(settings.getType1())){
                settings.setView(rowindex, View.GONE);
            }
        }
    }

    public void applyName(TableRow row, int rowindex){
        String name =((TextView) row.getChildAt(2)).getText().toString();
        if(settings.getPokemon()==""){
            settings.setView(rowindex, View.VISIBLE);
        }
        else if(!name.toLowerCase().contains(settings.getPokemon())){
            settings.setView(rowindex, View.GONE);
        }
        else if(name.toLowerCase().contains(settings.getPokemon())){
            settings.setView(rowindex, View.VISIBLE);
        }
    }
}
