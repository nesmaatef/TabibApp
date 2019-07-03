package com.example.tabibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class splashscreen extends AppCompatActivity {
    private GifImageView gifImageView;
   // private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        gifImageView =(GifImageView) findViewById(R.id.gifimageview);
        //progressBar =(ProgressBar) findViewById(R.id.progresspar);
        //progressBar.setVisibility(progressBar.VISIBLE);

        try {
            InputStream inputStream =getAssets().open("giphy (1).gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();

        }
        catch (IOException ex) {

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashscreen.this.startActivity(new Intent(splashscreen.this,MainActivity.class));
                splashscreen.this.finish();

            }
        },3000); //3000=3seconds
    }
}
