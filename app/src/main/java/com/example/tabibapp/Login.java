package com.example.tabibapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import info.hoang8f.widget.FButton;

public class Login extends AppCompatActivity {
    EditText edtphone;
    FButton btnlogin;
    TextView txtlogin;
    FirebaseDatabase database;
    DatabaseReference table_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtphone= (EditText) findViewById(R.id.edtphone);
        //  edtpassword = (EditText) findViewById(R.id.edtpassword);
        btnlogin = (FButton) findViewById(R.id.btnlogin);
        txtlogin= (TextView) findViewById(R.id.txtlogin);


        database =FirebaseDatabase.getInstance();
        table_user = database.getReference("users");

        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Login.this, signup.class);
                startActivity(intent);
            }
        });



        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mdialog = new ProgressDialog(Login.this);
                mdialog.setMessage("please waitting.....");
                mdialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (dataSnapshot.child(edtphone.getText().toString()).exists()) {
                            mdialog.dismiss();
                            users user = dataSnapshot.child(edtphone.getText().toString()).getValue(users.class);
                            user.setPhone(edtphone.getText().toString());  //set phone

                            if (Boolean.parseBoolean(user.getIsadmin())){
                                Intent homeintent = new Intent(Login.this, home.class);
                                common.currentuser = user;
                                startActivity(homeintent);
                                finish();
                                Toast.makeText(Login.this, "Hello Admin", Toast.LENGTH_SHORT).show();}

                            else if (Boolean.parseBoolean(user.getIsstaff())){
                                Intent docintent = new Intent(Login.this, doctor_details.class);
                                docintent.putExtra("doctorid1", edtphone.getText().toString());
                                common.currentuser = user;
                                startActivity(docintent);
                               finish();
                                Toast.makeText(Login.this, "Hello Doctor", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Intent homeintent = new Intent(Login.this, home.class);
                                common.currentuser = user;
                                startActivity(homeintent);
                                finish();
                                Toast.makeText(Login.this, "hello, you are sign successful", Toast.LENGTH_SHORT).show();

                            }


                        } else {

                            Toast.makeText(Login.this, "user is not exist in database", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}
