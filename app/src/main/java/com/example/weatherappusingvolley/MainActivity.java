package com.example.weatherappusingvolley;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherappusingvolley.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        am = ActivityMainBinding.inflate(getLayoutInflater());
       // setContentView(R.layout.activity_main);
        setContentView(am.getRoot());

        if( ! CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            checkInternetEntering();
        }

        am.amBtnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissKeyboard(MainActivity.this); // to dismiss key
                String apiKey = "9760ba6f5170f47a7a52b9a340e50c53";
                String cityName = am.amEdtEnterCity.getText().toString();
                String url = "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=";

                // to request for data
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // find the json object where your data is
                        try {
                            JSONObject object = response.getJSONObject("main");
                            String temp = object.getString("temp");
                            String humid = object.getString("humidity");
                            Double cel = Double.parseDouble(temp)-273.15;
                            String celcius = cel.toString().substring(0,5);

                            JSONObject object2 = response.getJSONObject("wind");
                            String speed = object2.getString("speed");
                            am.amTxtSetTemp.setText(celcius+ " °");
                            am.amTxtSetHumid.setText(humid);
                            am.amTxtSetWind.setText(speed+" ms");

                            // to get the json array
                            JSONArray jsonArray = response.getJSONArray("weather");
                            JSONObject mousam = jsonArray.getJSONObject(0);
                            am.amTxtSetWeather.setText(mousam.getString("main"));


                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                        new Response.ErrorListener() { // to check error
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Pleas enter Valid Country", Toast.LENGTH_SHORT).show();
                    }
                } );
                queue.add(request);
            }
        });

        // TODO : get the json array lesson and try to implement it. https://jsonformatter.org/


    }


    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
            new AlertDialog.Builder(this) //alert the person knowing they are about to close
                    .setTitle("EXIT")
                    .setMessage("Are you sure. You want to close this app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
    }

    void checkInternetEntering()
    {
        //if there is no internet do this
        setContentView(R.layout.activity_main);

        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher_round)//alert the person knowing they are about to close
                .setTitle("No internet connection available")
                .setMessage("Please Check you're Mobile data or Wifi network.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

}


class CheckNetwork {

    private static final String TAG = CheckNetwork.class.getSimpleName();

    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Log.d(TAG,"no internet connection");
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.d(TAG," internet connection available...");
                return true;
            }
            else
            {
                Log.d(TAG," internet connection");
                return true;
            }

        }
    }
}

/*{
  "coord": {
    "lon": 77.2167,
    "lat": 28.6667
  },
  "weather": [
    {
      "id": 721,
      "main": "Haze",
      "description": "haze",
      "icon": "50n"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 301.2,
    "feels_like": 304.47,
    "temp_min": 301.2,
    "temp_max": 301.2,
    "pressure": 1007,
    "humidity": 74
  },
  "visibility": 3500,
  "wind": {
    "speed": 1.54,
    "deg": 320
  },
  "clouds": {
    "all": 0
  },
  "dt": 1633627223,
  "sys": {
    "type": 1,
    "id": 9165,
    "country": "IN",
    "sunrise": 1633567627,
    "sunset": 1633609837
  },
  "timezone": 19800,
  "id": 1273294,
  "name": "Delhi",
  "cod": 200
}*/
