package com.example.tabibapp.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;



public class database extends SQLiteAssetHelper {

    private static final String DB_Name = "tabibDb.db";
    private static final int DB_Ver = 1;

    public database(Context context) {


        super(context, DB_Name, null, DB_Ver);
    }



    //favorites
    public void addtofavorite(String doctorid){
        SQLiteDatabase db =getReadableDatabase();
        String query =String.format("INSERT INTO fav(DocID) VALUES('%s');",doctorid);
        db.execSQL(query);
    }

    public void removefromfavorite(String doctorid){
        SQLiteDatabase db =getReadableDatabase();
        String query =String.format("DELETE FROM fav WHERE DocID='%s';",doctorid);
        db.execSQL(query);
    }

    public boolean isfavorite(String doctorid){
        SQLiteDatabase db =getReadableDatabase();
        String query =String.format("SELECT * FROM fav WHERE DocID='%s';",doctorid);
        Cursor cursor=db.rawQuery(query,null);
        if (cursor.getCount() <=0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}
