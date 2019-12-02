package com.example.tabibapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.serviceviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;

public class clinic_services extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference services;
    RecyclerView recycler1;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<com.example.tabibapp.Model.clinic_services, serviceviewholder> adapter;
    String clinicid="";
    String clinic_map ="";
    String clinic_price ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_services);

        database=FirebaseDatabase.getInstance();
        services=database.getReference("clinics");
        recycler1 =(RecyclerView) findViewById(R.id.recycler_service);

        recycler1.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler1.setLayoutManager(layoutManager);




        Intent intent = getIntent();
        clinicid = intent.getStringExtra("clinicid");
        clinic_map =intent.getStringExtra("namedoctor");
        clinic_price =intent.getStringExtra("clinicprice");

        loadclinicservices(clinicid);
    }
    private void loadclinicservices(final String clinicid) {
        adapter= new FirebaseRecyclerAdapter<com.example.tabibapp.Model.clinic_services, serviceviewholder>(
                com.example.tabibapp.Model.clinic_services.class,
                R.layout.service_item,
                serviceviewholder.class,
                services.child(clinicid).child("services")
        ) {

            @Override
            protected void populateViewHolder(serviceviewholder clinicviewholder, final com.example.tabibapp.Model.clinic_services clinic_services, int i) {
                clinicviewholder.value.setText(clinic_services.getName());
                clinicviewholder.value1.setText(clinic_services.getPrice());

                clinicviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) throws ParseException {
                        Intent intent =new Intent(com.example.tabibapp.clinic_services.this, appointment.class);

                        intent.putExtra("clinicid", clinicid);
                        intent.putExtra("namedoctor", clinic_map);
                        intent.putExtra("clinicprice", clinic_price);

                        startActivity(intent);


                    }
                });
            }


        };
        recycler1.setAdapter(adapter);
    }
}
