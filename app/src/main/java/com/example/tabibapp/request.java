package com.example.tabibapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.Model.reserve;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.doctorviewholder;
import com.example.tabibapp.viewholder.requestviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class request extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;
    String value ="";

    FirebaseRecyclerAdapter<reserve, requestviewholder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        //firebase
        database=FirebaseDatabase.getInstance();
        request=database.getReference("requests");

        recyclerView=(RecyclerView) findViewById(R.id.listbook);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent =getIntent();
        value =intent.getStringExtra("doctorphone");

        loadrequests(value);
    }

    private void loadrequests(final String value) {
        adapter=new FirebaseRecyclerAdapter<reserve, requestviewholder>(reserve.class,
                R.layout.request_item,
                requestviewholder.class,
                request.orderByChild("doctor_phone").equalTo(value)) {
            @Override
            protected void populateViewHolder(final requestviewholder viewHolder, reserve model, int position) {
                viewHolder.txtclinicname.setText(model.getClinic_name());
                viewHolder.clinicdate.setText(model.getClinic_date());
                viewHolder.userphone.setText(model.getUser_phone());


                viewHolder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // Toast.makeText(doc_list_admin.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                      //  Intent docdetails = new Intent(request.this, doc_details.class);

                        //docdetails.putExtra("DoctorId", adapter.getRef(position).getKey());
                        //startActivity(docdetails);


                    }
                });


            }


        };
        recyclerView.setAdapter(adapter);

    }
}
