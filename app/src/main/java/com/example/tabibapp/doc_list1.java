package com.example.tabibapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.category;
import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.doctorviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class doc_list1 extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference doctorlist;
    String categoryid="";
    RelativeLayout rootlayout;

    MaterialEditText edtname, edtdesc, edtprice, edtmap, edttimes, edttimeswait;
    Button btnselect, btnupload;

    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;

    doctor newdoctor;


    FirebaseRecyclerAdapter<doctor, doctorviewholder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);

        //firebase
        database=FirebaseDatabase.getInstance();
        doctorlist=database.getReference("doctor");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        rootlayout=(RelativeLayout) findViewById(R.id.rootlayout);

        recyclerView=(RecyclerView) findViewById(R.id.recycler_doc);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //getintent
        if (getIntent()!=null)
            categoryid=getIntent().getStringExtra("categoryid");
        if (!categoryid.isEmpty() && categoryid!=null) {
            loaddoctorlist(categoryid);
        }
    }

    private void loaddoctorlist(String categoryid) {

        adapter=new FirebaseRecyclerAdapter<doctor, doctorviewholder>(doctor.class,
                R.layout.doc_item,
                doctorviewholder.class,
                doctorlist.orderByChild("catid").equalTo(categoryid)) {
            @Override
            protected void populateViewHolder(doctorviewholder viewHolder, doctor model, int position) {
                Picasso.get().load(model.getImage()).into(viewHolder.imgdoc);
                viewHolder.txtname.setText(model.getName());
                viewHolder.txtprice.setText(model.getPrice());
                viewHolder.txtmap.setText(model.getMap());
                viewHolder.txtdesc.setText(model.getDesc());
                final doctor clickitem =model;

                viewHolder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // Toast.makeText(doc_list.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                        Intent docdetails = new Intent(doc_list1.this, doc_details.class);

                        docdetails.putExtra("DoctorId", adapter.getRef(position).getKey());
                        startActivity(docdetails);

                    }
                });


            }
        };
        recyclerView.setAdapter(adapter);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() ==R.id.more){
            Intent homeintent = new Intent(doc_list1.this, more.class);
            startActivity(homeintent);
            Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();}
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more, menu);
        return true;

    }

}
