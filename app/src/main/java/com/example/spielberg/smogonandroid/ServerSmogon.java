package com.example.spielberg.smogonandroid;

import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

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

/**
 * Created by Spielberg on 7/6/2017.
 */

public class ServerSmogon implements Runnable {
    URL url;
    HttpURLConnection urlConnection ;
    Context context;
    String gamever;
    String job;/*Determines what job serversmogon will do*/
    String pokemon;

    public ServerSmogon(Context context, String gen, String job, String pokemon){
        this.context = context;
        this.gamever = gen;
        this.job = job;
        this.pokemon = pokemon;
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
        else{
            downloadArticles();
        }

    }

    public void downloadStats(){
        JSONArray jsonArray, sortedpoke;
        int n;
        Collection<String> pokedexlist = new TreeSet<String>(Collator.getInstance());
        InputStream is = context.getResources().openRawResource(R.raw.smogon);
        Scanner scan = new Scanner(is).useDelimiter("\n");
        while(scan.hasNext() ){
            try {

                url = new URL("http://"+scan.nextLine());
                if(!url.toString().contains("/"+gamever+"/")){
                    continue;
                }
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                System.out.println(urlConnection.getErrorStream());
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                System.out.println(in.toString());
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
                    } finally{
                        stream.close();
                    }

                    n = obj.length();
                    System.out.println(n);

                }

            } catch (IOException | JSONException  e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
                finally
            {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(url.toString().contains("/"+gamever+"/")){
                    break;
                }
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

        InputStream is = context.getResources().openRawResource(R.raw.smogon);
        Scanner scan = new Scanner(is).useDelimiter("\n");
        while(scan.hasNext() ){
            try {

                url = new URL("http://"+scan.nextLine().concat("abomasnow/"));
                if(!url.toString().contains("/"+gamever+"/")){
                    continue;
                }
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Scanner sc = new Scanner(in, "UTF-8").useDelimiter("\n");
                while(sc.hasNext()){
                    String joke = sc.nextLine();
                    if(!joke.contains("dexSettings")){
                        continue;
                    }
                    System.out.println("Success "+gamever);

                    String string = joke.substring(26, joke.length());

                    JSONObject obj = new JSONObject(string);
                    jsonArray = obj.getJSONArray("injectRpcs").getJSONArray(2)
                            .getJSONObject(1).getJSONArray("strategies");
                    n = jsonArray.length();
                    System.out.println(n);

                }

            } catch (IOException | JSONException  e ) {
                e.printStackTrace();
            } finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(url.toString().contains("/"+gamever+"/")){
                    break;
                }
            }

        }

    }
}
