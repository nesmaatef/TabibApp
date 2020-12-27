package com.example.tabibapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.clinics;
import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.Model.users;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.doctorviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class doctor_list_hospital extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MaterialEditText edtname, edtdesc, edtprice;
    Button btnselect, btnupload;
    FirebaseDatabase database;
    DatabaseReference doctorlist;
    String categoryid="";
    RelativeLayout rootlayout;
ImageView img;


    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;
    doctor newdoctor;
    FirebaseRecyclerAdapter<doctor, doctorviewholder> adapter;
    MaterialSearchBar materialSearchBar;
    List<String> suggestList =new ArrayList<>();
    FirebaseRecyclerAdapter<doctor, doctorviewholder> searchadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list_hospital);

        database=FirebaseDatabase.getInstance();
        doctorlist=database.getReference("doctor_hospital");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        rootlayout=(RelativeLayout) findViewById(R.id.rootlayout);
        img=(ImageView) findViewById(R.id.imgmore1);
        recyclerView=(RecyclerView) findViewById(R.id.recycler_doc);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (common.currentisstaff.equals("true")){
            img.setVisibility(View.INVISIBLE);
        }
        if (getIntent()!=null){
            categoryid=getIntent().getStringExtra("DoctorId");
            loaddoctorlist1(categoryid);}

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });
    }
    private void showdialog() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(doctor_list_hospital.this);
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_doctor_hospital, null);
        edtname=add_menu_layout.findViewById(R.id.edtname);
        edtdesc=add_menu_layout.findViewById(R.id.edtdesc);
        edtprice=add_menu_layout.findViewById(R.id.edtprice);
        alertdialog.setView(add_menu_layout);
        alertdialog.setIcon(R.drawable.ic_add_to_photos_black_24dp);
        //setbutton
        alertdialog.setPositiveButton("تم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newdoctor=new doctor();
                newdoctor.setName(edtname.getText().toString());
                newdoctor.setDesc(edtdesc.getText().toString());
                newdoctor.setImage("default");
                newdoctor.setPrice(edtprice.getText().toString());
                newdoctor.setCatid(categoryid);

                if (newdoctor !=null)
                {
                    doctorlist.push().setValue(newdoctor);
                    Snackbar.make(rootlayout, "New category" +newdoctor.getName()+ "was added",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        alertdialog.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog.show();
    }

    private void loaddoctorlist1(String categoryid) {
        adapter=new FirebaseRecyclerAdapter<doctor, doctorviewholder>(doctor.class,
                R.layout.doc_item,
                doctorviewholder.class,
                doctorlist.orderByChild("catid").equalTo(categoryid)) {
            @Override
            protected void populateViewHolder(final doctorviewholder viewHolder, final doctor model, int position) {
               if (model.getImage().equals("default")){
                   viewHolder.imgdoc.setImageResource(R.mipmap.ic_launcher);}
               else {
                Picasso.get().load(model.getImage()).into(viewHolder.imgdoc);}
                viewHolder.txtname.setText(model.getName());
                viewHolder.txtdesc.setText(model.getDesc());
                viewHolder.txtprice.setText(model.getPrice());

                final doctor clickitem =model;

                common.currenthospital_service =model.getName();

                viewHolder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        common.currenthospital_service =model.getName();
                        common.currenthospital_service_price=model.getPrice();

                        // Toast.makeText(doc_list_admin.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                        Intent docdetails = new Intent(doctor_list_hospital.this, appointment_hospital1.class);

                        docdetails.putExtra("DoctorId", adapter.getRef(position).getKey());
                        startActivity(docdetails);


                    }
                });


            }


        };
        recyclerView.setAdapter(adapter);




    }
    private void chooseimage() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"), common.pick_image_request);
    }
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(common.UPDATE)){
            updatedate(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        if (item.getTitle().equals(common.DELETE)){
            deletefood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void updatedate(final String key, final doctor item) {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(doctor_list_hospital.this);
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.doctor_item_hospital, null);
        edtname=add_menu_layout.findViewById(R.id.edtname);
        edtdesc=add_menu_layout.findViewById(R.id.edtdesc);
        edtprice=add_menu_layout.findViewById(R.id.edtprice);

        edtname.setText(item.getName());
        edtdesc.setText(item.getDesc());
        edtprice.setText(item.getPrice());

        btnselect=add_menu_layout.findViewById(R.id.btnselect);
        btnupload=add_menu_layout.findViewById(R.id.btnupload);
        //event for button
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage();
            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeimage(item);
            }
        });
        alertdialog.setView(add_menu_layout);
        alertdialog.setIcon(R.drawable.ic_add_to_photos_black_24dp);
        //setbutton
        alertdialog.setPositiveButton("تعديل", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.setName(edtname.getText().toString());
                item.setDesc(edtdesc.getText().toString());
                item.setPrice(edtprice.getText().toString());

                    doctorlist.child(key).setValue(item);
                    Snackbar.make(rootlayout, "تم التعديل",Snackbar.LENGTH_SHORT).show();

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
        doctorlist.child(key).removeValue();
    }
    private void changeimage(final doctor item) {
        final ProgressDialog mdialog = new ProgressDialog(this);
        mdialog.setMessage("Uploading");
        mdialog.show();

        String imagename = UUID.randomUUID().toString();
        final StorageReference imagefolder =storageReference.child("Image/"+imagename);
        imagefolder.putFile(saveuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mdialog.dismiss();
                        Toast.makeText(doctor_list_hospital.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                item.setImage(uri.toString());
                                // newcategory=new Category(edtname.getText().toString(),uri.toString());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mdialog.dismiss();
                Toast.makeText(doctor_list_hospital.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress =(100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                mdialog.setMessage("Uploaded" +progress+"%");
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode== common.pick_image_request && resultCode==RESULT_OK
                && data !=null&& data.getData() !=null)
        {
            saveuri =data.getData();

        }
    }


}
