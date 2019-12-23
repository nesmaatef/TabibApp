package com.example.tabibapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.Model.doctor_requests;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.doctorviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

public class admin_requests extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference doctorlist;
    String admin="";
    FirebaseRecyclerAdapter<doctor_requests, doctorviewholder> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_requests);

        database=FirebaseDatabase.getInstance();
        doctorlist=database.getReference("DoctorRequests");
        recyclerView=(RecyclerView) findViewById(R.id.recycler_admin);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        admin =common.currentuserphone;

        loadrequests(admin);
    }

    private void loadrequests(String admin) {
        adapter=new FirebaseRecyclerAdapter<doctor_requests, doctorviewholder>(doctor_requests.class,
                R.layout.admin_requsets_item,
                doctorviewholder.class,
                doctorlist) {
            @Override
            protected void populateViewHolder(final doctorviewholder viewHolder, doctor_requests model, int position) {
                Picasso.get().load(model.getImage()).into(viewHolder.imgdoc);
                viewHolder.txtname.setText(model.getName());
                viewHolder.txtdesc.setText(model.getDesc());
                viewHolder.txtphone.setText(model.getPhone());
                final doctor_requests clickitem =model;


                viewHolder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // Toast.makeText(doc_list_admin.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                      //  Intent docdetails = new Intent(admin_requests.this, doc_details.class);

                       // docdetails.putExtra("DoctorId", adapter.getRef(position).getKey());
                       // startActivity(docdetails);


                    }
                });


            }


        };
        recyclerView.setAdapter(adapter);

    }
}
