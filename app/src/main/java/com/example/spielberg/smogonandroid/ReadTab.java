package com.example.spielberg.smogonandroid;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Spielberg on 2/21/2018.
 */

public class ReadTab extends Fragment {
    JSONObject object;
    String pokemon, gen, format, moveset, clipboard;
    public Handler mHandler;
    public ReadTab(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        try{
            Bundle bundle = getArguments();
            object = new JSONObject(bundle.getString("article"));
            pokemon = bundle.getString("pokemon");
            gen = bundle.getString("gen");
            format = bundle.getString("format");
            clipboard = new String();
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.read_tab, container, false);
        TextView textView = (TextView)v.findViewById(R.id.article_real);
        setupTextview(textView);
        return v;
    }

    private void setupTextview(TextView text){
        JSONArray test, movelist;
        JSONObject obj;
        int index;
        String title;
        try{
            clipboard = clipboard.concat(pokemon.toUpperCase()+"\n");
            moveset = object.getString("name");
            title = ("<h1>"+moveset+"</h1>");
            clipboard = clipboard.concat(moveset+"\n");

            text.setText( Html.fromHtml(title +"\n"));
            //add items
            text.append("Items: ");
            clipboard = clipboard.concat("Items: ");
            test = object.getJSONArray("items");
            index = test.length();
            for(int i=0;i<index;i++){
                text.append(test.getString(i));
                clipboard = clipboard.concat(test.getString(i));
                if(i<index-1){
                    text.append(" "+getText(R.string.backward)+" ");
                    clipboard = clipboard.concat(" "+getText(R.string.backward)+" ");
                }
            }
            text.append("\n");
            clipboard = clipboard.concat("\n");
            //add ability
            text.append("Ability: ");
            clipboard = clipboard.concat("Ability: ");
            test = object.getJSONArray("abilities");
            index = test.length();
            for(int i=0;i<index;i++){
                text.append(test.getString(i) +" ");
                clipboard = clipboard.concat(test.getString(i) +" ");
                if(i<index-1){
                    text.append( getText(R.string.backward)+" ");
                    clipboard = clipboard.concat(getText(R.string.backward)+" ");
                }
            }
            text.append("\n");
            clipboard = clipboard.concat("\n");
            //add EVs
            text.append("EVs: ");
            clipboard = clipboard.concat("EVs: ");
            test = object.getJSONArray("evconfigs");
            obj = test.getJSONObject(0);
            if(obj.getInt("hp")>0){
                text.append(" "+Integer.toString(obj.getInt("hp"))+" Hp "+
                        getText(R.string.backward));
                clipboard = clipboard.concat(" "+Integer.toString(obj.getInt("hp"))+" Hp "+
                        getText(R.string.backward));
            }
            if(obj.getInt("atk")>0){
                text.append(" "+Integer.toString(obj.getInt("atk"))+" Atk "+
                        getText(R.string.backward));
                clipboard = clipboard.concat(" "+Integer.toString(obj.getInt("atk"))+" Atk "+
                        getText(R.string.backward));
            }
            if(obj.getInt("def")>0){
                text.append(" "+Integer.toString(obj.getInt("def"))+" Def "+
                        getText(R.string.backward));
                clipboard = clipboard.concat(" "+Integer.toString(obj.getInt("def"))+" Def "+
                        getText(R.string.backward));
            }
            if(obj.getInt("spa")>0){
                text.append(" "+Integer.toString(obj.getInt("spa"))+" SpA "+
                        getText(R.string.backward));
                clipboard = clipboard.concat(" "+Integer.toString(obj.getInt("spa"))+" SpA "+
                        getText(R.string.backward));
            }
            if(obj.getInt("spd")>0){
                text.append(" "+Integer.toString(obj.getInt("spd"))+" SpD "+
                        getText(R.string.backward));
                clipboard = clipboard.concat(" "+Integer.toString(obj.getInt("spd"))+" SpD "+
                        getText(R.string.backward));
            }
            if(obj.getInt("spe")>0){
                text.append(" "+Integer.toString(obj.getInt("spe"))+" Spe ");
                clipboard = clipboard.concat(" "+Integer.toString(obj.getInt("spe"))+" Spe ");
            }
            else{
                CharSequence sample = text.getText();
                sample = sample.subSequence(0, sample.length()-1);
                text.setText(sample);

                sample = clipboard.subSequence(0, clipboard.length()-1);
                clipboard = sample.toString();

            }
            text.append("\n");
            clipboard = clipboard.concat("\n");
            //add Natures
            text.append("Natures: ");
            clipboard = clipboard.concat("Natures: ");
            test = object.getJSONArray("natures");
            index = test.length();
            for(int i=0;i<index;i++){
                text.append(test.getString(i) );
                clipboard = clipboard.concat(test.getString(i));
                if(i<index-1){
                    text.append(" " + getText(R.string.backward) + " ");
                    clipboard = clipboard.concat(" " + getText(R.string.backward) + " ");
                }
            }
            text.append("\n");
            clipboard = clipboard.concat("\n");
            //add Moves

            test = object.getJSONArray("moveslots");
            index = test.length();
            for(int i=0;i<index;i++){
                text.append("Move" +Integer.toString(i+1)+getText(R.string.colon)+" ");
                clipboard = clipboard.concat("Move" +Integer.toString(i+1)+getText(R.string.colon)+" ");
                movelist = test.getJSONArray(i);
                for(int j=0;j<movelist.length();j++){
                    text.append(movelist.getString(j) );
                    clipboard = clipboard.concat(movelist.getString(j));
                    if(j<movelist.length()-1){
                        text.append(" "+getText(R.string.backward) + " ");
                        clipboard = clipboard.concat(" "+getText(R.string.backward) + " ");
                    }
                }
                text.append("\n");
                clipboard = clipboard.concat("\n");

            }
            text.append("\n");
            clipboard = clipboard.concat("\n");
            //add ivconfigs, haha nope


            text.append(Html.fromHtml(object.getString("description")));

        } catch(JSONException e){
            e.printStackTrace();
        }

    }

    public String getClipboard(){
        return clipboard;
    }



    public static ReadTab newInstance(String text, String pokemon, String gen, String format,
                                      Handler m){
        ReadTab tab = new ReadTab();
        Bundle args = new Bundle();
        args.putString("article", text);
        args.putString("pokemon", pokemon);
        args.putString("gen", gen);
        args.putString("format", format);
        tab.setArguments(args);
        tab.mHandler = m;
        return tab;
    }
}
