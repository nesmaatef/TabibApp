package com.example.tabibapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tabibapp.Model.hospitals;
import com.example.tabibapp.common.common;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

import info.hoang8f.widget.FButton;

import static android.view.View.VISIBLE;

public class hospital_profile extends AppCompatActivity {

    TextView txt_name_hos,txt_price_hos,addrees,times,desc_hos,hospital_date,price,times1;
    ImageView imgprofile,imgmore;
    FButton fbhospital;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference hospital , getHospital;
    String hospitalid ="";
    hospitals currenthospital;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;
    MaterialEditText edtname,edtdesc,hospital_price,wait, map_hos;
    Button btnselect,btnupload;
    hospitals current;
    String hospitalname ="" ;

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

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
        addrees= (TextView) findViewById(R.id.map);
        times= (TextView) findViewById(R.id.times);
        desc_hos= (TextView) findViewById(R.id.desc);
        hospital_date= (TextView) findViewById(R.id.hospitaldate);
        imgmore= (ImageView) findViewById(R.id.imgmore);
        times1= (TextView) findViewById(R.id.txt_time);
        imgprofile=(ImageView) findViewById(R.id.imgprofile);

        hospital_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(hospital_profile.this, hospital_services.class);
                go.putExtra("hospitalid", hospitalid);
                startActivity(go);  }});

        if (hospitalname.equals("0")){
            imgmore.setVisibility(VISIBLE); }


        imgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showupdatedialoghospital(); }});

        if (getIntent() !=null)
            hospitalid =getIntent().getStringExtra("hospitalid");

        hospitalname = getIntent().getStringExtra("hospital");


        if (hospitalname.equals("06") )
        { gethospitaldetails(hospitalid); }
         else if (common.currenthospital.equals("true")){
            hospitalid= common.currentuserphone;
            gethospitaldetails(hospitalid);
            Toast.makeText(this, ""+hospitalname, Toast.LENGTH_SHORT).show();
        }
    imgprofile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chooseimage();
        }
    });
    }
    private void showupdatedialoghospital() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(hospital_profile.this);
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.update_hospital_info, null);
        edtname=add_menu_layout.findViewById(R.id.edtname_hos);
        edtdesc=add_menu_layout.findViewById(R.id.edtdesc_hos);
        wait=add_menu_layout.findViewById(R.id.edttime_hos);
        map_hos=add_menu_layout.findViewById(R.id.edtmap_hos);

        hospital.child(hospitalid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current=dataSnapshot.getValue(hospitals.class);
                edtname.setText(current.getName());
                edtdesc.setText(current.getDesc());
                wait.setText(current.getTimes());
                map_hos.setText(current.getMap());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        alertdialog.setView(add_menu_layout);
        alertdialog.setPositiveButton("تم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //update information
                current.setName(edtname.getText().toString());
                current.setDesc(edtdesc.getText().toString());
                current.setMap(map_hos.getText().toString());
                current.setTimes(wait.getText().toString());
                hospital.child(hospitalid).setValue(current);
            }
        });
        alertdialog.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog.show(); }


    private void chooseimage() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }


    private void gethospitaldetails(final String hospitalid) {
        if (hospitalname.equals("0")){
            imgmore.setVisibility(VISIBLE); }


        hospital.child(hospitalid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currenthospital=dataSnapshot.getValue(hospitals.class);
                if (currenthospital.getImage().equals("default")){
                    imgprofile.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                Picasso.get().load(currenthospital.getImage()).into(imgprofile);}
                txt_name_hos.setText(currenthospital.getName());
                addrees.setText(currenthospital.getMap());
                times.setText(currenthospital.getTimes());
                desc_hos.setText(currenthospital.getDesc());
                common.currentdoctorphone=currenthospital.getPhone();
                common.currenthospital_name =currenthospital.getName();
                Toast.makeText(hospital_profile.this, "hi"+hospitalid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //try change image
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null){
            final  StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        getHospital = FirebaseDatabase.getInstance().getReference("hospital").child(hospitalid);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("image", ""+mUri);
                        getHospital.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(hospital_profile.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(hospital_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(hospital_profile.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(hospital_profile.this, "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }



}
