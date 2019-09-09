package com.example.tabibapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.R;
import com.example.tabibapp.face.itemclicklistner;

import java.text.ParseException;

public class requestviewholder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtclinicname,clinicdate,userphone;
    private itemclicklistner itemClickListener;
    public void setItemClickListener(itemclicklistner itemClickListener){
        this.itemClickListener=itemClickListener;
    }


    public requestviewholder(@NonNull View itemView) {
        super(itemView);

        txtclinicname=(TextView) itemView.findViewById(R.id.clinic_name);
        clinicdate=(TextView) itemView.findViewById(R.id.clinic_date);
        userphone=(TextView) itemView.findViewById(R.id.Phone);

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
