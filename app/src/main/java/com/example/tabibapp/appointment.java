package com.example.tabibapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.tabibapp.common.common;
import com.example.tabibapp.Model.appoint;
import com.example.tabibapp.Model.doctor;
import com.example.tabibapp.book;

import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.appointmentviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class appointment extends AppCompatActivity {

    FirebaseDatabase  database3 ;
    DatabaseReference appointment3 ;
    RecyclerView recycler1,recycler2 ;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutmanager ;
    FirebaseRecyclerAdapter<appoint, appointmentviewholder> adapter3;
    doctor currentdoctor ;


String clinicid="";
    String docname="";
    String clinicprice="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        database3=FirebaseDatabase.getInstance();
  //      appointment1=database1.getReference("appointmenttoday");
    //    appointment2=database2.getReference("appointmenttomorrow");

        //try
        appointment3=database3.getReference("clinics");



        recycler1 =(RecyclerView) findViewById(R.id.recycler1);
      //  recycler2 =(RecyclerView) findViewById(R.id.recycler2);
       // recycler1.setLayoutManager(new GridLayoutManager(this,2));
        recycler1.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler1.setLayoutManager(layoutManager);

Intent intent=getIntent();
        docname = intent.getStringExtra("namedoctor");
        clinicprice = intent.getStringExtra("clinicprice");

    clinicid = intent.getStringExtra("clinicid");


    if (!clinicid.isEmpty())

        loadappointlist3(clinicid);





    }
    private void loadappointlist3(final String clinicid) {
        adapter3 =new FirebaseRecyclerAdapter<appoint,appointmentviewholder>(appoint.class,
                R.layout.appoint_item,
                appointmentviewholder.class,
                appointment3.child(clinicid).child("appointment")) {
            @Override
            protected void populateViewHolder(final appointmentviewholder viewholder, final appoint model, int i) {

                viewholder.value.setText(model.getFrom());

                final appoint clickitem =model;

                viewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // Toast.makeText(doc_list.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();

                      //  new database(getBaseContext()).addtobook(new reserve(
                        //        doctorid, current1,current2,
                          //      model.getFrom()


                        //));
                        Intent docdetails = new Intent(appointment.this,com.example.tabibapp.book.class);
                        docdetails.putExtra("name",docname);
                        docdetails.putExtra("price",clinicprice);
                        docdetails.putExtra("docdate",model.getFrom());
                        docdetails.putExtra("idclinic",clinicid);



                        startActivity(docdetails);




                    }
                });



            }
        };
        recycler1.setAdapter(adapter3);

    }


}
