package com.example.retrofit2stub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    Picasso picasso;
    Context ctx;
    ArrayList<Hit> images = new ArrayList<>();

    public GridViewAdapter(Context ctx, Picasso picasso) {
        this.ctx = ctx;
        this.picasso = picasso;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = images.get(position).previewURL;
        convertView = LayoutInflater.from(ctx).
                inflate(R.layout.grid_view_component, parent, false);
        ImageView img = convertView.findViewById(R.id.imageView);
        picasso.load(url).into(img);
        return convertView;
    }
}