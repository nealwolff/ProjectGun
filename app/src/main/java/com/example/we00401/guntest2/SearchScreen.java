package com.example.we00401.guntest2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import android.os.Handler;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SearchScreen extends AppCompatActivity {
    String username;
    DatabaseHelper helper = new DatabaseHelper(this);
    boolean check = true;
    private Handler handler;
    private ProgressDialog pdialog;
    //Linked list of lists of listings, used to store the lists of each page.
    private LinkedList<List<listings>> allPages = new  LinkedList<List<listings>>();

    String theSearchTerm = "!none";
    String cattegory = "";
    boolean strict =false;
    boolean gun = true;
    boolean AK = false;
    boolean arms =false;
    boolean CAL =false;
    boolean america = false;
    boolean FAL = false;
    Toolbar toolbar;

    //arraylists to store the results in each site

    //contains strings of URLS for quick duplicate checking
    ArrayList<String> gunDupilcate = new ArrayList<String>();
    ArrayList<String> armsDuplicate = new ArrayList<String>();
    ArrayList<String> akDuplicate = new ArrayList<String>();
    ArrayList<String> americaDuplicate = new ArrayList<String>();
    ArrayList<String> calDuplicate = new ArrayList<String>();
    ArrayList<String> falDupilcate = new ArrayList<String>();

    //These are the linked lists with the individual site listings.
    ArrayList<listings> gunListings = new ArrayList<listings>();
    ArrayList<listings> armsListings = new ArrayList<listings>();
    ArrayList<listings> akListings = new ArrayList<listings>();
    ArrayList<listings> americaListings = new ArrayList<listings>();
    ArrayList<listings> calListings = new ArrayList<listings>();
    ArrayList<listings> falListings = new ArrayList<listings>();


    private GestureDetectorCompat gestureObject;
    boolean first = true;

    //array list of listings, this is what is showin the in listview
    ArrayList<listings> arrayList;
    ListView lv; //The listveiw that displays the listings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        arrayList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.foundView2);

        username = getIntent().getStringExtra("username");
        String saveName = getIntent().getStringExtra("name");
        if (!saveName.equals("!!FIRSTSTART!!"))
                loadData(saveName);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customAdapter adapter = new customAdapter(
                        getApplicationContext(), R.layout.customlayout, arrayList
                );
                lv.setAdapter(adapter);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                listings entry= (listings) parent.getAdapter().getItem(position);

                Intent intent = new Intent(
                        SearchScreen.this, webScreen.class);
                intent.putExtra("url", entry.getURL());
                intent.putExtra("name", entry.getName());

                startActivity(intent);
                overridePendingTransition(R.anim.right_out,R.anim.left_in );

            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                listings entry= (listings) parent.getAdapter().getItem(position);


                AlertDialog.Builder adb=new AlertDialog.Builder(SearchScreen.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + entry.getName());
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.remove(positionToRemove);
                        if(gun){
                            gunListings.remove(positionToRemove);
                        }else if(AK){
                            akListings.remove(positionToRemove);
                        }else if(arms){
                            armsListings.remove(positionToRemove);
                        }else if(CAL){
                            calListings.remove(positionToRemove);
                        }else if(america){
                            americaListings.remove(positionToRemove);
                        }else if(FAL){
                            falListings.remove(positionToRemove);
                        }
                        lv.invalidateViews();
                    }});
                adb.show();

                //System.out.println("long clicked");

                return true;
            }
        });


        gestureObject = new GestureDetectorCompat(this, new SearchScreen.LearnGesture());

        Button btn1 = (Button)findViewById(R.id.btnLoad2);

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SearchScreen.this, SavedSearch.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        Button btnGO = (Button)findViewById(R.id.btnGo2);

        btnGO.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //hide the keyboard after the user presses "go"
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);



                //get the text from the search field
                EditText txtSearch = (EditText) findViewById(R.id.fieldSearch2);
                String input = txtSearch.getText().toString();
                final String searchTerm = input.trim(); //clear the text of any whitespace
                //if no terms entered, notify
                if(searchTerm.equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(SearchScreen.this).create();
                    alertDialog.setTitle("error");
                    alertDialog.setMessage("must enter data");
                    alertDialog.show();
                }
                else {
                    if(!searchTerm.equals(theSearchTerm) &&theSearchTerm !="!none"){
                        Toast.makeText(getApplicationContext(),
                                "Note: new search terms clears old search", Toast.LENGTH_SHORT).show();
                    }
                    //Ask the user to select categories to search.

                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(SearchScreen.this);
                    alt_bld.setTitle("Select a category");
                    final String[] grpname = {" Semi-Auto pistols "," Revolvers "," Bolt-Action rifles "," Semi-Auto rifles "," Shotguns "};
                    alt_bld.setSingleChoiceItems(grpname, -1, new DialogInterface
                            .OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            ///if a new search term is specified, clear the screen
                            if(!searchTerm.equals(theSearchTerm))
                                clearEverything();

                            theSearchTerm = searchTerm; //sets the global search term for when the user goes into other tabs.
                            cattegory = grpname[item];

                            //checks which tab the user is on
                            if(gun)
                                FindGunbroker(theSearchTerm,cattegory);
                            if(AK)
                                AkfilesFinder();
                            if (FAL)
                                FalfilesFinder();
                            if(america)
                                GunsamericaFinder();
                            if(CAL)
                                CalgunsFinder();
                            if(arms)
                                ArmslistFinder(theSearchTerm,cattegory);

                            dialog.dismiss();

                        }
                    });
                    AlertDialog alert = alt_bld.create();
                    alert.show();


                }



            }
        });
        Button btnClear = (Button)findViewById(R.id.clrBtn);
        btnClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                clearEverything();

            }
        });
        //get all the button "Tabs"
        Button btnGun = (Button)findViewById(R.id.gunbroker);
        Button btnArms = (Button)findViewById(R.id.armlsist);
        Button btnAK = (Button)findViewById(R.id.akfiles);
        Button btnAmerica = (Button)findViewById(R.id.GunsAmerica);
        Button btnCal = (Button)findViewById(R.id.calGuns);
        Button btnFal = (Button)findViewById(R.id.falfiles);

        //when the gunbroker tab is selected
        btnGun.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = true;
                AK = false;
                arms =false;
                CAL =false;
                america = false;
                FAL = false;

                setArraylist(gunListings); //if the current search term has listings, load them


                if(!theSearchTerm.equals("!none")){
                    FindGunbroker(theSearchTerm, cattegory);
                }

                lv.invalidateViews();//refresh the listings
            }
        });

        btnArms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = false;
                AK = false;
                arms =true;
                CAL =false;
                america = false;
                FAL = false;

                setArraylist(armsListings); //if the current search term has listings, load them


                if(!theSearchTerm.equals("!none")){
                    ArmslistFinder(theSearchTerm, cattegory);
                }

                lv.invalidateViews();//refresh the listings
            }
        });

        btnAK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = false;
                AK = true;
                arms =false;
                CAL =false;
                america = false;
                FAL = false;
                setArraylist(akListings); //if the current search term has listings, load them
                lv.invalidateViews();//refresh the listings

                //if there is currently a search
                if(!theSearchTerm.equals("!none")){
                    new AlertDialog.Builder(SearchScreen.this)
                            .setTitle("Decision")
                            .setMessage("Do you wish to find new items since the last search?" +
                                    "\nNote: It will take 30 seconds to search for new results")
                            .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    AkfilesFinder();
                                    lv.invalidateViews();//refresh the listings
                                }
                            })
                            .create()
                            .show();
                }
            }
        });

        btnFal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = false;
                AK = false;
                arms =false;
                CAL =false;
                america = false;
                FAL = true;
                setArraylist(falListings); //if the current search term has listings, load them
                lv.invalidateViews();//refresh the listings

                //if there is currently a search
                if(!theSearchTerm.equals("!none")){
                    FalfilesFinder();
                    lv.invalidateViews();//refresh the listings
                }

            }
        });
        btnAmerica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = false;
                AK = false;
                arms =false;
                CAL =false;
                america = true;
                FAL = false;
                setArraylist(americaListings); //if the current search term has listings, load them
                lv.invalidateViews();//refresh the listings

                //if there is currently a search
                if(!theSearchTerm.equals("!none")){
                    GunsamericaFinder();
                    lv.invalidateViews();//refresh the listings
                }
            }
        });
        btnCal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                gun = false;
                AK = false;
                arms =false;
                CAL =true;
                america = false;
                FAL = false;
                setArraylist(calListings); //if the current search term has listings, load them
                lv.invalidateViews();//refresh the listings

                //if there is currently a search
                if(!theSearchTerm.equals("!none")){
                    new AlertDialog.Builder(SearchScreen.this)
                            .setTitle("Decision")
                            .setMessage("Do you wish to find new items since the last search?" +
                                    "\nNote: It will take 60 seconds to search for new results")
                            .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    CalgunsFinder();
                                    lv.invalidateViews();//refresh the listings
                                }
                            })
                            .create()
                            .show();


                }
            }
        });

        Button btnSave =  (Button)findViewById(R.id.btnSave2);
        final EditText textSave = (EditText)findViewById(R.id.textSave2);

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText textSave = (EditText) findViewById(R.id.textSave2);
                String temp = textSave.getText().toString();
                temp=temp.replaceAll("\\s","");
                if (temp.matches("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(SearchScreen.this).create();
                    alertDialog.setTitle("error");
                    alertDialog.setMessage("You must enter a save name");

                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog.show();
                }else{
                    File file = new File(temp);
                    if(file.exists()) {
                        AlertDialog alertDialog = new AlertDialog.Builder(SearchScreen.this).create();
                        alertDialog.setTitle("error");
                        alertDialog.setMessage("File already exists");
                        alertDialog.show();
                    }else {
                        Savefile(temp);
                    }
                }




            }
        });

        final EditText searchText = (EditText)findViewById(R.id.fieldSearch2);

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(first == true ) {
                    searchText.setText("");
                    first = false;
                }
            }
        });
    }

    private void loadData(String saveName) {
        String [] seachCat = helper.getSearchTerm(username, saveName);
        theSearchTerm = seachCat[0];
        cattegory = seachCat[1];
        System.out.println("search Term: " + theSearchTerm +"\ncategory: " + cattegory);
        ArrayList<listings> kek = helper.getGunbroker(username, saveName,"gunbroker");
        ArrayList<listings> kek2 = helper.getGunbroker(username, saveName,"armslist");
        for(int i = 0; kek.size()<i;i++)
            gunDupilcate.add(kek.get(i).getURL());
        for(int i = 0; kek2.size()<i;i++)
            armsDuplicate.add(kek2.get(i).getURL());
        armsListings = kek2;
        gunListings = kek;

        setArraylist(kek);
    }

    public void Savefile(String name){
        boolean kek = helper.createSave(name,username, cattegory,theSearchTerm);
        if(!kek) {
            AlertDialog alertDialog = new AlertDialog.Builder(SearchScreen.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Save already exists");
            alertDialog.show();
            return;
        }
        final Thread t = new Thread() {
            public void run() {
                if(gunListings!= null) {
                    for (int i = 0; i < gunListings.size(); i++) {
                        listings tempElement = gunListings.get(i);
                        listings tempElement2 = armsListings.get(i);
                        //listings tempElement3 = akListings.get(i);
                        helper.insertSite(tempElement.getURL(),tempElement.getImage(),tempElement.getPrice(),tempElement.getName(),"gunbroker");
                        helper.insertSite(tempElement2.getURL(),tempElement2.getImage(),tempElement2.getPrice(),tempElement2.getName(),"armslist");
                        //helper.insertSite(tempElement3.getURL(),tempElement3.getImage(),tempElement3.getPrice(),tempElement3.getName(),"akfiles");
                    }
                }
                handler.sendEmptyMessage(0);
            }

        };





        pdialog = new ProgressDialog(SearchScreen.this); // this = YourActivity
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setMessage("Saving, please wait...");
        pdialog.setIndeterminate(true);
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.show();

        t.start();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                pdialog.dismiss();
            };
        };

