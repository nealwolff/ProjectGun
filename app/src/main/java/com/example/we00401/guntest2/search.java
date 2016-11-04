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

        listings kek = new listings("http://i0.kym-cdn.com/photos/images/facebook/000/617/382/1d8.png","topkek","$100");
        listings kek2 = new listings("https://c.thumbs.redditmedia.com/tSkMnJtm4xt_e4QD.png","kek","$1340");
        arrayList.add(kek);
        arrayList.add(kek2);

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




        Button btnSave =  (Button)findViewById(R.id.btnSave);
        final EditText textSave = (EditText)findViewById(R.id.textSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText textSave = (EditText) findViewById(R.id.textSave);
                String temp = textSave.getText().toString();

                if (temp.matches("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(search.this).create(); //Read Update
                    alertDialog.setTitle("error");
                    alertDialog.setMessage("You must enter a save name");

                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog.show();  //<-- See This!
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