package com.sda.fastlogistics;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sda.fastlogistics.databinding.ActivityViewFinancialReportsBinding;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewFinancialReports extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private ActivityViewFinancialReportsBinding binding;
    ArrayList barArraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewFinancialReportsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;
        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.fullscreenContent;

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        String Options[] = {"Petrol Buying", "Petrol In Cars", "Driver Trips", "Max Load", "Type Of Vehicle", "Driver Salary", "Driver Type"};
        ArrayAdapter<String> roomType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Options);
        binding.spinner2.setAdapter(roomType);
        BarChart barChart = findViewById(R.id.barchart);
        getData();
        BarDataSet barDataSet = new BarDataSet(barArraylist,"SDA");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(true);
        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BarChart barChart = findViewById(R.id.barchart);
                getData();
                BarDataSet barDataSet = new BarDataSet(barArraylist,"SDA");
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);
                barChart.getDescription().setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void getData()
    {

        if(binding.spinner2.getSelectedItem().toString().equals("Petrol Buying")) {
            barArraylist = new ArrayList();
            String Petrol = "";
            try {
                FileInputStream fin = openFileInput("petrol.txt");
                int a;
                StringBuilder temp = new StringBuilder();
                while ((a = fin.read()) != -1) {
                    temp.append((char) a);
                }
                Petrol = temp.toString();
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] parts = Petrol.split("\n");

            String str1 = parts[0]; // "57"
            String str2 = parts[1]; // "20"
            String str3 = parts[2]; // "14"
            String str4 = parts[3]; // "11"
            String str5 = parts[4]; // "12"
            barArraylist.add(new BarEntry(2f, Float.valueOf(str1), "Total"));
            barArraylist.add(new BarEntry(3f, Float.valueOf(str2), "Car"));
            barArraylist.add(new BarEntry(4f, Float.valueOf(str3), "Bike"));
            barArraylist.add(new BarEntry(5f, Float.valueOf(str4), "Truck"));
        }
        else if(binding.spinner2.getSelectedItem().toString().equals("Petrol In Cars")){
            barArraylist = new ArrayList();
            VehicleDBHelper db = new VehicleDBHelper(this);
            String Data[] = db.getPetrolQuantity();
            barArraylist.add(new BarEntry(2f, Float.valueOf(Data[0]), "Total"));
            barArraylist.add(new BarEntry(3f, Float.valueOf(Data[1]), "Total"));
            barArraylist.add(new BarEntry(4f, Float.valueOf(Data[2]), "Total"));
            barArraylist.add(new BarEntry(5f, Float.valueOf(Data[3]), "Total"));
            barArraylist.add(new BarEntry(6f, Float.valueOf(Data[4]), "Total"));
            barArraylist.add(new BarEntry(7f, Float.valueOf(Data[5]), "Total"));
            barArraylist.add(new BarEntry(8f, Float.valueOf(Data[6]), "Total"));
            barArraylist.add(new BarEntry(9f, Float.valueOf(Data[7]), "Total"));

        }
        else if(binding.spinner2.getSelectedItem().toString().equals("Max Load")){
            barArraylist = new ArrayList();
            VehicleDBHelper db = new VehicleDBHelper(this);
            String Data[] = db.getMaxLoad();
            barArraylist.add(new BarEntry(2f, Float.valueOf(Data[0]), "Total"));
            barArraylist.add(new BarEntry(3f, Float.valueOf(Data[1]), "Total"));
            barArraylist.add(new BarEntry(4f, Float.valueOf(Data[2]), "Total"));
            barArraylist.add(new BarEntry(5f, Float.valueOf(Data[3]), "Total"));
            barArraylist.add(new BarEntry(6f, Float.valueOf(Data[4]), "Total"));
            barArraylist.add(new BarEntry(7f, Float.valueOf(Data[5]), "Total"));
            barArraylist.add(new BarEntry(8f, Float.valueOf(Data[6]), "Total"));
            barArraylist.add(new BarEntry(9f, Float.valueOf(Data[7]), "Total"));
        }
        else if(binding.spinner2.getSelectedItem().toString().equals("Driver Trips")){
            barArraylist = new ArrayList();
            DriverDBHelper db = new DriverDBHelper(this);
            String Data[] = db.getTrips();
            barArraylist.add(new BarEntry(2f, Float.valueOf(Data[0]), "Total"));
            barArraylist.add(new BarEntry(3f, Float.valueOf(Data[1]), "Total"));
            barArraylist.add(new BarEntry(4f, Float.valueOf(Data[2]), "Total"));
            barArraylist.add(new BarEntry(5f, Float.valueOf(Data[3]), "Total"));
        }
        else if(binding.spinner2.getSelectedItem().toString().equals("Type Of Vehicle")){
            barArraylist = new ArrayList();
            VehicleDBHelper db = new VehicleDBHelper(this);
            String Data[] = db.countVehicleTypes();
            barArraylist.add(new BarEntry(2f, Float.valueOf(Data[0]), "Total"));
            barArraylist.add(new BarEntry(3f, Float.valueOf(Data[1]), "Total"));
            barArraylist.add(new BarEntry(4f, Float.valueOf(Data[2]), "Total"));
            barArraylist.add(new BarEntry(5f, Float.valueOf(Data[3]), "Total"));
        }
        else if(binding.spinner2.getSelectedItem().toString().equals("Driver Salary")){
            barArraylist = new ArrayList();
            DriverDBHelper db = new DriverDBHelper(this);
            String Data[] = db.getsalary();
            barArraylist.add(new BarEntry(2f, Float.valueOf(Data[0]), "Total"));
            barArraylist.add(new BarEntry(3f, Float.valueOf(Data[1]), "Total"));
            barArraylist.add(new BarEntry(4f, Float.valueOf(Data[2]), "Total"));
            barArraylist.add(new BarEntry(5f, Float.valueOf(Data[3]), "Total"));
        }
        else if(binding.spinner2.getSelectedItem().toString().equals("Driver Type")){
            barArraylist = new ArrayList();
            DriverDBHelper db = new DriverDBHelper(this);
            String Data[] = db.countDriverType();
            barArraylist.add(new BarEntry(2f, Float.valueOf(Data[0]), "Total"));
            barArraylist.add(new BarEntry(3f, Float.valueOf(Data[1]), "Total"));
            barArraylist.add(new BarEntry(4f, Float.valueOf(Data[2]), "Total"));
            barArraylist.add(new BarEntry(5f, Float.valueOf(Data[3]), "Total"));
        }

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}