package com.example.tabibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tabibapp.Model.clinics;
import com.example.tabibapp.Model.doctor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import info.hoang8f.widget.FButton;

public class clinicprofile extends AppCompatActivity {

    TextView txtname,txtmap,txtprice,txtwait;
    ImageView imgclinic;
    FButton button;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference clinicdata;
    String clinicid ="";
    clinics currentclinic;
    String doctormane="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinicprofile);

        //init firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        clinicdata =firebaseDatabase.getReference("clinics");

        txtname= (TextView) findViewById(R.id.doc_name);
        txtmap= (TextView) findViewById(R.id.map);
        txtprice= (TextView) findViewById(R.id.price);
        txtwait= (TextView) findViewById(R.id.times);
        imgclinic= (ImageView) findViewById(R.id.clinicimg);
        button= (FButton) findViewById(R.id.gotimes);

        Intent intent=getIntent();
        doctormane =getIntent().getStringExtra("docname");


        // get food id
        if (getIntent() !=null)
            clinicid =getIntent().getStringExtra("clinicid");

        if (!clinicid.isEmpty())
        {
            loadclinic(clinicid);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goappoint = new Intent(clinicprofile.this, appointment.class);
                goappoint.putExtra("clinicid", clinicid);
                goappoint.putExtra("namedoctor", doctormane);
                goappoint.putExtra("clinicprice", currentclinic.getPrice());


                startActivity(goappoint);
            }
        });
    }

    private void loadclinic(String clinicid) {
        clinicdata.child(clinicid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentclinic=dataSnapshot.getValue(clinics.class);
                Picasso.get().load(currentclinic.getImage()).into(imgclinic);
                txtname.setText(currentclinic.getName());
                txtprice.setText(currentclinic.getPrice());
                txtmap.setText(currentclinic.getMap());
                txtwait.setText(currentclinic.getTimeswait());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
