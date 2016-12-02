package com.example.we00401.guntest2;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

public class FalFilesHandler extends website {

	String globalSearchString = "";
	
    public FalFilesHandler(String Search){
//        String URL = "http://www.gunbroker.com/Auction/BrowseItems2.aspx?Keywords=" + Search +"&Cats=0";

    	globalSearchString = Search;
        String URL = "http://www.falfiles.com/forums/forumdisplay.php?f=11&order=desc"; //FALfiles marketplace URL; THIS IS WHERE TO START
        //subsequent pages look are formated like this
        //http://www.falfiles.com/forums/forumdisplay.php?f=11&order=desc&page=2
        //                                                                  ^^^ page number
        //STEP 01: Get total number of result pages

        List<String> HTMLfile = getURL(URL); //Gets html for the first page of results
        String FINDTHIS = "Page 1 of"; //line with this on it will have the correct url for last page
        String lastPageHolding = ""; //holding pen of raw html for this
        //FIND THAT LAST PAGE URL!!!!
        for(int i = 0; i < HTMLfile.size(); i++) {
            if(HTMLfile.get(i).contains(FINDTHIS)) {
//            	System.out.println(HTMLfile.get(i));
            	lastPageHolding = HTMLfile.get(i);
//                lastPageHolding = HTMLfile.get(i); //if its on this line, dump it
                break;
            }
        }
//
//
//
//        //STEP 01.2: isolate number from HTML
        lastPageHolding = lastPageHolding.substring(65, lastPageHolding.length()-5);
        System.out.println(lastPageHolding);
        
//        String Str2 = lastPageHolding;
//        int count = 0;
//        while (Str2.contains("page=")) {//go up to the point before the index of the last page
//            count++;
//            Str2 = "";
//            for(int i = count; i < lastPageHolding.length(); i++) {
//                Str2 += lastPageHolding.charAt(i);
//            }
////			System.out.println(Str2);
//        }//end while
//
//        //remove 'age=' from before the number
//        count = count+ 4;
//        Str2 = "";
//        for(int i = count; i < lastPageHolding.length(); i++) {
//            Str2 += lastPageHolding.charAt(i);
//        }
////		System.out.println();
////		System.out.println(Str2);
//
//        //remove all the fluff from the end of the number
//        count = 0;
//        //look for " from end of url
//        for(int i = 0; i < Str2.length(); i++) {
//            if(Str2.charAt(i)=='\"') {
//                break;
//            }
//            else {
//                count++;
//            }
//        }
//
//        lastPageHolding = "";
//        for(int i = 0; i < count; i++) {
//            lastPageHolding += Str2.charAt(i);
//        }

        //lastPageHolding is now the string reresentation of how many pages there are
//		System.out.println(Str1);
        //LAST PAGE INDEX IS NOW THE TOTAL NUMBER OF PAGES IN THE
//        System.out.println("Last Page Index");
        int LastPageIndex = Integer.parseInt(lastPageHolding);

        
//        System.out.println(LastPageIndex);
//    }/**REMOVE WHEN COMPLETE**/

        //STEP 02: Make URLs for each page
        //make an array of prefab URLs for easy insertion
        String[] URLs = new String[LastPageIndex];
//        http://www.falfiles.com/forums/forumdisplay.php?f=11&order=desc&page=2
//        URLs[0] = "http://www.akfiles.com/forums/forumdisplay.php?f=5&order=desc"; //PAGE ONE
        URLs[0] = "http://www.falfiles.com/forums/forumdisplay.php?f=11&order=desc"; //PAGE ONE
        //all other pages
        for(int i = 1; i < URLs.length; i++) {
            URLs[i] = "http://www.falfiles.com/forums/forumdisplay.php?f=11&order=desc&page=" + (i+1);
            
        }
//        //ghetto debug display urls
//        for(int i = 0; i < URLs.length; i++) {
//        	System.out.println(URLs[i]);
//        }



        //STEP 03: GO THROUGH EACH PAGE AND FIND RELEVANT RESULTS
        for(int i = 0; i < URLs.length; i++) {
        	System.out.println("+Getting Page "+i);
            HTMLfile = getURL(URLs[i]); //get the current page
//            System.out.println(URLs[i]);

    		System.out.println("^Parsing Page "+i);
            parseListing(HTMLfile);

        }//end loop step 3 i-loop



        //navigate the current page
//        for (int i =0;i<HTMLfile.size();i++) {
//
//            String temp = HTMLfile.get(i);
//
//            if (temp.contains("<td><a class=\"ItmTLnk\"")){
//                int j = i+1;
//                String temp2=HTMLfile.get(j);
//                listings workListing; //the current listing
//                String tempURL = ""; //Listing URL
//                String tempName = ""; //Listing name
//                String tempImage = ""; //the listing image
//                String tempPrice = ""; //the listing price
//
//                while(!temp2.contains("<td><a class=\"ItmTLnk\"")){
//
//
//
//                    //gets the listing URL
//                    if(temp2.contains("<a href=\"/item")){
//                        String regex = "\"([^\"]*)\"";
//                        Pattern pat = Pattern.compile(regex);
//                        Matcher m = pat.matcher(temp2);
//                        if(m.find()) {
//                            tempURL = "http://www.gunbroker.com" + m.group(1);
//                        }
//
//                        //gets the name
//                        //tempName = temp2.replaceAll("\\<.*?>","");
//                        //tempName = tempName.trim();
//                    }
//
//                    //gets the name
//                    if(temp2.contains("BItmTLnk")){
//                        tempName=android.text.Html.fromHtml(temp2).toString();
//                    }
//
//                    //gets the image
//                    if(temp2.contains("<img alt=")){
//                        String regex = "http([^\"]*).jpg";
//                        Pattern pat = Pattern.compile(regex);
//                        Matcher m = pat.matcher(temp2);
//                        if(m.find()) {
//                            tempImage = "http" + m.group(1) + ".jpg";
//                        }
//                    }
//
//                    //gets the price
//                    if(temp2.contains("<td class=\"lrt\">$")){
//                        tempPrice = temp2.replaceAll("\\<.*?>","");
//                        tempPrice = tempPrice.trim();
//                        if(tempPrice.equals(""))
//                            tempPrice="error";
//                    }
//
//                    j++;
//                    if(j<HTMLfile.size())
//                        temp2=HTMLfile.get(j);
//                    else
//                        break;
//                }
//                //if the listing does not have an image, replace it with a default one
//                if(tempImage.equals(""))
//                    tempImage="http://i.imgur.com/wqjK8ZG.png";
//
//                //create the listing
//                workListing = new listings(tempImage,tempName,tempPrice, tempURL);
//
//                //add the listing to the list
//                add(workListing);
//            }
//
//        }

    }//end of constructor

