package com.example.spielberg.smogonandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Spielberg on 7/6/2017.
 */

public class ServerSmogon implements Runnable {
    private URL url;
    private HttpURLConnection urlConnection ;
    private Context context;
    private String gamever;
    private String job;/*Determines what job serversmogon will do*/
    private String pokemon, format, moveset;//pokemon, format for analyses, moveset name
    private JSONObject baseArticle;
    private Handler mHandler;


    public ServerSmogon(Context context, String gen, String job, String pokemon){
        this.context = context;
        this.gamever = gen;
        this.job = job;
        this.pokemon = pokemon.toLowerCase();
    }
    public ServerSmogon(Context context, String gen, String job, String pokemon,
                        Handler m){
        this.context = context;
        this.gamever = gen;
        this.job = job;
        this.pokemon = pokemon.toLowerCase();
        mHandler = m;
    }

    @Override
    public void run(){
        // Moves the current Thread into the Background
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        /*
        * Code you want to run on the thread goes here
        * */
        if(job.contains("stats")){
            downloadStats();
        }
        else if(job.contains("strategy")){
            downloadArticles();
        }
        else if(job.contains("updateAll")){
            updateAllArticles();
        }
        

    }

    /**
     * This function should update the number of formats
     * and the number of articles in each format.
     */
    private void updateAllArticles() {
        JSONArray jsonArray, oldArray;
        JSONObject article;
        File f = new File(context.getFilesDir() +
                "/strategy_" + gamever + "/" + pokemon.toLowerCase() + ".txt");
        File temp = new File(context.getFilesDir() +
                "/strategy_" + gamever + "/temp_" + pokemon.toLowerCase() + ".txt");
        Scanner scan;
        FileOutputStream stream;
        Message completeMessage;

        try {

            url = new URL("https://www.smogon.com/dex/"+gamever+"/pokemon/"+
                    pokemon+"/");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner sc = new Scanner(in, "UTF-8").useDelimiter("\n");
            while(sc.hasNext()){
                String joke = sc.nextLine();
                if(!joke.contains("dexSettings")){
                    continue;
                }
                Log.i("Success: ",pokemon);

                String string = joke.substring(26, joke.length());

                JSONObject obj = new JSONObject(string);
                jsonArray = obj.getJSONArray("injectRpcs").getJSONArray(2)
                        .getJSONObject(1).getJSONArray("strategies");

                scan = new Scanner(f,"UTF-8").useDelimiter("/n");
                if(scan.hasNext()){
                    oldArray = new JSONArray(scan.nextLine());
                    if(jsonArray.toString().compareTo(oldArray.toString())!=0){
                        //update all the articles because it is different
                        stream = new FileOutputStream(temp);
                        try{
                            stream.write(jsonArray.toString().getBytes());
                            //delete old file
                            if(!f.delete()){
                                //couldn't delete file
                            }

                        } catch(IOException e){
                            e.printStackTrace();
                        } finally{
                            stream.close();
                        }
                        temp.renameTo(f);
                        //tell the handler the update was a success
                        completeMessage = mHandler.obtainMessage(1, "Update Success!");
                        completeMessage.sendToTarget();
                        break;

                    }
                    else{
                        //there is no update
                         completeMessage = mHandler.obtainMessage(0, "No Update");
                         completeMessage.sendToTarget();
                         break;
                    }
                }
                //file was empty, update anyway
                else{
                    stream = new FileOutputStream(temp);
                    try{
                        stream.write(jsonArray.toString().getBytes());
                        //delete old file
                        if(!f.delete()){
                            //couldn't delete file
                        }

                    } catch(IOException e){
                        e.printStackTrace();
                    } finally{
                        stream.close();
                    }
                    temp.renameTo(f);
                    //tell the handler the update was a success
                    completeMessage = mHandler.obtainMessage(1, "Update Success!");
                    completeMessage.sendToTarget();
                    break;
                }

                //first check if there are new formats

            }

        } catch (IOException | JSONException  e ) {
            completeMessage = mHandler.obtainMessage(2,
                    "There is no Internet Connection");
            completeMessage.sendToTarget();

            //what if there is no space to update article
            e.printStackTrace();
        } finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }

        }
    }

    public void downloadStats(){
        JSONArray jsonArray, sortedpoke;
        int n;

            try {

                url = new URL("https://www.smogon.com/dex/"+gamever+"/pokemon/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                Scanner sc = new Scanner(in, "UTF-8").useDelimiter("\n");
                while(sc.hasNext()){
                    String joke = sc.nextLine();
                    if(!joke.contains("dexSettings")){
                        continue;
                    }
                    //System.out.println("Success "+gamever);

                    String string = joke.substring(26, joke.length());

                    JSONObject obj = new JSONObject(string);
                    obj = obj.getJSONArray("injectRpcs").getJSONArray(1)
                            .getJSONObject(1);

                    //sort the pokemon array first
                    sortedpoke = sortJSONArray(obj.getJSONArray("pokemon"));
                    obj.put("pokemon",sortedpoke);
                    //end of sorting pokemon
                    File f = new File(context.getFilesDir() +
                            "/pokedex/"+ gamever+".txt");
                    FileOutputStream stream = new FileOutputStream(f);
                    //should save 7 objects from JSONObject obj
                    try{
                        stream.write(obj.toString().getBytes());
                    } catch(IOException e){
                        e.printStackTrace();
                    }finally{
                        stream.close();
                    }

                    n = obj.length();
                    System.out.println(n);
                    break;
                }

            } catch (IOException | JSONException  e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }


    }

    private JSONArray sortJSONArray(JSONArray pokemon) {
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < pokemon.length(); i++) {
            try {
                jsonValues.add(pokemon.getJSONObject(i));
            } catch(JSONException e){
                e.printStackTrace();
            }
        }


        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.get("name");
                    valB = (String) b.get("name");
                }
                catch (JSONException e) {
                    Log.e("comparator for pokelist",
                            "JSONException in combineJSONArrays sort section", e);
                }

                return valA.compareTo(valB);
            }
        });
        return new JSONArray(jsonValues);
    }

    public void downloadArticles(){
        JSONArray jsonArray;
        int n;




        try {

            url = new URL("https://www.smogon.com/dex/"+gamever+"/pokemon/"+
            pokemon+"/");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner sc = new Scanner(in, "UTF-8").useDelimiter("\n");
            while(sc.hasNext()){
                String joke = sc.nextLine();
                if(!joke.contains("dexSettings")){
                    continue;
                }
                Log.i("Success: ",pokemon);

                String string = joke.substring(26, joke.length());

                JSONObject obj = new JSONObject(string);
                jsonArray = obj.getJSONArray("injectRpcs").getJSONArray(2)
                        .getJSONObject(1).getJSONArray("strategies");
                File f= new File(context.getFilesDir() + "/strategy_" + gamever+
                "/" + pokemon.toLowerCase() + ".txt");

                FileOutputStream stream = new FileOutputStream(f);
                try{
                    stream.write(jsonArray.toString().getBytes());
                } catch(IOException e){
                    e.printStackTrace();
                } finally{
                    stream.close();
                }

                n = jsonArray.length();
                Log.i("ServerSmogon ", Integer.toString(n));
                break;

            }

        } catch (IOException | JSONException  e ) {
            e.printStackTrace();
        } finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }

        }




    }






    /**
     * this is a two step process
     */
    private void updatefileforNewArticles(){

    }


}
