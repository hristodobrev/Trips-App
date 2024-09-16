package com.example.trips;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "trips.db";
    public static final int DB_VERSION = 1;
    private SQLiteDatabase _db;

    public static final String CREATE_DB_SQL =
            "CREATE TABLE trips(" +
            "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Title TEXT NOT NULL, " +
            "Description TEXT NOT NULL, " +
            "Date DATE DEFAULT CURRENT_TIMESTAMP, " +
            "Picture TEXT, " +
            "Location TEXT" +
            ")";

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        _db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DB_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i1 > i){
            sqLiteDatabase.execSQL("DROP TABLE trips;");
            sqLiteDatabase.execSQL(CREATE_DB_SQL);
        }
    }

    public void insert(Trip trip){
        _db.execSQL("INSERT INTO trips(Title, Description, Picture, Location) " +
                        "VALUES(?, ?, ?, ?)",
                new Object[]{
                        trip.Title,
                        trip.Description,
                        trip.Picture,
                        trip.Location
                }
        );
    }

    public void update(Trip trip){
        _db.execSQL("UPDATE trips " +
                        "SET Title = ?, Description = ?, Picture = ?, Location = ? " +
                        "WHERE Id = ?",
                new Object[]{
                        trip.Title,
                        trip.Description,
                        trip.Picture,
                        trip.Location,
                        trip.Id
                }
        );
    }

    public void delete(Trip trip){
        _db.execSQL("DELETE FROM trips WHERE Id = ? ",
                new Object[]{
                        trip.Id
                }
        );
    }

    public List<Trip> select(){
        List<Trip> list = new ArrayList<>();
        Cursor cursor = _db.rawQuery(
                "SELECT * FROM trips ORDER BY Date; ",
                null
        );
        while (cursor.moveToNext()){
            @SuppressLint("Range")
            Trip trip = new Trip(
                    cursor.getString(cursor.getColumnIndex("Id")),
                    cursor.getString(cursor.getColumnIndex("Title")),
                    cursor.getString(cursor.getColumnIndex("Description")),
                    cursor.getString(cursor.getColumnIndex("Picture")),
                    cursor.getString(cursor.getColumnIndex("Location")),
                    cursor.getString(cursor.getColumnIndex("Date"))
                    );
            list.add(trip);
        }

        return list;
    }
}
