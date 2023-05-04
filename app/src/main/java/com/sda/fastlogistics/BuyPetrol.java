package com.sda.fastlogistics;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sda.fastlogistics.databinding.ActivityBuyPetrolBinding;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BuyPetrol extends AppCompatActivity {
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
    private ActivityBuyPetrolBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuyPetrolBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mVisible = true;


        VehicleDBHelper db = new VehicleDBHelper(this);

//        db.addVehicle("Bike79","BK79","1","10","2000-01-01","2020-01-02","bike");
//        db.addVehicle("Bike69","BK79","1","10","2000-01-01","2020-01-02","bike");
//        db.addVehicle("Bike59","BK79","1","10","2000-01-01","2020-01-02","bike");
//        db.addVehicle("Bike49","BK79","1","10","2000-01-01","2020-01-02","bike");

        String Types[] = {"bike", "car", "truck"};
        ArrayAdapter<String> roomType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Types);
        binding.spinnerA.setAdapter(roomType);

        String Options[] = db.getFreeVehicles(binding.spinnerA.getSelectedItem().toString());
        ArrayAdapter<String> veh = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Options);
        binding.spinnerA4.setAdapter(veh);


        binding.spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Options[] = db.getVehiclesToRefill(binding.spinnerA.getSelectedItem().toString());
                ArrayAdapter<String> veh = new ArrayAdapter<String>(BuyPetrol.this, android.R.layout.simple_spinner_dropdown_item, Options);
                binding.spinnerA4.setAdapter(veh);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updatePetrolQuantity(binding.spinnerA4.getSelectedItem().toString(), Integer.valueOf(binding.keyHolder.getText().toString()));
                Toast.makeText(BuyPetrol.this, "Vehicle Refilled!", Toast.LENGTH_SHORT).show();
                String Options[] = db.getVehiclesToRefill(binding.spinnerA.getSelectedItem().toString());
                ArrayAdapter<String> veh = new ArrayAdapter<String>(BuyPetrol.this, android.R.layout.simple_spinner_dropdown_item, Options);
                binding.spinnerA4.setAdapter(veh);


                String Petrol = "";
                try {
                    FileInputStream fin = openFileInput("petrol.txt");
                    int a;
                    StringBuilder temp = new StringBuilder();
                    while ((a = fin.read()) != -1) {
                        temp.append((char)a);
                    }
                    Petrol = temp.toString();
                    fin.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                String[] parts = Petrol.split("\n");

                String str1 = parts[0];
                String str2 = parts[1];
                String str3 = parts[2];
                String str4 = parts[3];
                String str5 = parts[4];

                if(binding.spinnerA.getSelectedItem().toString().equals("car")){
                    if(!str2.equals("")){
                        str2 = String.valueOf(Float.valueOf(str2)+Float.valueOf(binding.keyHolder2.getText().toString()));
                    }
                    else{
                        str2 = String.valueOf(binding.keyHolder2.getText().toString());
                    }
                }
                else  if(binding.spinnerA.getSelectedItem().toString().equals("bike")){
                    if(!str4.equals("")){
                        str4 = String.valueOf(Float.valueOf(str4)+Float.valueOf(binding.keyHolder2.getText().toString()));
                    }
                    else{
                        str4 = String.valueOf(binding.keyHolder2.getText().toString());
                    }
                }
                else  if(binding.spinnerA.getSelectedItem().toString().equals("truck")){
                    if(!str3.equals("")){
                        str3 = String.valueOf(Float.valueOf(str3)+Float.valueOf(binding.keyHolder2.getText().toString()));
                    }
                    else{
                        str3 = String.valueOf(binding.keyHolder2.getText().toString());
                    }
                }

                String TBR = str1+"\n"+str2+"\n"+str3+"\n"+str4+"\n"+str5;

                FileOutputStream fos = null;
                try {
                    fos = openFileOutput("petrol.txt", Context.MODE_PRIVATE);
                    fos.write(TBR.getBytes());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        binding.button111234567890.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyPetrol.this, MainMenuuu.class));
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