package com.example.tabibapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.menuviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class category_hospital extends AppCompatActivity {
    FirebaseDatabase database ;
    DatabaseReference category ;
    RecyclerView recyclercat ;
    RecyclerView.LayoutManager layoutmanager ;
    FirebaseRecyclerAdapter<com.example.tabibapp.Model.category, menuviewholder> adapter;
    String hospitalid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_hospital);

        database =FirebaseDatabase.getInstance();
        category =database.getReference("category_hospital");

        recyclercat =(RecyclerView) findViewById(R.id.recycler_cat);
        recyclercat.setLayoutManager(new GridLayoutManager(this,2));

        if (getIntent() !=null)
            hospitalid =getIntent().getStringExtra("hospitalid");



        loadmenu(hospitalid);

    }

    private void loadmenu( final String hospitalid) {
        adapter=new FirebaseRecyclerAdapter<com.example.tabibapp.Model.category, menuviewholder>(com.example.tabibapp.Model.category.class,
                R.layout.cat_hospital_item,
                menuviewholder.class,
                category.orderByChild("hospital_id").equalTo(hospitalid) ) {
            @Override
            protected void populateViewHolder(menuviewholder viewHolder, final com.example.tabibapp.Model.category model, int position) {
                viewHolder.txtmenuname.setText(model.getName());


                final com.example.tabibapp.Model.category clickitem =model;
                viewHolder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                           Intent docdetails = new Intent(category_hospital.this, doctor_list_hospital.class);

                          docdetails.putExtra("DoctorId", adapter.getRef(position).getKey());
                          startActivity(docdetails);








                    }
                });

            }
        };
        recyclercat.setAdapter(adapter);
    }
}
