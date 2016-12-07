//register.java

package com.example.we00401.guntest2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!?])(?=\\S+$).{8,}$";

        final String epattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        final Button register = (Button) findViewById(R.id.btnRegisterC);

        //goes back to the login screen
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //finish();

                if (v.getId() == R.id.btnRegisterC) {
                    EditText name = (EditText) findViewById(R.id.editText2);
                    EditText email = (EditText) findViewById(R.id.editText5);
                    EditText uname = (EditText) findViewById(R.id.editText6);
                    EditText pass1 = (EditText) findViewById(R.id.editText7);
                    EditText pass2 = (EditText) findViewById(R.id.editText8);


                    String namestr = name.getText().toString();
                    String emailstr = email.getText().toString();
                    String unamestr = uname.getText().toString();
                    String pass1str = pass1.getText().toString();
                    String pass2str = pass2.getText().toString();



                    if (!emailstr.matches(epattern)) {
                        Toast.makeText(register.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    }

                    else if (unamestr.contentEquals("")) {
                        Toast.makeText(register.this , "You must enter a username", Toast.LENGTH_SHORT).show();
                    }

                    else if (!pass1str.equals(pass2str)) {

                        // pop up message
                        Toast.makeText(register.this, "Your password doesnt match", Toast.LENGTH_SHORT).show();
                    }

                    else if (!pass1str.matches(pattern)) {
                        Toast.makeText(register.this, "Your password must be at least 8 characters and contain a number, symbol, upper and lower case", Toast.LENGTH_SHORT).show();
                    }

                    else if(helper.checkUser(unamestr) == "already exists"){
                        Toast.makeText(register.this, "This user already exists", Toast.LENGTH_SHORT).show();
                    }

                    else {

                        // insert details in database
                        Contact c = new Contact();
                        c.setName(namestr);
                        c.setEmail(emailstr);
                        c.setUname(unamestr);
                        c.setPass(pass1str);

                        helper.insertContact(c);
                        Toast.makeText(register.this, "Account created", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

//    public void onRegisterClick(View v){
//        if (v.getId() == R.id.btnRegisterC) {
//            EditText name = (EditText)findViewById(R.id.editText2);
//            EditText email = (EditText)findViewById(R.id.editText5);
//            EditText uname = (EditText)findViewById(R.id.editText6);
//            EditText pass1 = (EditText)findViewById(R.id.editText7);
//            EditText pass2 = (EditText)findViewById(R.id.editText8);
//
//
//            String namestr = name.getText().toString();
//            String emailstr = email.getText().toString();
//            String unamestr = uname.getText().toString();
//            String pass1str = pass1.getText().toString();
//            String pass2str = pass2.getText().toString();
//
//            if(!pass1str.equals(pass2str)) {
//
//                // pop up message
//                Toast.makeText(register.this, "Your password doesnt match f*cker", Toast.LENGTH_SHORT).show();
//
//            }
//
//            else {
//                finish();
//            }
//
//        }
//    }
    }
}