    //find the listings on each forum page, and turn them into listings
	private void parseListing(List<String> input) {
		String[] output = new String[4];
		String temp = "";
		// TODO Auto-generated method stub
		for(int i = 0; i < input.size(); i++) {
			if(input.get(i).contains("td_threadtitle_")) {
				//tempImage,tempName,tempPrice, tempURL
				

				temp = input.get(i);
				
				
				//So apperently AK files doesn't have its shit together,
				//and doesnt put the entire tag on the same line
				//the tag seems to end whenever it likes, so i had to 
				//improvise in order to find out where the hell the tag
				//decides to come to an end
				if(input.get(i).contains(">") != true) {
					for(int j = i+1; j < input.size(); j++) {
						temp += input.get(j);
						if(temp.contains(">")) {
							break;
						}
					}
				}
				
//				System.out.print(i+"\t");
				
				output[1] = parseName(temp);
//				System.out.println(output[1]);
				//if its not what we are looking for then fuckit
				if(relevantResult(output[1]) != true) {
//					System.out.println("NOPE");
					continue;
				}
				
				
				output[0] = "http://i.imgur.com/wqjK8ZG.png";
				
				output[2] = parsePrice(temp); // not everything has a price in it, but parse the first moneybags it sees
				output[3] = praseURL(i,input);
				//why does akfiles link to dead threads?
//				if(output[3].equals("http://www.akfiles.com/forums/f=")) {
//					continue;
//				}
				System.out.println("ITEM:\t"+output[1]);
				System.out.println("PRICE:\t"+output[2]);
				System.out.println("URL:\t"+output[3]);
				System.out.println();
				add(new listings(output[0],output[1],output[2],output[3]));
			}
		}
		
	}

