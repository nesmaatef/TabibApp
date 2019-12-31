package com.example.tabibapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.Model.users;
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

public class doc_list_admin extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference doctorlist;
    DatabaseReference adduser;

    String categoryid="";
    RelativeLayout rootlayout;

    MaterialEditText edtname, edtdesc, edtphone, edtmap, edttimes, edttimeswait;
    Button btnselect, btnupload;

    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView imgadmin,fav;
    Uri saveuri;
    Context context;
    String user="";
    doctor newdoctor;
    users newuser;
    FirebaseRecyclerAdapter<doctor, doctorviewholder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);



        //firebase
        database=FirebaseDatabase.getInstance();
        doctorlist=database.getReference("doctor");
        adduser =database.getReference("users");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
rootlayout=(RelativeLayout) findViewById(R.id.rootlayout);
imgadmin=(ImageView) findViewById(R.id.imgadmin);
        recyclerView=(RecyclerView) findViewById(R.id.recycler_doc);

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
         user =common.currentuser.toString();
        //getintent
        if (getIntent()!=null)
            categoryid=getIntent().getStringExtra("categoryid");
        if (!categoryid.isEmpty() && categoryid!=null) {
            loaddoctorlist(categoryid);
        }

        if (common.currenthospital1.equals("true") || common.currenthospital.equals("true")){
            imgadmin.setVisibility(View.INVISIBLE);
        }
        imgadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialoge();
            }
        });
    }
    private void loaddoctorlist(String categoryid) {
adapter=new FirebaseRecyclerAdapter<doctor, doctorviewholder>(doctor.class,
        R.layout.doc_item,
        doctorviewholder.class,
        doctorlist.orderByChild("catid").equalTo(categoryid)) {
    @Override
    protected void populateViewHolder(final doctorviewholder viewHolder, final doctor model, final int position) {
        Picasso.get().load(model.getImage()).into(viewHolder.imgdoc);

    viewHolder.txtname.setText(model.getName());
        viewHolder.txtdesc.setText(model.getDesc());
        final doctor clickitem =model;
        viewHolder.setItemClickListener(new itemclicklistner() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Toast.makeText(doc_list_admin.this, ""+model.getName(), Toast.LENGTH_SHORT).show();
            }
        }); }};
        recyclerView.setAdapter(adapter); }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId() ==R.id.action_settings) {
           showdialoge();}
       else if (item.getItemId()==R.id.action_refresh){
           loaddoctorlist(categoryid);
       }
           return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_doctor, menu);
        return true;

    }
    ///upload new doctor /delete/ update
    private void showdialoge() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(doc_list_admin.this);
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_doc, null);
        edtname=add_menu_layout.findViewById(R.id.edtname);
        edtdesc=add_menu_layout.findViewById(R.id.edtdesc);
        edtphone=add_menu_layout.findViewById(R.id.edtphone);

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
                uploadimage();
            }
        });

        alertdialog.setView(add_menu_layout);
        alertdialog.setIcon(R.drawable.ic_add_to_photos_black_24dp);

        //setbutton
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (newdoctor !=null)
                {
                   doctorlist.child(edtphone.getText().toString()).setValue(newdoctor);
                    adduser.child(edtphone.getText().toString()).setValue(newuser);

                    Snackbar.make(rootlayout, "New category" +newdoctor.getName()+ "was added",Snackbar.LENGTH_SHORT).show();
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
                        Toast.makeText(doc_list_admin.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                 newdoctor=new doctor();
                 newuser=new users();
                               newdoctor.setName(edtname.getText().toString());
                                newdoctor.setDesc(edtdesc.getText().toString());
                                newdoctor.setImage(uri.toString());
                                newdoctor.setCatid(categoryid);
                                newdoctor.setPhone(edtphone.getText().toString());
                                newuser.setIsstaff("true");
                                newuser.setIspatient("false");
                                newuser.setIsadmin("false");
                                newuser.setIshospital("false");
                                newuser.setIshospital1("false");}
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mdialog.dismiss();
                Toast.makeText(doc_list_admin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode== common.pick_image_request && resultCode==RESULT_OK
                && data !=null&& data.getData() !=null)
        {
            saveuri =data.getData();
            btnselect.setText("image selected !   ");

        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(common.DELETE)){

            deletefood(adapter.getRef(item.getOrder()).getKey());

        }

        return super.onContextItemSelected(item);
    }
    private void deletefood(String key) {
        doctorlist.child(key).removeValue();

    }







}
