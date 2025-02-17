package com.example.tabibapp.ui.hospitalprofile;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabibapp.Model.hospitals;
import com.example.tabibapp.R;
import com.example.tabibapp.category_hospital;
import com.example.tabibapp.common.common;
import com.example.tabibapp.hospital_profile;
import com.example.tabibapp.hospital_services;
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

import static android.app.Activity.RESULT_OK;

public class HospitalProfileFragment extends Fragment {

    private HospitalProfileViewModel mViewModel;
    TextView txt_name_hos,txt_price_hos,addrees,times,desc_hos,hospital_date,price,times1;
    ImageView imgprofile,imgmore;
    FButton fbhospital;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference hospital,hospital1 ;
    String hospitalid ="";
    hospitals currenthospital;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;
    MaterialEditText edtname,edtdesc,hospital_price,wait, map_hos;
    Button btnselect,btnupload;
    hospitals current;
    String hospitalname ="" ;

    public static HospitalProfileFragment newInstance() {
        return new HospitalProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hospital_profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HospitalProfileViewModel.class);
        //init firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        hospital =firebaseDatabase.getReference("hospital");
        hospital1 =firebaseDatabase.getReference("hospital1");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        // init view
        txt_name_hos= (TextView) txt_name_hos.findViewById(R.id.txt_name_hos);
        addrees= (TextView) addrees.findViewById(R.id.map);
        times= (TextView) times.findViewById(R.id.times);
        desc_hos= (TextView) desc_hos.findViewById(R.id.desc);
        hospital_date= (TextView) hospital_date.findViewById(R.id.hospitaldate);
        imgmore= (ImageView) imgmore.findViewById(R.id.imgmore);
        times1= (TextView) times1.findViewById(R.id.txt_time);
        imgprofile=(ImageView) imgprofile.findViewById(R.id.imgprofile);

        hospital_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hospitalname.equals("06") ||hospitalname.equals("0") ){
                    Intent go = new Intent(getActivity(), hospital_services.class);
                    go.putExtra("hospitalid", hospitalid);
                    startActivity(go);
                    common.currenthospital= "true";}

                else if (hospitalname.equals("11")){
                    Intent go = new Intent(getActivity(), category_hospital.class);
                    go.putExtra("hospitalid", hospitalid);
                    startActivity(go);
                } }});
        imgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showupdatedialoghospital();
            }
        });

        Intent intent =new Intent();
            hospitalid =intent.getStringExtra("hospitalid");
        hospitalname = intent.getStringExtra("hospital");


        if (hospitalname.equals("06") )
        {
            gethospitaldetails(hospitalid);
        }
        else if (hospitalname.equals("11")){
            gethospitaldetails1(hospitalid);
        }
        else if (common.currenthospital.equals("true")){
            hospitalid= common.currentuserphone;
            gethospitaldetails(hospitalid);
        }
    }
    private void showupdatedialoghospital() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(getContext());
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.update_hospital_info, null);
        edtname=add_menu_layout.findViewById(R.id.edtname_hos);
        edtdesc=add_menu_layout.findViewById(R.id.edtdesc_hos);
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
        alertdialog.show(); }
    private void changeimage(final hospitals item) {
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
    private void gethospitaldetails(final String hospitalid) {
        hospital.child(hospitalid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currenthospital=dataSnapshot.getValue(hospitals.class);
                Picasso.get().load(currenthospital.getImage()).into(imgprofile);
                txt_name_hos.setText(currenthospital.getName());
                addrees.setText(currenthospital.getMap());
                times.setText(currenthospital.getTimes());
                desc_hos.setText(currenthospital.getDesc());
                common.currentdoctorphone=currenthospital.getPhone();
                common.currenthospital_name =currenthospital.getName();

                Toast.makeText(getActivity(), "hi"+hospitalid, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void gethospitaldetails1(String hospitalid) {
        hospital1.child(hospitalid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currenthospital=dataSnapshot.getValue(hospitals.class);
                Picasso.get().load(currenthospital.getImage()).into(imgprofile);
                txt_name_hos.setText(currenthospital.getName());
                // txt_price_hos.setText(currenthospital.getPrice());

                addrees.setText(currenthospital.getMap());
                // times.setText(currenthospital.getTimes());
                desc_hos.setText(currenthospital.getDesc());
                common.currentdoctorphone=currenthospital.getPhone();

                txt_price_hos.setVisibility(View.INVISIBLE);
                times.setVisibility(View.INVISIBLE);
                price.setVisibility(View.INVISIBLE);
                times1.setVisibility(View.INVISIBLE);
                common.currenthospital_name =currenthospital.getName();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }}); }

}
