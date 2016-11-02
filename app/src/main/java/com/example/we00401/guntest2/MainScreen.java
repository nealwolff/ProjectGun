package com.example.we00401.guntest2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.*;

public class MainScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);



        //log in button
        Button btn1 = (Button)findViewById(R.id.btnSignIn);

        //logs in the user and loads the search page
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, search.class));
            }
        });

        //clears the name feild when clicked
        final EditText textName = (EditText)findViewById(R.id.textName);

        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textName.setText("");
            }
        });

        final EditText textPassword = (EditText)findViewById(R.id.textPassword);

        textPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                textPassword.setText("");
            }
        });


    }






    public void signIn(String username, String password){

    }

    public void register(){

    }

}

