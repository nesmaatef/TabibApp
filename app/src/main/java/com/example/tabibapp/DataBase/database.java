package com.example.tabibapp.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.tabibapp.Model.fav;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;


public class database extends SQLiteAssetHelper {

    private static final String DB_Name = "tabibDb.db";
    private static final int DB_Ver = 1;

    public database(Context context) {


        super(context, DB_Name, null, DB_Ver);
    }



    //favorites
    public void addtofavorite(fav doctor){
        SQLiteDatabase db =getReadableDatabase();
        String query =String.format("INSERT INTO fav(doctorid, userphone,doctorname,doctordescription,doctorimage,doctorcatid)" +
                " VALUES('%s','%s','%s','%s','%s','%s');",
                doctor.getDoctorid(),
                doctor.getUserphone(),
                doctor.getDoctorname(),
                doctor.getDoctordescription(),
                doctor.getDoctorimage(),
                doctor.getDoctorcatid());
        db.execSQL(query);
    }

    public void removefromfavorite(String doctorid, String userphone){
        SQLiteDatabase db =getReadableDatabase();
        String query =String.format("DELETE FROM fav WHERE doctorid='%s' and userphone='%s';",doctorid,userphone);
        db.execSQL(query);
    }

    public boolean isfavorite(String doctorid, String userphone){
        SQLiteDatabase db =getReadableDatabase();
        String query =String.format("SELECT * FROM fav WHERE doctorid='%s' and userphone='%s';",doctorid,userphone);
        Cursor cursor=db.rawQuery(query,null);
        if (cursor.getCount() <=0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public List<fav> getallfav( String userphone) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"doctorid", "userphone", "doctorname", "doctordescription", "doctorimage, doctorcatid"};
        String sqlTable = "fav";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, "userphone=?", new String[]{userphone}, null, null, null);

        final List<fav> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new fav(
                        c.getString(c.getColumnIndex("doctorid")),
                        c.getString(c.getColumnIndex("userphone")),
                        c.getString(c.getColumnIndex("doctorname")),
                        c.getString(c.getColumnIndex("doctordescription")),
                        c.getString(c.getColumnIndex("doctorimage")),
                        c.getString(c.getColumnIndex("doctorcatid"))

                        ));

            } while (c.moveToNext());


        }
        return result;
    }
    public void cleanfav() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM fav");
        db.execSQL(query);

    }

}
