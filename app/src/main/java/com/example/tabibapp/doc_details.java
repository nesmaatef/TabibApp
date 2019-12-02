package com.example.tabibapp;

import android.annotation.SuppressLint;
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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tabibapp.Model.clinics;
import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.Model.rating;
import com.example.tabibapp.common.common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

import info.hoang8f.widget.FButton;

import static android.view.View.VISIBLE;

public class doc_details extends AppCompatActivity implements RatingDialogListener {
TextView txtdesc,txtname,goclinic,txtcat;
ImageView imgdoc,imgmore;
RatingBar ratingBar;
Button btnselect,btnupload;
FButton button;
MaterialEditText edtname,edtdesc;
Button butselect,butupload;
FirebaseDatabase firebaseDatabase;
    DatabaseReference doctor;
    RelativeLayout root1;
    DatabaseReference rate,cliniclist;
    String doctorid ="";
    String doctorphone ="";
     doctor currentdoctor;
     FloatingActionButton fab;
     clinics newclinic;
     doctor newdoctor;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_details);

    //init firebase

        firebaseDatabase=FirebaseDatabase.getInstance();
        doctor =firebaseDatabase.getReference("doctor");
        rate =firebaseDatabase.getReference("Rating");
        cliniclist=firebaseDatabase.getReference("clinics");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();



        // init view

        txtdesc= (TextView) findViewById(R.id.doc_desc);

        txtname= (TextView) findViewById(R.id.doc_name);

        imgdoc=(ImageView) findViewById(R.id.img_doc);
        imgmore=(ImageView) findViewById(R.id.imgmore);

        goclinic=(TextView) findViewById(R.id.goclinics);
        ratingBar=(RatingBar) findViewById(R.id.ratingbar);
        fab =(FloatingActionButton) findViewById(R.id.fabrate);

        edtname =(MaterialEditText) findViewById(R.id.edtname);
        edtdesc =(MaterialEditText) findViewById(R.id.edtdesc);

        butselect =(Button) findViewById(R.id.btnselect);
        butupload =(Button) findViewById(R.id.btnupload);
        root1 =(RelativeLayout) findViewById(R.id.root1);

        if (common.person.equals("false")){
            fab.setVisibility(VISIBLE);
        }




        goclinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(doc_details.this, clinicslist.class);
                go.putExtra("DoctorId",doctorid);
                go.putExtra("doctorname",currentdoctor.getName());
                go.putExtra("doctorphone1",currentdoctor.getPhone());



                startActivity(go);

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialograte();
            }
        });

        imgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  showdialoge();
                showupdatedialogfood(doctorphone);

            }
        });

        Intent intent =getIntent();
        doctorid =intent.getStringExtra("DoctorId");
        doctorphone=intent.getStringExtra("doctorid1");

        if (doctorid==null) {

            getdoc_details(doctorphone);
        }
        else {
            getdoctor_details(doctorid);
            getratingfood(doctorid);
        }


    }

    private void getdoctor_details(String doctorid) {
     doctor.child(doctorid).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        currentdoctor=dataSnapshot.getValue(com.example.tabibapp.Model.doctor.class);

        Picasso.get().load(currentdoctor.getImage()).into(imgdoc);
        txtdesc.setText(currentdoctor.getDesc());
        txtname.setText(currentdoctor.getName());


              common.currentdoctorphone=currentdoctor.getPhone();
              common.person1="true";


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    }

    private void getdoc_details(String doctorphone) {
        imgmore.setVisibility(VISIBLE);


        doctor.child(doctorphone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentdoctor=dataSnapshot.getValue(com.example.tabibapp.Model.doctor.class);

               Picasso.get().load(currentdoctor.getImage()).into(imgdoc);
                txtdesc.setText(currentdoctor.getDesc());
                txtname.setText(currentdoctor.getName());
                common.currentdoctorphone=currentdoctor.getPhone();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getratingfood(String doctorid) {

        com.google.firebase.database.Query foodRating = rate.orderByChild("doctorid").equalTo(doctorid);

        foodRating.addValueEventListener(new ValueEventListener() {
            int count=0, sum=0 ;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    rating item =postSnapshot.getValue(rating.class);
                    sum+= Integer.parseInt(item.getRatevalue());
                    count++;
                }
                if (count!=0)
                {
                    float average =sum/count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showdialograte() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Sebmit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Quite Ok","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this doctor")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(doc_details.this)
                .show();


    }


    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NotNull final String comments) {
        final rating rating =new rating(common.currentuser.getPhone()
                ,doctorid, String.valueOf(value), comments);

        rate.child(common.currentuser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(common.currentuser.getPhone()).exists())
                {
                    rate.child(common.currentuser.getPhone()).removeValue();

                    rate.child(common.currentuser.getPhone()).setValue(rating);
                }
                else {
                    rate.child(common.currentuser.getPhone()).setValue(rating);

                }
                Toast.makeText(doc_details.this, "thank you for your feedinback", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                        Toast.makeText(doc_details.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newdoctor=new doctor();
                                newdoctor.setName(edtname.getText().toString());
                               newdoctor.setImage(uri.toString());
                                newdoctor.setDesc(edtdesc.getText().toString());



                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mdialog.dismiss();
                Toast.makeText(doc_details.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(doc_details.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(doc_details.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress =(100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                mdialog.setMessage("Uploaded" +progress+"%");
            }
        });
    }


    private void showupdatedialogfood( String item) {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(doc_details.this);
        alertdialog.setTitle("Update your info");
        alertdialog.setMessage("please fill full information");

        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.update_doctor, null);
        edtname=add_menu_layout.findViewById(R.id.edtname);
        edtdesc=add_menu_layout.findViewById(R.id.edtdesc);
        txtcat=add_menu_layout.findViewById(R.id.txtcatid);
// setdata

            doctor.child(doctorphone).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    newdoctor=dataSnapshot.getValue(com.example.tabibapp.Model.doctor.class);
                    edtdesc.setText(currentdoctor.getDesc());
                    edtname.setText(currentdoctor.getName());
                    txtcat.setText(currentdoctor.getCatid());
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
                changeimage(newdoctor);            }
        });

        alertdialog.setView(add_menu_layout);

        //setbutton
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //update information
                newdoctor.setName(edtname.getText().toString());
                newdoctor.setDesc(edtdesc.getText().toString());
                newdoctor.setCatid(txtcat.getText().toString());


                doctor.child(doctorphone).setValue(newdoctor);
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


}
