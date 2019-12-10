package com.example.tabibapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.R;
import com.example.tabibapp.face.itemclicklistner;

import java.text.ParseException;

public class service_hospital_viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;

    private itemclicklistner itemClickListener;

    public void setItemClickListener(itemclicklistner itemClickListener){
        this.itemClickListener=itemClickListener;
    }



    public service_hospital_viewholder(@NonNull View itemView) {
        super(itemView);

        name= (TextView) itemView.findViewById(R.id.txtvalue_neme);

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
