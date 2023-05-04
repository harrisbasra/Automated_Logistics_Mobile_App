package com.sda.fastlogistics;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public String[] getFreeVehicles(String vehicleType) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> vehicleList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LAST_CONTRACT_END_DATE + " < ? AND " + COLUMN_TYPE + " = ? AND " + COLUMN_PETROL_QUANTITY + " > ?";
        Cursor cursor = db.rawQuery(query, new String[]{currentDate, vehicleType, "1"});
        if (cursor.moveToFirst()) {
            do {
                String vehicle = cursor.getString(1);
                vehicleList.add(vehicle);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return vehicleList.toArray(new String[vehicleList.size()]);
    }



    public String[] getVehiclesToRefill(String vehicleType) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> vehicleList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TYPE + " = ? AND " + COLUMN_PETROL_QUANTITY + " <= ?";
        Cursor cursor = db.rawQuery(query, new String[]{vehicleType, "3"});

        if (cursor.moveToFirst()) {
            do {
                String vehicle = cursor.getString(1) ;
                vehicleList.add(vehicle);
            } while (cursor.moveToNext());
        }

        return vehicleList.toArray(new String[0]);
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
    public void deductPetrol(String vehicleName, int petrolQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{vehicleName});

        if (cursor.moveToFirst()) {
            int petrol = cursor.getInt(2);
            if (petrol >= petrolQuantity) {
                values.put(COLUMN_PETROL_QUANTITY, petrol - petrolQuantity);
                db.update(TABLE_NAME, values, COLUMN_NAME + " = ?", new String[]{vehicleName});
            } else {
                // Handle insufficient petrol
                Log.e(TAG, "Insufficient petrol for " + vehicleName);
            }
        }
        cursor.close();
        db.close();
    }

    public void updatePetrolQuantity(String vehicleName, int petrolQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PETROL_QUANTITY, petrolQuantity);
        db.update(TABLE_NAME, values, COLUMN_NAME + " = ?", new String[] { vehicleName });
        db.close();
    }

    public String[] getPetrolQuantity() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> petrolList = new ArrayList<>();
        String query = "SELECT " + COLUMN_PETROL_QUANTITY + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String petrol = cursor.getString(cursor.getColumnIndex(COLUMN_PETROL_QUANTITY));
                petrolList.add(petrol);
            } while (cursor.moveToNext());
        }

        String[] petrolArray = petrolList.toArray(new String[petrolList.size()]);
        return petrolArray;
    }

    public String[] getMaxLoad() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> petrolList = new ArrayList<>();
        String query = "SELECT " + COLUMN_MAX_LOAD + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String petrol = cursor.getString(cursor.getColumnIndex(COLUMN_MAX_LOAD));
                petrolList.add(petrol);
            } while (cursor.moveToNext());
        }

        String[] petrolArray = petrolList.toArray(new String[petrolList.size()]);
        return petrolArray;
    }

    public String[] countVehicleTypes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int totalVehicles = cursor.getInt(0);

        query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_TYPE + " = ?";
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