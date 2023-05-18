package com.sda.fastlogistics;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sda.fastlogistics.databinding.ActivityAdminControlsBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AdminControls extends AppCompatActivity {
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
    private ActivityAdminControlsBinding binding;

    private Spinner spinnerA;
    private TextView key_holder;
    private Button button11;
    private ArrayAdapter<String> spinnerAdapter;


    private List<String> loginInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_controls);

        // Read data from file
        ArrayList<String> loginInfos = new ArrayList<>();
        ArrayList<String> unapprovedLogins = new ArrayList<>();
        ArrayList<String> adminLogins = new ArrayList<>();
        ArrayList<String> adminPasswords = new ArrayList<>();
        try {
            InputStream inputStream = getApplicationContext().openFileInput("Login Info.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                loginInfos.add(line);
                if (line.startsWith("22")) {
                    unapprovedLogins.add(line);
                } else if (line.startsWith("2")) {
                    String[] parts = line.split("\\|");
                    adminLogins.add(parts[0]);
                    adminPasswords.add(parts[1]);
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Populate spinner with unapproved logins
        Spinner spinner = findViewById(R.id.spinnerA);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unapprovedLogins);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLogin = parent.getItemAtPosition(position).toString();
                String password = getPassword(selectedLogin);
                TextView keyHolder = findViewById(R.id.spinnerB);
                keyHolder.setText(selectedLogin.replace("22", ""));
                keyHolder.setText(selectedLogin.replace("|", " - "));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Handle approve button click
        Button approveButton = findViewById(R.id.button11);
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedIndex = spinner.getSelectedItemPosition();
                if (selectedIndex == AdapterView.INVALID_POSITION) {
                    Toast.makeText(AdminControls.this, "Please select a login to approve", Toast.LENGTH_SHORT).show();
                    return;
                }

                String selectedLogin = spinner.getItemAtPosition(selectedIndex).toString();
                if (!selectedLogin.startsWith("22")) {
                    Toast.makeText(AdminControls.this, "Login is already approved", Toast.LENGTH_SHORT).show();
                    return;
                }

                String approvedLogin = selectedLogin.replaceFirst("2", "");
                loginInfos.set(loginInfos.indexOf(selectedLogin), approvedLogin);

                // Write updated data to file
                try {
                    OutputStream outputStream = getApplicationContext().openFileOutput("Login Info.txt", Context.MODE_PRIVATE);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    for (String loginInfo : loginInfos) {
                        outputStreamWriter.write(loginInfo);
                        outputStreamWriter.write("\n");
                    }
                    outputStreamWriter.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                unapprovedLogins.remove(selectedLogin);
                spinnerAdapter.notifyDataSetChanged();
                Toast.makeText(AdminControls.this, "Login approved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to get password for a given login
    private String getPassword(String login) {
        String[] parts = login.split("\\|");
        return parts[1];
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