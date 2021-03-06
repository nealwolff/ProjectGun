package com.example.we00401.guntest2;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import static com.example.we00401.guntest2.R.id.btnLogOut;

public class settings extends AppCompatActivity {




    private GestureDetectorCompat gestureObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //logout button
        Button out = (Button)findViewById(btnLogOut);

        out.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(settings.this, MainScreen.class));
                finish();
            }
        });


        gestureObject = new GestureDetectorCompat(this, new settings.LearnGesture());

    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    class LearnGesture extends GestureDetector.SimpleOnGestureListener {

        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY){

            if(event2.getX() < event1.getX()){
                finish();
                overridePendingTransition(R.anim.right_in,R.anim.left_out);

            }
            return true;
        }
    }
}
