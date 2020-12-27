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
import android.widget.ImageView;
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

public class signup extends AppCompatActivity {
	MaterialEditText edtphone, edtdesc,edtname;
	Button btnsignup;
	FirebaseStorage storage;
	StorageReference storageReference;
	doctor_requests newdoctor;
	DatabaseReference doctorlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
	    edtphone= (MaterialEditText) findViewById(R.id.edtphone);
		edtname= (MaterialEditText) findViewById(R.id.edtname);
		edtdesc= (MaterialEditText) findViewById(R.id.edtdesc);
		storage=FirebaseStorage.getInstance();
		storageReference=storage.getReference();
		btnsignup= (Button) findViewById(R.id.btnsignup);


		FirebaseDatabase database =FirebaseDatabase.getInstance();
		final DatabaseReference table_user = database.getReference("users");
		 doctorlist=database.getReference("DoctorRequests");
		btnsignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final ProgressDialog mdialog = new ProgressDialog(signup.this);
				mdialog.setMessage("please waiting.....");
				mdialog.show();
				newdoctor=new doctor_requests();
				newdoctor.setName(edtname.getText().toString());
				newdoctor.setDesc(edtdesc.getText().toString());
				newdoctor.setPhone(edtphone.getText().toString());
				newdoctor.setImage("default");

				if (newdoctor!=null){
					doctorlist.push().setValue(newdoctor);
					Toast.makeText(signup.this, "لقد تم ارسال طلبك للادمن الخاص بالبرنامج  ", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(signup.this, "ادخل المعلومات كامله", Toast.LENGTH_SHORT).show();
				} }}); }

}
