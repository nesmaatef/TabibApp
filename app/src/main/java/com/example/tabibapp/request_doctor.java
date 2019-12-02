package com.example.tabibapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.requests;
import com.example.tabibapp.Model.reserve;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.ui.main.PageViewModel;
import com.example.tabibapp.viewholder.requestviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;

public class request_doctor extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request ,request1;
    String value ="";
    FButton btnaccept,btnnot;
    requests newrequest;
    AlertDialog dialog;
    MaterialEditText edtcomment;
    String currentname,currentprice,currentdate,cureentphone, currentdoctorphone;

    FirebaseRecyclerAdapter<reserve, requestviewholder> adapter;

    private static final String TAG = "SpeedDial";

    private PageViewModel pageViewModel;

    public request_doctor() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment Profile_Doctor.
     */
    public static request_doctor newInstance() {
        return new request_doctor();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.setIndex(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.recents_fragment, container, false);

        //firebase
        database=FirebaseDatabase.getInstance();
        request=database.getReference("requests");
        request1=database.getReference("requeststatus");


        recyclerView=root.findViewById(R.id.listbook);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Intent intent =new Intent();
        value =intent.getStringExtra("doctorphone");

        loadrequests(value);

        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    private void loadrequests(final String value) {
        adapter=new FirebaseRecyclerAdapter<reserve, requestviewholder>(reserve.class,
                R.layout.request_item,
                requestviewholder.class,
                request.orderByChild("doctor_phone").equalTo(value)) {
            @Override
            protected void populateViewHolder(final requestviewholder viewHolder, reserve model, int position) {
                viewHolder.txtclinicname.setText(model.getClinic_name());
                viewHolder.clinicdate.setText(model.getClinic_date());
                viewHolder.userphone.setText(model.getUser_phone());

                currentname = model.getClinic_name();
                currentdate=model.getClinic_date();
                currentprice =model.getClinic_price();
                cureentphone =model.getUser_phone();
                currentdoctorphone =model.getDoctor_phone();

                viewHolder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        showdialog();
                        deleteitem(adapter.getRef(position).getKey());


                    }
                });


            }


        };
        recyclerView.setAdapter(adapter);

    }

    private void deleteitem(String key) {
        request.child(key).removeValue();
    }

    private void showdialog() {
        final AlertDialog.Builder alertdialog= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.acceptance, null);
        btnaccept=add_menu_layout.findViewById(R.id.btnaccept);
        btnnot=add_menu_layout.findViewById(R.id.btnnot);
        edtcomment=add_menu_layout.findViewById(R.id.edtcomment);
        alertdialog.setView(add_menu_layout);
        alertdialog.setIcon(R.drawable.ic_add_to_photos_black_24dp);


        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {

                newrequest =new requests();
                newrequest.setClinic_name(currentname);
                newrequest.setClinic_date(currentdate);
                newrequest.setClinic_price(currentprice);
                newrequest.setUser_phone(cureentphone);
                newrequest.setStatus("طلب مقبول");
                newrequest.setDoctor_phone(currentdoctorphone);
                newrequest.setComment(edtcomment.getText().toString());

                if (newrequest !=null ){
                    request1.push().setValue(newrequest);
                    Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }




            }
        });
        btnnot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newrequest =new requests();
                newrequest.setClinic_name(currentname);
                newrequest.setClinic_date(currentdate);
                newrequest.setClinic_price(currentprice);
                newrequest.setUser_phone(cureentphone);
                newrequest.setStatus("طلب مرفوض");
                newrequest.setDoctor_phone(currentdoctorphone);
                newrequest.setComment(edtcomment.getText().toString());

                if (newrequest !=null ){
                    request1.push().setValue(newrequest);
                    Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }



            }

        });

        dialog= alertdialog.show();

    }

}
