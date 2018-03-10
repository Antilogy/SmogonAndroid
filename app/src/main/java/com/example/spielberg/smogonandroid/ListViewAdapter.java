package com.example.spielberg.smogonandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



/**
 * Created by Spielberg on 3/9/2018.
 */

public class ListViewAdapter extends ArrayAdapter<RowStats> {
    int fontSize;
    ArrayList<Integer> results;
    ArrayList<RowStats> rows;
    String gen;
    public ListViewAdapter(Context context, ArrayList<RowStats> linear,
                           int size, ArrayList<Integer> index, String gen){
        super(context, 0, linear);
        fontSize = size;
        results = index;
        rows = linear;
        this.gen = gen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        RowStats item = getItem(results.get(position));
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent,
                    false);
        }
        //data population
        TextView tvname = (TextView) convertView.findViewById(R.id.pokemon_nameid);
        TextView tvtype = (TextView) convertView.findViewById(R.id.pokemon_type_stats);

        tvname.setText(item.name);
        tvtype.setText(item.typeandStats);

        tvname.setTextSize(getContext().getResources().getDimension(fontSize));
        tvtype.setTextSize(getContext().getResources().getDimension(fontSize));
        convertView.setId(getItem(results.get(position)).index);
        convertView.setOnClickListener(new View.OnClickListener(){
            //TableRow is calling onClick()
            public void onClick(View v){
                LinearLayout littlerow = (LinearLayout) v;
                //TextView number = (TextView) littlerow.getChildAt(0);
                //Get original index for pokedex
                int index = littlerow.getId();
                Intent intent = new Intent(getContext(), pokearticle.class);
                Bundle bundle = new Bundle();
                bundle.putInt("index", index);
                bundle.putString("gen", gen);
//                    Log.i("tablerow",number.getText().toString()+
//                            text.getText().toString());
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
        return convertView;

    }
    @Override
    public int getCount(){
        return results.size();
    }

}
