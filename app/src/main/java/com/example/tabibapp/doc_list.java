package com.example.tabibapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.category;
import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.doctorviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class doc_list extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference doctorlist;
    String categoryid="";
    RelativeLayout rootlayout;

    MaterialEditText edtname, edtdesc, edtprice, edtmap, edttimes, edttimeswait;
    Button btnselect, btnupload;

    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;

    doctor newdoctor;
    EditText edtsearch;
    ImageButton imgsearch;


    FirebaseRecyclerAdapter<doctor, doctorviewholder> adapter;
    FirebaseRecyclerAdapter<doctor, doctorviewholder> searchadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);

        //firebase
        database=FirebaseDatabase.getInstance();
        doctorlist=database.getReference("doctor");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
rootlayout=(RelativeLayout) findViewById(R.id.rootlayout);

        recyclerView=(RecyclerView) findViewById(R.id.recycler_doc);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        edtsearch=(EditText) findViewById(R.id.edtsearch);
        imgsearch=(ImageButton) findViewById(R.id.imgsearch);


        //getintent
        if (getIntent()!=null)
            categoryid=getIntent().getStringExtra("categoryid");
        if (!categoryid.isEmpty() && categoryid!=null) {
            loaddoctorlist(categoryid);
        }

        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtext =edtsearch.getText().toString();
                searchfun(searchtext);

            }
        });
    }



    private void loaddoctorlist(String categoryid) {
      //  Query firebasesearch =doctorlist.orderByChild("name").startAt(searchtext).endAt(searchtext + "\uf8ff");

adapter=new FirebaseRecyclerAdapter<doctor, doctorviewholder>(doctor.class,
        R.layout.doc_item,
        doctorviewholder.class,
        doctorlist.orderByChild("catid").equalTo(categoryid)) {
    @Override
    protected void populateViewHolder(doctorviewholder viewHolder, doctor model, int position) {
        Picasso.get().load(model.getImage()).into(viewHolder.imgdoc);
            viewHolder.txtname.setText(model.getName());
        viewHolder.txtprice.setText(model.getPrice());
        viewHolder.txtmap.setText(model.getMap());
        viewHolder.txtdesc.setText(model.getDesc());
        final doctor clickitem =model;

        viewHolder.setItemClickListener(new itemclicklistner() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
               // Toast.makeText(doc_list.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                Intent docdetails = new Intent(doc_list.this, doc_details.class);

                docdetails.putExtra("DoctorId", adapter.getRef(position).getKey());
                startActivity(docdetails);

            }
        });


    }
};
        recyclerView.setAdapter(adapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId() ==R.id.action_settings)
           showdialoge();
        //   Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_doctor, menu);
        return true;

    }


    ///upload new doctor /delete/ update
    private void showdialoge() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(doc_list.this);
      //  alertdialog.setTitle("Add new doctor");
       // alertdialog.setMessage("please fill full information");

        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_doc, null);
        edtname=add_menu_layout.findViewById(R.id.edtname);
        edtdesc=add_menu_layout.findViewById(R.id.edtdesc);
        edtmap=add_menu_layout.findViewById(R.id.edtmap);
        edtprice=add_menu_layout.findViewById(R.id.edtprice);
        edttimes=add_menu_layout.findViewById(R.id.edttimes);
        edttimeswait=add_menu_layout.findViewById(R.id.edttimeswait);

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
                uploadimage();
            }
        });

        alertdialog.setView(add_menu_layout);
        alertdialog.setIcon(R.drawable.ic_add_to_photos_black_24dp);

        //setbutton
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if (newdoctor !=null)
                {
                    doctorlist.push().setValue(newdoctor);
                    Snackbar.make(rootlayout, "New category" +newdoctor.getName()+ "was added",Snackbar.LENGTH_SHORT).show();
                }

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
                        Toast.makeText(doc_list.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
newdoctor=new doctor();
                               newdoctor.setName(edtname.getText().toString());
                                newdoctor.setDesc(edtdesc.getText().toString());
                                newdoctor.setMap(edtmap.getText().toString());
                                newdoctor.setPrice(edtprice.getText().toString());
                                newdoctor.setTimes(edttimes.getText().toString());
                                newdoctor.setTimeswait(edttimeswait.getText().toString());
                                newdoctor.setImage(uri.toString());
                                newdoctor.setCatid(categoryid);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mdialog.dismiss();
                Toast.makeText(doc_list.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(common.UPDATE)){

            showupdatedialogfood(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if (item.getTitle().equals(common.DELETE)){

            deletefood(adapter.getRef(item.getOrder()).getKey());

        }

        return super.onContextItemSelected(item);
    }

    private void deletefood(String key) {
        doctorlist.child(key).removeValue();

    }

    private void showupdatedialogfood(final String key, final doctor item) {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(doc_list.this);
      //  alertdialog.setTitle("Update new food");
       // alertdialog.setMessage("please fill full information");

        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_doc, null);
        edtname=add_menu_layout.findViewById(R.id.edtname);
        edtdesc=add_menu_layout.findViewById(R.id.edtdesc);
        edtmap=add_menu_layout.findViewById(R.id.edtmap);
        edtprice=add_menu_layout.findViewById(R.id.edtprice);
        edttimes=add_menu_layout.findViewById(R.id.edttimes);
        edttimeswait=add_menu_layout.findViewById(R.id.edttimeswait);

        btnselect=add_menu_layout.findViewById(R.id.btnselect);
        btnupload=add_menu_layout.findViewById(R.id.btnupload);

        //set default value
        edtname.setText(item.getName());
        edtdesc.setText(item.getDesc());
        edtprice.setText(item.getPrice());
        edttimeswait.setText(item.getTimeswait());
        edttimes.setText(item.getTimes());
        edtmap.setText(item.getMap());




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
                changeimage(item);            }
        });

        alertdialog.setView(add_menu_layout);
      //  alertdialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //setbutton
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //update information
                item.setName(edtname.getText().toString());
                item.setPrice(edtprice.getText().toString());
                item.setDesc(edtdesc.getText().toString());
                item.setTimeswait(edttimeswait.getText().toString());
                item.setMap(edtmap.getText().toString());
                item.setTimes(edttimes.getText().toString());




                doctorlist.child(key).setValue(item);
                Snackbar.make(rootlayout, " doctor" +item.getName()+ "was updated",Snackbar.LENGTH_SHORT).show();

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

    private void changeimage(final doctor item) {
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
                        Toast.makeText(doc_list.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(doc_list.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress =(100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                mdialog.setMessage("Uploaded" +progress+"%");
            }
        });
    }





      private void searchfun(String searchtext) {
        Query firebasesearch =doctorlist.orderByChild("name").startAt(searchtext).endAt(searchtext + "\\utf-8");

          searchadapter =new FirebaseRecyclerAdapter<doctor, doctorviewholder>(doctor.class,
                  R.layout.doc_item,
                  doctorviewholder.class,
                  firebasesearch ) {
              @Override
              protected void populateViewHolder(doctorviewholder viewHolder, doctor model, int position) {
                  Picasso.get().load(model.getImage()).into(viewHolder.imgdoc);
                  viewHolder.txtname.setText(model.getName());
                  viewHolder.txtprice.setText(model.getPrice());
                  viewHolder.txtmap.setText(model.getMap());
                  viewHolder.txtdesc.setText(model.getDesc());
                  final doctor clickitem =model;

                  viewHolder.setItemClickListener(new itemclicklistner() {
                      @Override
                      public void onClick(View view, int position, boolean isLongClick) {
                          // Toast.makeText(doc_list.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                          Intent docdetails = new Intent(doc_list.this, doc_details.class);

                          docdetails.putExtra("DoctorId", adapter.getRef(position).getKey());
                          startActivity(docdetails);

                      }
                  });


              }
          };
          recyclerView.setAdapter( searchadapter);

    }
}
