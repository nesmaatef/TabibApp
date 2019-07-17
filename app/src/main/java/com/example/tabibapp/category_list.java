package com.example.tabibapp;import android.content.Context;import android.content.Intent;import android.os.Bundle;import android.view.View;import android.view.animation.Animation;import android.view.animation.AnimationUtils;import android.view.animation.LayoutAnimationController;import android.widget.TextView;import android.widget.Toast;import androidx.appcompat.app.AppCompatActivity;import androidx.recyclerview.widget.GridLayoutManager;import androidx.recyclerview.widget.RecyclerView;import com.example.tabibapp.Model.category;import com.example.tabibapp.face.itemclicklistner;import com.example.tabibapp.viewholder.menuviewholder;import com.firebase.ui.database.FirebaseRecyclerAdapter;import com.google.firebase.database.DatabaseReference;import com.google.firebase.database.FirebaseDatabase;import com.squareup.picasso.Picasso;public class category_list extends AppCompatActivity {    FirebaseDatabase database ;    DatabaseReference category ;    //    TextView txtfullname;    RecyclerView recyclermenu ;    RecyclerView.LayoutManager layoutmanager ;    FirebaseRecyclerAdapter<com.example.tabibapp.Model.category, menuviewholder> adapter;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_category_list);        //init frebase        database =FirebaseDatabase.getInstance();        category =database.getReference("category");        // load data        recyclermenu =(RecyclerView) findViewById(R.id.recycler_menu);        recyclermenu.setLayoutManager(new GridLayoutManager(this,2));        //   Animation controller = AnimationUtils.loadAnimation(recyclermenu.getContext(),        //         R.anim.layoutfalldown);        // recyclermenu.setAnimation(controller);        loadmenu();    }    private void loadmenu() {        adapter=new FirebaseRecyclerAdapter<com.example.tabibapp.Model.category, menuviewholder>(category.class,                R.layout.category_item,                menuviewholder.class,                category ) {            @Override            protected void populateViewHolder(menuviewholder viewHolder, com.example.tabibapp.Model.category model, int position) {                //  viewHolder.txtmenuname.setText((model.getName()));                Picasso.get().load(model.getImage())                        .into(viewHolder.imageView);                final category clickitem =model;                viewHolder.setItemClickListener(new itemclicklistner() {                    @Override                    public void onClick(View view, int position, boolean isLongClick) {                        // Toast.makeText(category_list.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();                        Intent doclist = new Intent(category_list.this, doc_list.class);                        //because categoryid is key , so we just get key of this                        doclist.putExtra("categoryid", adapter.getRef(position).getKey());                        startActivity(doclist);                    }                });            }        };        recyclermenu.setAdapter(adapter);        //    recyclermenu.getAdapter().notifyDataSetChanged();        //  recyclermenu.scheduleLayoutAnimation();    }}