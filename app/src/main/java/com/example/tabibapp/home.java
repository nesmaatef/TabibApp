package com.example.tabibapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.tabibapp.DataBase.database;
import com.example.tabibapp.Model.category;
import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.Model.fav;
import com.example.tabibapp.Model.users;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.doctorviewholder;
import com.example.tabibapp.viewholder.menuviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database ;
    DatabaseReference category ;
    //    TextView txtfullname;
    RecyclerView recyclermenu ;
    RecyclerView.LayoutManager layoutmanager ;
    FirebaseRecyclerAdapter<com.example.tabibapp.Model.category, menuviewholder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //try
        //init frebase
        database =FirebaseDatabase.getInstance();
        category =database.getReference("category");

        // load data
        recyclermenu =(RecyclerView) findViewById(R.id.recycler_menu);
        recyclermenu.setLayoutManager(new GridLayoutManager(this,2));
        //   Animation controller = AnimationUtils.loadAnimation(recyclermenu.getContext(),
        //         R.anim.layoutfalldown);
        // recyclermenu.setAnimation(controller);


        loadmenu();





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // try

    private void loadmenu() {
        adapter=new FirebaseRecyclerAdapter<com.example.tabibapp.Model.category, menuviewholder>(com.example.tabibapp.Model.category.class,
                R.layout.category_item,
                menuviewholder.class,
                category ) {
            @Override
            protected void populateViewHolder(menuviewholder viewHolder, final com.example.tabibapp.Model.category model, int position) {
                Picasso.get().load(model.getImage())
                        .into(viewHolder.imageView);
                viewHolder.txtmenuname.setText(model.getName());


                final category clickitem =model;
                viewHolder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        if (adapter.getRef(position).getKey().equals("06")){
                            Intent hoslist = new Intent(home.this, hospital.class);
                            hoslist.putExtra("categoryid1", adapter.getRef(position).getKey());
                            startActivity(hoslist);
                        }
                        else {
                            // Toast.makeText(category_list_admin.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                            Intent doclist = new Intent(home.this, doc_list_admin.class);

                            //because categoryid is key , so we just get key of this

                            doclist.putExtra("categoryid", adapter.getRef(position).getKey());

                            startActivity(doclist);
                        }


                    }
                });

            }
        };
        recyclermenu.setAdapter(adapter);
        //    recyclermenu.getAdapter().notifyDataSetChanged();
        //  recyclermenu.scheduleLayoutAnimation();

    }

}
