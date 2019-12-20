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

	//EditText edtphone;
	MaterialEditText edtphone, edtcompany,edtcity, edtarea;
	Button btnsignup;
	ImageView image;

	MaterialEditText ephone,ecompany,ecity,earea;
	Uri saveuri;
	FirebaseStorage storage;
	StorageReference storageReference;

	doctor_requests newdoctor;
	DatabaseReference doctorlist;


	MaterialEditText edtname, edtdesc, edtphone1, edtmap, edttimes, edttimeswait;
	Button btnselect, btnupload;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
	edtphone= (MaterialEditText) findViewById(R.id.edtphone);
		edtarea= (MaterialEditText) findViewById(R.id.edtarea);
		edtcity= (MaterialEditText) findViewById(R.id.edtcity);
		edtcompany= (MaterialEditText) findViewById(R.id.edtcompany);
		image= (ImageView) findViewById(R.id.imgmore);


		storage=FirebaseStorage.getInstance();
		storageReference=storage.getReference();

		btnsignup= (Button) findViewById(R.id.btnsignup);

		FirebaseDatabase database =FirebaseDatabase.getInstance();
		final DatabaseReference table_user = database.getReference("users");
		 doctorlist=database.getReference("DoctorRequests");



		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pop_up_menu();
			}
		});



		btnsignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				final ProgressDialog mdialog = new ProgressDialog(signup.this);
				mdialog.setMessage("please waiting.....");
				mdialog.show();


				table_user.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						if (edtphone.getText().toString().length()==0 || edtcompany.getText().toString().length()==0||
						edtarea.getText().toString().length()==0 || edtcity.getText().toString().length()==0) {
							mdialog.dismiss();
							edtphone.setError("ادخل رقم التليفون");
							edtcompany.setError("اكتب لا يوجد");
							edtcity.setError("ادخل المدينه");
							edtarea.setError("ادخل المنطقه");
							Toast.makeText(signup.this, "ادخل البيانات كامله", Toast.LENGTH_SHORT).show();

						}



						else if ( dataSnapshot.child(edtphone.getText().toString()).exists()) {
							mdialog.dismiss();
							Toast.makeText(signup.this, "this phone nuber is already exist", Toast.LENGTH_SHORT).show();
						}

						else {



								mdialog.dismiss();
								users user = new users();
								user.setIsadmin("false");
								user.setIspatient("true");
								user.setIsstaff("false");
								user.setIshospital("false");
								user.setCompany(edtcompany.getText().toString());
								user.setCity(edtcity.getText().toString());
								user.setArea(edtarea.getText().toString());
							user.setPhone(edtphone.getText().toString());

							table_user.child(edtphone.getText().toString()).setValue(user);
								Toast.makeText(signup.this, "signup successful", Toast.LENGTH_SHORT).show();
								Intent homeintent = new Intent(signup.this, home.class);
								homeintent.putExtra("true", "false");

								common.currentuser = user;
								common.currentuserphone =user.getPhone();
								common.currentcompany =user.getCompany();
								common.currentcity =user.getCity();
								startActivity(homeintent);
								finish();


							Toast.makeText(signup.this, "hi", Toast.LENGTH_SHORT).show();
						}




					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {

					}
				});

			}
		});
	}

	private void pop_up_menu() {

		PopupMenu popupmenu = new PopupMenu(signup.this, image);

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
		AlertDialog.Builder alertdialog= new AlertDialog.Builder(signup.this);
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
					Toast.makeText(signup.this, "لقد تم ارسال طلبك للادمن الخاص بالبرنامج  ", Toast.LENGTH_SHORT).show();

				}

				else{
					Toast.makeText(signup.this, "ادخل المعلومات كامله", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(signup.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
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
