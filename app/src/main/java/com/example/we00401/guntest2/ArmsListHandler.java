package com.example.we00401.guntest2;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArmsListHandler extends website {
    String pageNum ="";
    String globalSearchString = "";
    //	int pages = 0;
    String URL = "";
    List<String> HTMLfile;


    public ArmsListHandler(String search,String cat, ArrayList<String> urls, int pageNum) {
        globalSearchString = search;


        URL = "https://www.armslist.com/classifieds/search?location=usa&page="+pageNum+"&search="+search;

        if(cat.contains("Semi-Auto pistols")){
            URL += "&category=handguns";
        }else if(cat.contains("Revolvers")){
            URL += "&category=handguns";
        }else if(cat.contains("Semi-Auto rifles")){
            URL += "&category=rifles";
        }else if(cat.contains("Bolt-Action rifles")){
            URL += "&category=rifles";
        }else if(cat.contains("Shotguns")){
            URL += "&category=shotguns";
        }else{
            URL += "&category=guns";
        }

        HTMLfile = getURL(URL);

        parseListings(urls);


    }//constructor



    //parse listings
    void parseListings(ArrayList<String> urls) {
        String[] output = new String[4];

        for (int i =0;i<HTMLfile.size();i++) {

            String temp = HTMLfile.get(i);

            if(temp.contains("'>NEXT PAGE")){
                String kek=android.text.Html.fromHtml(temp).toString();
                String regex = "\n([^\"]*)\nNEXT PAGE";
                Pattern pat = Pattern.compile(regex);
                Matcher m = pat.matcher(kek);
                if(m.find()) {
                    kek = m.group(1);
                }
                Scanner scanner = new Scanner(kek);
                while (scanner.hasNextLine()) {
                    pageNum= scanner.nextLine();
                }
                scanner.close();

            }

            if (temp.contains("<div style=\"po")){
                String tempURL = "error"; ///Listing URL
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
                //if the object already exists, do not add, continue to the next.
                if(urls.contains(tempURL)) {
                    tempURL = "error";
                    continue;
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
                if(!tempURL.equals("error"))
                    add(workListing);
            }

        }


    }//parse listings


}//armslisthandler
