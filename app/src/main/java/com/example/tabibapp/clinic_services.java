package com.example.tabibapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.serviceviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;

import static android.view.View.VISIBLE;

public class clinic_services extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference services,services1;
    RecyclerView recycler1;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<com.example.tabibapp.Model.clinic_services, serviceviewholder> adapter;
    String clinicid="";
    String clinic_map ="";
    String clinic_price ="";
    ImageView imgmore;
    MaterialEditText edtservice, edtserviceprice;
    com.example.tabibapp.Model.clinic_services currentservice, newservice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_services);
        database=FirebaseDatabase.getInstance();
        services=database.getReference("clinics");
        recycler1 =(RecyclerView) findViewById(R.id.recycler_service);
        recycler1.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler1.setLayoutManager(layoutManager);
         imgmore = (ImageView) findViewById(R.id.imgmore1);

        imgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showdialog();
            }
        });

        Intent intent = getIntent();
        clinicid = intent.getStringExtra("clinicid");
        clinic_map =intent.getStringExtra("namedoctor");
        clinic_price =intent.getStringExtra("clinicprice");
        loadclinicservices(clinicid); }
    private void loadclinicservices(final String clinicid) {
        adapter= new FirebaseRecyclerAdapter<com.example.tabibapp.Model.clinic_services, serviceviewholder>(
                com.example.tabibapp.Model.clinic_services.class,
                R.layout.service_item,
                serviceviewholder.class,
                services.child(clinicid).child("services")
        ) {

            @Override
            protected void populateViewHolder(serviceviewholder clinicviewholder, final com.example.tabibapp.Model.clinic_services clinic_services, int i) {
                clinicviewholder.value.setText(clinic_services.getName());
                clinicviewholder.value1.setText(clinic_services.getPrice());
                clinicviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) throws ParseException {
                        Intent intent =new Intent(com.example.tabibapp.clinic_services.this, appointment.class);
                        intent.putExtra("clinicid", clinicid);
                        intent.putExtra("namedoctor", clinic_map);
                        intent.putExtra("clinicprice", clinic_price);
                        startActivity(intent);
                    }
                }); }};
        recycler1.setAdapter(adapter); }

    private void showdialog(){
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(clinic_services.this);
        alertdialog.setTitle("اضف حدمه جديده");
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_services, null);
        edtservice=add_menu_layout.findViewById(R.id.edtservice);
        edtserviceprice=add_menu_layout.findViewById(R.id.edtservice_price);
        alertdialog.setView(add_menu_layout);
        alertdialog.setPositiveButton("تم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                newservice =new  com.example.tabibapp.Model.clinic_services();
                newservice.setName(edtservice.getText().toString());
                newservice.setPrice(edtserviceprice.getText().toString());

                services.child(clinicid).child("services").push().setValue(newservice);

            }
        });
        alertdialog.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
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

    private void updatedate(final String key, final com.example.tabibapp.Model.clinic_services item) {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(clinic_services.this);
        alertdialog.setTitle("تعديل معلومات");
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_services, null);
        edtservice=add_menu_layout.findViewById(R.id.edtservice);
        edtserviceprice=add_menu_layout.findViewById(R.id.edtservice_price);
        alertdialog.setView(add_menu_layout);

        edtservice.setText(item.getName());
        edtserviceprice.setText(item.getPrice());
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setName(edtservice.getText().toString());
                item.setPrice(edtserviceprice.getText().toString());
                services.child(clinicid).child("services").child(key).setValue(item);

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
      services.child(clinicid).child("services").child(key).removeValue();
    }
}
