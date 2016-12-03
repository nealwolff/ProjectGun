package com.example.we00401.guntest2;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArmsListHandler extends website {

    String globalSearchString = "";
    //	int pages = 0;
    String URL = "";
    List<String> HTMLfile;


    public ArmsListHandler(String search) {
        globalSearchString = search;

        URL = "https://www.armslist.com/classifieds/search?location=usa&category=all";


        int pagecount = 1;
        while(true) {
            URL = "https://www.armslist.com/classifieds/search?location=usa&page="+pagecount+"&search="+search;
            HTMLfile = getURL(URL);
            System.out.println(pagecount);
            if(containsResults(URL) != true) {
                break;
            } else {
                try {
                    parseListings();
                } catch (Exception e) {
                    System.out.println("Page: " + pagecount + "\n" + e);
                }

            }
            pagecount++;
        }

    }//constructor



    //parse listings
    void parseListings() {
        String[] output = new String[4];

        for (int i =0;i<HTMLfile.size();i++) {

            String temp = HTMLfile.get(i);

            if (temp.contains("<div style=\"po")){
                String tempURL = ""; ///Listing URL
                String tempName = ""; //Listing name
                String tempImage = ""; //the listing image
                String tempPrice = ""; //the listing price

                //gets listing URL
                String regex = "href=\"([^\"]*)\">";
                Pattern pat = Pattern.compile(regex);
                Matcher m = pat.matcher(temp);
                if(m.find()) {
                    tempURL = "http://www.armslist.com" + m.group(1);
                }

                int j = i+1;
                String temp2=HTMLfile.get(j);
                listings workListing; //the current listing


                while(!temp2.contains("<div style=\"po")){


                    //gets the name
                    if(temp2.contains("<a style=\"color: #e0dbb3; font-weight: bold;")){
                        tempName=android.text.Html.fromHtml(temp2).toString();
                    }

                    //gets the image
                    if(temp2.contains("background-position:")){
                        regex = "'([^\"]*)'";
                        pat = Pattern.compile(regex);
                        m = pat.matcher(temp2);
                        if(m.find()) {
                            tempImage = m.group(1);
                        }
                    }

                    //gets the price
                    if(temp2.contains("    $ ")){
                        tempPrice = temp2;
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
                //if the item has already been added, do not.
                add(workListing);
            }

        }


    }//parse listings

    private String parseItem(int i) {
        String item = "";

        return item;
    }


    String parseImage(int index) {
        String output = "";

        for(int i = index; i < (index+10); i++) {
            if(HTMLfile.get(i).contains("background-repeat: no-repeat; background-image:")) {
                output = "[IMAGE]";
            }
        }
        return(output);
    }



    //checks if current page has results
    boolean containsResults(String input) {
        boolean output = true;

        HTMLfile = getURL(input);

        for(int i = 0; i<HTMLfile.size();i++) {
            if(HTMLfile.get(i).contains("No active listings.")) {
                output = false;
                break;
            }
        }

        return(output);

    }


}//armslisthandler
