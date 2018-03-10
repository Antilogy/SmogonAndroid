package com.example.spielberg.smogonandroid;

/**
 * Created by Spielberg on 3/3/2018.
 */

import android.text.SpannableStringBuilder;
import android.widget.LinearLayout;

/**
 * This class keeps a reference to one row and its stats for easy sorting
 */
public class RowStats {
    public int[] stats;
    public int visibility;
    public Boolean inUse;
    public int index;
    public SpannableStringBuilder name;
    public SpannableStringBuilder typeandStats;
    //HP = 0
    //ATK = 1
    //DEF = 2
    //SPA = 3
    //SPD = 4
    //SPE = 5
    public RowStats(SpannableStringBuilder name, SpannableStringBuilder typeandStats,
                    int[] stats, int index){
        this.name = name;
        this.typeandStats = typeandStats;
        this.stats = stats;
        inUse = false;
        this.index = index;
    }

    public void changeUsage(){
        inUse = !inUse;
    }



}
