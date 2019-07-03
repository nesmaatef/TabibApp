package com.example.tabibapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.category;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.menuviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class cat_list1 extends AppCompatActivity {

    FirebaseDatabase database ;
    DatabaseReference category ;
    //    TextView txtfullname;
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
        //   layoutmanager= new LinearLayoutManager(this);
        // recyclermenu.setLayoutManager(layoutmanager);
        recyclermenu.setLayoutManager(new GridLayoutManager(this,3));


        loadmenu();





    }

    private void loadmenu() {
        adapter=new FirebaseRecyclerAdapter<com.example.tabibapp.Model.category, menuviewholder>(category.class,
                R.layout.category_item,
                menuviewholder.class,
                category ) {
            @Override
            protected void populateViewHolder(menuviewholder viewHolder, com.example.tabibapp.Model.category model, int position) {
                //  viewHolder.txtmenuname.setText((model.getName()));
                Picasso.get().load(model.getImage())
                        .into(viewHolder.imageView);

                final category clickitem =model;
                viewHolder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        // Toast.makeText(category_list.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                        Intent doclist = new Intent(cat_list1.this, doc_list1.class);

                        //because categoryid is key , so we just get key of this

                        doclist.putExtra("categoryid", adapter.getRef(position).getKey());

                        startActivity(doclist);



                    }
                });

            }
        };
        recyclermenu.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId() ==R.id.more){
             Intent homeintent = new Intent(cat_list1.this, more.class);
        startActivity(homeintent);
        Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more, menu);
        return true;    }
}
