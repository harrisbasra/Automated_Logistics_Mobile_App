package com.sda.fastlogistics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmployeeTrack extends SQLiteOpenHelper {
    // Database info
    private static final String DATABASE_NAME = "employee_track";
    private static final int DATABASE_VERSION = 1;

    // Table info
    private static final String TABLE_DRIVER = "driver";
    private static final String COL_DRIVER_ID = "_id";
    private static final String COL_DRIVER_NAME = "name";
    private static final String COL_DRIVER_SALARY = "salary";
    private static final String COL_DRIVER_TRIPS = "trips";
    private static final String COL_DRIVER_LAST_TRIP = "last_trip";
    private static final String COL_DRIVER_TYPE = "type";

    public EmployeeTrack(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create driver table
        String createDriverTable = "CREATE TABLE " + TABLE_DRIVER + "(" +
                COL_DRIVER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DRIVER_NAME + " TEXT, " +
                COL_DRIVER_SALARY + " REAL, " +
                COL_DRIVER_TRIPS + " INTEGER, " +
                COL_DRIVER_LAST_TRIP + " TEXT, " +
                COL_DRIVER_TYPE + " TEXT" +
                ")";
        db.execSQL(createDriverTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Function to add a new driver
    public long addDriver(String name, double salary, int trips, String lastTrip, String type) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_DRIVER_NAME, name);
        values.put(COL_DRIVER_SALARY, salary);
        values.put(COL_DRIVER_TRIPS, trips);
        values.put(COL_DRIVER_LAST_TRIP, lastTrip);
        values.put(COL_DRIVER_TYPE, type);

        long driverId = db.insert(TABLE_DRIVER, null, values);


        return driverId;
    }

    // Function to get a free driver of a certain type
    public String getFreeDriver(String type) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COL_DRIVER_ID, COL_DRIVER_NAME, COL_DRIVER_SALARY, COL_DRIVER_TRIPS, COL_DRIVER_LAST_TRIP, COL_DRIVER_TYPE};
        String selection = COL_DRIVER_TYPE + " = ? AND " + COL_DRIVER_LAST_TRIP + " < ?";
        String[] selectionArgs = {type, String.valueOf(System.currentTimeMillis())};
        String limit = "1";

        Cursor cursor = db.query(TABLE_DRIVER, columns, selection, selectionArgs, null, null, null, limit);



        if (cursor.moveToFirst()) {
            long driverId = cursor.getLong(cursor.getColumnIndex(COL_DRIVER_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_DRIVER_NAME));
            double salary = cursor.getDouble(cursor.getColumnIndex(COL_DRIVER_SALARY));
            int trips = cursor.getInt(cursor.getColumnIndex(COL_DRIVER_TRIPS));
            String lastTrip = cursor.getString(cursor.getColumnIndex(COL_DRIVER_LAST_TRIP));
            String driverType = cursor.getString(cursor.getColumnIndex(COL_DRIVER_TYPE));
            return name;
        }
        return "";
    }
}
