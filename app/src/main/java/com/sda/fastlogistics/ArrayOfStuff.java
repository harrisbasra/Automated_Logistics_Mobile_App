package com.sda.fastlogistics;

import android.content.Context;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class ArrayOfStuff {
    String arr;
    String valarr;

    void adder(String array){
        if(arr!="" && arr!=null){
            arr = arr+array;
            arr = arr+"|";
            valarr = valarr + "0000000000000000000000000000000|";
        }
        else{
            arr = array;
            arr = arr+"|";
            valarr = "0000000000000000000000000000000|";
        }
    }
    boolean allowed(){
        if(arr==""||arr==null){
            return false;
        }
        return true;
    }
    boolean valallowed(){
        if(valarr==""||valarr==null){
            return false;
        }
        return true;
    }
    String[] arrayreturner(){
        if(allowed()){
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
    String[] valreturner(){
        if(valallowed()){
            int slashes=0;
            for (int i = 0; i < valarr.length(); i++) {
                if(valarr.charAt(i)=='|'){
                    slashes++;
                }
            }
            String divided[] = new String[slashes];
            int ii=0;
            for(int i=0;i<slashes;i++){
                divided[i]="";
            }
            for(int i=0;i<valarr.length();i++){
                if(valarr.charAt(i)=='|'){
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
    void MarkADay(int date, int idex){

        String Arrrr[] = valreturner();
        if(Arrrr!=null)
        {
            char one = '1';
            String presentValue = "";
            Arrrr[idex] = Arrrr[idex].substring(0,date-1)+one+Arrrr[idex].substring(date);
            valarr = ToOOString(Arrrr);
            return;
        }
    }
    String ToOOString(String rec[]){
        String H="";
        for(int i=0;i<rec.length;i++){
            H = H + rec[i] + "|";
        }
        return H;
    }
    public final String StringTBR(){
        return arr;
    }
}
