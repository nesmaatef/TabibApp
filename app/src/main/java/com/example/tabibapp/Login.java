package com.example.tabibapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.example.tabibapp.Model.doctor_requests;
import com.example.tabibapp.Model.users;
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

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class Login extends AppCompatActivity {
    EditText edtphone;
    FButton btnlogin;
    TextView txtlogin;
    FirebaseDatabase database;
    DatabaseReference table_user,doctorlist;
    MaterialEditText ephone,ecompany,ecity,earea;
    ImageView image;
    Uri saveuri;
    FirebaseStorage storage;
    StorageReference storageReference;

    doctor_requests newdoctor;


    MaterialEditText edtname, edtdesc, edtphone1, edtmap, edttimes, edttimeswait;
    Button btnselect, btnupload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtphone= (EditText) findViewById(R.id.edtphone);
        btnlogin = (FButton) findViewById(R.id.btnlogin);
        txtlogin= (TextView) findViewById(R.id.txtlogin);
        image= (ImageView) findViewById(R.id.imgmore);


        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        database =FirebaseDatabase.getInstance();
        table_user = database.getReference("users");
        doctorlist=database.getReference("DoctorRequests");


        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent intent =new Intent(Login.this, signup.class);
             //   startActivity(intent);

               showdialogbox();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop_up_menu();
            }
        });






        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mdialog = new ProgressDialog(Login.this);
                mdialog.setMessage("please waiting.....");
                mdialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(edtphone.getText().toString()).exists()) {
                            mdialog.dismiss();
                            users user = dataSnapshot.child(edtphone.getText().toString()).getValue(users.class);
                            user.setPhone(edtphone.getText().toString());  //set phone

                            if (Boolean.parseBoolean(user.getIsadmin())) {
                                Intent homeintent = new Intent(Login.this, home.class);
                                homeintent.putExtra("true", "false");
                                common.currentadmin = "true";
                                common.currentuser = user;
                                startActivity(homeintent);
                                finish();
                                Toast.makeText(Login.this, "Hello Admin", Toast.LENGTH_SHORT).show();
                            } else if (Boolean.parseBoolean(user.getIsstaff())) {

                                Intent docintent = new Intent(Login.this, Main2Activity.class);
                               // docintent.putExtra("doctorid1", edtphone.getText().toString());
                                common.doctorphone = edtphone.getText().toString();
                                common.doctortrue ="true";
                              //  docintent.putExtra("true", "true");
                                common.currentuser = user;
                                common.currentisstaff = "true";
                                common.person = "true";
                                common.currentuserphone = user.getPhone();
                                startActivity(docintent);
                                finish();

                                Toast.makeText(Login.this, "Hello Doctor", Toast.LENGTH_SHORT).show();

                            } else if (Boolean.parseBoolean(user.getIspatient())) {
                                Intent homeintent = new Intent(Login.this, home.class);
                                homeintent.putExtra("true", "false");
                                common.currentuser = user;
                                common.currentuserphone = user.getPhone();
                                startActivity(homeintent);
                                finish();

                                Toast.makeText(Login.this, "hello, you are sign successful", Toast.LENGTH_SHORT).show();

                            } else if (Boolean.parseBoolean(user.getIshospital())) {
                                Intent homeintent = new Intent(Login.this, home.class);
                                homeintent.putExtra("true", "true");
                                common.currentuser = user;
                                common.currenthospital = "true";
                                common.currenthospital1 = "true";
                                common.currentuserphone = user.getPhone();
                                startActivity(homeintent);
                                finish();

                                Toast.makeText(Login.this, "hello, you are sign successful", Toast.LENGTH_SHORT).show();

                            }
                        }

                        else {
                             Toast.makeText(Login.this, "user is not exist in database", Toast.LENGTH_SHORT).show();
                             mdialog.dismiss();

                        }







                                if (edtphone.getText().toString().length()==0){
                                    edtphone.setError("Please, enter your number");
                                }

                              //  Toast.makeText(Login.this, "user is not exist in database", Toast.LENGTH_SHORT).show();








                      //  finish();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    private void pop_up_menu() {

       PopupMenu popupmenu = new PopupMenu(Login.this, image);

        popupmenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupmenu.getMenu());

        popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {

               // Toast.makeText(Login.this, item.getTitle(),Toast.LENGTH_LONG).show();
                      showdialod_doctor_request();
                return true;
            }
        });

        popupmenu.show();

    

    }

    private void showdialod_doctor_request() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(Login.this);
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_doc, null);
        edtname=add_menu_layout.findViewById(R.id.edtname);
        edtdesc=add_menu_layout.findViewById(R.id.edtdesc);
        edtphone1=add_menu_layout.findViewById(R.id.edtphone);

        btnselect=add_menu_layout.findViewById(R.id.btnselect);
        btnupload=add_menu_layout.findViewById(R.id.btnupload);

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


        alertdialog.setPositiveButton("تسجيل", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // final ProgressDialog mdialog = new ProgressDialog(Login.this);
           //     mdialog.setMessage("please waiting.....");
            //    mdialog.show();
if (newdoctor!=null){
    doctorlist.push().setValue(newdoctor);
    Toast.makeText(Login.this, "لقد تم ارسال طلبك للادمن الخاص بالبرنامج  ", Toast.LENGTH_SHORT).show();

}

else{
    Toast.makeText(Login.this, "ادخل المعلومات كامله", Toast.LENGTH_SHORT).show();
}
            }
        });

        alertdialog.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
                        Toast.makeText(Login.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newdoctor=new doctor_requests();
                                newdoctor.setName(edtname.getText().toString());
                                newdoctor.setDesc(edtdesc.getText().toString());
                                newdoctor.setPhone(edtphone1.getText().toString());
                                newdoctor.setImage(uri.toString());




                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mdialog.dismiss();
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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


    private void showdialogbox() {

        final AlertDialog.Builder alertdialog= new AlertDialog.Builder(Login.this);
        alertdialog.setTitle("ادخل البيانات المطلوبه");

        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.signup, null);
        ephone=add_menu_layout.findViewById(R.id.ephone);
        ecity=add_menu_layout.findViewById(R.id.ecity);
        ecompany=add_menu_layout.findViewById(R.id.ecompany);
        earea=add_menu_layout.findViewById(R.id.earea);
        alertdialog.setView(add_menu_layout);
alertdialog.setPositiveButton("تسجيل", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        final ProgressDialog mdialog = new ProgressDialog(Login.this);
        mdialog.setMessage("please waiting.....");
        mdialog.show();

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(ephone.getText().toString()).exists()) {
                    mdialog.dismiss();
                    Toast.makeText(Login.this, "this phone nuber is already exist", Toast.LENGTH_SHORT).show();
                } else {

                    mdialog.dismiss();
                    users user = new users();
                    user.setIsadmin("false");
                    user.setIspatient("true");
                    user.setIsstaff("false");
                    user.setIshospital("false");
                    user.setCity(ecity.getText().toString());
                    user.setCompany(ecompany.getText().toString());
                    user.setArea(earea.getText().toString());
                    user.setPhone(ephone.getText().toString());



                    table_user.child(ephone.getText().toString()).setValue(user);
                    Toast.makeText(Login.this, "signup successful", Toast.LENGTH_SHORT).show();
                    Intent homeintent = new Intent(Login.this, home.class);
                    homeintent.putExtra("true", "false");

                    common.currentuser = user;
                    startActivity(homeintent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





            }
});

alertdialog.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
});



        alertdialog.show();
    }
}
