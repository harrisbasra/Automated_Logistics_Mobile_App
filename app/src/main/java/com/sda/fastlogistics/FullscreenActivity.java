package com.sda.fastlogistics;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.sda.fastlogistics.databinding.ActivityFullscreenBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
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
                        delayedHide(0);
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
    private ActivityFullscreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = new Intent(FullscreenActivity.this, ViewPetrol.class);
        startActivity(i);
        super.onCreate(savedInstanceState);

        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;

        EditText NameEdit = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText EmailEdit = (EditText) findViewById(R.id.editTextTextPersonName2);
        EditText PhoneNoEdit = (EditText) findViewById(R.id.editTextTextPersonName3);
        //  EditText PassEdit = (EditText) findViewById(R.id.editTextTextPersonName4);
        Button B1 = (Button) findViewById(R.id.button);
        Button B2 = (Button) findViewById(R.id.button2);
        NameEdit.setVisibility(View.INVISIBLE);
        EmailEdit.setVisibility(View.INVISIBLE);
        PhoneNoEdit.setVisibility(View.INVISIBLE);
        //     PassEdit.setVisibility(View.INVISIBLE);
        RadioButton r1 = findViewById(R.id.radioButton1);
        RadioButton r2 = findViewById(R.id.radioButton2);
        r1.setVisibility(View.INVISIBLE);
        r2.setVisibility(View.INVISIBLE);
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                B1.setVisibility(View.GONE);
                B2.setVisibility(View.VISIBLE);
                B2.setText("LOG IN");
                NameEdit.setVisibility(View.VISIBLE);
                EmailEdit.setVisibility(View.VISIBLE);
                EmailEdit.setHint("Enter Password");
                PhoneNoEdit.setVisibility(View.GONE);
                //          PassEdit.setVisibility(View.GONE);

            }

        });
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((PhoneNoEdit.getVisibility()== View.GONE)){
                    if((!NameEdit.getText().toString().equals(""))&&(!EmailEdit.getText().toString().equals(""))){
                        String LoginInfo = NameEdit.getText().toString()+"|"+EmailEdit.getText().toString();
                        StringBuilder temp = new StringBuilder();
                        String Whole_Data = "";
                        try {
                            FileInputStream fin = openFileInput("Login Info.txt");
                            int a;
                            while ((a = fin.read()) != -1) {
                                temp.append((char)a);
                                Whole_Data = temp.toString();
                            }
                            fin.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        String Users[] = valreturner(Whole_Data);
                        int lk = -1;
                        for(int i=0;i<Users.length;i++){


                            if((Users[i].equals("1"+LoginInfo))||(Users[i].equals("2"+LoginInfo))||(Users[i].equals("3"+LoginInfo))){
                                lk = i;
                                break;
                            }
                        }

                        if(lk!=-1){
                            Intent Starter = null;
                            if(Users[lk].equals("1"+LoginInfo)){
                                FileOutputStream fos = null;
                                try {
                                    fos = openFileOutput("AdminCheck.txt", Context.MODE_PRIVATE);
                                    fos.write("1".getBytes());
                                    fos.flush();
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Starter = new Intent(FullscreenActivity.this, MainMenuuu.class);
                            }
                            else if(Users[lk].equals("2"+LoginInfo)){
                                FileOutputStream fos = null;
                                try {
                                    fos = openFileOutput("AdminCheck.txt", Context.MODE_PRIVATE);
                                    fos.write("2".getBytes());
                                    fos.flush();
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Starter = new Intent(FullscreenActivity.this, MainMenuuu.class);
                            }
                            startActivity(Starter);
                        }
                        else{
                            Toast.makeText(FullscreenActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(FullscreenActivity.this, "LOGIN EMPTY", Toast.LENGTH_SHORT).show();
                    }
                }
                else if((PhoneNoEdit.getVisibility()== View.VISIBLE) &&(NameEdit.getVisibility()==View.VISIBLE)&&(EmailEdit.getVisibility()==View.VISIBLE)){
                    if((!NameEdit.getText().toString().equals(""))&&(!PhoneNoEdit.getText().toString().equals(""))){
                        if((EmailEdit.getText().toString().equals(""))&&(PhoneNoEdit.getText().toString().equals(""))){
                            Snackbar.make(view, "Enter Either Email Or Phone Number", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                        else{
                            if(!(!r1.isChecked() && !r2.isChecked())){
                                FileOutputStream fos = null;
                                String PatchedInfo = "";
                                if(r1.isChecked()){
                                    PatchedInfo = "1"+NameEdit.getText().toString()+"|"+PhoneNoEdit.getText().toString();
                                }
                                else if(r2.isChecked()){
                                    PatchedInfo = "22"+NameEdit.getText().toString()+"|"+PhoneNoEdit.getText().toString();
                                }
                                ////////////////////////////////////////////////////////////////////
                                String Whole_Data = "";
                                try {
                                    FileInputStream fin = openFileInput("Login Info.txt");
                                    int a;
                                    StringBuilder temp = new StringBuilder();
                                    while ((a = fin.read()) != -1) {
                                        temp.append((char)a);
                                    }
                                    Whole_Data = temp.toString();
                                    fin.close();
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if(Whole_Data==""){

                                }
                                else{
                                    PatchedInfo = Whole_Data+'\n'+PatchedInfo;
                                }
                                try {
                                    fos = openFileOutput("Login Info.txt", Context.MODE_PRIVATE);
                                    fos.write(PatchedInfo.getBytes());
                                    fos.flush();
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Intent Restarter = new Intent(FullscreenActivity.this, FullscreenActivity.class);
                                startActivity(Restarter);
                            }
                            else{
                                Snackbar.make(view, "Select Any One Role", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }

                        }

                    }
                    else{
                        Toast.makeText(FullscreenActivity.this, "SIGN UP EMPTY", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    B1.setVisibility(View.GONE);
                    B2.setVisibility(View.VISIBLE);
                    NameEdit.setVisibility(View.VISIBLE);
                    EmailEdit.setVisibility(View.VISIBLE);
                    PhoneNoEdit.setVisibility(View.VISIBLE);
                    PhoneNoEdit.setHint("New Password");
                    PhoneNoEdit.setInputType(NameEdit.getInputType());
                    //          PassEdit.setVisibility(View.GONE);
                    r1.setVisibility(View.VISIBLE);
                    r2.setVisibility(View.VISIBLE);
                }
            }
        });


        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(true) ;
                r2.setChecked(false) ;
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(false) ;
                r2.setChecked(true) ;
            }
        });
    }
    static String[] valreturner(String valarr) {
        if(valarr!=""){
            int slashes=1;
            for (int i = 0; i < valarr.length(); i++) {
                if(valarr.charAt(i)=='\n'){
                    slashes++;
                }
            }
            String divided[] = new String[slashes];
            int ii=0;
            for(int i=0;i<slashes;i++){
                divided[i]="";
            }
            for(int i=0;i<valarr.length();i++){
                if(valarr.charAt(i)=='\n'){
                    ii++;
                }
                else{
                    divided[ii] = divided[ii]+valarr.charAt(i);
                }
            }
            return divided;
        }
        return null;
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