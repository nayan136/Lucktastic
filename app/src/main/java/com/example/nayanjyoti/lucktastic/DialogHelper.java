package com.example.nayanjyoti.lucktastic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DialogHelper {
    public static ProgressDialog dialog = null;
    private static AlertDialog noInternetDialog;

    public static void createDialog(Context context){
        if(dialog != null){
            dialog = null;
        }
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading. Please wait...");
    }
    public static void showDialog(){
        dialog.show();
    }

    public static void cancelDialog(){
        dialog.dismiss();
        //dialog = null;
    }

    public static ProgressDialog getInstance(){
        return dialog;
    }

    public static void setNoInternetDialogCreate(Context context){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater li = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = li.inflate(R.layout.layout_dialog_no_internet,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        noInternetDialog = dialogBuilder.create();
    }

    public static void showNoInternetDialog(){
        noInternetDialog.show();
    }

    public static void cancelNoInternetDialog(){
        noInternetDialog.dismiss();
    }
}
