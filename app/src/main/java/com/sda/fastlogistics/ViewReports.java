package com.sda.fastlogistics;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
//import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
//import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
//import android.widget.ArrayAdapter;
//import android.widget.CompoundButton;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
//import android.widget.RadioButton;
//import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.Snackbar;
import com.sda.fastlogistics.databinding.ActivityViewReportsBinding;

//import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.lang.reflect.Array;
import java.util.Vector;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewReports extends AppCompatActivity {
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
    class MyOwnHandMadeMap{
        public
        String Name;
        String Val;
        MyOwnHandMadeMap(){
            Val = "";
            Name = "";
        }
        MyOwnHandMadeMap(String N, String V){
            Val = V;
            Name = N;
        }
        void Cocoloize(String N, String V){
            Val = V;
            Name = N;
        }
    };
    private ActivityViewReportsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewReportsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView t342534 = findViewById(R.id.textView8);
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

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        //////////////////////////////////////////////////////////////////DECLARATIONS////////////////////////////////////////////////////////////////


        ListView f6708 = findViewById(R.id.lv11);
        final String[] fg797 = {""};
        final String[] fg888 = {""};
        TextView t87654 = findViewById(R.id.textView9);
        TextView sfnjkvs = findViewById(R.id.textView7);
        Vector<MyOwnHandMadeMap> BIGDATA = new Vector<MyOwnHandMadeMap>();
        Vector<Vector<String>> GameChangesHere = null;
        final int[] IteratorOfDate = {0};
        final int[] IteratorOfTask = {0};


//        GestureDetector gestureDetector;
//        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());
//        t342534.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return gestureDetector.onTouchEvent(event);
//            }
//        });


        /////////////////////////////////////////////////////////////////STRUCT POPULATOR/////////////////////////////////////////////////////////


        try {
            FileInputStream fin = openFileInput("fatima.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char) a);
            }
            if (temp.toString() != "") {
                fg797[0] = temp.toString();
            }
            fin.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //..........................................................................................
        try {
            FileInputStream fin = openFileInput("harris.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char) a);
            }
            if (temp.toString() != "") {
                fg888[0] = temp.toString();
            }
            fin.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (!fg797[0].equals(null)){
            String NameArray[] = ArrayMaker(fg797[0]);
            String ValueArray[] = ArrayMaker(fg888[0]);
            for(int i=0;i<NameArray.length;i++){
                BIGDATA.add(new MyOwnHandMadeMap(NameArray[i], ValueArray[i]));
            }

            /////////////////////////////////////////////////////////////////TASK.DATE SWITCH/////////////////////////////////////////////////////////////


            t87654.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String FS="2";
                    try {
                        FileInputStream fin = openFileInput("FS.txt");
                        int a;
                        StringBuilder temp = new StringBuilder();
                        while ((a = fin.read()) != -1) {
                            temp.append((char)a);
                        }
                        if(temp.toString()!="") {
                            FS = temp.toString();
                        }
                        fin.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(FS.equals("2")){
                        t87654.setText("DATE VIEW");
                        if(IteratorOfDate[0] ==0)
                            sfnjkvs.setText("1st");
                        else if(IteratorOfDate[0] ==1)
                            sfnjkvs.setText("2nd");
                        else if(IteratorOfDate[0] ==2)
                            sfnjkvs.setText("3rd");
                        else
                            sfnjkvs.setText(String.valueOf(IteratorOfDate[0])+"th");
                        FileOutputStream fos = null;
                        try {
                            fos = openFileOutput("FS.txt", Context.MODE_PRIVATE);
                            fos.write("1".getBytes());
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        t87654.setText("Employee View");
                        sfnjkvs.setText(String.valueOf(BIGDATA.get(IteratorOfTask[0]).Name));
                        ////////////////////////////////////////////////////
                        FileOutputStream fos = null;
                        try {
                            fos = openFileOutput("FS.txt", Context.MODE_PRIVATE);
                            fos.write("2".getBytes());
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });




            /////////////////////////////////////////////////////////////////////ON CREATE SITUATION OF LISTVIEW////////////////////////////////////////

            String FS="2";
            try {
                FileInputStream fin = openFileInput("FS.txt");
                int a;
                StringBuilder temp = new StringBuilder();
                while ((a = fin.read()) != -1) {
                    temp.append((char)a);
                }
                if(temp.toString()!="") {
                    FS = temp.toString();
                }
                fin.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            if(FS.equals("1")){
                if(IteratorOfDate[0] ==0)
                    sfnjkvs.setText("1st");
                else if(IteratorOfDate[0] ==1)
                    sfnjkvs.setText("2nd");
                else if(IteratorOfDate[0] ==2)
                    sfnjkvs.setText("3rd");
                else
                    sfnjkvs.setText(String.valueOf(IteratorOfDate[0]+1)+"th");
            }
            else {
                sfnjkvs.setText(String.valueOf(BIGDATA.get(IteratorOfTask[0]).Name));
            }
            /////////////////////////////////////////////////////////////////////POPULATE THE ROUND BUTTON////////////////////////////////////////
            FloatingActionButton f60038 = findViewById(R.id.floatingActionButton5);

            f60038.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String GHJKL = "";
                    try {
                        FileInputStream fin = openFileInput("FS.txt");
                        int a;
                        StringBuilder temp = new StringBuilder();
                        while ((a = fin.read()) != -1) {
                            temp.append((char)a);
                        }
                        if(temp.toString()!="") {
                            GHJKL = temp.toString();
                        }
                        fin.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(GHJKL.equals("1")){

                        Vector<String> DaalneWale = new Vector<String>();
                        for (int o=0;o<BIGDATA.size();o++){
                            if(BIGDATA.get(o).Val.charAt(IteratorOfDate[0])=='1'){
                                DaalneWale.add(BIGDATA.get(o).Name);
                            }
                        }
                        ListView okbtw = findViewById(R.id.lv11);
                        ArrayAdapter<String> arr;
                        String UIO[] = new String[DaalneWale.size()];
                        for(int i=0;i< DaalneWale.size();i++){
                            UIO[i]=DaalneWale.get(i);
                        }

                        arr= new ArrayAdapter<String>(ViewReports.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,UIO );
                        okbtw.setAdapter(arr);
                    }
                    else {
                        Vector<String> DaalneWale = new Vector<String>();
                        for (int o=0;o<31;o++){
                            if(BIGDATA.get(IteratorOfTask[0]).Val.charAt(o)=='1'){
                                DaalneWale.add("Day # " + String.valueOf(o+1)+" of Month");
                            }
                        }
                        ListView okbtw = findViewById(R.id.lv11);
                        ArrayAdapter<String> arr;
                        String UIO[] = new String[DaalneWale.size()];
                        for(int i=0;i< DaalneWale.size();i++){
                            UIO[i]=DaalneWale.get(i);
                        }
                        arr= new ArrayAdapter<String>(ViewReports.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,UIO );
                        okbtw.setAdapter(arr);
                    }
                }
            });
            Button bprev = (Button) findViewById(R.id.button5);
            Button bnext = (Button) findViewById(R.id.button6);
            bprev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String KOP="2";
                    try {
                        FileInputStream fin = openFileInput("FS.txt");
                        int a;
                        StringBuilder temp = new StringBuilder();
                        while ((a = fin.read()) != -1) {
                            temp.append((char)a);
                        }
                        if(temp.toString()!="") {
                            KOP = temp.toString();
                        }
                        fin.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(KOP.equals("1")){
                        if(IteratorOfDate[0] > 0){
                            IteratorOfDate[0]--;
                            if(IteratorOfDate[0] ==0)
                                sfnjkvs.setText("1st");
                            else if(IteratorOfDate[0] ==1)
                                sfnjkvs.setText("2nd");
                            else if(IteratorOfDate[0] ==2)
                                sfnjkvs.setText("3rd");
                            else
                                sfnjkvs.setText(String.valueOf(IteratorOfDate[0]+1)+"th");
                        }
                        else if(IteratorOfDate[0]==0){
                            IteratorOfDate[0] =30;
                            if(IteratorOfDate[0] ==0)
                                sfnjkvs.setText("1st");
                            else if(IteratorOfDate[0] ==1)
                                sfnjkvs.setText("2nd");
                            else if(IteratorOfDate[0] ==2)
                                sfnjkvs.setText("3rd");
                            else
                                sfnjkvs.setText(String.valueOf(IteratorOfDate[0]+1)+"th");
                        }
                    }
                    else {
                        if (IteratorOfTask[0] > 0) {
                            IteratorOfTask[0]--;
                            sfnjkvs.setText(String.valueOf(BIGDATA.get(IteratorOfTask[0]).Name));
                        } else if (IteratorOfTask[0] == 0) {
                            IteratorOfTask[0] = BIGDATA.size() - 1;
                            sfnjkvs.setText(String.valueOf(BIGDATA.get(IteratorOfTask[0]).Name));
                        }
                    }
                    f60038.performClick();
                }
            });
            bnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String KOP="2";
                    try {
                        FileInputStream fin = openFileInput("FS.txt");
                        int a;
                        StringBuilder temp = new StringBuilder();
                        while ((a = fin.read()) != -1) {
                            temp.append((char)a);
                        }
                        if(temp.toString()!="") {
                            KOP = temp.toString();
                        }
                        fin.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(KOP.equals("1")){
                        if(IteratorOfDate[0] <30){
                            IteratorOfDate[0]++;
                            if(IteratorOfDate[0] ==0)
                                sfnjkvs.setText("1st");
                            else if(IteratorOfDate[0] ==1)
                                sfnjkvs.setText("2nd");
                            else if(IteratorOfDate[0] ==2)
                                sfnjkvs.setText("3rd");
                            else
                                sfnjkvs.setText(String.valueOf(IteratorOfDate[0]+1)+"th");
                        }
                        else if(IteratorOfDate[0]==30){
                            IteratorOfDate[0] =0;
                            if(IteratorOfDate[0] ==0)
                                sfnjkvs.setText("1st");
                            else if(IteratorOfDate[0] ==1)
                                sfnjkvs.setText("2nd");
                            else if(IteratorOfDate[0] ==2)
                                sfnjkvs.setText("3rd");
                            else
                                sfnjkvs.setText(String.valueOf(IteratorOfDate[0]+1)+"th");
                        }
                    }
                    else{
                        if(IteratorOfTask[0] <BIGDATA.size()-1){
                            IteratorOfTask[0]++;
                            sfnjkvs.setText(String.valueOf(BIGDATA.get(IteratorOfTask[0]).Name));
                        }
                        else if(IteratorOfTask[0]==BIGDATA.size()-1){
                            IteratorOfTask[0]=0;
                            sfnjkvs.setText(String.valueOf(BIGDATA.get(IteratorOfTask[0]).Name));
                        }
                    }
                    f60038.performClick();
                }

            });
        }
        else{
            Toast.makeText(this, "No Data Rn, See ya Soon!", Toast.LENGTH_SHORT).show();
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

    {
//    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
//        private static final int SWIPE_THRESHOLD = 100;
//        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            boolean result = false;
//            try {
//                float diffY = e2.getY() - e1.getY();
//                float diffX = e2.getX() - e1.getX();
//                if (Math.abs(diffX) > Math.abs(diffY)) {
//                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffX > 0) {
//                            onSwipeRight();
//                        } else {
//                            onSwipeLeft();
//                        }
//                        result = true;
//                    }
//                }
//                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (diffY > 0) {
//                        //   onSwipeBottom();
//                    } else {
//                        //    onSwipeTop();
//                    }
//                    result = true;
//                }
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//            return result;
//        }
//    }
//
//    private void onSwipeRight() {
//        // Do something when the user swipes right
//        //Toast.makeText(this, "Swipe Right", Toast.LENGTH_SHORT).show();
//
//        TextView t342534 = findViewById(R.id.textView8);
//        if(t342534.getText()=="CALENDER VIEW"){
//            Toast.makeText(this, "Swipe Left", Toast.LENGTH_SHORT).show();
//            t342534.setText("TASK VIEW");
//        }
//        else if(t342534.getText()=="TASK VIEW"){
//            Toast.makeText(this, "Swipe Left", Toast.LENGTH_SHORT).show();
//            t342534.setText("CALENDER VIEW");
//        }
//    }
//
//    private void onSwipeLeft() {
//        // Do something when the user swipes left
//
//        TextView t342534 = findViewById(R.id.textView8);
//        if(t342534.getText()=="TASK VIEW"){
//            Toast.makeText(this, "Swipe Left", Toast.LENGTH_SHORT).show();
//            t342534.setText("CALENDER VIEW");
//        }
//        else if(t342534.getText()=="CALENDER VIEW"){
//            Toast.makeText(this, "Swipe Left", Toast.LENGTH_SHORT).show();
//            t342534.setText("TASK VIEW");
//        }
//    }
    }
    String[] ArrayMaker(String arr){
        if(arr!=""){
            int slashes=0;
            for (int i = 0; i < arr.length(); i++) {
                if(arr.charAt(i)=='|'){
                    slashes++;
                }
            }
            String divided[] = new String[slashes];
            int ii=0;
            for(int i=0;i<slashes;i++){
                divided[i]="";
            }
            for(int i=0;i<arr.length();i++){
                if(arr.charAt(i)=='|'){
                    ii++;
                }
                else{
                    divided[ii] = divided[ii]+arr.charAt(i);
                }
            }
            return divided;
        }
        return null;
    }
    private void toggle() {

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