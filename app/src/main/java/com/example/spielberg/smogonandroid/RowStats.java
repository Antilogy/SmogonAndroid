package com.example.spielberg.smogonandroid;

/**
 * Created by Spielberg on 3/3/2018.
 */

import android.widget.TableRow;

/**
 * This class keeps a reference to one row and its stats for easy sorting
 */
public class RowStats {
    public TableRow row;
    public int[] stats;
    public int visibility;
    //HP = 0
    //ATK = 1
    //DEF = 2
    //SPA = 3
    //SPD = 4
    //SPE = 5
    public RowStats(TableRow row, int[] stats){
        this.row = row;
        this.stats = stats;
    }



}
