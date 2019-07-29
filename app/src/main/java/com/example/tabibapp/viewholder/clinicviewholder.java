package com.example.tabibapp.viewholder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.R;
import com.example.tabibapp.face.itemclicklistner;

public class clinicviewholder extends RecyclerView.ViewHolder implements View.OnClickListener ,
        View.OnCreateContextMenuListener {

    public TextView txtmap;
    private itemclicklistner itemClickListener;
    public void setItemClickListener(itemclicklistner itemClickListener){
        this.itemClickListener=itemClickListener;
    }


    public clinicviewholder(@NonNull View itemView) {
        super(itemView);
        txtmap= (TextView) itemView.findViewById(R.id.txtmap);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);


    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }
}
