package com.example.spielberg.smogonandroid;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Spielberg on 2/9/2018.
 */

public class pokearticle extends AppCompatActivity {
    ServerSmogon smogon;
    private Toolbar toolbar;
    private TabLayout tabLayout, tablayout2;
    private ViewPager viewPager;
    private String pokemon, gen;
    private final String gen1="rb",gen2="gs",gen3="rs",gen4="dp",gen5="bw",gen6="xy",gen7="sm";
    private JSONArray strtgy_art, stats;
    TabActivity1 tab1;
    TabActivity2 tab2;
    TabActivity3 tab3;
    private int index;
    private Boolean test=false;
    Handler handler;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        this.gen = bundle.getString("gen");
        setContentView(R.layout.pokearticle);
        this.index = bundle.getInt("index");
        if(savedInstanceState != null){
            //restore tabs
            List<Fragment> fraglist = getSupportFragmentManager().getFragments();
            tab1 = (TabActivity1) fraglist.get(0);
            tab2 = (TabActivity2) fraglist.get(1);
            FragmentManager fm = getSupportFragmentManager();
            if(fraglist.size() == 3){
                tab3 = (TabActivity3) fraglist.get(2);
            }
//            else{
//                setupTab3();
//            }

        }
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                String result = (String) message.obj;
                switch(message.what){
                    case 0:
                        //setup pop window with warning
                        setupPopWindow(message.what);
                        break;

                    case 1:
                        //file was updated
                        //update the contents of this view
                        setupPopWindow(message.what);
                        refreshLayout();
                        break;


                    case 2:
                        //no internet connection
                        setupPopWindow(message.what);
                        break;

                    case 3:
                        //articles were downloaded
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                        setupArticleandStats();
                        refreshlayoutpart2();
                        break;

                    default:
                        break;
                }
            }
        };
        callSmogon();



    }



    /**
     * Refresh contents of this layout
     */
    public void refreshLayout(){
        JSONObject obj;
        JSONArray pokedex;
        Thread thread;
        TextView stats,overview, article;
        Boolean directory;


        File f = new File(getFilesDir() +
                "/pokedex/" + gen+".txt");
        try{
            Scanner scan = new Scanner(f,"UTF-8").useDelimiter("/n");
            if(scan.hasNext()){
                obj = new JSONObject(scan.nextLine());
                pokedex = obj.getJSONArray("pokemon");
                setupDirectory(pokedex.getJSONObject(index).getString("name"));

            }
        } catch(FileNotFoundException | JSONException e){
            e.printStackTrace();
        }

        tablayout2 = (TabLayout) findViewById(R.id.format);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupFormat(tablayout2);
        //update the overview and article tabs; tab2 and tab3 respectively
        updateTabs();





    }
    @Override
    public void onPause(){
        super.onPause();
        if( dialog != null){
            dialog.dismiss();
        }
    }

    public void refreshlayoutpart2(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tablayout2 = (TabLayout) findViewById(R.id.format);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.removeAllTabs();
        tabLayout.setupWithViewPager(viewPager);


        //add a tab for all formats the pokemon is in
        setupFormat(tablayout2);

    }


    public void updateTabs(){
        TabLayout.Tab tab = tablayout2.getTabAt(0);
        String format = tab.getText().toString();
        String overviewString;
        JSONArray move;
        //change contents of overview and articles
        //based on tab selected
        try {
            overviewString = strtgy_art.getJSONObject(tab.getPosition()
            ).getString("overview");
            if(overviewString == null){
                //this is a sample analyses
                tab2.newOverview("This is a sample analyses.");
                return;
            }
            //otherwise use the given overview and comments
            overviewString = overviewString.concat(strtgy_art.getJSONObject(
                    0).getString("comments"));
            tab2.newOverview(overviewString);

            move = strtgy_art.getJSONObject(tab.getPosition()
            ).getJSONArray("movesets");
            if(tab3 != null){
                tab3.newMoveset(move.toString(), format);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void callSmogon(){
        JSONObject obj;
        JSONArray pokedex;
        Thread thread;
        TextView stats,overview, article;
        Boolean directory;


        File f = new File(getFilesDir() +
        "/pokedex/" + gen+".txt");
        try{
            Scanner scan = new Scanner(f,"UTF-8").useDelimiter("/n");
            if(scan.hasNext()){
                obj = new JSONObject(scan.nextLine());
                pokedex = obj.getJSONArray("pokemon");
                setupDirectory(pokedex.getJSONObject(index).getString("name"));

            }
        } catch(FileNotFoundException | JSONException e){
            e.printStackTrace();
        }


//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);







    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id== R.id.update_button){
            //do something here
            ServerSmogon sm = new ServerSmogon(this, gen, "updateAll", pokemon,
                    handler);
            Thread thread = new Thread(sm);
            thread.start();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupFormat(TabLayout tablayout2) {
        String format;
        TabLayout.Tab format_tab;
        int length = 0;
        if(strtgy_art != null){
            length = strtgy_art.length();
        }

        tablayout2.removeAllTabs();
        if(length >0){
            for(int j=0;j<length;j++){
                try{
                    format = strtgy_art.getJSONObject(j).getString("format");
                    format_tab = tablayout2.newTab();
                    format_tab.setText(format);
                    tablayout2.addTab(format_tab);

                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
            tablayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    String format = tab.getText().toString();
                    String overview;
                    JSONArray move;
                    //change contents of overview and articles
                    //based on tab selected
                    try {
                        overview = strtgy_art.getJSONObject(tab.getPosition()
                        ).getString("overview");
                        if(overview == null){
                            //this is a sample analyses
                            tab2.newOverview("This is a sample analyses.");
                            return;
                        }
                        //otherwise use the given overview and comments
                        overview = overview.concat(strtgy_art.getJSONObject(
                                0).getString("comments"));
                        tab2.newOverview(overview);

                        move = strtgy_art.getJSONObject(tab.getPosition()
                        ).getJSONArray("movesets");
                        tab3.newMoveset(move.toString(), format);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.i("Format ", format);

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }



    }

    private void setupViewPager(ViewPager viewpager){
//        Log.i("tabs "+viewpager.getChildCount(), "yes");
//        viewpager.removeAllViews();
        pokearticle.ViewPagerAdapter adapter = new pokearticle.ViewPagerAdapter(
                getSupportFragmentManager());
        if(tab2 == null && tab3 == null){
            tab1 = new TabActivity1();
            tab2 = new TabActivity2();
            tab3 = new TabActivity3();

            setupTab1();
            setupTab2();
            setupTab3();

        }
        else if(tab3==null){
            setupTab3();
        }


        adapter.addFragment(tab1, "Stats");
        adapter.addFragment(tab2, "Overview");
        adapter.addFragment(tab3, "Articles");


        viewpager.setAdapter(adapter);
    }

    private void setupTab1() {
        tab1 = TabActivity1.newInstance(stats.toString(), pokemon.substring(0,1).toUpperCase()
        +pokemon.substring(1));
    }
    private void setupTab2(){
        String over;
        if(strtgy_art == null){
            //the article was not downloaded
            tab2 = TabActivity2.newInstance("There is no analyses. \n" +
                    "Try connecting to the internet and try again.");
        }
        else if(strtgy_art.length()==0){
            tab2 = TabActivity2.newInstance("There is no analyses");
        }
        else{
            try{
                over = strtgy_art.getJSONObject(0).getString("overview");
                if(over == null){
                    //this is a sample analyses
                    tab2 = TabActivity2.newInstance("This is a sample analyses.");
                    return;
                }
                //otherwise use the given overview and comments
                over = over.concat(strtgy_art.getJSONObject(0).getString("comments"));
                tab2 = TabActivity2.newInstance(over);
            } catch (JSONException e){
                e.printStackTrace();
            }

        }
    }
    private void setupTab3(){
        JSONArray moveset;

        if(strtgy_art == null){
            //could not download article
            tab3 = TabActivity3.newInstance("", pokemon, gen, "no format");
        }
        else if(strtgy_art.length()==0){
            tab3 = TabActivity3.newInstance(strtgy_art.toString(), pokemon, gen, "no format");
        }
        else{
            try{
                moveset = strtgy_art.getJSONObject(0).getJSONArray("movesets");
                tab3 = TabActivity3.newInstance(moveset.toString(), pokemon, gen,
                        strtgy_art.getJSONObject(0).getString("format"));
            } catch (JSONException e){
                e.printStackTrace();
            }
        }

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }

    public void setupArticleandStats(){
        JSONArray poke;
        File f = new File(getFilesDir() +
                "/strategy_" + gen + "/" + pokemon + ".txt");
        File stat = new File(getFilesDir() +
                "/pokedex/" + gen + ".txt");
        try{
            Scanner scan = new Scanner(f,"UTF-8").useDelimiter("/n");
            if(scan.hasNext()){
                //strtgy_art might be empty if there is no analyses
                strtgy_art = new JSONArray(scan.nextLine());

            }
            scan = new Scanner(stat, "UTF-8").useDelimiter("/n");
            if(scan.hasNext()){
                poke = new JSONObject(scan.nextLine()).getJSONArray("pokemon");
                stats = poke.getJSONObject(index).getJSONArray("alts");
            }
        } catch (FileNotFoundException | JSONException e){
            e.printStackTrace();
        }
    }

    /**setup directory for specified game version
    * */
    private void setupDirectory(String name){
        this.pokemon = toLowerCase(name);
        ServerSmogon sm;
        File folder = new File(getFilesDir() +
                "/strategy_" + gen + "/");
        File f = new File(getFilesDir() +
                "/strategy_" + gen + "/" + pokemon + ".txt");
        //File[] contents = folder.listFiles();
        Thread thread;
        //check if directory is made
        if(folder.exists() && folder.isDirectory() && f.exists()){
            setupArticleandStats();
            refreshlayoutpart2();
            return;
        }
        //if not make it

        folder.mkdirs();
        try{
            f.createNewFile();
        } catch(IOException e){
            e.printStackTrace();
        }


        //download strategy articles
        sm = new ServerSmogon(getBaseContext(), gen, "strategy", pokemon, handler);
        thread = new Thread(sm);

        thread.start();
        dialog=new ProgressDialog(this);
        dialog.setMessage("Downloading "+pokemon);
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
//        try{
//
//            thread.join();
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }

    }
    public String toLowerCase(String string){
        String proto = string.toLowerCase();
        String lower = "";
        Character c;

        for(int i=0;i<proto.length();i++){
            c = proto.charAt(i);
            //remove special characters from name
            //and leave only the underscore
            if (c.compareTo('.')==0) {
                lower = checkflag( lower);
                continue;
            }
            else if (c.compareTo(' ')==0) {
                lower = checkflag( lower);
                continue;
            }
            else if (c.compareTo('%')==0){
                continue;
            }
            lower = lower + c;
            test = false;

        }
        return lower;
    }

    /**
     * Used to add underscore to a string
     * @param string
     * @return string+'_'
     */
    public String checkflag( String string){
        if(test){
            return string;
        }
        else{
            test = true;
            string = string + '_';
            return string;
        }
    }
    private void setupPopWindow(int status){
        CoordinatorLayout mainLayout = (CoordinatorLayout) findViewById(R.id.pokearticle);

        //inflate the layout of the popupwindow
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_checkarticles, null);
        //choose the popup window to use
        if(status==1){
            //success with update
            popupView = inflater.inflate(R.layout.popup_success_update, null);
        }
        else if(status==2){
            popupView = inflater.inflate(R.layout.popup_no_wifi, null);
        }

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = (mainLayout.getWidth()/4)*3;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
