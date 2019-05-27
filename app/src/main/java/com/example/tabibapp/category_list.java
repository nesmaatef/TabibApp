package com.example.tabibapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabibapp.Model.category;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.menuviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class category_list extends AppCompatActivity {

    FirebaseDatabase database ;
    DatabaseReference category ;
    TextView txtfullname;
    RecyclerView recyclermenu ;
    RecyclerView.LayoutManager layoutmanager ;
    FirebaseRecyclerAdapter<com.example.tabibapp.Model.category, menuviewholder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        //init frebase
        database =FirebaseDatabase.getInstance();
        category =database.getReference("category");

        // load data
        recyclermenu =(RecyclerView) findViewById(R.id.recycler_menu);
        recyclermenu.setHasFixedSize(true);
      //  layoutmanager= new LinearLayoutManager(this);
       // recyclermenu.setLayoutManager(layoutmanager);
recyclermenu.setLayoutManager(new GridLayoutManager(this,2));
        loadmenu();





    }

    private void loadmenu() {
        adapter=new FirebaseRecyclerAdapter<com.example.tabibapp.Model.category, menuviewholder>(category.class,R.layout.category_item,menuviewholder.class,category ) {
            @Override
            protected void populateViewHolder(menuviewholder viewHolder, com.example.tabibapp.Model.category model, int position) {
                viewHolder.txtmenuname.setText((model.getName()));
                Picasso.get().load(model.getImage())
                        .into(viewHolder.imageView);
                final category clickitem =model;
viewHolder.setItemClickListener(new itemclicklistner() {
    @Override
    public void onClick(View view, int position, boolean isLongClick) {
        Toast.makeText(category_list.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();

    }
});

            }
        };
        recyclermenu.setAdapter(adapter);

    }
}
