package com.example.we00401.guntest2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class SavedSearch extends AppCompatActivity {
    DatabaseHelper helper = new DatabaseHelper(this);
    ListView lv;
    ArrayList<String> files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_search);

        File theFiles = this.getFilesDir();
        File [] FilesArray = theFiles.listFiles();

        final String username = getIntent().getStringExtra("username");

        lv = (ListView) findViewById(R.id.savedView);

        files =  helper.getDatabase("",username);

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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String savename = (String) parent.getAdapter().getItem(position);
                Intent intent = new Intent(
                        SavedSearch.this, SearchScreen.class);
                intent.putExtra("username", username);
                intent.putExtra("name", savename);

                startActivity(intent);
                overridePendingTransition(R.anim.right_out,R.anim.left_in );

            }
        });

//        for(int i=0;i<FilesArray.length;i++)
//            files.add(FilesArray[i].toString().replaceAll(this.getFilesDir().toString()+"/",""));


        Button btn1 = (Button)findViewById(R.id.btnLoad);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
