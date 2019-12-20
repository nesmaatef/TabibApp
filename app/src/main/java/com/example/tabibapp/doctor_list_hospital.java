package com.example.tabibapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.doctorviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class doctor_list_hospital extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference doctorlist;
    String categoryid="";
    RelativeLayout rootlayout;

    MaterialEditText edtname, edtdesc;
    Button btnselect, btnupload;

    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;

    doctor newdoctor;

    FirebaseRecyclerAdapter<doctor, doctorviewholder> adapter;

    MaterialSearchBar materialSearchBar;
    List<String> suggestList =new ArrayList<>();
    FirebaseRecyclerAdapter<doctor, doctorviewholder> searchadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list_hospital);

        database=FirebaseDatabase.getInstance();
        doctorlist=database.getReference("doctor_hospital");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        rootlayout=(RelativeLayout) findViewById(R.id.rootlayout);

        recyclerView=(RecyclerView) findViewById(R.id.recycler_doc);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent()!=null){
            categoryid=getIntent().getStringExtra("DoctorId");
            loaddoctorlist1(categoryid);}


            materialSearchBar=(MaterialSearchBar) findViewById(R.id.searchBar);
            materialSearchBar.setHint("ابحث عن الدكتور هنا...");
            loadsuggest();
            materialSearchBar.setLastSuggestions(suggestList);
            materialSearchBar.setCardViewElevation(10);
            materialSearchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<String> suggest =new ArrayList<String>();
                    for (String search:suggestList){
                        if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                            suggest.add(search);
                    }
                    materialSearchBar.setLastSuggestions(suggest);


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {
                    if (!enabled)
                        recyclerView.setAdapter(adapter);
                }

                @Override
                public void onSearchConfirmed(CharSequence text) {
                    startSearch(text);

                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });





        }
    private void startSearch(CharSequence text) {
        searchadapter =new FirebaseRecyclerAdapter<doctor, doctorviewholder>(doctor.class,
                R.layout.doc_item,
                doctorviewholder.class,
                doctorlist.orderByChild("name").equalTo(text.toString()) ) {
            @Override
            protected void populateViewHolder(doctorviewholder viewHolder, doctor model, int position) {
                Picasso.get().load(model.getImage()).into(viewHolder.imgdoc);
                viewHolder.txtname.setText(model.getName());
                viewHolder.txtdesc.setText(model.getDesc());
                final doctor clickitem =model;

                viewHolder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // Toast.makeText(doc_list_admin.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                        Intent docdetails = new Intent(doctor_list_hospital.this, doc_details.class);

                        docdetails.putExtra("DoctorId", adapter.getRef(position).getKey());
                        startActivity(docdetails);

                    }
                });


            }
        };
        recyclerView.setAdapter( searchadapter);
    }
    private void loadsuggest() {
        doctorlist.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapshot:dataSnapshot.getChildren()){

                    doctor item =postsnapshot.getValue(doctor.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void loaddoctorlist1(String categoryid) {

        adapter=new FirebaseRecyclerAdapter<doctor, doctorviewholder>(doctor.class,
                R.layout.doc_item,
                doctorviewholder.class,
                doctorlist.orderByChild("cat_id").equalTo(categoryid)) {
            @Override
            protected void populateViewHolder(final doctorviewholder viewHolder, doctor model, int position) {
                Picasso.get().load(model.getImage()).into(viewHolder.imgdoc);
                viewHolder.txtname.setText(model.getName());
                viewHolder.txtdesc.setText(model.getDesc());
                final doctor clickitem =model;

                viewHolder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // Toast.makeText(doc_list_admin.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                        Intent docdetails = new Intent(doctor_list_hospital.this, appointment_hospital1.class);

                        docdetails.putExtra("DoctorId", adapter.getRef(position).getKey());
                        startActivity(docdetails);


                    }
                });


            }


        };
        recyclerView.setAdapter(adapter);




    }

}