	//checks if the discription has the search string in it
	private boolean relevantResult(String input) {
//input.c
//		"ABCDEFGHIJKLMNOP".toLowerCase().contains("gHi".toLowerCase())
		
//		return(input.contains(globalSearchString));
		return(input.toLowerCase().contains(globalSearchString.toLowerCase()));
	}

	//find the URL
	private String praseURL(int index, List<String> input) {
		String output = "";
		String temp = "";
		String pattern = "<a[^>]+href\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
		
		for(int i = index; i < input.size(); i++) {
			if(input.get(i).contains("<a ")) {
//				System.out.println(input.get(i));
				temp = input.get(i).substring(12);
				//OKAY REGEX, BE THAT WAY, SEE IF I CARE
				for(int j = 0; j < temp.length(); j++) {
					if(temp.charAt(j) == '"') {
						temp = temp.substring(0, j);
						
						//remove "&amp;" and just have '&'
						for(int k = 0; k < temp.length(); k++) {
							if(temp.charAt(k) == '&') {
								temp = temp.substring(0, k+1) + temp.substring(k+5);
							}
						}
						break;
					}
				}
//				http://www.falfiles.com/forums/forumdisplay.php?f=11&order=desc&page=2
//				return("http://www.akfiles.com/forums/"+temp);
// 				return("www.falfiles.com/forums/"+temp);
				if(temp.equals("f=")) {
					System.out.print("u wut m8?");
					return("http://www.akfiles.com/forums/f=");
				}
				
				for(int kek = 0; kek < temp.length(); kek++) {
					if(temp.charAt(kek) == '&') {
						temp = temp.substring(kek+1,temp.length());
					}
				}
//				http://www.falfiles.com/forums/forumdisplay.php?f=11&order=desc&page=2
//				return("http://www.akfiles.com/forums/"+temp);
				return("http://www.calguns.net/calgunforum/showthread.php?"+temp);
//				return(temp);
			}
		}
//		
		Pattern p = Pattern.compile(pattern);
		
		Matcher cupid = p.matcher(temp);
		cupid.find();
		output = cupid.group(1);
		
		return output;
	}

	//if there is '$' in the title, parse it
	private String parsePrice(String input) {
		String output = "NA";
		String holding = "";
		
		//find that money
		for(int i = 0; i < input.length(); i++) {
			if(input.charAt(i) == '$') {
				holding = input.substring(i);
//				output = holding;
				for(int j = 0; j < holding.length(); j++) {
					if(holding.charAt(1) == ' ') {
						continue;
					}
					if(holding.charAt(j) == ' ') {
						output = holding.substring(0, j);
						break;
					}
				}
				
				break;
			}
		}
		
		
		return output;
	}

	private String parseName(String input) {
		// TODO Auto-generated method stub
		String output = "";
		
		String pattern = "<td[^>]+title\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
		
		Pattern p = Pattern.compile(pattern);
		
		Matcher cupid = p.matcher(input);
		cupid.find();
		output = cupid.group(1);
//		cupid.group(0)
		
		return output;
	}


		private String parseTitle(int index, List<String> input) {
		String output = "";
		
		for(int i = index; i < input.size(); i++) {
			if(input.get(i).contains("<a")) {
				output = input.get(i);
				break;
			}
		}
		
		for(int i = 0; i < output.length(); i++) {
			if(output.charAt(i) == '>') {
				output = output.substring(i+1,output.length()-4);
//				System.out.println(output);
				break;
			}
		}
		
		return(output);
	}
}

