package com.example.tabibapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.Model.hospitals;
import com.example.tabibapp.Model.users;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.hospitalviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class hospital extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference hospital, hospital1,adduser;
    FirebaseRecyclerAdapter<hospitals, hospitalviewholder> adapter;
String hospitalid= "";
ImageView img;
MaterialEditText edtname,edtphone ;
    Button btnselect, btnupload;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;
    users newuser;
    hospitals newhospital;
    TextView text ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        //firebase
        database=FirebaseDatabase.getInstance();
        hospital=database.getReference("hospital");
        hospital1=database.getReference("hospital1");
        adduser =database.getReference("users");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        recyclerView=(RecyclerView) findViewById(R.id.recycler_hospital);
        img=(ImageView) findViewById(R.id.imgmore);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        //getintent
        if (getIntent()!=null)
            hospitalid=getIntent().getStringExtra("categoryid1");
        if (hospitalid.equals("06"))

            loadhospitallist(hospitalid);
        else if (hospitalid.equals("11")) {
            loadhospitallist1(hospitalid);
            common.current_item = "true"; }
        if (common.currentadmin.equals("true")) {
            img.setVisibility(View.VISIBLE);
        }


            img.setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hospitalid.equals("11"))
                        showdialog();
                    else if (hospitalid.equals("06"))
                        showdialog1(); }}); }

    private void showdialog1() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(hospital.this);
        alertdialog.setTitle("اضف جديده");
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_hospital, null);
        edtname=add_menu_layout.findViewById(R.id.edtname);
        edtphone=add_menu_layout.findViewById(R.id.edtphone);
        text=add_menu_layout.findViewById(R.id.txtcatid);
        btnselect=add_menu_layout.findViewById(R.id.btnselect);
        btnupload=add_menu_layout.findViewById(R.id.btnupload);
        text.setText(hospitalid);
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage();

            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadimage1();
            }
        });
        alertdialog.setView(add_menu_layout);
        alertdialog.setPositiveButton("تم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (newhospital !=null){
                    hospital.child(edtphone.getText().toString()).setValue(newhospital);
                    adduser.child(edtphone.getText().toString()).setValue(newuser);
                } }});
        alertdialog.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog.show();
    }
    private void loadhospitallist(String hospitalid) {
        adapter=new FirebaseRecyclerAdapter<hospitals,hospitalviewholder>(hospitals.class,
                R.layout.hospital_item,
                hospitalviewholder.class,
               hospital.orderByChild("catid").equalTo(hospitalid)) {
            @Override
            protected void populateViewHolder(final hospitalviewholder hospitalviewholder, hospitals hospitals, int i) {

                hospitalviewholder.txtname.setText(hospitals.getName());
                Picasso.get().load(hospitals.getImage()).into(hospitalviewholder.imghospital);


                hospitalviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if ( common.currentisstaff.equals("true")){
                        Intent gohospital = new Intent(hospital.this, hospital_profile.class);
                        gohospital.putExtra("hospitalid", adapter.getRef(position).getKey());
                        gohospital.putExtra("hospital","06");

                        startActivity(gohospital);}
                        else
                            Toast.makeText(hospital.this, ""+hospitalviewholder.txtname.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);


    }
    private void loadhospitallist1(String hospitalid) {
        adapter=new FirebaseRecyclerAdapter<hospitals,hospitalviewholder>(hospitals.class,
                R.layout.hospital_item,
                hospitalviewholder.class,
                hospital1.orderByChild("catid").equalTo(hospitalid)) {
            @Override
            protected void populateViewHolder(final hospitalviewholder hospitalviewholder, hospitals hospitals, int i) {

                hospitalviewholder.txtname.setText(hospitals.getName());
                Picasso.get().load(hospitals.getImage()).into(hospitalviewholder.imghospital);


                hospitalviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                //        if (common.currenthospital1.equals("true")){
                  //      Intent gohospital = new Intent(hospital.this, hospital1_profile.class);
                    //    gohospital.putExtra("hospitalid", adapter.getRef(position).getKey());
                      //  gohospital.putExtra("hospital", "11");
                        //startActivity(gohospital);}
                        //else
                            Toast.makeText(hospital.this, ""+hospitalviewholder.txtname.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);


    }
    private void showdialog() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(hospital.this);
        alertdialog.setTitle("اضف جديده");
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_hospital, null);
        edtname=add_menu_layout.findViewById(R.id.edtname);
       edtphone=add_menu_layout.findViewById(R.id.edtphone);
        text=add_menu_layout.findViewById(R.id.txtcatid);
        btnselect=add_menu_layout.findViewById(R.id.btnselect);
        btnupload=add_menu_layout.findViewById(R.id.btnupload);
        text.setText(hospitalid);
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage();

            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadimage();
            }
        });
        alertdialog.setView(add_menu_layout);
        alertdialog.setPositiveButton("تم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (newhospital !=null){
                    hospital1.child(edtphone.getText().toString()).setValue(newhospital);
                    adduser.child(edtphone.getText().toString()).setValue(newuser);
                } }});
        alertdialog.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog.show();
    }
    private void uploadimage() {
        final ProgressDialog mdialog = new ProgressDialog(this);
        mdialog.setMessage("Uploading");
        mdialog.show();
        String imagename = UUID.randomUUID().toString();
        final StorageReference imagefolder =storageReference.child("image/"+imagename);
        imagefolder.putFile(saveuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mdialog.dismiss();
                        Toast.makeText(hospital.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newhospital=new hospitals();
                                newuser=new users();
                                newhospital.setName(edtname.getText().toString());
                                newhospital.setImage(uri.toString());
                                newhospital.setCatid(hospitalid);
                                newhospital.setPhone(edtphone.getText().toString());
                                newuser.setIsstaff("false");
                                newuser.setIspatient("false");
                                newuser.setIsadmin("false");
                                newuser.setIshospital("false");
                                newuser.setIshospital1("true");}
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mdialog.dismiss();
                Toast.makeText(hospital.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress =(100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                mdialog.setMessage("Uploaded" +progress+"%");
            }
        });
    }
    private void chooseimage() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"), common.pick_image_request);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode== common.pick_image_request && resultCode==RESULT_OK
                && data !=null&& data.getData() !=null)
        {
            saveuri =data.getData();
            btnselect.setText("image selected !   ");

        }
    }
    private void uploadimage1() {
        final ProgressDialog mdialog = new ProgressDialog(this);
        mdialog.setMessage("Uploading");
        mdialog.show();
        String imagename = UUID.randomUUID().toString();
        final StorageReference imagefolder =storageReference.child("image/"+imagename);
        imagefolder.putFile(saveuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mdialog.dismiss();
                        Toast.makeText(hospital.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newhospital=new hospitals();
                                newuser=new users();
                                newhospital.setName(edtname.getText().toString());
                                newhospital.setImage(uri.toString());
                                newhospital.setCatid(hospitalid);
                                newhospital.setPhone(edtphone.getText().toString());
                                newuser.setIsstaff("false");
                                newuser.setIspatient("false");
                                newuser.setIsadmin("false");
                                newuser.setIshospital("true");
                                newuser.setIshospital1("false");}
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mdialog.dismiss();
                Toast.makeText(hospital.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress =(100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                mdialog.setMessage("Uploaded" +progress+"%");
            }
        });
    }



}
