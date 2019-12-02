package com.example.tabibapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.reserve;
import com.example.tabibapp.common.common;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import info.hoang8f.widget.FButton;

public class book extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database1;
    DatabaseReference book;
EditText edtname,edtprice,edtdate, edtcomment;
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

                if (newreserve !=null){
                    book.push().setValue(newreserve);
                    Snackbar.make(root, "your request is done" ,Snackbar.LENGTH_SHORT).show();







                }





            }
        });
        edtname=(EditText) findViewById(R.id.edtdocname);
        edtprice=(EditText) findViewById(R.id.edtdocprice);
        edtdate=(EditText)findViewById(R.id.edtdocdate);
        edtcomment=(EditText)findViewById(R.id.edtcomment);

        root=(RelativeLayout) findViewById(R.id.root);

        Intent intent=getIntent();
        doctorname = intent.getStringExtra("name");
        clinicprice = intent.getStringExtra("price");

        doctordate = intent.getStringExtra("docdate");
        clinicid = intent.getStringExtra("name");


        edtname.setText(clinicid);
        edtprice.setText(clinicprice);
        edtdate.setText(doctordate);



    }




}
