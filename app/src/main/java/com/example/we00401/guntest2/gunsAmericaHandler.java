//package com.example.we00401.guntest2;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class gunsAmericaHandler extends website {

	/** 
	 * TO DO : REPLACE SPACES WITH %20
	 * @param Search
	 */
    public gunsAmericaHandler(String Search){
    	//RAW URL
    	//https://www.gunsamerica.com/Search.htm?T=shotgun&pagenum=1
    	//											^^^Searchterm	^^^pagenumber
    	
    	// NEW PLAN: GET THIS SHIT ENMASS
    	//https://www.gunsamerica.com/Search.htm?T=shotgun&ltid-all=1&og=1&as=365&ns=0&numberperpage=1000&=&pagenum=2
    	// look for "Sorry, no results were found. Try changing your search term."
    	
    	String URL = "";
    	List<String>HTMLfile;
    	String modifiedSearch = checkem(Search);
    	
    	//count for current page number of results
    	int count = 1;
    	
    	//boolean that controls the loop; set to false to prevent advancement to next page
    	boolean flag = true;
    	
    	//page advancement loop
    	while(flag) {
    		System.out.println("+Getting Page "+count);
    		URL = "https://www.gunsamerica.com/Search.htm?T="+modifiedSearch+"&ltid-all=1&og=1&as=365&ns=0&numberperpage=10&=&pagenum="+count;
//    		URL = "https://www.gunsamerica.com/Search.htm?T="+modifiedSearch+"&ltid-all=1&og=1&as=365&ns=0&numberperpage=1000&=&pagenum="+count;
    		HTMLfile = getURL(URL);
    		String currentInfo[] = new String[4];
    		System.out.println("^Parsing Page "+count);

//    		for(int i = 0; i < HTMLfile.size(); i++) {
    		for(int i = 0; i < 2000; i++) {
//    			System.out.println(i);
    			if(HTMLfile.get(i).contains("Sorry, no results were found")) {
    				flag = false;
    				System.out.println("=Page "+count+" End of Results");
    				break;
    			}
    		}
    		if(flag != true) { //abort all further searches if there are no page results
//    			GHETTO DEBUG STATEMENT; PLEASE IGNORE
//    			System.out.println("No Results on page " + count);
    			//ABANDON THREAD!
    			break;//!!!!!! 
    			
    		}
    		else { //try to make sense of this mess; parse results into listings
//    			System.out.println("Result Page: " + count);
    			//parse data
    			
    			//transverse the html file looking for listings
//    			int listingsFoundCount = 0;
    			for(int j = 0; j < HTMLfile.size(); j++) {//find the listings
    				if(HTMLfile.get(j).contains("listings-list listings-item-box clearfix")) {
    					currentInfo = parseListing(j,HTMLfile);
//    					listingsFoundCount++;
////    					System.out.println(listingsFoundCount+"\t"+HTMLfile.get(j));
    				}
    			}
//    			System.out.println("Number of Listings Found: "+listingsFoundCount);
    			
    		}
    		
    		//ALWAYS ADVANCE THE COUNT
    		count++;
    	}//end while(flag) search page advancement loop
    	
    	
    	
    	
    	
//    	//STEP 01: GET THE INDEX
//    	String URL = "https://www.gunsamerica.com/Search.htm?T="+Search+"&pagenum=1";
//    	List<String>HTMLfile = getURL(URL);
//    	
//    	//add search results while there are still pages
//    	int pageCounter = 1;
//    	while(hasSearchResults("https://www.gunsamerica.com/Search.htm?T="+Search+"&pagenum="+pageCounter)) {
//    		URL = "https://www.gunsamerica.com/Search.htm?T="+Search+"&pagenum="+pageCounter;
//    		HTMLfile = getURL(URL);
//    		
//    		System.out.println(pageCounter);
//    		//finally
//    		pageCounter++;
//    	}
    	
//        String URL = "http://www.gunbroker.com/Auction/BrowseItems2.aspx?Keywords=" + Search +"&Cats=0";
//        List<String>HTMLfile = getURL(URL);
//
//
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
    		} else {
    			output += input.charAt(i);
    		}
    	}
    	System.out.println(output);
		return output;
	}
    //outputs the components of the listing as a string array
//    tempImage,tempName,tempPrice, tempURL
	//take the line the html line the listing is on and get the various infos
	private String[] parseListing(int listingIndex, List<String> file) {
		String[] output = new String[4];
		
		//find image
		for(int i = listingIndex+0; i < file.size(); i++) {
			if(file.get(i).contains("itemprop=\"image\"")) {
//				System.out.println(file.get(i));
				
				output[0] = parseImage2(file.get(i));
				output[1] = parseName(file.get(i));
				output[3] = parseURL(file.get(i));
				
				//find that price
				for(int j = listingIndex; j < file.size(); j++){
					if(file.get(j).contains("<span itemprop=\"price\">")) {
//						System.out.println(file.get(j));
						output[2] = parsePrice(file.get(j));
						break;
					}
				}

//				System.out.println(output[2]);
				System.out.println("\nImgURL:\t"+output[0]);
				System.out.println("Name:\t"+output[1]);
				System.out.println("Price:\t"+output[2]);
				System.out.println("URL:\t"+output[3]);
				
				add(new listings(output[0],output[1],output[2],output[3]));
				break;
			}
			
		}
		
//		output[0] = parseImage2();
//		output[1] = parseName();
//		output[2] = parsePrice();
//		output[3] = parseURL();
		return null;
	}

	
	private String parsePrice(String input) {
		String output = "";
		for(int i = 52; i < input.length()-7; i++) {
			output += input.charAt(i);
//			System.out.print(input.charAt(i));
		}
		
		return output;
	}

	private String parseURL(String input) {

		String output;
		String pattern = "<a[^>]+href\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
		
		Pattern p = Pattern.compile(pattern);
		
		Matcher cupid = p.matcher(input);
		cupid.find();
		output = "http://www.gunsamerica.com"+cupid.group(1);
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
		output = "http://www.gunsamerica.com"+cupid.group(1);
		
		return output;
	}

	//original parseImage. takes forever
	private String parseImage(String input) {
		//if there is no photo, then save ourselfs alot of trouble
		String output = "";
		
		if(input.contains("nophoto.gif")) {
			output = "http://i.imgur.com/wqjK8ZG.png";
			return(output);
		}
		String holding = input;
		//find the src
		int count = 0; //new starting location when pulling from input
		while(holding.contains("src=\"")) {
			holding = "";
			count++;
			for(int i = count; i < input.length(); i++) {
				holding += input.charAt(i);
			}
		}
		//remove rest of "src=\""
		count = count+4;
		holding = "http://www.gunsamerica.com";
		for(int i = count; i < input.length(); i++) {
			holding += input.charAt(i);
		}
		
		
		//remove backs stuff
		count = holding.length();
		do {
			output = "";
			count--;
			for(int i = 0; i < count; i++) {
//				System.out.println(holding.length()+"\t"+i);
				output += holding.charAt(i);
			}
		}
		while(output.contains(".jpg")||output.contains(".JPG"));
		
		//add g back to jpg
		output += "g";
		return output;
	}

//	private boolean hasSearchResults(String inputString) {
//		boolean output = false;
//		List<String>holding = getURL(inputString);
//		
//		for(int i = 0; i < holding.size(); i++) {
//			if(holding.get(i).contains("ctl00_MainContent_rptItems_ctl")) {
//				output = true;
//				break;
//			}
//		}
//		
//		return output;
//	}


}
