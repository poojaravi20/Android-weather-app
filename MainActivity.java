package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar spinner;
    private RequestQueue requestQueue;
    private TextView currentLocation;
    private TextView currentTemp;
    private TextView currentWeather;
    private ImageView image;
    private ImageView humidity_card;
    private ImageView wind_card;
    private ImageView visibility_card;
    private ImageView pressure_card;
    private TextView humidityVal;
    private TextView windVal;
    private TextView visibilityVal;
    private TextView pressureVal;
    private String lat;
    private String lng;
    private String city;
    private String state;
    private String country;
    private CardView firstCard;
    private CardView secondCard;
    private CardView thirdCard;
    private TextView fetching;
    private RequestQueue autoQueue;
    private List<String> suggestions2 = new ArrayList<>();
    private ArrayAdapter suggestionsAdapter;
    ArrayAdapter myAdapter;
    private String displayLoc;
    private String temp;
    AutoCompleteTextView autoComplete;
    private Context context;
    private FloatingActionButton plusbutton;
    private boolean favorite;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        currentLocation = (TextView) findViewById(R.id.currentLocation);
        currentWeather = (TextView) findViewById(R.id.currentWeather);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        image = (ImageView) findViewById(R.id.imageView);
        humidity_card = (ImageView) findViewById(R.id.imageHumidity);
        wind_card = (ImageView) findViewById(R.id.imageWindSpeed);
        visibility_card = (ImageView) findViewById(R.id.imageVisibility);
        pressure_card = (ImageView) findViewById(R.id.imagePressure);
        humidityVal = (TextView) findViewById(R.id.humidityValue);
        pressureVal = (TextView) findViewById(R.id.pressureValue);
        visibilityVal = (TextView) findViewById(R.id.visibilityValue);
        windVal = (TextView) findViewById(R.id.windValue);
        secondCard = (CardView) findViewById(R.id.textView2);
        thirdCard = (CardView) findViewById(R.id.thirdCard);
        fetching = (TextView) findViewById(R.id.fetchingweather);
        plusbutton = (FloatingActionButton) findViewById(R.id.fab);
//        autoComplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
//        suggestions = new ArrayList<>();


        spinner.setVisibility(View.GONE);


        firstCard = (CardView) findViewById(R.id.firstCardView);
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
                    Toast.makeText(MainActivity.this, city +","+state + ","+country +" was added to favorites",
                            Toast.LENGTH_LONG).show();
                    plusbutton.setImageResource(R.drawable.remove_favorites);
                    favorite = true;
                }else{
                    Toast.makeText(MainActivity.this, city +","+state + ","+country +" was removed from favorites",
                            Toast.LENGTH_LONG).show();
                    plusbutton.setImageResource(R.drawable.add_favorites);
                    favorite = false;
                }

            }
        });

        requestQueue = Volley.newRequestQueue(this);
        autoQueue = Volley.newRequestQueue(this);
        spinner.setVisibility(View.VISIBLE);
        fetching.setVisibility(View.VISIBLE);
        firstCard.setVisibility(View.GONE);
        secondCard.setVisibility(View.GONE);
        thirdCard.setVisibility(View.GONE);

        getCurrentCoord();


        context = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
        final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);


        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setQuery(suggestions2.get(position), false);
            }
        });

        searchAutoComplete.setBackgroundColor(getResources().getColor(R.color.darkgrey));
        searchAutoComplete.setDropDownBackgroundResource(R.color.white);
        searchAutoComplete.setTextColor(getResources().getColor(R.color.white));
//        final ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,suggestions);
//        autoComplete.setAdapter(myAdapter);

//        int searchId = getResources().getIdentifier("android:id/search_button", null, null);
//        ImageView v = (ImageView) searchView.findViewById(searchId);
//        suggestionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, suggestions);
//        v.setImageResource(R.drawable.search);
//        suggestions.setAdapter(suggestionsAdapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("onQueryTextSubmit");
                Intent intent = new Intent(MainActivity.this, Searchable.class);
                Bundle bundle = new Bundle();
                bundle.putString("location", query);
                intent.putExtras(bundle);
                photos fragobj = new photos();
                fragobj.setArguments(bundle);
                startActivity(intent);
                return true;
//                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                suggestions2 = new ArrayList<>();
                String autoCompleteURL = "http://10.0.2.2:3000/complete?keyword=" + newText;
//                String autoCompleteURL = "http://nodejsandroid-csci571.us-east-2.elasticbeanstalk.com/complete?keyword=" + newText;
                final JsonObjectRequest autocomplete= new JsonObjectRequest(
                        Request.Method.GET,
                        autoCompleteURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject responseCurrent) {
                                JSONObject jsonObjectCurrent = responseCurrent;
                            try {
                                    int len = jsonObjectCurrent.getJSONArray("predictions").length();
                                    for(int i = 0; i < len; i++) {
                                        if (i < 5) {
                                            suggestions2.add((jsonObjectCurrent.getJSONArray("predictions").getJSONObject(i).getString("description")));
                                        }
                                    }
                                    System.out.println(suggestions2);
                                    myAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,suggestions2);
                                    searchAutoComplete.setAdapter(myAdapter);
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
                autoQueue.add(autocomplete);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCurrentCoord() {

        String CurrentLocation = "http://ip-api.com/json";
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                CurrentLocation,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject = response;
//                        System.out.println(jsonObject);
                        try {
                            city = jsonObject.getString("city");
//                            Log.i("city aaaa",city);
                            String countryCode = jsonObject.getString("countryCode");
                            String region = jsonObject.getString("region");
                            state = countryCode;
                            country = region;
                            displayLoc = city + ", " + region + ", " + countryCode;

                            currentLocation.append(displayLoc);
                            lat = jsonObject.getString("lat");
                            lng = jsonObject.getString("lon");
//                            Intent intent2 = new Intent(getApplicationContext(), DetailActivity.class);
//                            Bundle bundle2 = new Bundle();
//                            bundle2.putString("lat", lat);
//                            bundle2.putString("lng", lng);
//                            intent2.putExtras(bundle2);
                            getCurrDetails();
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

        requestQueue.add(objectRequest);


    }


    public void getCurrDetails(){
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
        requestQueue.add(currentWeatherInfo);

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
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("lat", lat);
        bundle2.putString("lng", lng);
        bundle2.putString("city", city);
        bundle2.putString("displayFullLoc", displayLoc);
        bundle2.putString("currentTemp", temp);
        intent.putExtras(bundle2);
        spinner.setVisibility(View.VISIBLE);
        fetching.setVisibility(View.VISIBLE);
        firstCard.setVisibility(View.GONE);
        secondCard.setVisibility(View.GONE);
        thirdCard.setVisibility(View.GONE);
        startActivity(intent);
        spinner.postDelayed(new Runnable() {
            public void run() {
                spinner.setVisibility(View.GONE);
                fetching.setVisibility(View.GONE);
                firstCard.setVisibility(View.VISIBLE);
                secondCard.setVisibility(View.VISIBLE);
                thirdCard.setVisibility(View.VISIBLE);
            }
        }, 10000);
//        spinner.setVisibility(View.GONE);
//        fetching.setVisibility(View.GONE);
//        firstCard.setVisibility(View.VISIBLE);
//        secondCard.setVisibility(View.VISIBLE);
//        thirdCard.setVisibility(View.VISIBLE);
    }

}
