package com.example.tabibapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabibapp.Model.category;
import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.doctorviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class doc_list extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference doctorlist;
    String categoryid="";



    FirebaseRecyclerAdapter<doctor, doctorviewholder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);

        //firebase
        database=FirebaseDatabase.getInstance();
        doctorlist=database.getReference("doctor");

        recyclerView=(RecyclerView) findViewById(R.id.recycler_doc);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //getintent
        if (getIntent()!=null)
            categoryid=getIntent().getStringExtra("categoryid");
        if (!categoryid.isEmpty() && categoryid!=null) {
            loaddoctorlist(categoryid);
        }
    }

    private void loaddoctorlist(String categoryid) {

adapter=new FirebaseRecyclerAdapter<doctor, doctorviewholder>(doctor.class,
        R.layout.doc_item,
        doctorviewholder.class,
        doctorlist.orderByChild("catid").equalTo(categoryid)) {
    @Override
    protected void populateViewHolder(doctorviewholder viewHolder, doctor model, int position) {
        Picasso.get().load(model.getImage()).into(viewHolder.imgdoc);
            viewHolder.txtname.setText(model.getName());
        viewHolder.txtprice.setText(model.getPrice());
        viewHolder.txtmap.setText(model.getMap());
        viewHolder.txtdesc.setText(model.getDesc());
        final doctor clickitem =model;

        viewHolder.setItemClickListener(new itemclicklistner() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Toast.makeText(doc_list.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();

            }
        });


    }
};
        recyclerView.setAdapter(adapter);

    }
}
