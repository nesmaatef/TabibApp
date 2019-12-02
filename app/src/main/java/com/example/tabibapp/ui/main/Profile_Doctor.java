package com.example.tabibapp.ui.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tabibapp.Model.clinics;
import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.Model.rating;
import com.example.tabibapp.R;
import com.example.tabibapp.clinicslist;
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

import static android.app.Activity.RESULT_OK;
import static android.view.View.VISIBLE;


public class Profile_Doctor extends Fragment implements RatingDialogListener {
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
    com.example.tabibapp.Model.doctor currentdoctor;
    FloatingActionButton fab;
    clinics newclinic;
    doctor newdoctor;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;

    @SuppressLint("RestrictedApi")


    private static final String TAG = "profile";

    private PageViewModel pageViewModel;

    public Profile_Doctor() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment Profile_Doctor.
     */
    public static Profile_Doctor newInstance() {
        return new Profile_Doctor();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.setIndex(TAG);



    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.speed_dial_fragment, container, false);

        //init firebase

        firebaseDatabase=FirebaseDatabase.getInstance();
        doctor =firebaseDatabase.getReference("doctor");
        rate =firebaseDatabase.getReference("Rating");
        cliniclist=firebaseDatabase.getReference("clinics");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();



        // init view

        txtdesc= root.findViewById(R.id.doc_desc);

        txtname=root.findViewById(R.id.doc_name);

        imgdoc=root.findViewById(R.id.img_doc);
        imgmore=root.findViewById(R.id.imgmore);

        goclinic=root.findViewById(R.id.goclinics);
        ratingBar=root.findViewById(R.id.ratingbar);
        fab =root.findViewById(R.id.fabrate);

        edtname =root.findViewById(R.id.edtname);
        edtdesc =root.findViewById(R.id.edtdesc);

        butselect =root.findViewById(R.id.btnselect);
        butupload =root.findViewById(R.id.btnupload);
        root1 =root.findViewById(R.id.root1);

        if (common.person.equals("false")){
            fab.setVisibility(VISIBLE);

        }




        goclinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getActivity(), clinicslist.class);
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

        Intent intent =new Intent();
        doctorid =intent.getStringExtra("DoctorId");
     //   doctorphone=intent.getStringExtra("doctorid1");

       doctorphone =common.doctorphone;

            getdoc_details(doctorphone);

        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
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
                .create(getActivity())
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
                Toast.makeText(getActivity(), "thank you for your feedinback", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void uploadimage() {
        final ProgressDialog mdialog = new ProgressDialog(getContext());
        mdialog.setMessage("Uploading");
        mdialog.show();

        String imagename = UUID.randomUUID().toString();
        final StorageReference imagefolder =storageReference.child("image/"+imagename);
        imagefolder.putFile(saveuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mdialog.dismiss();
                        Toast.makeText(getActivity(), "Uploaded!!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode== common.pick_image_request && resultCode==RESULT_OK
                && data !=null&& data.getData() !=null)
        {
            saveuri =data.getData();
            // butselect.setText("image selected !");

        }
    }
    private void changeimage(final doctor item) {
        final ProgressDialog mdialog = new ProgressDialog(getContext());
        mdialog.setMessage("Uploading");
        mdialog.show();

        String imagename = UUID.randomUUID().toString();
        final StorageReference imagefolder =storageReference.child("Image/"+imagename);
        imagefolder.putFile(saveuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mdialog.dismiss();
                        Toast.makeText(getActivity(), "Uploaded!!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(getActivity());
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