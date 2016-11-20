package com.example.we00401.guntest2;


import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;


abstract public class website{
    private List <listings>siteListings = new LinkedList<listings>();

    public List<String> getURL(String theURL) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url;
        List <String> HTMLfile = new LinkedList<String>();

        try {
            url = new URL(theURL);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            //add each line of the HTML file to the linked list
            //each line is stored as a string
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                HTMLfile.add(inputLine);
            }
            br.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return HTMLfile;

    }

    public List<listings> getListings(){
        return siteListings;
    }

    public void add(listings listing){
        siteListings.add(listing);
    }

    //for debugging
    public void print(){
        for (int i =0;i<siteListings.size();i++){
            listings kek= siteListings.get(i);
            System.out.println(kek.getName());
            System.out.println(kek.getImage());
            System.out.println(kek.getURL());
            System.out.println(kek.getPrice());
            System.out.println();
        }

    }
}