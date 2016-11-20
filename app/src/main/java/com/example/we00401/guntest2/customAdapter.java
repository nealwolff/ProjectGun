package com.example.we00401.guntest2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
//this adapter allows for our custom list view to show.
public class customAdapter extends ArrayAdapter<listings> {
    ArrayList<listings> listing;
    Context context;
    int resource;

    public customAdapter(Context context, int resource, ArrayList<listings> listing) {
        super(context, resource, listing);
        this.listing = listing;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.customlayout, null, true);

        }
        listings listing = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewListing);
        Picasso.with(context).load(listing.getImage()).into(imageView);

        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        txtName.setText(listing.getName());

        TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
        txtPrice.setText(listing.getPrice());

        TextView txtURL = (TextView) convertView.findViewById(R.id.txtURL);
        txtURL.setText(listing.getURL());

        return convertView;
    }
}
