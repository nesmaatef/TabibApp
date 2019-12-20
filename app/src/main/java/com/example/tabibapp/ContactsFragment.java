package com.example.tabibapp;

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
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.ui.main.PageViewModel;
import com.example.tabibapp.viewholder.request_status_viewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;

public class ContactsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requeststatus;
    FirebaseRecyclerAdapter<requests, request_status_viewholder> adapter;
    String value,v ;

    private static final String TAG = "Profile";

    private PageViewModel pageViewModel;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment Profile_Doctor.
     */
    public static ContactsFragment newInstance() {
        return new ContactsFragment();
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
        View root = inflater.inflate(R.layout.contacts_fragment, container, false);

        database=FirebaseDatabase.getInstance();
        requeststatus=database.getReference("requeststatus");


        recyclerView=root.findViewById(R.id.listbook1);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        value = common.currentuserphone;
        if (common.person.equals("true") || common.currenthospital.equals("true")){
            loadrequesstatus(value);
            Toast.makeText(getActivity(), "true", Toast.LENGTH_SHORT).show();}
        else if (common.person.equals("false")){
            loadrequesstatus1(value);
            Toast.makeText(getActivity(), "false"+value, Toast.LENGTH_SHORT).show();}

        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    private void loadrequesstatus(final String value) {
        adapter=new FirebaseRecyclerAdapter<requests, request_status_viewholder>(requests.class,
                R.layout.item_reqest_status,
                request_status_viewholder.class,
                requeststatus.orderByChild("doctor_phone").equalTo(value) ) {
            @Override
            protected void populateViewHolder(request_status_viewholder viewholder, requests model, int i) {
                viewholder.clinic_name.setText(model.getClinic_name());
                viewholder.clinic_date.setText(model.getClinic_date());
                viewholder.price_clinic.setText(model.getClinic_price());
                viewholder.clinic_comment.setText(model.getComment());
                viewholder.status.setText(model.getStatus());

                v =model.getStatus();
                viewholder.setItemClickListener(new itemclicklistner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) throws ParseException {

                    }
                });




            }
        };
        recyclerView.setAdapter(adapter);

    }

    private void loadrequesstatus1(final String value) {
        adapter=new FirebaseRecyclerAdapter<requests, request_status_viewholder>(requests.class,
                R.layout.item_reqest_status,
                request_status_viewholder.class,
                requeststatus.orderByChild("user_phone").equalTo(value)) {
            @Override
            protected void populateViewHolder(request_status_viewholder viewholder, requests model, int i) {
                viewholder.clinic_name.setText(model.getClinic_name());
                viewholder.clinic_date.setText(model.getClinic_date());
                viewholder.price_clinic.setText(model.getClinic_price());
                viewholder.clinic_comment.setText(model.getComment());
                viewholder.status.setText(model.getStatus());

            }
        };
        recyclerView.setAdapter(adapter);

    }

}
