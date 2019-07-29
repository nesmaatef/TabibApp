package com.example.tabibapp.viewholder;


import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.R;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;

import de.hdodenhof.circleimageview.CircleImageView;

public class doctorviewholder extends RecyclerView.ViewHolder implements View.OnClickListener ,
        View.OnCreateContextMenuListener


{
    public TextView txtname, txtdesc;
    public CircleImageView imgdoc;
    public ImageView fav;
    private itemclicklistner itemClickListener;

    public void setItemClickListener(itemclicklistner itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    public doctorviewholder(@NonNull View itemView) {
        super(itemView);

        txtname= (TextView) itemView.findViewById(R.id.txtname);
        txtdesc= (TextView) itemView.findViewById(R.id.txtdesc);
        imgdoc = (CircleImageView) itemView.findViewById(R.id.doctor_image);
        fav = (ImageView) itemView.findViewById(R.id.fav);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);


    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");
        menu.add(0,0,getAdapterPosition(),common.UPDATE);
        menu.add(0,1,getAdapterPosition(),common.DELETE);
    }
}


