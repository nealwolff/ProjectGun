package com.example.we00401.guntest2;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class settings extends AppCompatActivity {
    private GestureDetectorCompat gestureObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        gestureObject = new GestureDetectorCompat(this, new settings.LearnGesture());

        //get all the checkboxes
        CheckBox isStrict =  (CheckBox)findViewById(R.id.checkStrict);
        CheckBox gunbroker =  (CheckBox)findViewById(R.id.checkGunbroker);
        CheckBox AKfiles =  (CheckBox)findViewById(R.id.checkAKfiles);
        CheckBox armslist =  (CheckBox)findViewById(R.id.checkArmslist);
        CheckBox calguns =  (CheckBox)findViewById(R.id.checkCal);
        CheckBox GunsAmerica =  (CheckBox)findViewById(R.id.checkGunsA);
        CheckBox FALfiles =  (CheckBox)findViewById(R.id.checkFAL);

        if(((globals) this.getApplication()).getStrict()){
            isStrict.setChecked(true);
        }
        if(((globals) this.getApplication()).getGun()){
            gunbroker.setChecked(true);
        }
        if(((globals) this.getApplication()).getAK()){
            AKfiles.setChecked(true);
        }
        if(((globals) this.getApplication()).getArms()){
            armslist.setChecked(true);
        }
        if(((globals) this.getApplication()).getCal()){
            calguns.setChecked(true);
        }
        if(((globals) this.getApplication()).getAmerica()){
            GunsAmerica.setChecked(true);
        }
        if(((globals) this.getApplication()).getFAL()){
            FALfiles.setChecked(true);
        }

        isStrict.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setStrict();
            }
        });

        gunbroker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setGunbroker();
            }
        });

        AKfiles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setAkfiles();
            }
        });

        armslist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setArmslist();
            }
        });

        calguns.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setCalguns();
            }
        });
        GunsAmerica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setGunsAmerica();
            }
        });

        FALfiles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setFALfiles();
            }
        });




    }

    //sets the strict
    public void setStrict(){
        if(((globals) this.getApplication()).getStrict())
            ((globals) this.getApplication()).setStrict(false);
        else
            ((globals) this.getApplication()).setStrict(true);
    }
    //sets gunbroker value
    public void setGunbroker(){
        if(((globals) this.getApplication()).getGun())
            ((globals) this.getApplication()).setGun(false);
        else
            ((globals) this.getApplication()).setGun(true);
    }

    //sets AKfiles value
    public void setAkfiles(){
        if(((globals) this.getApplication()).getAK())
            ((globals) this.getApplication()).setAK(false);
        else
            ((globals) this.getApplication()).setAK(true);
    }
    //sets armslist value
    public void setArmslist(){
        if(((globals) this.getApplication()).getArms())
            ((globals) this.getApplication()).setArms(false);
        else
            ((globals) this.getApplication()).setArms(true);
    }
    //sets calguns value
    public void setCalguns(){
        if(((globals) this.getApplication()).getCal())
            ((globals) this.getApplication()).setCal(false);
        else
            ((globals) this.getApplication()).setCal(true);
    }

    //sets GunsAmerica value
    public void setGunsAmerica(){
        if(((globals) this.getApplication()).getAmerica())
            ((globals) this.getApplication()).setAmerica(false);
        else
            ((globals) this.getApplication()).setAmerica(true);
    }
    //sets FALfiles value
    public void setFALfiles(){
        if(((globals) this.getApplication()).getFAL())
            ((globals) this.getApplication()).setFAL(false);
        else
            ((globals) this.getApplication()).setFAL(true);
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
