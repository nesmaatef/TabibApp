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

import com.example.tabibapp.Model.Hospital_services;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.service_hospital_viewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;

public class Services_hospital extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference services;
    RecyclerView recycler1;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Hospital_services, service_hospital_viewholder> adapter;
    String hospitalid ="";
    ImageView img;
    MaterialEditText edtroom;
    Hospital_services currentroom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_hospital);
        database=FirebaseDatabase.getInstance();
        services=database.getReference("Rooms");
        recycler1 =(RecyclerView) findViewById(R.id.recycler_service_hospital);
        img =(ImageView) findViewById(R.id.imgmore1);
        if (common.currentisstaff.equals("true")){
            img.setVisibility(View.INVISIBLE);
        }
img.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showdialog();
    }});
        recycler1.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler1.setLayoutManager(layoutManager);

      Intent intent = getIntent();
        hospitalid = intent.getStringExtra("hospitalid");
        load_hospital_services(hospitalid);
    }

    private void showdialog() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(Services_hospital.this);
        alertdialog.setTitle("اضافه غرفه");
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_room, null);
        edtroom=add_menu_layout.findViewById(R.id.edtroom);


        alertdialog.setView(add_menu_layout);
        alertdialog.setPositiveButton("تم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
currentroom =new Hospital_services();
currentroom.setName(edtroom.getText().toString());
currentroom.setHospitalid(hospitalid);

                services.push().setValue(currentroom);

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

    private void load_hospital_services(final String hospital) {
        adapter= new FirebaseRecyclerAdapter<Hospital_services, service_hospital_viewholder>(
                Hospital_services.class,
                R.layout.service_hospital_item,
                service_hospital_viewholder.class,
                services.orderByChild("hospitalid").equalTo(hospitalid)
        ) {

            @Override
            protected void populateViewHolder(service_hospital_viewholder clinicviewholder, final Hospital_services clinic_services, int i) {
                clinicviewholder.name.setText(clinic_services.getName());
                common.currenthospital_room =clinic_services.getName();
                clinicviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) throws ParseException {
                      Intent intent =new Intent(Services_hospital.this, appoint_hospital.class);
                        intent.putExtra("serviceid", adapter.getRef(position).getKey());
                        startActivity(intent);


                    }
                });
            }


        };
        recycler1.setAdapter(adapter);
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

    private void updatedate(final String key, final Hospital_services item) {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(Services_hospital.this);
        alertdialog.setTitle("تعديل");
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_room, null);
        edtroom=add_menu_layout.findViewById(R.id.edtroom);

        edtroom.setText(item.getName());

        alertdialog.setView(add_menu_layout);
        alertdialog.setPositiveButton("تم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setName(edtroom.getText().toString());

                services.child(key).setValue(item);

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
    private void deletefood(String key) {
        services.child(key).removeValue();

    }

}

