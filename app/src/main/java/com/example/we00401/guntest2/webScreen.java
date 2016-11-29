package com.example.we00401.guntest2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class webScreen extends AppCompatActivity {
    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WebView webview=(WebView)findViewById(R.id.webBrowser);


        gestureObject = new GestureDetectorCompat(this, new webScreen.LearnGesture());


        String name= getIntent().getStringExtra("name");
        setTitle(name);


        String url= getIntent().getStringExtra("url");

        webview.loadUrl(url);
    }
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
