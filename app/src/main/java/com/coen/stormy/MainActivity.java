package com.coen.stormy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coen.stormy.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity<imageView> extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather currentWeather;
    private ImageView icon;
    private enum LocationType {ZIP_CODE, CITY, GPS};
    private String city, state, country;
    private String zipCode;
    private String apiLocationString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserLocation();
        getForecast();

    }

    public void openLocationDialog(View view) {
        //creates dialogfragment w/ userinput for city/state autofill
        LocationDialogFragment locationDialogFragment = new LocationDialogFragment();
        locationDialogFragment.show(getSupportFragmentManager(), "location");

        getUserLocation();
    }

    private void getUserLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //ActivityResultLauncher<String[]> locationPermissionRequest =

        //check permissions
        //if avaliable
               // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        //run this else

        //ask for location

    }
    //calls API depending on type of location information provided
    private void createAPILocationString(LocationType locationType){
        switch (locationType) {
            case ZIP_CODE:
                int zipCode = 77303;
                apiLocationString = "&postal_code=" + zipCode;
                break;
            case CITY:
                String city = "Houston";
                String state = "TX";
                String country = "US";
                apiLocationString = "&city=" + city + "," + state;
                if (!country.isEmpty()) {
                    apiLocationString += "&country=" + country;
                }
                break;
            case GPS:
                Double latitude = 40.7128;
                Double longitude = -78.6382;
                apiLocationString = "lat=" + latitude + "&lon=" + longitude;
                break;
        }
    }

    // uses WeatherBit API to get current weather at location
    private void getForecast() {
        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        TextView weatherBit = findViewById(R.id.legalView);
        icon = findViewById(R.id.iconImageView);
        weatherBit.setMovementMethod(LinkMovementMethod.getInstance());

        String apiKey = "e7451049a11c40868a0d4669371f3d04";
        createAPILocationString(LocationType.GPS);
        String forecastURL = "https://api.weatherbit.io/v2.0/current?"+ apiLocationString + "&key=" + apiKey + "&units=I";

        //calls API
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastURL).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        String JSONData = response.body().string();
                        Log.v(TAG, JSONData);
                        if (response.isSuccessful()) {
                            currentWeather = getCurrentDetails(JSONData);

                        // getting data from api call

                            CurrentWeather displayWeather = new CurrentWeather(
                                    currentWeather.getLocationLabel(),
                                    currentWeather.getIcon(),
                                    currentWeather.getLocalTime(),
                                    currentWeather.getTemperature(),
                                    currentWeather.getHumidity(),
                                    currentWeather.getPrecipChance(),
                                    currentWeather.getSummary(),
                                    currentWeather.getTimeZone()
                            );

                            binding.setWeather(displayWeather);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int resID = getResources().getIdentifier(displayWeather.getIcon() , "drawable", getPackageName());
                                    icon.setImageResource(resID);
                                }
                            });
                        } else {
                            Log.d(TAG,"error");
                            alertUserError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception Caught");
                    } catch (JSONException e){
                        Log.e(TAG, "JSON Exception Caught");
                    }
                }
            });
        }
        Log.v(TAG, "System UI running");
    }

    private CurrentWeather getCurrentDetails(String JSONData) throws JSONException {
        // Alternating objects and array are used to get to the object nested in the array
        JSONObject forecast = new JSONObject(JSONData);
        JSONArray data = forecast.getJSONArray("data");
        JSONObject current = data.getJSONObject(0);
        JSONObject weather_icon = current.getJSONObject("weather");

        CurrentWeather currentWeather = new CurrentWeather();
        String location = current.getString("city_name") + ", " + current.getString("state_code");
        currentWeather.setTimeZone(current.getString("timezone"));
        currentWeather.setLocationLabel(location);
        currentWeather.setTemperature(current.getDouble("temp"));
        currentWeather.setHumidity(current.getDouble("rh"));
        currentWeather.setPrecipChance(current.getDouble("precip"));
        currentWeather.setSummary(weather_icon.getString("description"));
        currentWeather.setIcon(weather_icon.getString("icon"));
        Log.v(TAG,currentWeather.getLocalTime());
        return currentWeather;
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
    public void refreshOnclick(View view){
        getForecast();
        Toast.makeText(this, "Refreshing Data", Toast.LENGTH_SHORT).show();
    }
}