package com.example.we00401.guntest2;

public class listings {
    private String image;
    private String name;
    private String price;
    private String URL;

    public listings(String image, String name, String price, String URL) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.URL = URL;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }


    public String getPrice() {
        return price;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }



}
