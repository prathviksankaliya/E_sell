package com.itcraftsolution.esell.Extra;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.itcraftsolution.esell.R;

public class LoadingDialog {

    private AlertDialog dialog;
    private Activity activity;
    public LoadingDialog(Activity myactivity) {
    activity = myactivity;
    }

    //Loading Dialog Start Method
    public void StartLoadingDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loadingdialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

    }

    //Loading Dialog Stop Method
    public void StopLoadingDialog()
    {
        dialog.dismiss();
    }
}
