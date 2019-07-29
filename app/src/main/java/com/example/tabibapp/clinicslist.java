package com.example.tabibapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.clinics;
import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.clinicviewholder;
import com.example.tabibapp.viewholder.doctorviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class clinicslist extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference cliniclist;
    String doctorid="";
    FirebaseRecyclerAdapter<clinics, clinicviewholder> adapter;
    String doctorname="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinicslist);


        //firebase
        database=FirebaseDatabase.getInstance();
        cliniclist=database.getReference("clinics");

        recyclerView=(RecyclerView) findViewById(R.id.recycler_clinic);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent=getIntent();
        doctorname = intent.getStringExtra("doctorname");


//getintent
        if (getIntent()!=null)
            doctorid=getIntent().getStringExtra("DoctorId");
        if (!doctorid.isEmpty() && doctorid!=null) {
            loadcliniclist(doctorid);
        }
    }

    private void loadcliniclist(String doctorid) {
        adapter=new FirebaseRecyclerAdapter<clinics, clinicviewholder>(clinics.class,
                R.layout.clinic_item,
                clinicviewholder.class,
                cliniclist.orderByChild("docid").equalTo(doctorid)) {
            @Override
            protected void populateViewHolder(clinicviewholder clinicviewholder, clinics clinics, int i) {

                clinicviewholder.txtmap.setText(clinics.getMap());

                clinicviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                      //  Toast.makeText(clinicslist.this, "clinic here", Toast.LENGTH_SHORT).show();
                        Intent goclinic = new Intent(clinicslist.this, clinicprofile.class);
                        goclinic.putExtra("clinicid", adapter.getRef(position).getKey());
                        goclinic.putExtra("docname", doctorname);
                        startActivity(goclinic);

                    }
                });

            }
        };
    recyclerView.setAdapter(adapter);
    }

}
