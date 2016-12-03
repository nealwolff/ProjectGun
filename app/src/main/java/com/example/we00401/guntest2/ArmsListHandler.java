package com.example.we00401.guntest2;
import java.util.List;

public class ArmsListHandler extends website {

    String globalSearchString = "";
    //	int pages = 0;
    String URL = "";
    List<String> HTMLfile;


    public ArmsListHandler(String search) {
        globalSearchString = search;

//		URL = "https://www.armslist.com/classifieds/search?location=usa&category=all&page=2&posttype=7&ships=False";
        URL = "https://www.armslist.com/classifieds/search?location=usa&category=all";

//		https://www.armslist.com/classifieds/search?search=I+dont+-+even&location=usa&category=all&posttype=7&ships=False

        //get number of pages
//		pages = getTotalNumberofPages();
//		System.out.println(pages);

        int pagecount = 1;
        while(true) {
            URL = "https://www.armslist.com/classifieds/search?location=usa&page="+pagecount+"&search="+search;
            HTMLfile = getURL(URL);
            System.out.println(pagecount);
            if(containsResults(URL) != true) {
                break;
            } else {
                try{
                    parseListings();
                } catch(Exception e) {
                    System.out.println("Page: "+pagecount+"\n"+e);
                }

            }

//

            pagecount++;
        }

    }//constructor



    //parse listings
    void parseListings() {
        String[] output = new String[4];

        for(int i = 0; i < HTMLfile.size(); i++) {

            if(HTMLfile.get(i).contains("<div style=\"po")) {
//				System.out.println(i+"\t"+HTMLfile.get(i));
                output[0] = parseImage(i);

                System.out.println(i+"\t"+"IMAGE:\t"+output[0]);
                System.out.println(i+"\t"+"ITEM:\t"+output[1]);
                System.out.println(i+"\t"+"PRICE:\t"+output[2]);
                System.out.println(i+"\t"+"URL:\t"+output[3]);
                System.out.println();

            }//if
        }//for i

    }//parse listings


    String parseImage(int index) {
        String output = "";

        for(int i = index; i < (index+10); i++) {
            if(HTMLfile.get(i).contains("background-size: cover; background-repeat: no-repeat; background-image:")) {
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



    int getTotalNumberofPages() {
        int output = 0;
        String temp = "";
        String[] tempA;
        String[] tempB;
        HTMLfile = getURL(URL);
        for(int i = 0; i < HTMLfile.size(); i++) {
            if(HTMLfile.get(i).contains(" results in All Categories")) {
                temp = HTMLfile.get(i);
                break;
            }//if
        }//for

//		System.out.println(temp);
        tempA = temp.split(" of ");
        int per = Integer.parseInt(tempA[0].substring(16));
//		System.out.println(per);
        int total = Integer.parseInt(tempA[1].substring(0,tempA[1].length()- 31));

        return((int)Math.ceil((double)total/per));
    }//getTotalNumberofpages


}//armslisthandler
