package com.example.tabibapp.ui.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import com.example.tabibapp.home;
import com.example.tabibapp.hospital1_profile;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import info.hoang8f.widget.FButton;

import static android.app.Activity.RESULT_OK;
import static android.view.View.VISIBLE;


public class Profile_Doctor extends Fragment {
    TextView txtdesc,txtname,goclinic,txtcat;
    ImageView imgdoc,imgmore;
    RatingBar ratingBar;
    Button btnselect,btnupload;
    FButton button;
    MaterialEditText edtname,edtdesc;
    Button butselect,butupload;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference doctor, getDoctor;
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
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;


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
        fab =root.findViewById(R.id.floating_action_button);

        edtname =root.findViewById(R.id.edtname);
        edtdesc =root.findViewById(R.id.edtdesc);

        butselect =root.findViewById(R.id.btnselect);
        butupload =root.findViewById(R.id.btnupload);
        root1 =root.findViewById(R.id.root1);

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
               Intent fab =new Intent(getActivity(), home.class);
               startActivity(fab);
            }
        });

        imgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showupdatedialogfood(doctorphone);
            }});

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

        imgdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });
        return root;
    }
    private void getdoc_details(String doctorphone) {
        doctor.child(doctorphone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentdoctor=dataSnapshot.getValue(com.example.tabibapp.Model.doctor.class);
                if (currentdoctor.getImage().equals("default")){
                    imgdoc.setImageResource(R.mipmap.ic_launcher);
                }
                else {
                Picasso.get().load(currentdoctor.getImage()).into(imgdoc);}
                txtdesc.setText(currentdoctor.getDesc());
                txtname.setText(currentdoctor.getName());
                common.currentdoctorphone=currentdoctor.getPhone();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }}); }
    private void chooseimage() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
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

        alertdialog.setView(add_menu_layout);

        //setbutton
        alertdialog.setPositiveButton("تم", new DialogInterface.OnClickListener() {
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
        alertdialog.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog.show(); }

    //try change image
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
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

                        getDoctor = FirebaseDatabase.getInstance().getReference("doctor").child(doctorphone);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("image", ""+mUri);
                        getDoctor.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}