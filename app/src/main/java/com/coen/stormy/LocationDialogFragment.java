
package com.coen.stormy;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.HashMap;

import com.coen.stormy.MainActivity;

public class LocationDialogFragment extends DialogFragment {
    HashMap userLocation = new HashMap<String,String>();
    Button gpsLocation;
    Button updateLocation;


    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Context context = getActivity();

        ActivityResultCallback resultCallback = new ActivityResultCallback() {
            @Override
            public void onActivityResult(Object result) {

            }
        };

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.locationfragment,null);

        gpsLocation = view.findViewById(R.id.gpsLocationButton);
        updateLocation = view.findViewById(R.id.updateLocationButtton);

        gpsLocation.setOnClickListener(v -> {
            Log.d("location","gps button clicked");
            System.out.println("gps button clicked");

            boolean courseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;


            if (fineLocationPermission && courseLocationPermission){
                getLocationData();

            }else{
                Toast.makeText(context, "Please enable location data to use the GPS feature.",Toast.LENGTH_SHORT).show();

            }

        });

        updateLocation.setOnClickListener(v -> {
            Log.d("location","updatelocation button clicked");
            EditText cityText = view.findViewById(R.id.cityEditText);
            EditText stateText = view.findViewById(R.id.stateEditText);
            EditText countryText = view.findViewById(R.id.countryEditText);
            EditText zipCodeText = view.findViewById(R.id.zipcodeEditText);
            String city = cityText.getText().toString();
            String state = stateText.getText().toString();
            String country = countryText.getText().toString();
            String zipCode = zipCodeText.getText().toString();

            if( !city.isEmpty()){
                Log.d("location",city);
                userLocation.put("City",city);
                if(!state.isEmpty()){
                    Log.d("location",state);
                    userLocation.put("State",state);
                }
                if(!country.isEmpty()){
                    Log.d("location", country);
                    userLocation.put("Country", country);
                }

            }
            else if(!zipCode.isEmpty())
            {
                Log.d("location", zipCode);
                userLocation.put("ZipCode", zipCode);
            }
            else{
                Toast.makeText(context,"Please enter city and state or zip code.",Toast.LENGTH_LONG).show();
            }
            if(!userLocation.isEmpty()){
                MainActivity.setUserLocation(userLocation);
                LocationDialogFragment.this.dismiss();
            }
        });

        AlertDialog.Builder locationBuilder = new AlertDialog.Builder(context);
        locationBuilder.setTitle("Update Location")
                .setView(view);

        return locationBuilder.create();

    }

    private void getLocationData(){
        Log.d("LocationFragment","clicked gps button");
    }

    @Override
    public void dismiss(){
        onDismiss(this);
        super.dismiss();

    }

    public void onDismiss(LocationDialogFragment dialog)
    {
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((MainActivity)activity).handleDialogClose(dialog);
    }

}
