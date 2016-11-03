package com.example.we00401.guntest2;

import android.app.Application;

public class globals extends Application {

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

}