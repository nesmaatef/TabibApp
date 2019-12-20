package com.example.tabibapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.hospitals;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.hospitalviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class hospital extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference hospital, hospital1;
    FirebaseRecyclerAdapter<hospitals, hospitalviewholder> adapter;
String hospitalid= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        //firebase
        database=FirebaseDatabase.getInstance();
        hospital=database.getReference("hospital");
        hospital1=database.getReference("hospital1");


        recyclerView=(RecyclerView) findViewById(R.id.recycler_hospital);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //getintent
        if (getIntent()!=null)
            hospitalid=getIntent().getStringExtra("categoryid1");
       // if (!hospitalid.isEmpty() && hospitalid!=null) {
       //     loadhospitallist(hospitalid);

        
        
        if (hospitalid.equals("06"))
            loadhospitallist(hospitalid);
        
        else if (hospitalid.equals("11")) {
            loadhospitallist1(hospitalid);
            common.current_item = "true";
        }
        

    }

    private void loadhospitallist(String hospitalid) {
        adapter=new FirebaseRecyclerAdapter<hospitals,hospitalviewholder>(hospitals.class,
                R.layout.hospital_item,
                hospitalviewholder.class,
               hospital.orderByChild("catid").equalTo(hospitalid)) {
            @Override
            protected void populateViewHolder(hospitalviewholder hospitalviewholder, hospitals hospitals, int i) {

                hospitalviewholder.txtname.setText(hospitals.getName());
                Picasso.get().load(hospitals.getImage()).into(hospitalviewholder.imghospital);


                hospitalviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //  Toast.makeText(clinicslist.this, "clinic here", Toast.LENGTH_SHORT).show();
                        Intent gohospital = new Intent(hospital.this, hospital_profile.class);
                        gohospital.putExtra("hospitalid", adapter.getRef(position).getKey());
                        gohospital.putExtra("hospital","06");

                        //goclinic.putExtra("docname", doctorname);
                        startActivity(gohospital);

                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);


    }

    private void loadhospitallist1(String hospitalid) {
        adapter=new FirebaseRecyclerAdapter<hospitals,hospitalviewholder>(hospitals.class,
                R.layout.hospital_item,
                hospitalviewholder.class,
                hospital1.orderByChild("catid").equalTo(hospitalid)) {
            @Override
            protected void populateViewHolder(hospitalviewholder hospitalviewholder, hospitals hospitals, int i) {

                hospitalviewholder.txtname.setText(hospitals.getName());
                Picasso.get().load(hospitals.getImage()).into(hospitalviewholder.imghospital);


                hospitalviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent gohospital = new Intent(hospital.this, hospital_profile.class);
                        gohospital.putExtra("hospitalid", adapter.getRef(position).getKey());
                        gohospital.putExtra("hospital", "11");
                        startActivity(gohospital);

                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);


    }



}
