package com.example.we00401.guntest2;

import android.app.Application;

import java.util.ArrayList;

public class globals extends Application {

    //array list of listings
    private ArrayList<String> arrayList = new ArrayList<>();
    private boolean strict =false;
    private boolean gun = true;
    private boolean AK = true;
    private boolean arms =true;
    private boolean CAL =true;
    private boolean america = true;
    private boolean FAL = true;

    //getters
    public boolean getStrict() {
        return strict;
    }

    public boolean getGun() {
        return gun;
    }
    public boolean getAK() {
        return AK;
    }
    public boolean getArms() {
        return arms;
    }
    public boolean getCal() {
        return CAL;
    }
    public boolean getAmerica() {
        return america;
    }
    public boolean getFAL() {
        return FAL;
    }
    public ArrayList<String> getArraylist() {
        if(arrayList == null){
            arrayList.add("empty");
        }
        return arrayList;
    }

    //setters
    public void setStrict(boolean input) {
        strict=input;
    }

    public void setGun(boolean input) {
        gun=input;
    }
    public void setAK(boolean input) {
        AK=input;
    }
    public void setArms(boolean input) {
        arms=input;
    }
    public void setCal(boolean input) {
        CAL=input;
    }
    public void setAmerica(boolean input) {
        america=input;
    }
    public void setFAL(boolean input) {
        FAL=input;
    }
    public void setArrayList(ArrayList<String> input) {
        arrayList = input;
    }

}