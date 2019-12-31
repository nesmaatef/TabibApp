package com.example.tabibapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.clinic_services;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.serviceviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;

import static android.view.View.VISIBLE;

public class hospital_services extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference services;
    RecyclerView recycler1;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<clinic_services, serviceviewholder> adapter;
    ImageView imgmore;
    String hospitalid ="";
    MaterialEditText edtservice, edtserviceprice;
    com.example.tabibapp.Model.clinic_services currentservice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_services);
        database=FirebaseDatabase.getInstance();
        services=database.getReference("hospital");
        recycler1 =(RecyclerView) findViewById(R.id.recycler_service);
        recycler1.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler1.setLayoutManager(layoutManager);
         imgmore = (ImageView) findViewById(R.id.imgmore1);
         if (common.currentisstaff.equals("true")){
             imgmore.setVisibility(View.INVISIBLE);
         }
        imgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog(hospitalid); }});
        if (getIntent() !=null)
            hospitalid =getIntent().getStringExtra("hospitalid");
        loadhospitalservices(hospitalid); }

    private void loadhospitalservices(final String hospital_id) {
        adapter= new FirebaseRecyclerAdapter<com.example.tabibapp.Model.clinic_services, serviceviewholder>(
                com.example.tabibapp.Model.clinic_services.class,
                R.layout.service_item,
                serviceviewholder.class,
                services.child(hospital_id).child("services_items")
        ) {

            @Override
            protected void populateViewHolder(serviceviewholder clinicviewholder, final com.example.tabibapp.Model.clinic_services clinic_services, int i) {
                clinicviewholder.value.setText(clinic_services.getName());
                clinicviewholder.value1.setText(clinic_services.getPrice());
                common.currenthospital_service =clinic_services.getName();
                common.currenthospital_service_price =clinic_services.getPrice();
                Toast.makeText(hospital_services.this, "hi"+ hospital_id, Toast.LENGTH_SHORT).show();
                clinicviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) throws ParseException {
                        Intent intent =new Intent(hospital_services.this, Services_hospital.class);

                         intent.putExtra("hospitalid",hospital_id);
                        // intent.putExtra("namedoctor", clinic_map);
                        // intent.putExtra("clinicprice", clinic_price);

                        startActivity(intent);


                    }
                });
            }


        };
        recycler1.setAdapter(adapter);
    }

    private void showdialog(final String clinicid){
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(hospital_services.this);
        alertdialog.setTitle("اضف حدمه جديده");
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_services, null);
        edtservice=add_menu_layout.findViewById(R.id.edtservice);
        edtserviceprice=add_menu_layout.findViewById(R.id.edtservice_price);

        alertdialog.setView(add_menu_layout);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                currentservice =new com.example.tabibapp.Model.clinic_services();
                currentservice.setName(edtservice.getText().toString());
                currentservice.setPrice(edtserviceprice.getText().toString());

                services.child(clinicid).child("services_items").push().setValue(currentservice);

            }
        });
        alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog.show();
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(common.UPDATE)){
            updatedate(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }

        if (item.getTitle().equals(common.DELETE)){

            deletefood(adapter.getRef(item.getOrder()).getKey());

        }

        return super.onContextItemSelected(item);
    }

    private void updatedate(final String key, final clinic_services item) {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(hospital_services.this);
        alertdialog.setTitle("تعديل");
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_services, null);
        edtservice=add_menu_layout.findViewById(R.id.edtservice);
        edtserviceprice=add_menu_layout.findViewById(R.id.edtservice_price);

        edtservice.setText(item.getName());
        edtserviceprice.setText(item.getPrice());

        alertdialog.setView(add_menu_layout);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
               item.setName(edtservice.getText().toString());
               item.setPrice(edtserviceprice.getText().toString());

                services.child(hospitalid).child("services_items").child(key).setValue(item);

            }
        });
        alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog.show();

    }

    private void deletefood(String key) {
        services.child(hospitalid).child("services_items").child(key).removeValue();
    }

    }
