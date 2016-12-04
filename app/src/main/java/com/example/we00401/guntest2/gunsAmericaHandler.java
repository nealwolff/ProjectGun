package com.example.we00401.guntest2;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class gunsAmericaHandler extends website {
    String URL = "";
    public String theNUM ="";
    boolean kill = false;
    public gunsAmericaHandler(String Search,ArrayList<String> urls,int pageNum){


        List<String>HTMLfile;
        String modifiedSearch = checkem(Search);



            URL = "https://www.gunsamerica.com/Search.htm?T="+modifiedSearch+"&ltid-all=1&og=1&as=365&ns=0&sort=ListingStartDate&=&pagenum="+pageNum;
            HTMLfile = getURL(URL);


        for(int j = 0; j < HTMLfile.size(); j++) {
            if(kill)
                break;
            if(HTMLfile.get(j).contains("Listings Found</span>")){
                String regex = "allcaps'>Page 1 of ([^\"]*)</span> <span";
                Pattern pat = Pattern.compile(regex);
                Matcher m = pat.matcher(HTMLfile.get(j));
                if(m.find()) {
                    theNUM = android.text.Html.fromHtml(m.group(1)).toString().replaceAll(" 1","");
                    theNUM.trim();
                    if(theNUM.equals(""))
                        theNUM="1";
                }
            }

            if (HTMLfile.get(j).contains("listings-list listings-item-box clearfix"))
                parseListing(j, HTMLfile, urls);
        }


    }

    //modifies search term to match site formating
    //ROFL, guns'merica breaks if you enter '~'
    //nice quality control there guys
    private String checkem(String input) {
        String output = "";
        for(int i = 0; i < input.length(); i++) {
            if(input.charAt(i) == '&') { //%26
                output += "%26";
            } else if (input.charAt(i) == ' ') { //%20
                output += "%20";
            } else if (input.charAt(i) == '~') { //lol, not supported; bad STQA on thier part; make it a space, nobody will notice
                output += "%20";
            } else if (input.charAt(i) == '?') { //nope, just nope
                output += "%20";
            } else if (input.charAt(i) == '-') { //nope, just nope
                output += "%20";
            } else {
                output += input.charAt(i);
            }
        }
        return output;
    }
    //outputs the components of the listing as a string array
//    tempImage,tempName,tempPrice, tempURL
    //take the line the html line the listing is on and get the various infos
    private void parseListing(int listingIndex, List<String> file, ArrayList<String> urls) {
        String[] output = new String[4];

        //find image
        for(int i = listingIndex+0; i < file.size(); i++) {

            if(file.get(i).contains("itemprop=\"image\"")) {

                output[0] = parseImage2(file.get(i));
                output[1] = parseName(file.get(i));
                output[3] = parseURL(file.get(i));



                //find that price
                for(int j = listingIndex; j < file.size(); j++){
                    if(file.get(j).contains("<span itemprop=\"price\">")) {

                        output[2] = parsePrice(file.get(j));
                        break;
                    }
                }

               // System.out.println("\nImgURL:\t"+output[0]);
               // System.out.println("Name:\t"+output[1]);
               // System.out.println("Price:\t"+output[2]);
               // System.out.println("URL:\t"+output[3]);
                if(urls.contains(output[3])) {
                    kill = true;
                    return;
                }
                add(new listings(output[0],output[1],output[2],output[3]));
                break;
            }

        }
    }


    private String parsePrice(String input) {
        String output = "";
        for(int i = 52; i < input.length()-7; i++) {
            output += input.charAt(i);
        }

        return output;
    }

    private String parseURL(String input) {

        String output;
        String pattern = "<a[^>]+href\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

        Pattern p = Pattern.compile(pattern);

        Matcher cupid = p.matcher(input);
        cupid.find();
        output = "https://www.gunsamerica.com"+cupid.group(1);
        return output;
    }

    private String parseName(String input) {
        // TODO Auto-generated method stub
        String output;

        String pattern = "<a[^>]+title\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

        Pattern p = Pattern.compile(pattern);

        Matcher cupid = p.matcher(input);
        cupid.find();
        output = cupid.group(1);

        return output;
    }

    //doesnt work any faster, but looks hell of a lot better
    private String parseImage2(String input) {
        //if there is no photo, dont waste anyone's time
        if(input.contains("nophoto.gif")) {
            return("http://i.imgur.com/wqjK8ZG.png");
        }

        String output = "TEST";
        String pattern = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

        Pattern p = Pattern.compile(pattern);

        Matcher cupid = p.matcher(input);
        cupid.find();
        output = "https://www.gunsamerica.com"+cupid.group(1);

        return output;
    }

}

