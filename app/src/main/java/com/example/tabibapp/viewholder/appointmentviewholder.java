package com.example.tabibapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.R;
import com.example.tabibapp.face.itemclicklistner;

import java.text.ParseException;

public class appointmentviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView value,value1,value2;

    private itemclicklistner itemClickListener;

    public void setItemClickListener(itemclicklistner itemClickListener){
        this.itemClickListener=itemClickListener;
    }


    public appointmentviewholder(@NonNull View itemView) {
        super(itemView);

        value= (TextView) itemView.findViewById(R.id.txtvalue);
        value1= (TextView) itemView.findViewById(R.id.txtvalue2);
        value2= (TextView) itemView.findViewById(R.id.txtvalue4);

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
