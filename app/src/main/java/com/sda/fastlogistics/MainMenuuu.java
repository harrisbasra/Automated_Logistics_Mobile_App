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
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sda.fastlogistics.databinding.ActivityMainMenuuuBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainMenuuu extends AppCompatActivity {
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
    private ActivityMainMenuuuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuuuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mVisible = true;

        try {
            FileInputStream fin = openFileInput("AdminCheck.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char)a);
            }
            if(temp.toString().equals("2" +
                    "")){
                binding.button12.setEnabled(false);
            }
            fin.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }



        TextView c1 = findViewById(R.id.textView4);
        TextView c2 = findViewById(R.id.textView5);
        TextView c3 = findViewById(R.id.textView6);

        c1.setVisibility(View.GONE);
        c2.setVisibility(View.GONE);
        c3.setVisibility(View.GONE);

        ConstraintLayout frameLayout = findViewById(R.id.cl);

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1537600612132-f04da4e02886?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=436&q=80")
                .placeholder(R.color.teal_200) // Placeholder image until the image is loaded
                .error(R.color.light_blue_600) // Error image if the image fails to load
                .into(new CustomViewTarget<ConstraintLayout, Drawable>(frameLayout) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Toast.makeText(MainMenuuu.this, "Load Failed", Toast.LENGTH_SHORT).show();
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
        TextView t1 = findViewById(R.id.button3);
        TextView t2 = findViewById(R.id.button4);
        TextView t3 = findViewById(R.id.button5);
        TextView t4 = findViewById(R.id.button6);
        TextView t5 = findViewById(R.id.button7);
        TextView t6 = findViewById(R.id.button8);
        TextView t7 = findViewById(R.id.button9);
        TextView t8 = findViewById(R.id.button10);
        TextView t9 = findViewById(R.id.button12);
        TextView t10 = findViewById(R.id.button11);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---------------------------------------- 100%
                Intent i = new Intent(MainMenuuu.this, NewTransport.class);
                startActivity(i);
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---------------------------------------- 100%
                Intent i = new Intent(MainMenuuu.this, ViewVehicles.class);
                startActivity(i);
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---------------------------------------- 100%
                Intent i = new Intent(MainMenuuu.this, Addprogress.class);
                startActivity(i);
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---------------------------------------- 100% (Check Integration)
                Intent i = new Intent(MainMenuuu.this, ViewPetrol.class);
                startActivity(i);
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---------------------------------------- 100%
                Intent i = new Intent(MainMenuuu.this, BuyCars.class);
                startActivity(i);
            }
        });
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---------------------------------------- 100%
                Intent i = new Intent(MainMenuuu.this, BuyPetrol.class);
                startActivity(i);
            }
        });
        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---------------------------------------- 0%
                Intent i = new Intent(MainMenuuu.this, ViewReports.class);
                startActivity(i);
            }
        });
        t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---------------------------------------- 0%
                Intent i = new Intent(MainMenuuu.this, ViewFinancialReports.class);
                startActivity(i);
            }
        });
        t9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---------------------------------------- 100%
                Intent i = new Intent(MainMenuuu.this, AdminControls.class);
                startActivity(i);
            }
        });
        t10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---------------------------------------- 100% (Just Check)
                Intent i = new Intent(MainMenuuu.this, SendSalary.class);
                startActivity(i);
            }
        });
        FloatingActionButton fbi = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.button12.getVisibility()==View.VISIBLE){
                    t1.setVisibility(View.GONE);
                    t2.setVisibility(View.GONE);
                    t3.setVisibility(View.GONE);
                    t4.setVisibility(View.GONE);
                    t5.setVisibility(View.GONE);
                    t6.setVisibility(View.GONE);
                    t7.setVisibility(View.GONE);
                    t8.setVisibility(View.GONE);
                    binding.button12.setVisibility(View.GONE);
                    binding.button11.setVisibility(View.GONE);

                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                }
                else{
                    binding.textView6.performClick();
                }
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenuuu.this, FullscreenActivity.class);
                startActivity(i);
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.setVisibility(View.VISIBLE);
                t2.setVisibility(View.VISIBLE);
                t3.setVisibility(View.VISIBLE);
                t4.setVisibility(View.VISIBLE);
                t5.setVisibility(View.VISIBLE);
                t6.setVisibility(View.VISIBLE);
                t7.setVisibility(View.VISIBLE);
                t8.setVisibility(View.VISIBLE);
                binding.button12.setVisibility(View.VISIBLE);
                binding.button11.setVisibility(View.VISIBLE);

                c1.setVisibility(View.GONE);
                c2.setVisibility(View.GONE);
                c3.setVisibility(View.GONE);
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