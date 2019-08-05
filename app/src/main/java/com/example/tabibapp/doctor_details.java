package com.example.tabibapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.Model.users;
import com.example.tabibapp.common.common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import info.hoang8f.widget.FButton;

public class doctor_details extends AppCompatActivity {
    TextView txtdesc,txtname;
    ImageView imgdoc;
    RatingBar ratingBar;
    FButton button;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference doctor;
    DatabaseReference rating;
    String doctorid ="";
    String doctorphone ="";
    doctor currentdoctor;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        //init firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        doctor =firebaseDatabase.getReference("doctor");
        //rating =firebaseDatabase.getReference("Rating");

        // init view
        ratingBar=(RatingBar)  findViewById(R.id.ratingbar1);
        txtdesc= (TextView) findViewById(R.id.doc_desc1);
        txtname= (TextView) findViewById(R.id.doc_name1);

        imgdoc=(ImageView) findViewById(R.id.img_doc1);
        button=(FButton) findViewById(R.id.goclinics1);

        //getintent
        if (getIntent()!=null)
            doctorphone=getIntent().getStringExtra("doctorid1");
        if (!doctorphone.isEmpty() && doctorphone!=null) {
            Toast.makeText(this, "here we are"+doctorphone, Toast.LENGTH_SHORT).show();
              getdoc_details(doctorphone);
        }
    }

    private void getdoc_details(String doctorphone) {
           doctor.child(doctorphone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentdoctor=dataSnapshot.getValue(com.example.tabibapp.Model.doctor.class);

       Picasso.get().load(currentdoctor.getImage()).into(imgdoc);
                txtdesc.setText(currentdoctor.getDesc());
                txtname.setText(currentdoctor.getName());






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
