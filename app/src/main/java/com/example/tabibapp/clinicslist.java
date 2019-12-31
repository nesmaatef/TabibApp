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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.clinics;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.clinicviewholder;
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

import java.util.UUID;

import static android.view.View.VISIBLE;

public class clinicslist extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference cliniclist;
    String doctorid="";
     String docphone="";
    String docphone1="";
    FirebaseRecyclerAdapter<clinics, clinicviewholder> adapter;
    String doctorname="";
    ImageView imgmore;
    clinics newclinic;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;
    MaterialEditText edtname,edtmap,edtprice,edttimes;
    Button butselect,butupload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinicslist);
        //firebase
        database=FirebaseDatabase.getInstance();
        cliniclist=database.getReference("clinics");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        recyclerView=(RecyclerView) findViewById(R.id.recycler_clinic);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        imgmore=(ImageView) findViewById(R.id.imgmore);


        Intent intent=getIntent();
        doctorname = intent.getStringExtra("doctorname");


        if (common.person.equals("false")){
            docphone=intent.getStringExtra("doctorphone1") ;
        loadcliniclist1(docphone);
            Toast.makeText(this, "hi"+docphone, Toast.LENGTH_SHORT).show();}

        else  if (common.person.equals("true")){
            docphone1=intent.getStringExtra("doctorphone1") ;
            loadcliniclist(docphone1);
        }

        imgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialoge();
            }
        });

    }

    private void loadcliniclist(final String docphone1) {

        imgmore.setVisibility(VISIBLE);

        adapter=new FirebaseRecyclerAdapter<clinics, clinicviewholder>(clinics.class,
                R.layout.clinic_item,
                clinicviewholder.class,
                cliniclist.orderByChild("docid").equalTo(docphone1)) {
            @Override
            protected void populateViewHolder(clinicviewholder clinicviewholder, clinics clinics, int i) {

                clinicviewholder.txtmap.setText(clinics.getMap());

                clinicviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                      //  Toast.makeText(clinicslist.this, "clinic here", Toast.LENGTH_SHORT).show();
                        Intent goclinic = new Intent(clinicslist.this, clinicprofile.class);
                        goclinic.putExtra("clinicid", adapter.getRef(position).getKey());
                        goclinic.putExtra("docname", doctorid);
                        startActivity(goclinic);

                    }
                });

            }
        };
    recyclerView.setAdapter(adapter);
    }
    private void loadcliniclist1(final String docphone) {

        adapter=new FirebaseRecyclerAdapter<clinics, clinicviewholder>(clinics.class,
                R.layout.clinic_item,
                clinicviewholder.class,
                cliniclist.orderByChild("docid").equalTo(docphone)

        ) {
            @Override
            protected void populateViewHolder(clinicviewholder clinicviewholder, clinics clinics, int i) {

               clinicviewholder.txtmap.setText(clinics.getMap());



                clinicviewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //  Toast.makeText(clinicslist.this, "clinic here", Toast.LENGTH_SHORT).show();
                        Intent goclinic = new Intent(clinicslist.this, clinicprofile.class);
                        goclinic.putExtra("clinicid", adapter.getRef(position).getKey());
                        goclinic.putExtra("docname", doctorname);
                        goclinic.putExtra("Doctorphone", docphone);

                        startActivity(goclinic);

                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }
//add new clinic
    private void showdialoge() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(clinicslist.this);
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_clinic, null);
        edtname=add_menu_layout.findViewById(R.id.edtname);
        edtmap=add_menu_layout.findViewById(R.id.edtmap);
        edtprice=add_menu_layout.findViewById(R.id.edtprice);
        edttimes=add_menu_layout.findViewById(R.id.edttimes);


        butselect=add_menu_layout.findViewById(R.id.btnselect);
        butupload=add_menu_layout.findViewById(R.id.btnupload);

        //event for button
        butselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage();

            }
        });

        butupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadimage();
            }
        });

        alertdialog.setView(add_menu_layout);
        alertdialog.setIcon(R.drawable.ic_add_to_photos_black_24dp);

        //setbutton
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if (newclinic !=null)
                {
                    //     doctorlist.push().setValue(newdoctor);
                    cliniclist.push().setValue(newclinic);
                    //  adduser.child(edtphone.getText().toString()).setValue(newuser);
                    //doctorlist.child(edtphone.getText().toString()).setValue(common.currentuser.toString());

                    //  Snackbar.make(root1, "New category" +newclinic.getName()+ "was added",Snackbar.LENGTH_SHORT).show();
                }

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
                        Toast.makeText(clinicslist.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newclinic=new clinics();
                                newclinic.setName(edtname.getText().toString());
                                newclinic.setMap(edtmap.getText().toString());
                                newclinic.setImage(uri.toString());
                                newclinic.setPrice(edtprice.getText().toString());
                                newclinic.setTimeswait(edttimes.getText().toString());
                                newclinic.setDocid(common.currentdoctorphone);



                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mdialog.dismiss();
                Toast.makeText(clinicslist.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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
            butselect.setText("image selected !   ");

        }
    }


}
