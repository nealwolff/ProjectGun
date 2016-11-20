package com.example.we00401.guntest2;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.view.WindowManager;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Toast;

public class search extends AppCompatActivity {
    private GestureDetectorCompat gestureObject;
    boolean first = true;

    //array list of listings
    ArrayList<listings> arrayList;
    ListView lv; //The listveiw that displays the listings

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        arrayList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.foundView);



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customAdapter adapter = new customAdapter(
                        getApplicationContext(), R.layout.customlayout, arrayList
                );
                lv.setAdapter(adapter);
            }
        });



        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        Button btn1 = (Button)findViewById(R.id.btnLoad);

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(search.this, SavedSearch.class));
            }
        });

        Button btnGO = (Button)findViewById(R.id.btnGo);

        btnGO.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //hide the keyboard after the user presses "go"
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);



                //get the text from the search field
                EditText txtSearch = (EditText) findViewById(R.id.fieldSearch);
                String input = txtSearch.getText().toString();
                final String searchTerm = input.trim(); //clear the text of any whitespace
                //if no terms entered, notify
                if(searchTerm.equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(search.this).create();
                    alertDialog.setTitle("error");
                    alertDialog.setMessage("must enter data");
                    alertDialog.show();
                }
                else {
                    //Ask the user to select categories to search.

                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(search.this);
                    alt_bld.setTitle("Select a category");
                    final String[] grpname = {" Semi-Auto pistols "," Revolvers "," Bolt-Action rifles "," Semi-Auto rifles "};
                    alt_bld.setSingleChoiceItems(grpname, -1, new DialogInterface
                            .OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Toast.makeText(getApplicationContext(),
                                    "Searching " + searchTerm + " in \""+grpname[item] +"\"", Toast.LENGTH_LONG).show();


                            String temp=searchTerm.replaceAll("\\s","+");
                            //testHandler test = new testHandler();
                            //if the gunbroker checkbox is clicked, search gunbroker.

                            ArrayList<String> compares = getGlobalArraylist();
                            gunbrokerHandler GBH = new gunbrokerHandler(temp,grpname[item],compares);
                            List<listings> GBList = GBH.getListings();

                            //get the gunbroker listings
                            for (int i = GBList.size()-1; i >= 0; i--) {
                                listings listing = GBList.get(i);
                                arrayList.add(0,listing);
                            }
                            //create a linked list of strings containing all the urls for duplicate comparison
                            ArrayList<String> urls = new ArrayList<String>();
                            for(int i = 0;i<arrayList.size();i++){
                                urls.add(arrayList.get(i).getURL());
                            }
                            //set the global arraylist containing URLS to the current arraylsit
                            setGlobalArraylist(urls);
                            //refresh the list view
                            lv.invalidateViews();

                            if(arrayList.size()==0){
                                Toast.makeText(getApplicationContext(),
                                        "No Items found", Toast.LENGTH_LONG).show();
                            }

                            dialog.dismiss();

                        }
                    });
                    AlertDialog alert = alt_bld.create();
                    alert.show();


                }



            }
        });




        Button btnSave =  (Button)findViewById(R.id.btnSave);
        final EditText textSave = (EditText)findViewById(R.id.textSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText textSave = (EditText) findViewById(R.id.textSave);
                String temp = textSave.getText().toString();
                temp=temp.replaceAll("\\s","");
                if (temp.matches("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(search.this).create();
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

        final EditText searchText = (EditText)findViewById(R.id.fieldSearch);

        searchText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(first == true ) {
                    searchText.setText("");
                    first = false;
                }
            }
        });
    }

    //function to set the arraylist.
    public void setGlobalArraylist(ArrayList<String> arraylist){
        ((globals) this.getApplication()).setArrayList(arraylist);
    }
    public ArrayList<String> getGlobalArraylist(){
        return ((globals) this.getApplication()).getArraylist();
    }

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
                        search.this, settings.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_out,R.anim.left_in );

            }
            return true;
        }
    }
}