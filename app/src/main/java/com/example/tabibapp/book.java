package com.example.tabibapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.reserve;
import com.example.tabibapp.common.common;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import info.hoang8f.widget.FButton;

public class book extends AppCompatActivity {

    FirebaseDatabase database1;
    DatabaseReference book;
TextView edtname,edtprice,edtdate, edtcompany, edtcity;
EditText edtcomment;
RelativeLayout root;
  FButton fab ;
   reserve newreserve;
  String doctorname ="";
    String clinicprice ="";
    String doctordate ="";
    String clinicid="";
    //List<com.example.tabibapp.Model.reserve> req =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);


        database1=FirebaseDatabase.getInstance();
        book=database1.getReference("requests");


        fab=(FButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newreserve =new reserve();
                newreserve.setDoctor_phone(common.currentdoctorphone);
                newreserve.setClinic_name(edtname.getText().toString());
                newreserve.setClinic_price(edtprice.getText().toString());
                newreserve.setClinic_date(edtdate.getText().toString());
                newreserve.setUser_phone(common.currentuserphone);
                newreserve.setComment(edtcomment.getText().toString());
                newreserve.setCity(edtcity.getText().toString());
                newreserve.setCompany(edtcompany.getText().toString());



                if (newreserve !=null){
                    book.push().setValue(newreserve);
                    Snackbar.make(root, "your request is done" ,Snackbar.LENGTH_SHORT).show();







                }





            }
        });
        edtname=(TextView) findViewById(R.id.edtdocname);
        edtprice=(TextView) findViewById(R.id.edtdocprice);
        edtdate=(TextView) findViewById(R.id.edtdocdate);
        edtcomment=(EditText) findViewById(R.id.edtcomment);
        edtcity=(TextView) findViewById(R.id.edtcity);
        edtcompany=(TextView) findViewById(R.id.edtcompany);

        root=(RelativeLayout) findViewById(R.id.root);

        Intent intent=getIntent();
        doctorname = intent.getStringExtra("name");
        clinicprice = intent.getStringExtra("price");

        doctordate = intent.getStringExtra("docdate");
        clinicid = intent.getStringExtra("name");


        edtname.setText(clinicid);
        edtprice.setText(clinicprice);
        edtdate.setText(doctordate);
        edtcompany.setText(common.currentcompany);
        edtcity.setText(common.currentcity);



    }




}
