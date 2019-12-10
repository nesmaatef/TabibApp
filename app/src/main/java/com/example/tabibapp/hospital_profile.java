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

import com.example.tabibapp.Model.hospitals;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import info.hoang8f.widget.FButton;

import static android.view.View.VISIBLE;

public class hospital_profile extends AppCompatActivity {

    TextView txt_name_hos,txt_price_hos,addrees,times,desc_hos,hospital_date;
    ImageView imgprofile,imgmore;
    FButton fbhospital;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference hospital;
    String hospitalid ="";
    hospitals currenthospital;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;
    MaterialEditText edtname,edtdesc,hospital_price,wait, map_hos;
    Button btnselect,btnupload;
    hospitals current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_profile);

        //init firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        hospital =firebaseDatabase.getReference("hospital");

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        // init view
        txt_name_hos= (TextView) findViewById(R.id.txt_name_hos);
        txt_price_hos= (TextView) findViewById(R.id.price);
        addrees= (TextView) findViewById(R.id.map);

        times= (TextView) findViewById(R.id.times);
        desc_hos= (TextView) findViewById(R.id.desc);
        hospital_date= (TextView) findViewById(R.id.hospitaldate);
        imgmore= (ImageView) findViewById(R.id.imgmore);



        imgprofile=(ImageView) findViewById(R.id.imgprofile);

        hospital_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent go = new Intent(hospital_profile.this, appointment.class);
                Intent go = new Intent(hospital_profile.this, Services_hospital.class);

                go.putExtra("hospitalid", hospitalid);
                go.putExtra("hospitalname", currenthospital.getName());
                go.putExtra("hospitalprice", currenthospital.getPrice());
                startActivity(go);
                common.currenthospital= "true";


            }
        });

        if (common.currenthospital.equals("true")){
            imgmore.setVisibility(VISIBLE);
        }

        imgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showupdatedialoghospital();


            }
        });


        if (getIntent() !=null)
            hospitalid =getIntent().getStringExtra("hospitalid");

        if (!hospitalid.isEmpty())
        {
            gethospitaldetails(hospitalid);
        }

    }

    private void showupdatedialoghospital() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(hospital_profile.this);
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.update_hospital_info, null);
        edtname=add_menu_layout.findViewById(R.id.edtname_hos);
        edtdesc=add_menu_layout.findViewById(R.id.edtdesc_hos);
        hospital_price=add_menu_layout.findViewById(R.id.edtprice_hos);
        wait=add_menu_layout.findViewById(R.id.edttime_hos);
        map_hos=add_menu_layout.findViewById(R.id.edtmap_hos);

        btnselect=add_menu_layout.findViewById(R.id.btnselect);
        btnupload=add_menu_layout.findViewById(R.id.btnupload);





        hospital.child(hospitalid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current=dataSnapshot.getValue(hospitals.class);
                edtname.setText(current.getName());
                edtdesc.setText(current.getDesc());
                hospital_price.setText(current.getPrice());
                wait.setText(current.getTimes());
                map_hos.setText(current.getMap());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage();

            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeimage(current);            }
        });
        alertdialog.setView(add_menu_layout);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //update information
                current.setName(edtname.getText().toString());
                current.setDesc(edtdesc.getText().toString());
                current.setMap(map_hos.getText().toString());
                current.setPrice(hospital_price.getText().toString());
                current.setTimes(wait.getText().toString());

                hospital.child(hospitalid).setValue(current);

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
    private void changeimage(final hospitals item) {
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
                        Toast.makeText(hospital_profile.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(hospital_profile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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


    private void gethospitaldetails(String hospitalid) {
        hospital.child(hospitalid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currenthospital=dataSnapshot.getValue(hospitals.class);
                Picasso.get().load(currenthospital.getImage()).into(imgprofile);
                txt_name_hos.setText(currenthospital.getName());
                txt_price_hos.setText(currenthospital.getPrice());
                addrees.setText(currenthospital.getMap());
                times.setText(currenthospital.getTimes());
                desc_hos.setText(currenthospital.getDesc());
                common.currentdoctorphone=currenthospital.getPhone();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
