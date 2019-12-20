package com.example.tabibapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tabibapp.Model.clinics;
import com.example.tabibapp.common.common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

import static android.view.View.VISIBLE;

public class clinicprofile extends AppCompatActivity {

    TextView txtname,txtmap,txtprice,txtwait,button,txtphone;
    ImageView imgclinic,imgmore;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference clinicdata;
    String clinicid ="";
    clinics currentclinic;
    String doctormane="";
    String doctorphone="";
    Button btnselect,btnupload;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;
    ListView listView;
    ArrayList<String> arrayList =new ArrayList<>();
  ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinicprofile);

        //init firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        clinicdata =firebaseDatabase.getReference("clinics");


        txtname= (TextView) findViewById(R.id.clinic_name);
        txtmap= (TextView) findViewById(R.id.clinic_map);
        txtprice= (TextView) findViewById(R.id.clinic_price);
        txtwait= (TextView) findViewById(R.id.clinic_times);
        imgclinic= (ImageView) findViewById(R.id.img_clinic);
        imgmore= (ImageView) findViewById(R.id.imgmore);

        button= (TextView) findViewById(R.id.goappoints);

        Intent intent=getIntent();
        doctormane =getIntent().getStringExtra("docname");
        doctorphone =getIntent().getStringExtra("Doctorphone");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        TextView txtday = (TextView) findViewById(R.id.txtday);



        if (getIntent() !=null)
            clinicid =getIntent().getStringExtra("clinicid");

        if (!clinicid.isEmpty())
        {
            loadclinic(clinicid);
        }

        if (doctorphone!=null){
            loadclinic1(clinicid);

        }

        if (common.person.equals("true")){
            imgmore.setVisibility(VISIBLE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent goappoint = new Intent(clinicprofile.this, clinic_services.class);
                 goappoint.putExtra("clinicid", clinicid);
                 goappoint.putExtra("namedoctor", currentclinic.getMap());
                 goappoint.putExtra("clinicprice", currentclinic.getPrice());


                startActivity(goappoint);
                ;

            } });


        imgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showupdatedialogclinic(doctorphone);


            }
        });
    }

    private void showupdatedialogclinic(String doctorphone) {

        AlertDialog.Builder alertdialog= new AlertDialog.Builder(clinicprofile.this);
        alertdialog.setTitle("Update your info");
        alertdialog.setMessage("please fill full information");

        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.update_clinic, null);
        txtname=add_menu_layout.findViewById(R.id.edtname);
        txtmap=add_menu_layout.findViewById(R.id.edtmap);
        txtprice=add_menu_layout.findViewById(R.id.edtprice);
        txtwait=add_menu_layout.findViewById(R.id.edttimes);
        txtphone=add_menu_layout.findViewById(R.id.edtphone1);



// setdata

        clinicdata.child(clinicid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentclinic=dataSnapshot.getValue(clinics.class);
                txtname.setText(currentclinic.getName());
                txtmap.setText(currentclinic.getMap());
                txtprice.setText(currentclinic.getPrice());
                txtwait.setText(currentclinic.getTimeswait());

                txtphone.setText(currentclinic.getDocid());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });





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
                changeimage(currentclinic);            }
        });

        alertdialog.setView(add_menu_layout);

        //setbutton
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //update information
                currentclinic.setName(txtname.getText().toString());
                currentclinic.setMap(txtmap.getText().toString());
                currentclinic.setPrice(txtprice.getText().toString());
                currentclinic.setTimeswait(txtwait.getText().toString());
                currentclinic.setDocid(txtphone.getText().toString());



                clinicdata.child(clinicid).setValue(currentclinic);
                // Snackbar.make(rootlayout, " Food" +item.getName()+ "was updated",Snackbar.LENGTH_SHORT).show();

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

    private void loadclinic(String clinicid) {
        clinicdata.child(clinicid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentclinic=dataSnapshot.getValue(clinics.class);
                Picasso.get().load(currentclinic.getImage()).into(imgclinic);
                txtname.setText(currentclinic.getName());
                txtprice.setText(currentclinic.getPrice());
                txtmap.setText(currentclinic.getMap());
                txtwait.setText(currentclinic.getTimeswait());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void loadclinic1(String clinicid) {

        clinicdata.child(clinicid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentclinic=dataSnapshot.getValue(clinics.class);
                Picasso.get().load(currentclinic.getImage()).into(imgclinic);
                txtname.setText(currentclinic.getName());
                txtprice.setText(currentclinic.getPrice());
                txtmap.setText(currentclinic.getMap());
                txtwait.setText(currentclinic.getTimeswait());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void changeimage(final clinics item) {
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
                        Toast.makeText(clinicprofile.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(clinicprofile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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
            // butselect.setText("image selected !");

        }
    }


}
