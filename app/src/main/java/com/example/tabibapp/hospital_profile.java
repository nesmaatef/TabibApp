package com.example.tabibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.Model.hospitals;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import info.hoang8f.widget.FButton;

public class hospital_profile extends AppCompatActivity {

    TextView txt_name_hos,txt_price_hos,addrees,times,desc_hos;
    ImageView imgprofile;
    FButton fbhospital;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference hospital;
    String hospitalid ="";
    hospitals currenthospital;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_profile);

        //init firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        hospital =firebaseDatabase.getReference("hospital");

        // init view
        txt_name_hos= (TextView) findViewById(R.id.txt_name_hos);
        txt_price_hos= (TextView) findViewById(R.id.txt_price_hos);
        addrees= (TextView) findViewById(R.id.addrees);

        times= (TextView) findViewById(R.id.times);
        desc_hos= (TextView) findViewById(R.id.desc_hos);


        imgprofile=(ImageView) findViewById(R.id.imgprofile);
        fbhospital=(FButton) findViewById(R.id.fbhospital);

        if (getIntent() !=null)
            hospitalid =getIntent().getStringExtra("hospitalid");

        if (!hospitalid.isEmpty())
        {
            gethospitaldetails(hospitalid);
        }

    }

    private void gethospitaldetails(String hospitalid) {


        hospital.child(hospitalid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currenthospital=dataSnapshot.getValue(hospitals.class);
                Picasso.get().load(currenthospital.getImage()).into(imgprofile);
                txt_name_hos.setText(currenthospital.getName());
                txt_price_hos.setText(currenthospital.getPrice());
                addrees.setText(currenthospital.getMap());
                times.setText(currenthospital.getTimes());
                desc_hos.setText(currenthospital.getDesc());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
