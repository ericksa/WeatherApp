package com.a_erickson.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    EditText cityName;
    TextView resultTextView;
    private WeatherAdapter adapter;

    public void findWeather(View view) {
        Log.i("CityName", cityName.getText().toString());

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

        try {
            String encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");

            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + ",us&units=imperial&APPID=382c108de9814dc1715cd1145b8875d6");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = (EditText) findViewById(R.id.cityName);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        adapter = new WeatherAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            Log.i("Website Content", result);
            try {
                DateFormat df = DateFormat.getTimeInstance();
                String message = "";
                JSONObject jsonObject = new JSONObject(result);
                String all = jsonObject.toString();
                Log.i("ALL AGAIN", all);

                String sunrise = df.format(new Date(jsonObject.getJSONObject("sys").getLong("sunrise") * 1000).getTime());
                String sunset = df.format(new Date(jsonObject.getJSONObject("sys").getLong("sunset") * 1000).getTime());
                Long pressure = jsonObject.getJSONObject("main").getLong("pressure");
                Long humidity = jsonObject.getJSONObject("main").getLong("humidity");
                String temp = jsonObject.getJSONObject("main").getString("temp");
                String maxTemp = jsonObject.getJSONObject("main").getString("temp_max");
                String minTemp = jsonObject.getJSONObject("main").getString("temp_min");
                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                String icon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                icon = Data.ICON_ADDR + icon + ".png";


                message += description + "\r\nTemp " + temp + "°F \r\n Need to download image - " + icon + " \r\nMin "
                        + minTemp + "°F\r\nMax " + maxTemp + "°F\r\n" + pressure + " Pressure\r\n" + " Humidity " + humidity + "%"
                        + "\r\n Sunrise " + sunrise + " \r\n" + "Sunset " + sunset;


                if (message != "") {
                    resultTextView.setText(message);
                } else {
                    Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);
                }


            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);
            }


        }
    }
}
