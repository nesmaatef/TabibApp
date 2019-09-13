package com.example.tabibapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class signup extends AppCompatActivity {

	EditText edtphone;
	FButton btnsignup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		// edtname= (EditText) findViewById(R.id.edtname);
		//  edtage= (EditText) findViewById(R.id.edtage);
		// edtpassword= (EditText) findViewById(R.id.edtpassword);
		edtphone= (EditText) findViewById(R.id.edtphone);
		btnsignup= (FButton) findViewById(R.id.btnsignup);

		FirebaseDatabase database =FirebaseDatabase.getInstance();
		final DatabaseReference table_user = database.getReference("users");


		btnsignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				final ProgressDialog mdialog = new ProgressDialog(signup.this);
				mdialog.setMessage("please waitting.....");
				mdialog.show();


				table_user.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						if (dataSnapshot.child(edtphone.getText().toString()).exists()) {
							mdialog.dismiss();
							Toast.makeText(signup.this, "this phone nuber is already exist", Toast.LENGTH_SHORT).show();
						} else {

							mdialog.dismiss();
							users user = new users();
							user.setIsadmin("false");
							user.setIspatient("true");
							user.setIsstaff("false");
							user.setIshospital("false");

							table_user.child(edtphone.getText().toString()).setValue(user);
							Toast.makeText(signup.this, "signup successful", Toast.LENGTH_SHORT).show();
							Intent homeintent = new Intent(signup.this, home.class);
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
	}
}
