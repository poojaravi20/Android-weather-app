package com.example.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.ui.main.SectionsPagerAdapter2;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private LineChart mChart;

    private String lat;
    private String lng;
    private String city;
    private RequestQueue requestCurQueue;
    private String fullLocation;
    private TextView title;
    private ImageButton back_button;
    private ImageButton twitterButton;
    private String tweet;
    private String temp;
    private ProgressBar spinner;
    private TextView fetching;
    private CardView wind;
    private CardView pressure;
    private CardView precipitation;
    private CardView tempCard;
    private CardView icon;
    private CardView humidity;
    private CardView visibility;
    private CardView cloud;
    private CardView ozone;
    private TabLayout tab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        SectionsPagerAdapter2 sectionsPagerAdapter2 = new SectionsPagerAdapter2(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter2);
        viewPager.setOffscreenPageLimit(3);
        TabLayout tabs = findViewById(R.id.tabs);
//        System.out.println(tabs.getTabAt(0));
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0). setIcon(R.drawable.calender_today);
        tabs.getTabAt(1).setIcon(R.drawable.trending_up);
        tabs.getTabAt(2).setIcon(R.drawable.google_photos);
        FloatingActionButton fab = findViewById(R.id.fab);
        title = (TextView) findViewById(R.id.title);
        twitterButton = (ImageButton) findViewById(R.id.twitter);
        back_button = (ImageButton) findViewById(R.id.back_button);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        fetching = (TextView) findViewById(R.id.fetchingweather);
        wind = (CardView) findViewById(R.id.windCardView);
        pressure = (CardView) findViewById(R.id.pressureCardView);
        precipitation = (CardView) findViewById(R.id.precipCardView);
        tempCard = (CardView) findViewById(R.id.tempCardView);
        icon = (CardView) findViewById(R.id.iconCardView);
        humidity = (CardView) findViewById(R.id.humCardView);
        visibility = (CardView) findViewById(R.id.visibilityCardView);
        cloud = (CardView) findViewById(R.id.cloudCoverCardView);
        ozone = (CardView) findViewById(R.id.ozoneCardView);
        tab = (TabLayout) findViewById(R.id.tabs);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityMain();
            }
        });

        requestCurQueue = Volley.newRequestQueue(this);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Bundle bundle2 = getIntent().getExtras();
        lat = bundle2.getString("lat");
        lng = bundle2.getString("lng");
        city = bundle2.getString("city");
        fullLocation = bundle2.getString("displayFullLoc");
        temp = bundle2.getString("currentTemp");
        title.append(fullLocation);
//        String loc = bundle2.getString("location");
//        Log.i("city", city);
//        Log.i("lng", lng);
//        Log.i("location is", fullLocation);

        Bundle cityBundle = new Bundle();
        cityBundle.putString("city", city);
        photos fragObj = photos.newInstance(cityBundle);

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTweet();
            }
        });

//        spinner.setVisibility(View.VISIBLE);
//        fetching.setVisibility(View.VISIBLE);

//        icon.setVisibility(View.GONE);
//        findViewById(R.id.cloudCoverCardView).setVisibility(View.GONE);
//        findViewById(R.id.humCardView).setVisibility(View.GONE);
//        findViewById(R.id.tempCardView).setVisibility(View.GONE);
//        findViewById(R.id.ozoneCardView).setVisibility(View.GONE);
//        findViewById(R.id.precipCardView).setVisibility(View.GONE);
//        findViewById(R.id.visibilityCardView).setVisibility(View.GONE);
//        findViewById(R.id.pressureCardView).setVisibility(View.GONE);
//        findViewById(R.id.windCardView).setVisibility(View.GONE);
//        fragObj.setArguments(cityBundle);


        getDetails();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
//        item.expandActionView();
//        searchView.setQuery(fullLocation, true);
//        searchView.clearFocus();
        getSupportActionBar().hide();
        tab.setSelectedTabIndicatorColor(Color.parseColor("#242121"));
