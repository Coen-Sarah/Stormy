package com.coen.stormy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LocationDialogFragment extends DialogFragment {

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder locationBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        locationBuilder.setTitle("Update Location")
                .setView(inflater.inflate(R.layout.locationfragment,null))
                .setPositiveButton("Update",null);
        return locationBuilder.create();

    }

}
