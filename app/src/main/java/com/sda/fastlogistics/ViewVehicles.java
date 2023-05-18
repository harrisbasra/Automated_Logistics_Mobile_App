package com.sda.fastlogistics;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sda.fastlogistics.databinding.ActivityViewVehiclesBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewVehicles extends AppCompatActivity {
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
    private ActivityViewVehiclesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityViewVehiclesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mVisible = true;
        Date h = new Date();
        String A = String.valueOf(h.getDate());
        String B = String.valueOf(h.getMonth());
        String F = "Today,  "+A+"-"+B+"-"+"2022";
        TextView textVew = findViewById(R.id.textView5);
        textVew.setText(F);

        ConstraintLayout frameLayout = findViewById(R.id.cl);
        Glide.with(this)
                .load("https://images.unsplash.com/photo-1564240651370-4d55b06a1375?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80")
                .placeholder(R.color.teal_200) // Placeholder image until the image is loaded
                .error(R.color.light_blue_600) // Error image if the image fails to load
                .into(new CustomViewTarget<ConstraintLayout, Drawable>(frameLayout) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Toast.makeText(ViewVehicles.this, "Load Failed", Toast.LENGTH_SHORT).show();
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

        VehicleDBHelper db = new VehicleDBHelper(this);
//        db.addVehicle("Bike 1", "BIKE001", "5", "200", "2022-01-01", "2022-12-31", "bike");
//        db.addVehicle("Bike 2", "BIKE002", "4", "180", "2022-02-01", "2022-12-31", "bike");
//        db.addVehicle("Car 1", "CAR001", "30", "1000", "2022-03-01", "2022-12-31", "car");
//        db.addVehicle("Car 2", "CAR002", "25", "800", "2022-04-01", "2022-12-31", "car");
//        db.addVehicle("Truck 1", "TRUCK001", "60", "5000", "2022-05-01", "2022-12-31", "truck");
//        db.addVehicle("Truck 2", "TRUCK002", "50", "4000", "2022-06-01", "2022-12-31", "truck");


        List<String> var = db.getAllVehicleInfo();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                var);
        binding.lvlv.setBackgroundColor(Color.argb(245, 255, 255, 255));
        binding.lvlv.setAdapter(adapter);
        binding.floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewVehicles.this, BuyCars.class));
            }
        });
        String SEL = "";
        for(int i=0;i<var.size();i++){
            SEL += "\n" + var.get(i).toString();
        }
        String finalSEL = SEL;
        binding.floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream outputStream = null;
                try {
                    String fileName = "Fast Logistics Vehicles Report.txt";
                    File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                    File file = new File(documentsDir, fileName);
                    outputStream = new FileOutputStream(file);
                    outputStream.write(finalSEL.getBytes());
                    outputStream.close();
                    Toast.makeText(ViewVehicles.this, "File has been exported to Documents as Fast Logistics Vehicles Report.txt", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
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