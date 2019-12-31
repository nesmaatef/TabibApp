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
	Uri saveuri;
	FirebaseStorage storage;
	StorageReference storageReference;
	doctor_requests newdoctor;
	DatabaseReference doctorlist;
	Button btnselect, btnupload;
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
		btnselect= (Button) findViewById(R.id.btnselect);
		btnupload= (Button) findViewById(R.id.btnupload);

		FirebaseDatabase database =FirebaseDatabase.getInstance();
		final DatabaseReference table_user = database.getReference("users");
		 doctorlist=database.getReference("DoctorRequests");
		btnselect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				chooseimage();
			}});
		btnupload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				uploadimage();
			}});
		btnsignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final ProgressDialog mdialog = new ProgressDialog(signup.this);
				mdialog.setMessage("please waiting.....");
				mdialog.show();
				if (newdoctor!=null){
					doctorlist.push().setValue(newdoctor);
					Toast.makeText(signup.this, "لقد تم ارسال طلبك للادمن الخاص بالبرنامج  ", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(signup.this, "ادخل المعلومات كامله", Toast.LENGTH_SHORT).show();
				} }}); }
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
						Toast.makeText(signup.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
						imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
							@Override
							public void onSuccess(Uri uri) {
								newdoctor=new doctor_requests();
								newdoctor.setName(edtname.getText().toString());
								newdoctor.setDesc(edtdesc.getText().toString());
								newdoctor.setPhone(edtphone.getText().toString());
								newdoctor.setImage(uri.toString());




							}
						});
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				mdialog.dismiss();
				Toast.makeText(signup.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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
		startActivityForResult(Intent.createChooser(intent,"اختر الصوره"), common.pick_image_request);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

		if (requestCode== common.pick_image_request && resultCode==RESULT_OK
				&& data !=null&& data.getData() !=null)
		{
			saveuri =data.getData();
			btnselect.setText("تم الاختيار");

		}
	}

}
