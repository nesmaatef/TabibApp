package com.example.tabibapp.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.Model.fav;
import com.example.tabibapp.R;
import com.example.tabibapp.doc_details;
import com.example.tabibapp.doc_list_admin;
import com.example.tabibapp.face.itemclicklistner;
import com.squareup.picasso.Picasso;

import java.util.List;

public class fav_adapter extends RecyclerView.Adapter<favviewholder> {
   private Context context;
   private List<fav> favoritelist;

    public fav_adapter(Context context, List<fav> favoritelist) {
        this.context = context;
        this.favoritelist = favoritelist;
    }

    @NonNull
    @Override
    public favviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).
                inflate(R.layout.fav_item,parent,false);
        return new favviewholder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull favviewholder viewHolder, int position) {
        Picasso.get().load(favoritelist.get(position).getDoctorimage()).into(viewHolder.imgdoc);

        viewHolder.txtname.setText(favoritelist.get(position).getDoctorname());



        viewHolder.txtdesc.setText(favoritelist.get(position).getDoctordescription());


        final fav clickitem =favoritelist.get(position);


        viewHolder.setItemClickListener(new itemclicklistner() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                // Toast.makeText(doc_list_admin.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();



                Intent docdetails = new Intent(context, doc_details.class);
                docdetails.putExtra("fav_id", favoritelist.get(position).getDoctorid());
                context.startActivity(docdetails);

            }
        });


    }

    @Override
    public int getItemCount() {
        return favoritelist.size();
    }
}
