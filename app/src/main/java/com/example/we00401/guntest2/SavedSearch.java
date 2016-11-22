package com.example.we00401.guntest2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class SavedSearch extends AppCompatActivity {

    ListView lv;
    ArrayList<String> files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_search);

        File theFiles = this.getFilesDir();
        File [] FilesArray = theFiles.listFiles();




        files =  new ArrayList<>();
        lv = (ListView) findViewById(R.id.savedView);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_list_item_multiple_choice,
                        files);
                lv.setAdapter(adapter);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice,
                files);
        lv.setAdapter(adapter);
        for(int i=0;i<FilesArray.length;i++)
            files.add(FilesArray[i].toString().replaceAll(this.getFilesDir().toString()+"/",""));

        Button btn1 = (Button)findViewById(R.id.btnLoad);

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
