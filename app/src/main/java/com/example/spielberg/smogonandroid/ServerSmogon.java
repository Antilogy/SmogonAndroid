package com.example.spielberg.smogonandroid;

import android.content.Context;
import android.os.Process;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Spielberg on 7/6/2017.
 */

public class ServerSmogon implements Runnable {
    URL url;
    HttpURLConnection urlConnection ;
    Context context;
    String gamever;

    public ServerSmogon(Context context, String strings){
        this.context = context;
        this.gamever = strings;
    }
    @Override
    public void run(){
        JSONArray jsonArray;
        int n;
        // Moves the current Thread into the Background
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        /*
        * Code you want to run on the thread goes here
        * */

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
