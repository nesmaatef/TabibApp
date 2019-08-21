package com.example.tabibapp.viewholder;


import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.R;
import com.example.tabibapp.face.itemclicklistner;

import java.text.ParseException;


public class menuviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtmenuname;
    public ImageView imageView;
    private itemclicklistner itemClickListener;


    public void setItemClickListener(itemclicklistner itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    public menuviewholder(@NonNull View itemView) {
        super(itemView);

        txtmenuname=(TextView) itemView.findViewById(R.id.txt1);
        imageView=(ImageView) itemView.findViewById(R.id.menu_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        try {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
