package com.sda.fastlogistics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddString extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_string);
        TextView t007 = (TextView) findViewById(R.id.enteringstring);
        ArrayOfStuff THEARRAY = new ArrayOfStuff();
        Button b2007 = (Button) findViewById(R.id.button3);
        b2007.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t007.getText().toString().length()>1){
                    THEARRAY.adder(t007.getText().toString());
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("fatima.txt", Context.MODE_APPEND);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String A = THEARRAY.arr;
                    A = A.replaceAll("null","");
                    try {
                        fos.write(A.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ////////////////////////////////////////////////
                    FileOutputStream fos2 = null;
                    try {
                        fos2 = openFileOutput("harris.txt", Context.MODE_APPEND);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String A2 = THEARRAY.valarr;
                    A2 = A2.replaceAll("null","");
                    try {
                        fos2.write(A2.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos2.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent a = new Intent(AddString.this, Addprogress.class);
                    startActivity(a);

                }
                else{
                    Snackbar.make(view, "Name Must be of Length greater than 1", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }
}