package com.example.tabibapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.requests;
import com.example.tabibapp.common.common;
import com.example.tabibapp.viewholder.request_status_viewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class requestStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requeststatus;
    FirebaseRecyclerAdapter<requests, request_status_viewholder> adapter;
String value,v ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_status);
        database=FirebaseDatabase.getInstance();
        requeststatus=database.getReference("requeststatus");


        recyclerView=(RecyclerView) findViewById(R.id.listbook1);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        value =common.currentuserphone;
        if (common.person.equals("true") || common.currenthospital.equals("true")){
        loadrequesstatus(value);
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();}
        else if (common.person.equals("false")){
            loadrequesstatus1(value);
            Toast.makeText(this, "false"+value, Toast.LENGTH_SHORT).show();}



    }

    private void loadrequesstatus(final String value) {
        adapter=new FirebaseRecyclerAdapter<requests, request_status_viewholder>(requests.class,
                R.layout.item_reqest_status,
                request_status_viewholder.class,
                requeststatus.orderByChild("doctor_phone").equalTo(value) ) {
            @Override
            protected void populateViewHolder(request_status_viewholder viewholder, requests model, int i) {
                viewholder.clinic_name.setText(model.getClinic_name());
                viewholder.clinic_date.setText(model.getClinic_date());
                viewholder.price_clinic.setText(model.getClinic_price());
                viewholder.clinic_comment.setText(model.getComment());
                viewholder.status.setText(model.getStatus());

                v =model.getStatus();




            }
        };
        recyclerView.setAdapter(adapter);

    }

    private void loadrequesstatus1(final String value) {
        adapter=new FirebaseRecyclerAdapter<requests, request_status_viewholder>(requests.class,
                R.layout.item_reqest_status,
                request_status_viewholder.class,
                requeststatus.orderByChild("user_phone").equalTo(value)) {
            @Override
            protected void populateViewHolder(request_status_viewholder viewholder, requests model, int i) {
                viewholder.clinic_name.setText(model.getClinic_name());
                viewholder.clinic_date.setText(model.getClinic_date());
                viewholder.price_clinic.setText(model.getClinic_price());
                viewholder.clinic_comment.setText(model.getComment());
                viewholder.status.setText(model.getStatus());

            }
        };
        recyclerView.setAdapter(adapter);

    }


}
