package com.example.tabibapp.face;

import android.view.View;

import java.text.ParseException;

public interface itemclicklistner {

    void onClick(View view, int position, boolean isLongClick) throws ParseException;
}
