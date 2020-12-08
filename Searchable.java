package com.example.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Searchable extends AppCompatActivity {

    private RequestQueue geoQueue;
    private String lat;
    private String lng;
    private TextView currentLocation;
    private TextView currentTemp;
    private TextView currentWeather;
    private ImageView image;
    private TextView humidityVal;
    private TextView windVal;
    private TextView visibilityVal;
    private TextView pressureVal;
    private ImageView humidity_card;
    private ImageView wind_card;
    private ImageView visibility_card;
    private ImageView pressure_card;
    private CardView firstCard;
    private CardView secondCard;
    private CardView thirdCard;
    private ProgressBar spinner;
    private TextView fetching;
    private TextView searchResult;
    private String city;
    private String loc;
    private String temp;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private FloatingActionButton plusbutton;
    private boolean favorite;
//    private TextView displayLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        geoQueue = Volley.newRequestQueue(this);
        currentLocation = (TextView) findViewById(R.id.currentLocation);
        currentWeather = (TextView) findViewById(R.id.currentWeather);
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        image = (ImageView) findViewById(R.id.imageView);
        humidityVal = (TextView) findViewById(R.id.humidityValue);
        pressureVal = (TextView) findViewById(R.id.pressureValue);
        visibilityVal = (TextView) findViewById(R.id.visibilityValue);
        windVal = (TextView) findViewById(R.id.windValue);
        humidity_card = (ImageView) findViewById(R.id.imageHumidity);
        wind_card = (ImageView) findViewById(R.id.imageWindSpeed);
        visibility_card = (ImageView) findViewById(R.id.imageVisibility);
        pressure_card = (ImageView) findViewById(R.id.imagePressure);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        fetching = (TextView) findViewById(R.id.fetchingweather);
//        displayLocation = (TextView) findViewById(R.id.title);
        plusbutton = (FloatingActionButton) findViewById(R.id.fab);

        firstCard = (CardView) findViewById(R.id.firstCardView);
        secondCard = (CardView) findViewById(R.id.textView2);
        thirdCard = (CardView) findViewById(R.id.thirdCard);
        searchResult = (TextView) findViewById(R.id.searchresult);
        spinner.setVisibility(View.VISIBLE);
        fetching.setVisibility(View.VISIBLE);
        searchResult.setVisibility(View.GONE);
        firstCard.setVisibility(View.GONE);
        secondCard.setVisibility(View.GONE);
        thirdCard.setVisibility(View.GONE);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        Bundle bundle = getIntent().getExtras();
        loc = bundle.getString("location");

//        displayLocation.append(loc);


        firstCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        favorite = false;
        plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!favorite){
                    Toast.makeText(Searchable.this, loc + " was added to favorites",
                            Toast.LENGTH_LONG).show();
                    plusbutton.setImageResource(R.drawable.remove_favorites);
                    mEditor.putString(loc, lat + "," + lng);
                    mEditor.commit();
//                    mEditor.clear();
//                    mEditor.commit();
                    favorite = true;
                }else{
                    Toast.makeText(Searchable.this, loc +" was removed from favorites",
                            Toast.LENGTH_LONG).show();
                    plusbutton.setImageResource(R.drawable.add_favorites);
                    favorite = false;
                }

            }
        });

        getCurrDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportActionBar().setTitle(loc);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        if(id == android.R.id.home){
            super.onBackPressed();
//            startActivity(new Intent(this, MainActivity.class));
            startActivity(new Intent(this, Home.class));
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCurrDetails(){
        String[] cityValues = loc.split(",");
        city = cityValues[0];
        currentLocation.append(loc);

        Log.i("current Location: ",  loc);
        String geoURL = "http://10.0.2.2:3000/getCoordinate?address=" + "" + "," + loc;
//        String geoURL = "http://nodejsandroid-csci571.us-east-2.elasticbeanstalk.com/getCoordinate?address=" + "" + "," + loc;

        final JsonObjectRequest geoCoordinate= new JsonObjectRequest(
                Request.Method.GET,
                geoURL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseCurrent) {
                        JSONObject jsonObjectCurrent = responseCurrent;
                        //Log.i("response from geoURL:", jsonObjectCurrent.toString());

                        try {
                           // Log.i("response for lcoation: ", (jsonObjectCurrent.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").toString()));
                            lat = jsonObjectCurrent.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                            lng = jsonObjectCurrent.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
//                            Log.i("lat: ", lat);
//                            Log.i("lng: ", lng);

                            getGeoDetails();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }


        );
        geoQueue.add(geoCoordinate);

    }

    public void getGeoDetails(){
        String darkskyURL = "http://10.0.2.2:3000/getInfo?val=" + lat + "," + lng;
//        String darkskyURL = "http://nodejsandroid-csci571.us-east-2.elasticbeanstalk.com/getInfo?val=" + lat + "," + lng;
        final JsonObjectRequest currentWeatherInfo= new JsonObjectRequest(
                Request.Method.GET,
                darkskyURL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseCurrent) {
                        JSONObject jsonObjectCurrent = responseCurrent;
                        try {
                            String icon = jsonObjectCurrent.getJSONObject("currently").getString("icon");
                            String temperature = jsonObjectCurrent.getJSONObject("currently").getString("temperature");
                            String summary = jsonObjectCurrent.getJSONObject("currently").getString("summary");
//                            System.out.println(icon + temperature + summary);
                            currentWeather.append(summary);
                            temp = roundTemp(temperature);
                            currentTemp.append(temp + "˚F");
//                            image.setImageResource(R.drawable.sunny);
//                            System.out.println(icon + " " + temperature + " " + summary);
                            if(icon.equals("Clear-night")){
                                image.setImageResource(R.drawable.night);
                            }else if(icon.equals("rain")){
                                image.setImageResource(R.drawable.rainy);
                            }else if(icon.equals("sleet")){
                                image.setImageResource(R.drawable.snowy_rainy);
                            }else if (icon.equals("snow")){
                                image.setImageResource(R.drawable.snowy);
                            }else if(icon.equals("wind")){
                                image.setImageResource(R.drawable.windy_variant);
                            }else if(icon.equals("fog")){
                                image.setImageResource(R.drawable.fog);
                            }else if(icon.equals("cloudy")){
//                                System.out.println("here");
                                image.setImageResource(R.drawable.cloudy);
                            }else if(icon.equals("partly-cloudy-night")){
                                image.setImageResource(R.drawable.night_partly_cloudy);
                            }else if(icon.equals("partly-cloudy-day")){
                                image.setImageResource(R.drawable.partly_cloudy);
                            }else{
                                image.setImageResource(R.drawable.sunny);
                            }



                            humidity_card.setImageResource(R.drawable.humidity);
                            wind_card.setImageResource(R.drawable.wind_card);
                            visibility_card.setImageResource(R.drawable.visibility_card);
                            pressure_card.setImageResource(R.drawable.pressure_card);
                            ImageView infoCard = (ImageView) findViewById(R.id.infoImg);
                            infoCard.setImageResource(R.drawable.info_icon);

                            String humidityValue = jsonObjectCurrent.getJSONObject("currently").getString("humidity");
                            String windValue = jsonObjectCurrent.getJSONObject("currently").getString("windSpeed");
                            String visibilityValue = jsonObjectCurrent.getJSONObject("currently").getString("visibility");
                            String pressureValue = jsonObjectCurrent.getJSONObject("currently"). getString("pressure");

                            humidityVal.append(String.format("%.2f",Float.parseFloat(humidityValue))+ "%");
                            windVal.append(String.format("%.2f",Float.parseFloat(windValue)) + " mph");
                            visibilityVal.append(String.format("%.2f",Float.parseFloat(visibilityValue)) + " km");
                            pressureVal.append(String.format("%.2f",Float.parseFloat(pressureValue)) +  " mb");

                            TextView dateW1 = (TextView) findViewById(R.id.Date1);
                            dateW1.append(convertTime(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getString("time")));
                            TextView dateW2 = (TextView) findViewById(R.id.Date2);
                            dateW2.append(convertTime(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(1).getString("time")));
                            TextView dateW3 = (TextView) findViewById(R.id.Date3);
                            dateW3.append(convertTime(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(2).getString("time")));
                            TextView dateW4 = (TextView) findViewById(R.id.Date4);
                            dateW4.append(convertTime(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(3).getString("time")));
                            TextView dateW5 = (TextView) findViewById(R.id.Date5);
                            dateW5.append(convertTime(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(4).getString("time")));
                            TextView dateW6 = (TextView) findViewById(R.id.Date6);
                            dateW6.append(convertTime(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(5).getString("time")));
                            TextView dateW7 = (TextView) findViewById(R.id.Date7);
                            dateW7.append(convertTime(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(6).getString("time")));
                            TextView dateW8 = (TextView) findViewById(R.id.Date8);
                            dateW8.append(convertTime(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(7).getString("time")));


                            String strVal1 = roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getString("temperatureLow"));
                            ((TextView) findViewById(R.id.tempLow1)).append(strVal1);

                            String strVal2 = roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(1).getString("temperatureLow"));
                            ((TextView) findViewById(R.id.tempLow2)).append(strVal2);

                            String strVal3 = roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(2).getString("temperatureLow"));
                            ((TextView) findViewById(R.id.tempLow3)).append(strVal3);

                            String strVal4 = roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(3).getString("temperatureLow"));
                            ((TextView) findViewById(R.id.tempLow4)).append(strVal4);


                            ((TextView) findViewById(R.id.tempLow5)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(4).getString("temperatureLow")));
                            ((TextView) findViewById(R.id.tempLow6)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(5).getString("temperatureLow")));
                            ((TextView) findViewById(R.id.tempLow7)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(6).getString("temperatureLow")));
                            ((TextView) findViewById(R.id.tempLow8)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(7).getString("temperatureLow")));

                            ((TextView) findViewById(R.id.tempHigh1)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getString("temperatureHigh")));
                            ((TextView) findViewById(R.id.tempHigh2)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(1).getString("temperatureHigh")));
                            ((TextView) findViewById(R.id.tempHigh3)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(2).getString("temperatureHigh")));
                            ((TextView) findViewById(R.id.tempHigh4)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(3).getString("temperatureHigh")));
                            ((TextView) findViewById(R.id.tempHigh5)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(4).getString("temperatureHigh")));
                            ((TextView) findViewById(R.id.tempHigh6)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(5).getString("temperatureHigh")));
                            ((TextView) findViewById(R.id.tempHigh7)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(6).getString("temperatureHigh")));
                            ((TextView) findViewById(R.id.tempHigh8)).append(roundTemp(jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(7).getString("temperatureHigh")));

                            String img1 = jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getString("icon");
                            ImageView imgLayout1 = (ImageView) findViewById(R.id.image1);
                            fillupImage(img1, imgLayout1);

                            String img2 = jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(1).getString("icon");
                            ImageView imgLayout2 = (ImageView) findViewById(R.id.image2) ;
                            fillupImage(img2, imgLayout2);

                            String img3 = jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(2).getString("icon");
                            ImageView imgLayout3 = (ImageView) findViewById(R.id.image3) ;
                            fillupImage(img3, imgLayout3);

                            String img4 = jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(3).getString("icon");
                            ImageView imgLayout4 = (ImageView) findViewById(R.id.image4) ;
                            fillupImage(img4, imgLayout4);

                            String img5 = jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(4).getString("icon");
                            ImageView imgLayout5 = (ImageView) findViewById(R.id.image5) ;
                            fillupImage(img5, imgLayout5);

                            String img6 = jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(5).getString("icon");
                            ImageView imgLayout6 = (ImageView) findViewById(R.id.image6) ;
                            fillupImage(img6, imgLayout6);

                            String img7 = jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(6).getString("icon");
                            ImageView imgLayout7 = (ImageView) findViewById(R.id.image7) ;
                            fillupImage(img7, imgLayout7);

                            String img8 = jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(7).getString("icon");
                            ImageView imgLayout8 = (ImageView) findViewById(R.id.image8) ;
                            fillupImage(img8, imgLayout8);

                            spinner.setVisibility(View.GONE);
                            fetching.setVisibility(View.GONE);
                            searchResult.setVisibility(View.VISIBLE);
                            firstCard.setVisibility(View.VISIBLE);
                            secondCard.setVisibility(View.VISIBLE);
                            thirdCard.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }


        );
        geoQueue.add(currentWeatherInfo);

    }

    public void fillupImage(String imageName, ImageView imgPlace){
        if(imageName.equals("Clear-night")){
            imgPlace.setImageResource(R.drawable.night);
        }else if(imageName.equals("rain")){
            imgPlace.setImageResource(R.drawable.rainy);
        }else if(imageName.equals("sleet")){
            imgPlace.setImageResource(R.drawable.snowy_rainy);
        }else if (imageName.equals("snow")){
            imgPlace.setImageResource(R.drawable.snowy);
        }else if(imageName.equals("wind")){
            imgPlace.setImageResource(R.drawable.windy_variant);
        }else if(imageName.equals("fog")){
            imgPlace.setImageResource(R.drawable.fog);
        }else if(imageName.equals("cloudy")){
            imgPlace.setImageResource(R.drawable.cloudy);
        }else if(imageName.equals("partly-cloudy-night")){
            imgPlace.setImageResource(R.drawable.night_partly_cloudy);
        }else if(imageName.equals("partly-cloudy-day")){
            imgPlace.setImageResource(R.drawable.partly_cloudy);
        }else{
            imgPlace.setImageResource(R.drawable.sunny);
        }
    }

    public String roundTemp(String tmp){
        return String.valueOf ((int) Math.round(Double.parseDouble(tmp)));
    }

    public String convertTime(String dt){
        Date expiry = new Date(Long.parseLong(dt) * 1000);
        int dat = expiry.getDate();
        int mth = expiry.getMonth();
        int yr = expiry.getYear() + 1900;
        String retDate = mth + "/" + dat + "/" + yr;
        return retDate;
    }

    public void openActivity2(){
//        Intent intent = new Intent(this, DetailActivity.class);
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("lat", lat);
        bundle2.putString("lng", lng);
        bundle2.putString("city", city);
        bundle2.putString("displayFullLoc", loc);
        bundle2.putString("currentTemp", temp);
        intent.putExtras(bundle2);
//        spinner.setVisibility(View.VISIBLE);
//        fetching.setVisibility(View.VISIBLE);
//        firstCard.setVisibility(View.GONE);
//        secondCard.setVisibility(View.GONE);
//        thirdCard.setVisibility(View.GONE);
        startActivity(intent);
//        spinner.postDelayed(new Runnable() {
//            public void run() {
//                spinner.setVisibility(View.GONE);
//                fetching.setVisibility(View.GONE);
//                firstCard.setVisibility(View.VISIBLE);
//                secondCard.setVisibility(View.VISIBLE);
//                thirdCard.setVisibility(View.VISIBLE);
//            }
//        }, 10000);
    }

}
