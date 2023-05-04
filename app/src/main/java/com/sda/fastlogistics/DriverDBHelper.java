package com.sda.fastlogistics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DriverDBHelper extends SQLiteOpenHelper {
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

    public DriverDBHelper(Context context) {
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
        // Upgrade logic here
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
        db.close();

        return driverId;
    }
    public String getAvailableDriverName(String type) {
        SQLiteDatabase db = getReadableDatabase();

        // Get the current date and time
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = dateFormat.format(currentDate);

        // Define the columns we want to retrieve
        String[] projection = {COL_DRIVER_ID, COL_DRIVER_NAME, COL_DRIVER_LAST_TRIP};

        // Define the selection criteria
        String selection = COL_DRIVER_TYPE + " = ? AND " + COL_DRIVER_LAST_TRIP + " < ?";
        String[] selectionArgs = {type, currentDateTime};

        // Define the sorting criteria
        String sortOrder = COL_DRIVER_LAST_TRIP + " ASC";

        // Query the database
        Cursor cursor = db.query(
                TABLE_DRIVER,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        String driverName = null;

        if (cursor.moveToFirst()) {
            // Get the ID and name of the first available driver
            int driverId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_DRIVER_ID));
            driverName = cursor.getString(cursor.getColumnIndexOrThrow(COL_DRIVER_NAME));

            // Update the driver's last trip and trip count
            ContentValues values = new ContentValues();
            values.put(COL_DRIVER_LAST_TRIP, currentDateTime);


            db.update(TABLE_DRIVER, values, COL_DRIVER_ID + "=?", new String[]{String.valueOf(driverId)});
        }

        return driverName;
    }
    public void incrementTripCount(String driverName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DRIVER_TRIPS, getTripCount(driverName) + 1);
        db.update(TABLE_DRIVER, values, COL_DRIVER_NAME + " = ?", new String[]{driverName});

    }

    private int getTripCount(String driverName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_DRIVER_TRIPS + " FROM " + TABLE_DRIVER + " WHERE " + COL_DRIVER_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{driverName});
        int tripCount = 0;
        if (cursor.moveToFirst()) {
            tripCount = cursor.getInt(0);
        }

        return tripCount;
    }
    public String[] getTrips() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> petrolList = new ArrayList<>();
        String query = "SELECT " + COL_DRIVER_TRIPS + " FROM " + TABLE_DRIVER;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String petrol = cursor.getString(cursor.getColumnIndex(COL_DRIVER_TRIPS));
                petrolList.add(petrol);
            } while (cursor.moveToNext());
        }

        String[] petrolArray = petrolList.toArray(new String[petrolList.size()]);
        return petrolArray;
    }
    public String[] getsalary() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> petrolList = new ArrayList<>();
        String query = "SELECT " + COL_DRIVER_SALARY + " FROM " + TABLE_DRIVER;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String petrol = cursor.getString(cursor.getColumnIndex(COL_DRIVER_SALARY));
                petrolList.add(petrol);
            } while (cursor.moveToNext());
        }

        String[] petrolArray = petrolList.toArray(new String[petrolList.size()]);
        return petrolArray;
    }

    public String[] countDriverType() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_DRIVER;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int totalVehicles = cursor.getInt(0);

        query = "SELECT COUNT(*) FROM " + TABLE_DRIVER + " WHERE " + COL_DRIVER_TYPE + " = ?";
        cursor = db.rawQuery(query, new String[]{"car"});
        cursor.moveToFirst();
        int numCars = cursor.getInt(0);

        cursor = db.rawQuery(query, new String[]{"bike"});
        cursor.moveToFirst();
        int numBikes = cursor.getInt(0);

        cursor = db.rawQuery(query, new String[]{"truck"});
        cursor.moveToFirst();
        int numTrucks = cursor.getInt(0);

        String[] result = new String[4];
        result[0] = String.valueOf(totalVehicles);
        result[1] = String.valueOf(numCars);
        result[2] = String.valueOf(numBikes);
        result[3] = String.valueOf(numTrucks);
        return result;
    }

}
