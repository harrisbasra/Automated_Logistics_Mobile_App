package com.sda.fastlogistics;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.sda.fastlogistics.databinding.ActivityNewTransportBinding;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class NewTransport extends AppCompatActivity {
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

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
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
    private ActivityNewTransportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewTransportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;
//        DriverDBHelper db2 = new DriverDBHelper(NewTransport.this);
//        db2.addDriver("Driver1", 100, 0, "2023-02-01", "bike");
//        db2.addDriver("Driver2", 90, 0, "2023-06-06", "bike");
//        db2.addDriver("Driver3", 80, 0, "2023-02-01", "car");
//        db2.addDriver("Driver4", 70, 0, "2023-06-06", "car");
//        db2.addDriver("Driver5", 60, 0, "2023-02-01", "truck");
//        db2.addDriver("Driver6", 50, 0, "2023-06-06", "truck");


        ConstraintLayout frameLayout = findViewById(R.id.cl);

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1537600612132-f04da4e02886?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=436&q=80")
                .placeholder(R.color.teal_200) // Placeholder image until the image is loaded
                .error(R.color.light_blue_600) // Error image if the image fails to load
                .into(new CustomViewTarget<ConstraintLayout, Drawable>(frameLayout) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Toast.makeText(NewTransport.this, "Load Failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        getView().setBackground(resource);
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                        // handle resource cleared
                    }
                });

        VehicleDBHelper db1 = new VehicleDBHelper(this);
        String Options[] = {"bike", "car", "truck"};
        ArrayAdapter<String> roomType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Options);
        binding.spinnerA.setAdapter(roomType);

        String Vehicles[] = db1.getFreeVehicles(binding.spinnerA.getSelectedItem().toString());
        ArrayAdapter<String> veh = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Vehicles);
        binding.spinnerB.setAdapter(veh);

        binding.spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Vehicles[] = db1.getFreeVehicles(binding.spinnerA.getSelectedItem().toString());
                ArrayAdapter<String> veh = new ArrayAdapter<String>(NewTransport.this, android.R.layout.simple_spinner_dropdown_item, Vehicles);
                binding.spinnerB.setAdapter(veh);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.button19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(binding.key2.getText().toString().equals("") || binding.key3.getText().toString().equals(""))){
                    if(!(binding.key2.getText().toString().startsWith("-") || binding.key3.getText().toString().startsWith("-"))){
                    DriverDBHelper db2 = new DriverDBHelper(NewTransport.this);
                    String DriverBeingUsed = db2.getAvailableDriverName(binding.spinnerA.getSelectedItem().toString());
                    db2.incrementTripCount(DriverBeingUsed);
                    db1.deductPetrol(binding.spinnerB.getSelectedItem().toString(), Integer.valueOf(binding.key3.getText().toString()));
                    binding.key4.setText("Driver: "+DriverBeingUsed);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    startActivity(new Intent(NewTransport.this, MainMenuuu.class));
                    }
                    else{
                        Toast.makeText(NewTransport.this, "Nothing Can Be Negative", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(NewTransport.this, "Fill All Columns", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar

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