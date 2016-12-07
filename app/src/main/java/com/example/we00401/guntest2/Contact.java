//Contact.java

package com.example.we00401.guntest2;

/**
 * Created by Joshua on 12/1/16.
 */

public class Contact {
//    int id;
    String name, email, uname, pass, nameSave;

    String imageurl, url, title, price;

//    public void setId(int id) {
//        this.id=id;
//    }
//
//    public int getId() {
//        return this.id;
//    }


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }


    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }


    public void setUname(String uname) {
        this.uname = uname;
    }
    public String getUname() {
        return this.uname;
    }


    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getPass() {
        return this.pass;
    }


    public void setNameSave(String nameSave) { this.nameSave = nameSave; }
    public String getNameSave() { return this.nameSave; }


    public void setImageUrl(String imageurl) { this.imageurl = imageurl; }
    public String getImageUrl(){ return this.imageurl; }

    public void setPrice(String price) { this.price = price; }
    public String getPrice(){ return this.price; }

    public void setTitle(String title){ this.title = title; }
    public String getTitle(){ return this.title; }

    public void setUrl(String url) { this.url = url; }
    public String getUrl(){ return this.url; }

}
