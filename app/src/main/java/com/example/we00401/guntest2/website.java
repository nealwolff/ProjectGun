package com.example.we00401.guntest2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

abstract public class website{
    public List<String> getURL(String theURL) {

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
}