//        getSupportActionBar().setHomeButtonEnabled(true);
//       searchView.setIconified(false);
//        int searchId = getResources().getIdentifier("android:id/search_button", null, null);
//        ImageView v = (ImageView) searchView.findViewById(searchId);
//        v.setImageResource(R.drawable.search);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Intent intent = new Intent(getApplicationContext(), Searchable.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("location", query);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                return true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        if(id == android.R.id.home){
            super.onBackPressed();
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void getDetails(){
        final ArrayList<Float> tempLow = new ArrayList<>();
        final ArrayList<Float> tempHigh = new ArrayList<>();

        tab.setVisibility(View.GONE);
        String darkskyURL = "http://10.0.2.2:3000/getInfo?val=" + lat + "," + lng;
//        String darkskyURL = "http://nodejsandroid-csci571.us-east-2.elasticbeanstalk.com/getInfo?val=" + lat + "," + lng;
        final JsonObjectRequest currentInfo= new JsonObjectRequest(
                Request.Method.GET,
                darkskyURL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseCurrent) {
                        JSONObject jsonObjectCurrent = responseCurrent;
//                        System.out.println("inside the onresponse for getdetails");
//                        System.out.print(jsonObjectCurrent);
                        //System.out.println("outside the on response for getdetails");
                        try {
                            ImageView humImage = (ImageView) findViewById(R.id.imghumid);
                            humImage.setImageResource(R.drawable.humidity);

                            ImageView presImage = (ImageView) findViewById(R.id.imgPressure);
                            presImage.setImageResource(R.drawable.pressure_card);

                            ImageView windImage = (ImageView) findViewById(R.id.imgWind);
                            windImage.setImageResource(R.drawable.wind_card);

                            ImageView precImage = (ImageView) findViewById(R.id.imgprecep);
                            precImage.setImageResource(R.drawable.rain_card);

                            ImageView tempImage = (ImageView) findViewById(R.id.imgtemp);
                            tempImage.setImageResource(R.drawable.temperature_card);

                            ImageView visImage = (ImageView) findViewById(R.id.imgvisible);
                            visImage.setImageResource(R.drawable.visibility_card);

                            ImageView cloudImage = (ImageView) findViewById(R.id.imgcloud);
                            cloudImage.setImageResource(R.drawable.cloudcover_card);

                            ImageView ozoneImage = (ImageView) findViewById(R.id.imgozone);
                            ozoneImage.setImageResource(R.drawable.ozone_card);


                            String windSpeed = jsonObjectCurrent.getJSONObject("currently").getString("windSpeed");
                            TextView wind = (TextView) findViewById(R.id.windCardText);

                            String pressure = jsonObjectCurrent.getJSONObject("currently").getString("pressure");
                            TextView press = (TextView) findViewById(R.id.pressureCardText);

                            String precep = jsonObjectCurrent.getJSONObject("currently").getString("precipIntensity");
                            TextView pre = (TextView) findViewById(R.id.precepCardText);

                            String tmp = jsonObjectCurrent.getJSONObject("currently").getString("temperature");
                            TextView temp = (TextView) findViewById(R.id.tempCardText);

                            String humid = jsonObjectCurrent.getJSONObject("currently").getString("humidity");
                            TextView hum = (TextView) findViewById(R.id.humCardText);

                            String visi = jsonObjectCurrent.getJSONObject("currently").getString("visibility");
                            TextView visiText = (TextView) findViewById(R.id.visibleCardText);

                            String cloudCover = jsonObjectCurrent.getJSONObject("currently").getString("cloudCover");
                            TextView cloud = (TextView) findViewById(R.id.cloudCardText);

                            String ozone = jsonObjectCurrent.getJSONObject("currently").getString("ozone");
                            TextView oz = (TextView) findViewById(R.id.ozoneCardText);


                            if(!windSpeed.equals(null)){
                                wind.append(String.format("%.2f",Float.parseFloat(windSpeed)) + " mph");
                            }else if(windSpeed.equals(null)){
                                wind.append("0");
                            }

                            if(!pressure.equals(null)){
                                press.append(String.format("%.2f",Float.parseFloat(pressure)) + " mb");
                            }else if(pressure.equals(null)){
                                press.append("0");
                            }

                            if(!precep.equals(null)){
                                pre.append(String.format("%.2f",Float.parseFloat(precep)) + " mmph");
                            }else if(precep.equals(null)){
                                pre.append("0");
                            }

                            if(!tmp.equals(null)){
                                temp.append(roundTemp(tmp) + "˚F");
                            }else if(tmp.equals(null)){
                                temp.append("0");
                            }

                            if(!humid.equals(null)){
                                hum.append(String.format("%.2f",Float.parseFloat(humid)) + "%");
                            }else if(humid.equals(null)){
                                hum.append("0");
                            }

                            if(!visi.equals(null)){
                                visiText.append(String.format("%.2f",Float.parseFloat(visi)) + " km");
                            }else if(visi.equals(null)){
                                visiText.append("0");
                            }

                            if(!cloudCover.equals(null)){
                                cloud.append(String.format("%.2f",Float.parseFloat(cloudCover)) + "%");
                            }else if(windSpeed.equals(null)){
                                cloud.append("0");
                            }

                            if(!ozone.equals(null)){
                                oz.append(String.format("%.2f",Float.parseFloat(ozone)) + " DU");
                            }else if(windSpeed.equals(null)){
                                oz.append("0");
                            }

                            String icon = jsonObjectCurrent.getJSONObject("currently").getString("icon");
                            ImageView todayImage = findViewById(R.id.imageToday);
                            loadImage(icon, todayImage);

                            String summary = jsonObjectCurrent.getJSONObject("currently").getString("icon");
                            TextView displaysummary = (TextView) findViewById(R.id.summaryToday);
                            if(summary.equals("partly-cloudy-day")){
                                displaysummary.append("cloudy day");
                            }else if(summary.equals("partly-cloudy-night")){
                                displaysummary.append("cloudy night");
                            }else{
                                displaysummary.append(summary.replace("-", " "));
                            }

                            String weeklysum = jsonObjectCurrent.getJSONObject("daily").getString("summary");
                            String weeklyic = jsonObjectCurrent.getJSONObject("daily").getString("icon");
                            weekly(weeklyic, weeklysum);
//                            Log.i("value", windSpeed + temperature + summary);

//                            JSONObject temps = jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(0);
//                            Log.i("temps Array: ", temps.toString());

                            for(int i = 0; i < 8; i++){
                                JSONObject temps = jsonObjectCurrent.getJSONObject("daily").getJSONArray("data").getJSONObject(i);
                                tempLow.add((float) Double.parseDouble(temps.getString("temperatureLow")));
                                tempHigh.add((float) Double.parseDouble(temps.getString("temperatureHigh")));
                            }

                            createChart(tempLow, tempHigh);
                            tab.postDelayed(new Runnable() {
                                public void run() {
                                    tab.setVisibility(View.VISIBLE);
                                }
                            }, 2500);


//                            spinner.setVisibility(View.GONE);
//                            fetching.setVisibility(View.GONE);
//
//                            Log.i("tempLowArray: ", tempLow.toString());
//                            Log.i("tempHighArray: ", tempHigh.toString());
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
        requestCurQueue.add(currentInfo);

    }

    public void weekly(String icon, String summary){
        ImageView icon2 = (ImageView) findViewById(R.id.weekIcon);
        TextView weekText = (TextView) findViewById(R.id.weekSummary);
        weekText.append(summary);
        if(icon.equals("Clear-night")){
            icon2.setImageResource(R.drawable.night);
        }else if(icon.equals("rain")){
            icon2.setImageResource(R.drawable.rainy);
        }else if(icon.equals("sleet")){
            icon2.setImageResource(R.drawable.snowy_rainy);
        }else if (icon.equals("snow")){
            icon2.setImageResource(R.drawable.snowy);
        }else if(icon.equals("wind")){
            icon2.setImageResource(R.drawable.windy_variant);
        }else if(icon.equals("fog")){
            icon2.setImageResource(R.drawable.fog);
        }else if(icon.equals("cloudy")){
            icon2.setImageResource(R.drawable.cloudy);
        }else if(icon.equals("partly-cloudy-night")){
            icon2.setImageResource(R.drawable.night_partly_cloudy);
        }else if(icon.equals("partly-cloudy-day")){
            icon2.setImageResource(R.drawable.partly_cloudy);
        }else{
            icon2.setImageResource(R.drawable.sunny);
        }


    }

    public void loadImage(String imageName, ImageView imgPlace){
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

    public void createChart(ArrayList<Float> tempLow, ArrayList<Float> tempHigh){
        mChart = (LineChart) findViewById(R.id.lineChart);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        ArrayList<Entry> lowValues = new ArrayList<>();
        ArrayList<Entry> highValues = new ArrayList<>();
        for(int i = 0 ; i < 8; i ++){
            lowValues.add(new Entry(i, tempLow.get(i)));
            highValues.add(new Entry(i, tempHigh.get(i)));
        }

        LineDataSet set1 = new LineDataSet(lowValues, "Minimum Temperature");
        LineDataSet set2 = new LineDataSet(highValues, "Maximum Temperature");
        set1.setFillAlpha(110);
        set2.setFillAlpha(110);
        set1.setColor(Color.rgb(166,72,196));
        set2.setColor(Color.rgb(255,176,33));
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(2f);


        LimitLine upperLimit = new LimitLine(80, "");
        upperLimit.setLineColor(Color.WHITE);
//        upperLimit.setLineWidth(4f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upperLimit);
//        leftAxis.setAxisMaximum(90);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);

        mChart.getAxisRight().setTextColor(Color.WHITE);
        mChart.getAxisLeft().setTextColor(Color.WHITE);
        mChart.getLegend().setTextColor(Color.WHITE);
        mChart.getXAxis().setTextColor(Color.WHITE);
        mChart.getLegend().setTextSize(15f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);

        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.invalidate();

    }

    public void openActivityMain(){
        Intent intent = new Intent(this, Home.class);
//        Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        this.finish();
    }


    public void displayTweet(){
        tweet = "Check Out " + fullLocation + "\'s Weather! It is " + temp + "˚F!" + "&hashtags=CSCI571WeatherSearch";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=" + tweet)));
    }

    public String roundTemp(String tmp){
        return String.valueOf ((int) Math.round(Double.parseDouble(tmp)));
    }


}