//        try {
//                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(name, Context.MODE_PRIVATE));
//                outputStreamWriter.write(data);
//                outputStreamWriter.close();
//        }catch (IOException e) {
//            AlertDialog alertDialog = new AlertDialog.Builder(SearchScreen.this).create();
//            alertDialog.setTitle("Error");
//            alertDialog.setMessage("cannot save file");
//            alertDialog.show();
//        }
    }

    private void clearEverything() {
        arrayList.clear(); //clear the arraylist
        gunListings.clear();
        armsListings.clear();
        akListings.clear();
        americaListings.clear();
        calListings.clear();
        falListings.clear();

        gunDupilcate.clear();
        armsDuplicate.clear();
        akDuplicate.clear();
        americaDuplicate.clear();
        calDuplicate.clear();
        falDupilcate.clear();

        lv.invalidateViews();//refresh the listings
    }

    public void AkfilesFinder(){
        Toast.makeText(getApplicationContext(),
                "Searching " + theSearchTerm + " on the AKfiles", Toast.LENGTH_LONG).show();

        allPages.clear();

        final Thread t = new Thread() {
            public void run() {

                int pagenum=0;
                check = true;
                while(pagenum<40) {
                    pagenum++;
                    //ArrayList<String> compares = getGlobalArraylist();
                    forumHandler GBH = new forumHandler(theSearchTerm,akDuplicate,"akfiles",cattegory,pagenum);
                    List<listings> GBList = GBH.getListings();
                    //GBH.print();
                    //System.out.println("page number: " + pagenum);

                    if(!check){
                        check=true;
                        break;
                    }
                    if (GBList.size() != 0) {
                        allPages.add(GBList);
                    }


                    final int page = pagenum;
                    runOnUiThread(new Runnable() {

                        public void run() {
                            pdialog.setMessage("Loading. Please wait...\nNote, searching 40 pages of listings, can take up to 30 seconds\n"+
                                    "you can stop this on whatever page you desire\nOn page: " + page + " of " + 40);
                        }
                    });

                }

                //traveres the pages and add the objects to their respective lists.
                for(int j = allPages.size()-1;j>=0;j--) {

                    //if nothing was found
                    if(allPages.size()==0){
                        Toast.makeText(getApplicationContext(),
                                "No Items found", Toast.LENGTH_LONG).show();
                        break;
                    }

                    //get the page
                    List<listings> GBList = allPages.get(j);
                    //get the akfiles listings from that page
                    for (int i = GBList.size() - 1; i >= 0; i--) {
                        listings listing = GBList.get(i);
                        arrayList.add(0, listing);
                        akListings.add(0, listing);

                        //create a linked list of strings containing all the urls for duplicate comparison
                        akDuplicate.add(0,listing.getURL());
                    }

                    runOnUiThread(new Runnable() {

                        public void run() {
                            lv.invalidateViews();
                        }
                    });

                }

                runOnUiThread(new Runnable() {

                    public void run() {
                        if (arrayList.size() == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "No Items found", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                handler.sendEmptyMessage(0);
            }

        };





        pdialog = new ProgressDialog(SearchScreen.this); // this = YourActivity
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setMessage("Loading. Please wait...\nIf your term was too broad this could take some time");
        pdialog.setIndeterminate(true);
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                check=false;
            }
        });
        pdialog.show();

        t.start();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                pdialog.dismiss();
            };
        };




    }

    //find armslist listings
    public void ArmslistFinder(String searchTerm, String grpname){
        Toast.makeText(getApplicationContext(),
                "Searching " + searchTerm + " in \""+grpname+"\"", Toast.LENGTH_LONG).show();


        final String temp=searchTerm.replaceAll("\\s","+");
        //testHandler test = new testHandler();
        //if the gunbroker checkbox is clicked, search gunbroker.



        allPages.clear();

        final String grpname2= grpname;
        final Thread t = new Thread() {
            public void run() {

                int pagenum=0;
                check = true;
                String theFinalPageNum="kek";
                while(check) {
                    pagenum++;
                    //ArrayList<String> compares = getGlobalArraylist();
                    ArmsListHandler GBH = new ArmsListHandler(temp, grpname2, armsDuplicate, pagenum);
                    List<listings> GBList = GBH.getListings();

                    if(theFinalPageNum.equals("kek"))
                        theFinalPageNum=GBH.pageNum;

                    if (GBList.size() == 0) {
                        break;
                    }
                    allPages.add(GBList);

                    if(!check){
                        check=true;
                        break;
                    }

                    final int page = pagenum;
                    final String finalPage = theFinalPageNum;
                    runOnUiThread(new Runnable() {

                        public void run() {
                            pdialog.setMessage("Loading. Please wait...\nNote, if your term was too broad this could take some time\n"+
                                    " On page: " + page + " of " + finalPage);
                        }
                    });
                }

                //traveres the pages and add the objects to their respective lists.
                for(int j = allPages.size()-1;j>=0;j--) {

                    //if nothing was found
                    if(allPages.size()==0){
                        Toast.makeText(getApplicationContext(),
                                "No Items found", Toast.LENGTH_LONG).show();
                        break;
                    }

                    //get the page
                    List<listings> GBList = allPages.get(j);
                    //get the gunbroker listings from that page
                    for (int i = GBList.size() - 1; i >= 0; i--) {
                        listings listing = GBList.get(i);
                        arrayList.add(0, listing);
                        armsListings.add(0, listing);

                        //create a linked list of strings containing all the urls for duplicate comparison
                        armsDuplicate.add(0,listing.getURL());
                    }

                    runOnUiThread(new Runnable() {

                        public void run() {
                            lv.invalidateViews();
                        }
                    });

                }

                runOnUiThread(new Runnable() {

                    public void run() {
                        if (arrayList.size() == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "No Items found", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                handler.sendEmptyMessage(0);
            }

        };





        pdialog = new ProgressDialog(SearchScreen.this); // this = YourActivity
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setMessage("Loading. Please wait...\nIf your term was too broad this could take some time");
        pdialog.setIndeterminate(true);
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                check=false;
            }
        });
        pdialog.show();

        t.start();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                pdialog.dismiss();
            };
        };//*/




    }

    public void FalfilesFinder(){
        Toast.makeText(getApplicationContext(),
                "Searching " + theSearchTerm + " on the FALfiles", Toast.LENGTH_LONG).show();

        allPages.clear();

        final Thread t = new Thread() {
            public void run() {

                int pagenum=0;
                check = true;
                while(pagenum<3) {
                    pagenum++;
                    //ArrayList<String> compares = getGlobalArraylist();
                    forumHandler GBH = new forumHandler(theSearchTerm,falDupilcate,"falfiles",cattegory,pagenum);
                    List<listings> GBList = GBH.getListings();
                    GBH.print();
                    System.out.println("page number: " + pagenum);

                    if(!check){
                        check=true;
                        break;
                    }
                    if (GBList.size() != 0) {
                        allPages.add(GBList);
                    }


                    final int page = pagenum;
                    runOnUiThread(new Runnable() {

                        public void run() {
                            pdialog.setMessage("Searching. Please wait...\nOn page: " + page + " of " + 3);
                        }
                    });

                }

                //traveres the pages and add the objects to their respective lists.
                for(int j = allPages.size()-1;j>=0;j--) {

                    //if nothing was found
                    if(allPages.size()==0){
                        Toast.makeText(getApplicationContext(),
                                "No Items found", Toast.LENGTH_LONG).show();
                        break;
                    }

                    //get the page
                    List<listings> GBList = allPages.get(j);
                    //get calguns listings from that page
                    for (int i = GBList.size() - 1; i >= 0; i--) {
                        listings listing = GBList.get(i);
                        arrayList.add(0, listing);
                        falListings.add(0, listing);

                        //create a linked list of strings containing all the urls for duplicate comparison
                        falDupilcate.add(0,listing.getURL());
                    }

                    runOnUiThread(new Runnable() {

                        public void run() {
                            lv.invalidateViews();
                        }
                    });

                }

                runOnUiThread(new Runnable() {

                    public void run() {
                        if (arrayList.size() == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "No Items found", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                handler.sendEmptyMessage(0);
            }

        };





        pdialog = new ProgressDialog(SearchScreen.this); // this = YourActivity
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setMessage("Loading. Please wait...\nIf your term was too broad this could take some time");
        pdialog.setIndeterminate(true);
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                check=false;
            }
        });
        pdialog.show();

        t.start();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                pdialog.dismiss();
            };
        };


    }

    public void GunsamericaFinder(){
        Toast.makeText(getApplicationContext(),
                "Searching " + theSearchTerm + " in \""+cattegory+"\"", Toast.LENGTH_LONG).show();


        final String temp=theSearchTerm.replaceAll("\\s","+");


        allPages.clear();

        final String grpname2= cattegory;
        final Thread t = new Thread() {
            public void run() {

                int pagenum=0;
                check = true;
                String theFinalPageNum="kek";
                while(check) {
                    pagenum++;
                    //ArrayList<String> compares = getGlobalArraylist();
                    gunsAmericaHandler GBH = new gunsAmericaHandler(temp, americaDuplicate, pagenum);
                    List<listings> GBList = GBH.getListings();

                    if(theFinalPageNum.equals("kek"))
                        theFinalPageNum=GBH.theNUM;

                    if (GBList.size() == 0) {
                        break;
                    }

                    allPages.add(GBList);

                    //}
                    // });
                    if(!check){
                        check=true;
                        break;
                    }

                    final int page = pagenum;
                    final String finalPage = theFinalPageNum;
                    runOnUiThread(new Runnable() {

                        public void run() {
                            pdialog.setMessage("Loading. Please wait...\nNote, if your term was too broad this could take some time\n"+
                                    " On page: " + page + " of " + finalPage);
                        }
                    });
                }

                //traveres the pages and add the objects to their respective lists.
                for(int j = allPages.size()-1;j>=0;j--) {

                    //if nothing was found
                    if(allPages.size()==0){
                        Toast.makeText(getApplicationContext(),
                                "No Items found", Toast.LENGTH_LONG).show();
                        break;
                    }

                    //get the page
                    List<listings> GBList = allPages.get(j);
                    //get the gunbroker listings from that page
                    for (int i = GBList.size() - 1; i >= 0; i--) {
                        listings listing = GBList.get(i);
                        arrayList.add(0, listing);
                        americaListings.add(0, listing);

                        //create a linked list of strings containing all the urls for duplicate comparison
                        americaDuplicate.add(0,listing.getURL());
                    }

                    runOnUiThread(new Runnable() {

                        public void run() {
                            lv.invalidateViews();
                        }
                    });

                }

                runOnUiThread(new Runnable() {

                    public void run() {
                        if (arrayList.size() == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "No Items found", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                handler.sendEmptyMessage(0);
            }

        };





        pdialog = new ProgressDialog(SearchScreen.this); // this = YourActivity
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setMessage("Loading. Please wait...\nIf your term was too broad this could take some time");
        pdialog.setIndeterminate(true);
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                check=false;
            }
        });
        pdialog.show();

        t.start();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                pdialog.dismiss();
            };
        };




    }

    public void CalgunsFinder(){
        Toast.makeText(getApplicationContext(),
                "Searching " + theSearchTerm + " on CalGuns", Toast.LENGTH_LONG).show();

        allPages.clear();

        final Thread t = new Thread() {
            public void run() {

                int pagenum=0;
                check = true;
                while(pagenum<100) {
                    pagenum++;
                    //ArrayList<String> compares = getGlobalArraylist();
                    forumHandler GBH = new forumHandler(theSearchTerm,calDuplicate,"calguns", cattegory,pagenum);
                    List<listings> GBList = GBH.getListings();
                    GBH.print();
                    System.out.println("page number: " + pagenum);

                    if(!check){
                        check=true;
                        break;
                    }
                    if (GBList.size() != 0) {
                        allPages.add(GBList);
                    }


                    final int page = pagenum;
                    runOnUiThread(new Runnable() {

                        public void run() {
                            pdialog.setMessage("Loading. Please wait...\nNote, searching 100 pages of listings, can take about 60 seconds\n"+
                                    "you can stop this on whatever page you desire\nOn page: " + page + " of " + 100);
                        }
                    });

                }

                //traveres the pages and add the objects to their respective lists.
                for(int j = allPages.size()-1;j>=0;j--) {

                    //if nothing was found
                    if(allPages.size()==0){
                        Toast.makeText(getApplicationContext(),
                                "No Items found", Toast.LENGTH_LONG).show();
                        break;
                    }

                    //get the page
                    List<listings> GBList = allPages.get(j);
                    //get calguns listings from that page
                    for (int i = GBList.size() - 1; i >= 0; i--) {
                        listings listing = GBList.get(i);
                        arrayList.add(0, listing);
                        calListings.add(0, listing);

                        //create a linked list of strings containing all the urls for duplicate comparison
                        calDuplicate.add(0,listing.getURL());
                    }

                    runOnUiThread(new Runnable() {

                        public void run() {
                            lv.invalidateViews();
                        }
                    });

                }

                runOnUiThread(new Runnable() {

                    public void run() {
                        if (arrayList.size() == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "No Items found", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                handler.sendEmptyMessage(0);
            }

        };





        pdialog = new ProgressDialog(SearchScreen.this); // this = YourActivity
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setMessage("Loading. Please wait...\nIf your term was too broad this could take some time");
        pdialog.setIndeterminate(true);
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                check=false;
            }
        });
        pdialog.show();

        t.start();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                pdialog.dismiss();
            };
        };


    }

    //finds the gunbroker results
    public void FindGunbroker(String searchTerm, String grpname){
        Toast.makeText(getApplicationContext(),
                "Searching " + searchTerm + " in \""+grpname+"\"", Toast.LENGTH_LONG).show();


        final String temp=searchTerm.replaceAll("\\s","+");
        //testHandler test = new testHandler();
        //if the gunbroker checkbox is clicked, search gunbroker.



        allPages.clear();

        final String grpname2= grpname;
        final Thread t = new Thread() {
            public void run() {

                int pagenum=0;
                check = true;
                String theFinalPageNum="kek";
                while(check) {
                    pagenum++;
                    //ArrayList<String> compares = getGlobalArraylist();
                    gunbrokerHandler GBH = new gunbrokerHandler(temp, grpname2, gunDupilcate, pagenum);
                    List<listings> GBList = GBH.getListings();

                    if(theFinalPageNum.equals("kek"))
                        theFinalPageNum=GBH.pageNum;

                    if (GBList.size() == 0) {
                        break;
                    }
                    //runOnUiThread(new Runnable() {

                    // public void run() {
                    //GBH.print();
                    allPages.add(GBList);

                    //}
                    // });
                    if(!check){
                        check=true;
                        break;
                    }

                    final int page = pagenum;
                    final String finalPage = theFinalPageNum;
                    runOnUiThread(new Runnable() {

                        public void run() {
                            pdialog.setMessage("Loading. Please wait...\nNote, if your term was too broad this could take some time\n"+
                                    " On page: " + page + " of " + finalPage);
                        }
                    });
                }

                //traveres the pages and add the objects to their respective lists.
                for(int j = allPages.size()-1;j>=0;j--) {

                    //if nothing was found
                    if(allPages.size()==0){
                        Toast.makeText(getApplicationContext(),
                                "No Items found", Toast.LENGTH_LONG).show();
                        break;
                    }

                    //get the page
                    List<listings> GBList = allPages.get(j);
                    //get the gunbroker listings from that page
                    for (int i = GBList.size() - 1; i >= 0; i--) {
                        listings listing = GBList.get(i);
                        arrayList.add(0, listing);
                        gunListings.add(0, listing);

                        //create a linked list of strings containing all the urls for duplicate comparison
                        gunDupilcate.add(0,listing.getURL());
                    }

                    runOnUiThread(new Runnable() {

                        public void run() {
                            lv.invalidateViews();
                        }
                    });

                }

                runOnUiThread(new Runnable() {

                    public void run() {
                        if (arrayList.size() == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "No Items found", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                handler.sendEmptyMessage(0);
            }

        };





        pdialog = new ProgressDialog(SearchScreen.this); // this = YourActivity
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setMessage("Loading. Please wait...\nIf your term was too broad this could take some time");
        pdialog.setIndeterminate(true);
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                check=false;
            }
        });
        pdialog.show();

        t.start();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                pdialog.dismiss();
            };
        };//*/



    }

    //this method clears the global arraylist and sets one of the
    public void setArraylist(ArrayList<listings> replace){
        arrayList.clear();
        for (int i = 0 ; i<replace.size();i++){
            arrayList.add(replace.get(i)) ;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener {

        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY){

            if(event2.getX() > event1.getX()){
                Intent intent = new Intent(
                        SearchScreen.this, settings.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_out,R.anim.left_in );

            }
            return true;
        }
    }
}