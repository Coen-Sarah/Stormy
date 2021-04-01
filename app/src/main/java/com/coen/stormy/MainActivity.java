package com.coen.stormy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey = "e7451049a11c40868a0d4669371f3d04";
        Double lattitude = 35.7796;
        Double logitude = -78.6382;
        String forcastURL = "https://api.weatherbit.io/v2.0/current?lat=" +
                lattitude + "&lon=" + logitude + "&key=" + apiKey;

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forcastURL).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {

                        } else {
                            alertUserError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception Caught");
                    }
                }
            });
        }
        Log.v(TAG, "System UI running");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }else{
            networkUserError();
        }
        return isAvailable;
    }

    private void alertUserError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getSupportFragmentManager(),"error");
    }
    private void networkUserError(){
        NetworkDialogFragment dialog = new NetworkDialogFragment();
        dialog.show(getSupportFragmentManager(),"network_error");
    }
}