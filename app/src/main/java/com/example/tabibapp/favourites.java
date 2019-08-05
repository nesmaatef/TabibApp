package com.example.tabibapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.tabibapp.DataBase.database;
import com.example.tabibapp.common.common;

import com.example.tabibapp.viewholder.fav_adapter;

public class favourites extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout rootlayout;
    fav_adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        rootlayout=(RelativeLayout) findViewById(R.id.rootfav);
        recyclerView=(RecyclerView) findViewById(R.id.recycler_fav);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadfavlist();
    }

    private void loadfavlist() {

        adapter=new fav_adapter(this,new database (this).getallfav(common.currentuser.getPhone()));
        recyclerView.setAdapter(adapter);
    }
}
