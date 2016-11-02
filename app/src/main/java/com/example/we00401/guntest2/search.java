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
public class search extends AppCompatActivity {
    private GestureDetectorCompat gestureObject;
    boolean first = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        Button btn1 = (Button)findViewById(R.id.btnSave);

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(search.this, SavedSearch.class));
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
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.right_out,R.anim.left_in );

            }
            return true;
        }
    }
}
