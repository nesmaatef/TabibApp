package com.example.tabibapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabibapp.Model.reserve;
import com.example.tabibapp.common.common;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import info.hoang8f.widget.FButton;

public class book_hospital_doctor extends AppCompatActivity {

    FirebaseDatabase database1;
    DatabaseReference book;
    TextView edtname, edtprice, edtdate, edtroom, edtservice, edtdocdate1;
    EditText edtcomment;
    RelativeLayout root;
    FButton fab;
    reserve newreserve;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_hospital_doctor);

        database1 = FirebaseDatabase.getInstance();
        book = database1.getReference("requests");


        fab = (FButton) findViewById(R.id.fab);
        edtname = (TextView) findViewById(R.id.edtdocname);
        edtprice = (TextView) findViewById(R.id.edtdocprice);
        edtroom = (TextView) findViewById(R.id.edtroom);
        edtdocdate1 = (TextView) findViewById(R.id.edtdocdate1);
        edtservice = (TextView) findViewById(R.id.edtservice);
        edtdate = (TextView) findViewById(R.id.edtdocdate);
        edtcomment = (EditText) findViewById(R.id.edtcomment);

        root = (RelativeLayout) findViewById(R.id.root);

        edtname.setText(common.currenthospital_name);
        edtprice.setText(common.currenthospital_service_price);
        edtroom.setText(common.currenthospital_room);
        edtdocdate1.setText(common.currenthospital_date);
        edtdate.setText(common.currenthospital_date1);
        edtservice.setText(common.currenthospital_service);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newreserve = new reserve();
                newreserve.setDoctor_phone(common.currentdoctorphone);
                newreserve.setClinic_name(edtname.getText().toString());
                newreserve.setClinic_price(edtprice.getText().toString());
                newreserve.setClinic_date(edtdate.getText().toString());
                newreserve.setComment(edtcomment.getText().toString());
                newreserve.setService(edtservice.getText().toString());
                newreserve.setSection(edtroom.getText().toString());
                newreserve.setClinic_date1(edtdocdate1.getText().toString());


                if (newreserve != null) {
                    book.push().setValue(newreserve);
                    Snackbar.make(root, "your request is done", Snackbar.LENGTH_SHORT).show();


                }
            }
        });
    }
}
