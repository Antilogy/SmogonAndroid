package com.example.spielberg.smogonandroid;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Spielberg on 3/8/2018.
 */

public class HelperClass {
    Map<String, String> colormap;
    public HelperClass(){
        colormap = new HashMap<String, String>();
        colormap.put("Bug      ", "#a8b820");
        colormap.put("Dark     ", "#705848");
        colormap.put("Dragon   ", "#7038f8");
        colormap.put("Electric ", "#f8d030");
        colormap.put("Fairy    ", "#F98CFF");
        colormap.put("Fighting ", "#c03028");
        colormap.put("Fire     ", "#f08030");
        colormap.put("Flying   ", "#a890f0");
        colormap.put("Ghost    ", "#705898");
        colormap.put("Grass    ", "#78c850");
        colormap.put("Ground   ", "#e0c068");
        colormap.put("Ice      ", "#98d8d8");
        colormap.put("Normal   ", "#a8a878");
        colormap.put("Poison   ", "#a040a0");
        colormap.put("Psychic  ", "#f85888");
        colormap.put("Rock     ", "#b8a038");
        colormap.put("Steel    ", "#b8b8d0");
        colormap.put("Water    ", "#6890f0");
        colormap.put("         ", "black");
    }

    public void ColorCodeType(SpannableStringBuilder string){
        String type1 = string.subSequence(0,9).toString();
        String type2 = string.subSequence(9,18).toString();
        string.setSpan(new BackgroundColorSpan(Color.parseColor(colormap.get(type1))),
                    0,10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new BackgroundColorSpan(Color.parseColor(colormap.get(type2))),
                9,17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);




    }
}
