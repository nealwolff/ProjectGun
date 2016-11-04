package com.example.we00401.guntest2;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class gunbrokerHandler extends website {

    public gunbrokerHandler(String Search){
        String URL = "http://www.gunbroker.com/Auction/BrowseItems2.aspx?Keywords=" + Search +"&Cats=0";
        List<String>HTMLfile = getURL(URL);


        for (int i =0;i<HTMLfile.size();i++) {

            String temp = HTMLfile.get(i);

            if (temp.contains("<td><a class=\"ItmTLnk\"")){
                int j = i+1;
                String temp2=HTMLfile.get(j);
                listings workListing; //the current listing
                String tempURL = ""; //Listing URL
                String tempName = ""; //Listing name
                String tempImage = ""; //the listing image
                String tempPrice = ""; //the listing price

                while(!temp2.contains("<td><a class=\"ItmTLnk\"")){



                    //gets the listing URL
                    if(temp2.contains("<a href=\"/item")){
                        String regex = "\"([^\"]*)\"";
                        Pattern pat = Pattern.compile(regex);
                        Matcher m = pat.matcher(temp2);
                        if(m.find()) {
                            tempURL = "http://www.gunbroker.com" + m.group(1);
                        }

                        //gets the name
                        //tempName = temp2.replaceAll("\\<.*?>","");
                        //tempName = tempName.trim();
                    }

                    if(temp2.contains("BItmTLnk")){
                        tempName=android.text.Html.fromHtml(temp2).toString();
                    }

                    //gets the name
                    /*
                    if(temp2.contains("BItmTLnk")){
                        String regex = ">([^\"]*)</";
                        Pattern pat = Pattern.compile(regex);
                        Matcher m = pat.matcher(temp2);
                        if(m.find()) {
                            tempName =m.group(1);
                        }
                        if(tempName==""){
                            tempName=Search;
                        }
                    }
                    */
                    //gets the image
                    if(temp2.contains("<img alt=")){
                        String regex = "http([^\"]*).jpg";
                        Pattern pat = Pattern.compile(regex);
                        Matcher m = pat.matcher(temp2);
                        if(m.find()) {
                            tempImage = "http" + m.group(1) + ".jpg";
                        }
                    }

                    //gets the price
                    if(temp2.contains("<td class=\"lrt\">$")){
                        tempPrice = temp2.replaceAll("\\<.*?>","");
                        tempPrice = tempPrice.trim();
                        if(tempPrice.equals(""))
                            tempPrice="error";
                    }

                    j++;
                    if(j<HTMLfile.size())
                        temp2=HTMLfile.get(j);
                    else
                        break;
                }
                //if the listing does not have an image, replace it with a default one
                if(tempImage.equals(""))
                    tempImage="http://i.imgur.com/wqjK8ZG.png";

                //create the listing
                workListing = new listings(tempImage,tempName,tempPrice, tempURL);

                //add the listing to the list
                add(workListing);
            }

        }
        print();

    }//end of constructor


}

