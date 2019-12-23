package com.example.tabibapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tabibapp.Model.users;
import com.example.tabibapp.common.common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Login extends AppCompatActivity {
    MaterialEditText edtphone;
    Button btnlogin;
    TextView txtlogin;
    FirebaseDatabase database;
    DatabaseReference table_user,doctorlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtphone= (MaterialEditText) findViewById(R.id.edtphone);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        txtlogin= (TextView) findViewById(R.id.txtlogin);



        database =FirebaseDatabase.getInstance();
        table_user = database.getReference("users");


        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Login.this, signup.class);
                startActivity(intent);

             //  showdialogbox();
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
                                common.currentuserphone =user.getPhone();
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
                                common.current_patient ="true";
                                common.currentcompany =user.getCompany();
                                common.currentcity =user.getCity();
                                startActivity(homeintent);
                                finish();

                                Toast.makeText(Login.this, "hello, you are sign successful", Toast.LENGTH_SHORT).show();

                            } else if (Boolean.parseBoolean(user.getIshospital())) {
                                Intent homeintent = new Intent(Login.this, home.class);
                                homeintent.putExtra("true", "true");
                                common.currentuser = user;
                                common.currenthospital = "true";
                              //  common.currenthospital1 = "true";
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






}
