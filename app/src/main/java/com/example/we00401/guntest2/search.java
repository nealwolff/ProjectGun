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

public class search extends AppCompatActivity {
    private GestureDetectorCompat gestureObject;
    boolean first = true;

    //array list of listings
    ArrayList<listings> arrayList;
    ListView lv;

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
                EditText txtSearch = (EditText) findViewById(R.id.fieldSearch);
                String temp = txtSearch.getText().toString();
                //testHandler test = new testHandler();
                //if the gunbroker checkbox is clicked, saerch gunbroker.
                gunbrokerHandler GBH = new gunbrokerHandler(temp);
                List<listings> GBList = GBH.getListings();

                //get the gunbroker listings
                for (int i = 0; i < GBList.size(); i++) {
                   listings listing = GBList.get(i);
                    arrayList.add(listing);
                }
                /*
                AlertDialog alertDialog = new AlertDialog.Builder(search.this).create();
                alertDialog.setTitle("error");
                alertDialog.setMessage(temp);
                alertDialog.show();
                */
            }
        });




        Button btnSave =  (Button)findViewById(R.id.btnSave);
        final EditText textSave = (EditText)findViewById(R.id.textSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText textSave = (EditText) findViewById(R.id.textSave);
                String temp = textSave.getText().toString();

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