package com.example.we00401.guntest2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchScreen extends AppCompatActivity {

    String theSearchTerm = "!none";
    String cattegory = "";
    boolean strict =false;
    boolean gun = true;
    boolean AK = false;
    boolean arms =false;
    boolean CAL =false;
    boolean america = false;
    boolean FAL = false;
    Toolbar toolbar;

    //arraylists to store the results in each site

    //contains strings of URLS for quick duplicate checking
    ArrayList<String> gunDupilcate = new ArrayList<String>();
    ArrayList<String> armsDuplicate = new ArrayList<String>();
    ArrayList<String> akDuplicate = new ArrayList<String>();
    ArrayList<String> americaDuplicate = new ArrayList<String>();
    ArrayList<String> calDuplicate = new ArrayList<String>();
    ArrayList<String> falDupilcate = new ArrayList<String>();

    ArrayList<listings> gunListings = new ArrayList<listings>();
    ArrayList<listings> armsListings = new ArrayList<listings>();
    ArrayList<listings> akListings = new ArrayList<listings>();
    ArrayList<listings> americaListings = new ArrayList<listings>();
    ArrayList<listings> calListings = new ArrayList<listings>();
    ArrayList<listings> falListings = new ArrayList<listings>();


    private GestureDetectorCompat gestureObject;
    boolean first = true;

    //array list of listings, this is what is showin the in listview
    ArrayList<listings> arrayList;
    ListView lv; //The listveiw that displays the listings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        arrayList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.foundView2);



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customAdapter adapter = new customAdapter(
                        getApplicationContext(), R.layout.customlayout, arrayList
                );
                lv.setAdapter(adapter);
            }
        });



        gestureObject = new GestureDetectorCompat(this, new SearchScreen.LearnGesture());

        Button btn1 = (Button)findViewById(R.id.btnLoad2);

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SearchScreen.this, SavedSearch.class));
            }
        });

        Button btnGO = (Button)findViewById(R.id.btnGo2);

        btnGO.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //hide the keyboard after the user presses "go"
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);



                //get the text from the search field
                EditText txtSearch = (EditText) findViewById(R.id.fieldSearch2);
                String input = txtSearch.getText().toString();
                final String searchTerm = input.trim(); //clear the text of any whitespace
                //if no terms entered, notify
                if(searchTerm.equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(SearchScreen.this).create();
                    alertDialog.setTitle("error");
                    alertDialog.setMessage("must enter data");
                    alertDialog.show();
                }
                else {
                    if(!searchTerm.equals(theSearchTerm) &&theSearchTerm !="!none"){
                        Toast.makeText(getApplicationContext(),
                                "Note: new search terms clears old search", Toast.LENGTH_SHORT).show();
                    }
                    //Ask the user to select categories to search.

                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(SearchScreen.this);
                    alt_bld.setTitle("Select a category");
                    final String[] grpname = {" Semi-Auto pistols "," Revolvers "," Bolt-Action rifles "," Semi-Auto rifles "," Shotguns "};
                    alt_bld.setSingleChoiceItems(grpname, -1, new DialogInterface
                            .OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            ///if a new search term is specified, clear the screen
                            if(!searchTerm.equals(theSearchTerm))
                                clearEverything();

                            theSearchTerm = searchTerm; //sets the global search term for when the user goes into other tabs.
                            cattegory = grpname[item];

                            //checks which tab the user is on
                            if(gun)
                                FindGunbroker(theSearchTerm, cattegory);

                            dialog.dismiss();

                        }
                    });
                    AlertDialog alert = alt_bld.create();
                    alert.show();


                }



            }
        });
        Button btnClear = (Button)findViewById(R.id.clrBtn);
        btnClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                clearEverything();

            }
        });
        //get all the button "Tabs"
        Button btnGun = (Button)findViewById(R.id.gunbroker);
        Button btnArms = (Button)findViewById(R.id.armlsist);
        Button btnAK = (Button)findViewById(R.id.akfiles);
        Button btnAmerica = (Button)findViewById(R.id.GunsAmerica);
        Button btnCal = (Button)findViewById(R.id.calGuns);
        Button btnFal = (Button)findViewById(R.id.falfiles);

        //when the gunbroker tab is selected
        btnGun.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = true;
                AK = false;
                arms =false;
                CAL =false;
                america = false;
                FAL = false;

                setArraylist(gunListings); //if the current search term has listings, load them


                if(!theSearchTerm.equals("!none")){
                    FindGunbroker(theSearchTerm, cattegory);
                }

                lv.invalidateViews();//refresh the listings
            }
        });

        btnArms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = false;
                AK = false;
                arms =true;
                CAL =false;
                america = false;
                FAL = false;
                setArraylist(armsListings); //if the current search term has listings, load them

                if(!theSearchTerm.equals("!none")){
                    //toDO create armslistFinder
                }
                lv.invalidateViews();//refresh the listings
            }
        });

        btnAK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = false;
                AK = true;
                arms =false;
                CAL =false;
                america = false;
                FAL = false;
                setArraylist(akListings); //if the current search term has listings, load them

                if(!theSearchTerm.equals("!none")){
                    //toDO create AkfilesFinder
                }
                lv.invalidateViews();//refresh the listings
            }
        });

        btnFal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = false;
                AK = false;
                arms =false;
                CAL =false;
                america = false;
                FAL = true;
                setArraylist(falListings); //if the current search term has listings, load them

                if(!theSearchTerm.equals("!none")){
                    //toDO create FalfilesFinder
                }
                lv.invalidateViews();//refresh the listings
            }
        });
        btnAmerica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = false;
                AK = false;
                arms =false;
                CAL =false;
                america = true;
                FAL = false;
                setArraylist(americaListings); //if the current search term has listings, load them

                if(!theSearchTerm.equals("!none")){
                    //toDO create americaFinder
                }
                lv.invalidateViews();//refresh the listings
            }
        });
        btnCal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = false;
                AK = false;
                arms =false;
                CAL =true;
                america = false;
                FAL = false;
                setArraylist(calListings); //if the current search term has listings, load them

                if(!theSearchTerm.equals("!none")){
                    //toDO create calgunsFinder
                }
                lv.invalidateViews();//refresh the listings
            }
        });

        Button btnSave =  (Button)findViewById(R.id.btnSave2);
        final EditText textSave = (EditText)findViewById(R.id.textSave2);

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText textSave = (EditText) findViewById(R.id.textSave2);
                String temp = textSave.getText().toString();
                temp=temp.replaceAll("\\s","");
                if (temp.matches("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(SearchScreen.this).create();
                    alertDialog.setTitle("error");
                    alertDialog.setMessage("You must enter a save name");

                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog.show();
                }

            }
        });

        final EditText searchText = (EditText)findViewById(R.id.fieldSearch2);

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(first == true ) {
                    searchText.setText("");
                    first = false;
                }
            }
        });
    }

    private void clearEverything() {
        arrayList.clear(); //clear the arraylist
        gunListings.clear();
        armsListings.clear();
        akListings.clear();
        americaListings.clear();
        calListings.clear();
        falListings.clear();

        gunDupilcate.clear();
        armsDuplicate.clear();
        akDuplicate.clear();
        americaDuplicate.clear();
        calDuplicate.clear();
        falDupilcate.clear();

        lv.invalidateViews();//refresh the listings
    }

    //finds the gunbroker results
    public void FindGunbroker(String searchTerm, String grpname){
        Toast.makeText(getApplicationContext(),
                "Searching " + searchTerm + " in \""+grpname+"\"", Toast.LENGTH_LONG).show();


        String temp=searchTerm.replaceAll("\\s","+");
        //testHandler test = new testHandler();
        //if the gunbroker checkbox is clicked, search gunbroker.
        boolean check = true;
        int pagenum=0;
        //Linked list of lists of listings, used to store the lists of each page.
        LinkedList<List<listings>> allPages = new  LinkedList<List<listings>>();
        while(check) {
            pagenum++;
            //ArrayList<String> compares = getGlobalArraylist();
            gunbrokerHandler GBH = new gunbrokerHandler(temp, grpname, gunDupilcate, pagenum);
            List<listings> GBList = GBH.getListings();

            if (GBList.size() == 0) {
                break;
            }
            allPages.add(GBList);
        }

        //traveres the pages and add the objects to their respective lists.
        for(int j = allPages.size()-1;j>=0;j--) {

            //if nothing was found
            if(allPages.size()==0){
                Toast.makeText(getApplicationContext(),
                        "No Items found", Toast.LENGTH_LONG).show();
                break;
            }

            //get the page
            List<listings> GBList = allPages.get(j);
            //get the gunbroker listings from that page
            for (int i = GBList.size() - 1; i >= 0; i--) {
                listings listing = GBList.get(i);
                arrayList.add(0, listing);
                gunListings.add(0, listing);
            }
            //create a linked list of strings containing all the urls for duplicate comparison
            //ArrayList<String> urls = new ArrayList<String>();
            for (int i = 0; i < arrayList.size(); i++) {
                gunDupilcate.add(arrayList.get(i).getURL());
            }
            //set the global arraylist containing URLS to the current arraylsit
            //setGlobalArraylist(urls);
            //refresh the list view
            lv.invalidateViews();
        }

        if (arrayList.size() == 0) {
            Toast.makeText(getApplicationContext(),
                    "No Items found", Toast.LENGTH_LONG).show();
        }
    }

    //this method clears the global arraylist and sets one of the
    public void setArraylist(ArrayList<listings> replace){
        arrayList.clear();
        for (int i = 0 ; i<replace.size();i++){
            arrayList.add(replace.get(i)) ;
        }
    }
    /*
    //function to set the arraylist.
    public void setGlobalArraylist(ArrayList<String> arraylist){
        ((globals) this.getApplication()).setArrayList(arraylist);
    }
    //function to get the arraylist
    public ArrayList<String> getGlobalArraylist(){
        return ((globals) this.getApplication()).getArraylist();
    }
    */

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener {

        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY){

            if(event2.getX() > event1.getX()){
                Intent intent = new Intent(
                        SearchScreen.this, settings.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_out,R.anim.left_in );

            }
            return true;
        }
    }
}