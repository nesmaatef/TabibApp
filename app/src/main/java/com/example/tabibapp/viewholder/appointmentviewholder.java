package com.example.tabibapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.R;
import com.example.tabibapp.face.itemclicklistner;

public class appointmentviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView value;

    private itemclicklistner itemClickListener;

    public void setItemClickListener(itemclicklistner itemClickListener){
        this.itemClickListener=itemClickListener;
    }


    public appointmentviewholder(@NonNull View itemView) {
        super(itemView);

        value= (TextView) itemView.findViewById(R.id.txtvalue);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
