package com.example.tabibapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.category;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.menuviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database ;
    DatabaseReference category ;
    RecyclerView recyclermenu ;
    RecyclerView.LayoutManager layoutmanager ;
    FirebaseRecyclerAdapter<com.example.tabibapp.Model.category, menuviewholder> adapter;
    String doctor="";
    String value="false";
    String value1;
    String val;


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

        Intent intent =getIntent();
        value =intent.getStringExtra("true");
        doctor =intent.getStringExtra("doctorid1");
        value1=common.currentadmin;



        if (value.equals("true")) {
            Menu menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.nav_myprofile);
            MenuItem target1 = menu.findItem(R.id.nav_dates);
           // MenuItem target2 = menu.findItem(R.id.nav_mydates);
            target1.setVisible(true);
           // target2.setVisible(false);

        } else if (value.equals("false")) {
            Menu menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.nav_myprofile);
            MenuItem target1 = menu.findItem(R.id.nav_dates);
           // MenuItem target2 = menu.findItem(R.id.nav_mydates);
            target.setVisible(false);
            target1.setVisible(false);
           // target2.setVisible(true);


        }

         if (value1.equals("true")) {
            Menu menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.nav_setting);
            target.setVisible(true);

        }
        else if (value1.equals("false")) {
            Menu menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.nav_setting);
            target.setVisible(false);

        }


        if (common.currentadmin.equals("true")){
            Menu menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.nav_add_doctor);
            MenuItem target1 = menu.findItem(R.id.nav_mydates);
            target.setVisible(true);
            target1.setVisible(false);
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        }
        else if (common.currentadmin.equals("false")){
            Menu menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.nav_add_doctor);
            target.setVisible(false);
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();

        }



        View headerview =navigationView.getHeaderView(0);
        TextView txtfullname =(TextView)headerview.findViewById(R.id.textView);
        txtfullname.setText(common.currentuser.getPhone());



        //try
        //init frebase
        database =FirebaseDatabase.getInstance();
        category =database.getReference("category");

        //try animation
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

                        if (adapter.getRef(position).getKey().equals("06") || adapter.getRef(position).getKey().equals("11")){
                            Intent hoslist = new Intent(home.this, hospital.class);
                            hoslist.putExtra("categoryid1", adapter.getRef(position).getKey());
                            startActivity(hoslist);
                        }
                        else if (value.equals("true") || common.currentadmin.equals("true")) {
                            Intent doclist = new Intent(home.this, doc_list_admin.class);
                            doclist.putExtra("categoryid", adapter.getRef(position).getKey());
                            startActivity(doclist);
                        }
                        else if (value.equals("false")) {
                            Intent doclist = new Intent(home.this, doc_list_patient.class);
                            doclist.putExtra("categoryid", adapter.getRef(position).getKey());
                            startActivity(doclist);
                        }




                    }
                });

            }
        };

        // load data
        recyclermenu =(RecyclerView) findViewById(R.id.recycler_menu);
        recyclermenu.setLayoutManager(new GridLayoutManager(this,2));
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclermenu.getContext(),
                R.anim.layoutfalldown);
        recyclermenu.setLayoutAnimation(controller);

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
       // getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dates) {
            Intent homeintent = new Intent(home.this, request.class);
            homeintent.putExtra("doctorphone",common.currentuserphone);
            startActivity(homeintent);
        } else if (id == R.id.nav_callus) {
            Intent homeintent = new Intent(home.this, more.class);
            startActivity(homeintent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(home.this, more1.class);
            startActivity(intent);

        }else if (id == R.id.nav_myprofile) {

            if (common.currenthospital.equals("true")){
                Intent intent = new Intent(home.this, hospital_profile.class);
                intent.putExtra("hospitalid", common.currentuserphone);
                intent.putExtra("hospital", "0");
                startActivity(intent);
            }else if (common.currenthospital.equals("false")) {
                Intent intent = new Intent(home.this, doc_details.class);
                intent.putExtra("doctorid1", doctor);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_chat) {
            Intent intent = new Intent(home.this, StartActivity.class);
            startActivity(intent);


        }
        else if (id == R.id.nav_setting) {
            Intent intent = new Intent(home.this, add_setting.class);
            startActivity(intent);


        }
        else if (id == R.id.nav_mydates) {
            Intent intent = new Intent(home.this, requestStatus.class);
            startActivity(intent);


        }
        else if (id == R.id.nav_Exit) {
            finish();
            moveTaskToBack(true);


        }

        else if (id == R.id.nav_add_doctor) {
            Intent intent = new Intent(home.this, admin_requests.class);
            startActivity(intent);

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // try

    private void loadmenu() {
        recyclermenu.setAdapter(adapter);
           recyclermenu.getAdapter().notifyDataSetChanged();
          recyclermenu.scheduleLayoutAnimation();

    }

}
