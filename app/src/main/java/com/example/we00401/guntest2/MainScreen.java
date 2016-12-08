package com.example.we00401.guntest2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.*;

public class MainScreen extends AppCompatActivity {


    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);



        //log in button
        Button btn1 = (Button)findViewById(R.id.btnSignIn);

        //register button
        Button register = (Button)findViewById(R.id.btnSignIn2);


        //logs in the user and loads the search page
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText a = (EditText)findViewById(R.id.textName);
                String str = a.getText().toString();
                EditText b = (EditText)findViewById(R.id.textPassword);
                String pass = b.getText().toString();

                String password = helper.searchPass(str);


                if (pass.equals(password)) {
                    Intent intent = new Intent(MainScreen.this, SearchScreen.class);
                    intent.putExtra("username", str);
                    intent.putExtra("name", "!!FIRSTSTART!!");
                    startActivity(intent);
                    finish();
                }

                else {
                    Toast.makeText(MainScreen.this, "Username and Password don't match", Toast.LENGTH_SHORT).show();

                }
            }
        });



        // new log in method
//        public void onButtonClick(View v) {
//
//    }

        //loads the register page
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, register.class));
            }
        });

    }

}

