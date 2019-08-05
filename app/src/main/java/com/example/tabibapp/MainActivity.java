package com.example.tabibapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {
    FButton patient, doctor;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        patient =(FButton) findViewById(R.id.btnpatient);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/THESANSARABIC-LIGHT.ttf");
        patient.setTypeface(typeface);

        doctor =(FButton) findViewById(R.id.btndoctor);
        doctor.setTypeface(typeface);

        img =(ImageView) findViewById(R.id.imgadmin);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });




        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }


}



