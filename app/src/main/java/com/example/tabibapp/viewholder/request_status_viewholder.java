package com.example.tabibapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.R;
import com.example.tabibapp.face.itemclicklistner;

import java.text.ParseException;

public class request_status_viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView clinic_name,clinic_date,price_clinic,clinic_comment,status;
    private itemclicklistner itemClickListener;
    public void setItemClickListener(itemclicklistner itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    public request_status_viewholder(@NonNull View itemView) {
        super(itemView);
        clinic_name=(TextView) itemView.findViewById(R.id.clinic_name);
        clinic_date=(TextView) itemView.findViewById(R.id.clinic_date);
        price_clinic=(TextView) itemView.findViewById(R.id.price_clinic);
        clinic_comment=(TextView) itemView.findViewById(R.id.clinic_comment);
        status=(TextView) itemView.findViewById(R.id.status);


        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
