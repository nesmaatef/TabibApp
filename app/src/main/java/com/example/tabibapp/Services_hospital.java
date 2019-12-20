package com.example.tabibapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.Hospital_services;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.service_hospital_viewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;

public class Services_hospital extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference services;
    RecyclerView recycler1;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Hospital_services, service_hospital_viewholder> adapter;

    String hospitalid ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_hospital);
        database=FirebaseDatabase.getInstance();
        services=database.getReference("Rooms");
        recycler1 =(RecyclerView) findViewById(R.id.recycler_service_hospital);

        recycler1.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler1.setLayoutManager(layoutManager);




      Intent intent = getIntent();
        hospitalid = intent.getStringExtra("hospitalid");
        //clinic_map =intent.getStringExtra("namedoctor");
        //clinic_price =intent.getStringExtra("clinicprice");

       // hospitalid =common.currentuserphone;

        load_hospital_services(hospitalid);
    }
    private void load_hospital_services(final String hospital) {
        adapter= new FirebaseRecyclerAdapter<Hospital_services, service_hospital_viewholder>(
                Hospital_services.class,
                R.layout.service_hospital_item,
                service_hospital_viewholder.class,
                services.orderByChild("hospitalid").equalTo(hospitalid)
        ) {

            @Override
            protected void populateViewHolder(service_hospital_viewholder clinicviewholder, final Hospital_services clinic_services, int i) {
                clinicviewholder.name.setText(clinic_services.getName());

                clinicviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) throws ParseException {
                      Intent intent =new Intent(Services_hospital.this, appoint_hospital.class);

                        intent.putExtra("serviceid", adapter.getRef(position).getKey());


                        startActivity(intent);


                    }
                });
            }


        };
        recycler1.setAdapter(adapter);
    }
    }

