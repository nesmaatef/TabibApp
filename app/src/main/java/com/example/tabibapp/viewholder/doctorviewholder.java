package com.example.tabibapp.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tabibapp.R;
import com.example.tabibapp.face.itemclicklistner;

import de.hdodenhof.circleimageview.CircleImageView;

public class doctorviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
public TextView txtname, txtdesc, txtmap, txtprice;
public CircleImageView imgdoc;
    private itemclicklistner itemClickListener;

    public void setItemClickListener(itemclicklistner itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    public doctorviewholder(@NonNull View itemView) {
        super(itemView);

        txtname= (TextView) itemView.findViewById(R.id.txtname);
        txtdesc= (TextView) itemView.findViewById(R.id.txtdesc);
        txtmap= (TextView) itemView.findViewById(R.id.txtmap);
        txtprice= (TextView) itemView.findViewById(R.id.txtprice);
         imgdoc = (CircleImageView) itemView.findViewById(R.id.doctor_image);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);


    }
}
