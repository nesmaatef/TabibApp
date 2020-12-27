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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.category;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.menuviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class category_hospital extends AppCompatActivity {
    FirebaseDatabase database ;
    DatabaseReference category ;
    RecyclerView recyclercat ;
    RecyclerView.LayoutManager layoutmanager ;
    FirebaseRecyclerAdapter<com.example.tabibapp.Model.category, menuviewholder> adapter;
    String hospitalid;
    ImageView img;
    MaterialEditText text ;
    com.example.tabibapp.Model.category newcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_hospital);

        database =FirebaseDatabase.getInstance();
        category =database.getReference("category_hospital");

        recyclercat =(RecyclerView) findViewById(R.id.recycler_cat);
        img =(ImageView) findViewById(R.id.imgadmin);


      if (common.currentisstaff.equals("true")){
       img.setVisibility(View.INVISIBLE);
}
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });

        recyclercat.setLayoutManager(new GridLayoutManager(this,2));
        //if (getIntent() !=null)
            // hospitalid =getIntent().getStringExtra("hospitalid");
        hospitalid =common.currentuserphone;
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
                common.currenthospital_room =model.getName();
                final com.example.tabibapp.Model.category clickitem =model;
                viewHolder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                           Intent docdetails = new Intent(category_hospital.this, doctor_list_hospital.class);
                          docdetails.putExtra("DoctorId", adapter.getRef(position).getKey());
                          startActivity(docdetails);
                    }
                }); }};
        recyclercat.setAdapter(adapter);
    }
    private void showdialog(){
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(category_hospital.this);
        alertdialog.setTitle("اضف قسم للمستشفي");
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_category, null);
        text=add_menu_layout.findViewById(R.id.edtname);

        alertdialog.setView(add_menu_layout);
        alertdialog.setPositiveButton("اضافه", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                newcat =new  com.example.tabibapp.Model.category();
                newcat.setName(text.getText().toString());
                newcat.setHospital_id(hospitalid);

                category.push().setValue(newcat);

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

    private void updatedate(final String key, final com.example.tabibapp.Model.category item) {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(category_hospital.this);
        alertdialog.setTitle("تعديل قسم");
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_category, null);
        text=add_menu_layout.findViewById(R.id.edtname);
        alertdialog.setView(add_menu_layout);
        text.setText(item.getName());
        alertdialog.setPositiveButton("تعديل", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
               item.setName(text.getText().toString());

                category.child(key).setValue(item);

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
        category.child(key).removeValue();
    }
}
