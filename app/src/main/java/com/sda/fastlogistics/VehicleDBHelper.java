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

public class VehicleDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "vehicles.db";
    private static final String TABLE_NAME = "vehicles";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_PETROL_QUANTITY = "petrol_quantity";
    private static final String COLUMN_MAX_LOAD = "max_load";
    private static final String COLUMN_LAST_CONTRACT_START_DATE = "last_contract_start_date";
    private static final String COLUMN_LAST_CONTRACT_END_DATE = "last_contract_end_date";
    private static final String COLUMN_TYPE = "type";

    // SQL statements
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_NUMBER + " TEXT," +
                    COLUMN_PETROL_QUANTITY + " TEXT," +
                    COLUMN_MAX_LOAD + " TEXT," +
                    COLUMN_LAST_CONTRACT_START_DATE + " TEXT," +
                    COLUMN_LAST_CONTRACT_END_DATE + " TEXT," +
                    COLUMN_TYPE + " TEXT" +
                    ")";
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public VehicleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void addVehicle(String name, String number, String petrolQuantity, String maxLoad,
                           String lastContractStartDate, String lastContractEndDate, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_NUMBER, number);
        values.put(COLUMN_PETROL_QUANTITY, petrolQuantity);
        values.put(COLUMN_MAX_LOAD, maxLoad);
        values.put(COLUMN_LAST_CONTRACT_START_DATE, lastContractStartDate);
        values.put(COLUMN_LAST_CONTRACT_END_DATE, lastContractEndDate);
        values.put(COLUMN_TYPE, type);

        db.insert(TABLE_NAME, null, values);

    }

    public List<String> getFreeVehicles() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> vehicleList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LAST_CONTRACT_END_DATE + " < ?";
        Cursor cursor = db.rawQuery(query, new String[]{currentDate});
        if (cursor.moveToFirst()) {
            do {
                String vehicle = cursor.getString(1) + " (" + cursor.getString(2) + ")";
                vehicleList.add(vehicle);
            } while (cursor.moveToNext());
        }

        return vehicleList;
    }

    public List<String> getVehiclesToRefill() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> vehicleList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PETROL_QUANTITY + " < ?";
        Cursor cursor = db.rawQuery(query, new String[]{"5"});

        if (cursor.moveToFirst()) {
            do {
                String vehicle = cursor.getString(1) + " (" + cursor.getString(2) + ")";
                vehicleList.add(vehicle);
            } while (cursor.moveToNext());
        }

        return vehicleList;
    }
    public List<String> getAllVehicleInfo() {
        List<String> vehicleInfoList = new ArrayList<>();

        // Select query to get name, number, and type from the vehicles table
        String selectQuery = "SELECT name, number, type FROM vehicles";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add the name, number, and type to the list
        if (cursor.moveToFirst()) {
            do {
                String vehicleInfo = "• A " + cursor.getString(0) +
                        " with Number: " + cursor.getString(1) +
                        "\n  Type: " + cursor.getString(2);

                // Add the vehicle info to the list
                vehicleInfoList.add(vehicleInfo);
            } while (cursor.moveToNext());
        }
        // Return the list of vehicle info strings
        return vehicleInfoList;
    }

}