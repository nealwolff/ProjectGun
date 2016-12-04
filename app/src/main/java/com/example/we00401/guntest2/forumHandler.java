package com.example.we00401.guntest2;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class forumHandler extends website {
    String globalSearchString = "";
    //	int pages = 0;
    String URL = "";
    String linkURL ="";
    List<String> HTMLfile;


    public forumHandler(String search, ArrayList<String> urls, String site, String cat,int pageNum) {
        if(site.equals("akfiles")) {
            URL = "http://www.akfiles.com/forums/forumdisplay.php?f=5&order=desc&page=" + pageNum;
            linkURL = "http://www.akfiles.com/forums/showthread.php?t=";
        } else if(site.equals("falfiles")) {
            URL = "http://www.falfiles.com/forums/forumdisplay.php?f=11&order=desc&page=" + pageNum;
            linkURL = "http://www.falfiles.com/forums/showthread.php?t=";
        } else if(site.equals("calguns")) {
            if (cat.contains("Semi-Auto pistols")) {
                URL = "http://www.calguns.net/calgunforum/forumdisplay.php?f=332&order=desc&page=" + pageNum;
            } else if (cat.contains("Revolvers")) {
                URL = "http://www.calguns.net/calgunforum/forumdisplay.php?f=332&order=desc&page=" + pageNum;
            } else if (cat.contains("Semi-Auto rifles")) {
                URL = "http://www.calguns.net/calgunforum/forumdisplay.php?f=92&order=desc&page=" + pageNum;
            } else if (cat.contains("Bolt-Action rifles")) {
                URL = "http://www.calguns.net/calgunforum/forumdisplay.php?f=92&order=desc&page=" + pageNum;
            } else if (cat.contains("Shotguns")) {
                URL = "http://www.calguns.net/calgunforum/forumdisplay.php?f=92&order=desc&page=" + pageNum;
            } else {
                URL = "http://www.calguns.net/calgunforum/forumdisplay.php?f=92&order=desc&page=" + pageNum;
            }
            linkURL = "http://www.calguns.net/calgunforum/showthread.php?t=";
        }

        globalSearchString = search;

        HTMLfile = getURL(URL);

        parseListings(search,urls);

    }//constructor

    //parse listings
    void parseListings(String search,ArrayList<String> urls) {

        for (int i =400;i<HTMLfile.size();i++) {
            String temp = HTMLfile.get(i);
            if(temp.contains("end show threads"))
                return;

            try{
                if(contains(temp,search)) {

                    String tempURL = "failed"; ///Listing URL
                    String tempName = "failed"; //Listing name
                    String tempImage = "http://i.imgur.com/wqjK8ZG.png"; //the listing image
                    String tempPrice = "$Check listing"; //the listing price

                    while (!temp.contains("</div>")) {
                        if(temp.contains("end show threads"))
                            break;

                        //gets the name
                        if (temp.contains("thread_title_")) {
                            tempName = android.text.Html.fromHtml(temp).toString();

                            String regex = "thread_title_([^\"]*)\">";
                            Pattern pat = Pattern.compile(regex);
                            Matcher m = pat.matcher(temp);
                            if(m.find()) {
                                tempURL = linkURL + m.group(1);
                            }
                        }
                        List<listings> theTemps = getListings();
                        //if the object already exists, do not add, continue to the next.
                        if (urls.contains(tempURL)) {
                            tempURL = "failed";
                            break;

                        }
                        else if(theTemps.contains(tempURL)){
                            break;
                        }

                        i++;
                        if (i < HTMLfile.size())
                            temp = HTMLfile.get(i);
                        else
                            break;
                    }
                    if(!tempURL.equals("failed")) {
                        //create the listing
                        listings workListing = new listings(tempImage, tempName, tempPrice, tempURL);

                        //add the listing to the list
                        //if the item has already been added, do not.
                        add(workListing);
                    }
                }
            }catch(Exception e){
                System.out.println("Exception at line " + i);
            }
        }
    }//parse listings


}