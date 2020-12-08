package com.example.weatherapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link photos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link photos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class photos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static photos fragment;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private int[] images = {R.drawable.weatherlogo,R.drawable.darkskylogo};
    private ArrayList<String> imageList;
    private RequestQueue imagesQueue;
    private String city;

    private OnFragmentInteractionListener mListener;

    public photos() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment photos.
     */
    // TODO: Rename and change types and number of parameters
    public static photos newInstance(Bundle bundle) {
        fragment = new photos();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        imagesQueue = Volley.newRequestQueue(this.getContext());
        imageList = new ArrayList<>();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photos, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this.getContext(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //set Adaoter here
        adapter = new RecyclerAdapter(imageList);
        recyclerView.setAdapter(adapter);


        Bundle bundle = fragment.getArguments();
        if(fragment.getArguments() != null){
            city = bundle.getString("city");
//            System.out.println(strtext);
        }
//        System.out.println("Current city"+ strtext);
        populateImages();
        return v;
//        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void populateImages(){

        String imageURL = "http://10.0.2.2:3000/getCityImage?q=" + city;
//        String imageURL = "http://nodejsandroid-csci571.us-east-2.elasticbeanstalk.com/getCityImage?q=" + city;
//        System.out.println(imageURL);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                imageURL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject = response;
//                        Log.i("images: ", jsonObject.toString());
                        System.out.println(jsonObject);
                        try {

//                            Log.i("First img: ", img);
                            for(int i = 0 ; i < 8; i++){
                                String img = jsonObject.getJSONArray("items").getJSONObject(i).getString("link");
                                imageList.add(img);
                            }

//                            Log.i("Image Array is: ", imageList.toString());
                            adapter = new RecyclerAdapter(imageList);
//                            Log.i("Array of images is:", imageList.toString());
                            recyclerView.setAdapter(adapter);
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

        imagesQueue.add(objectRequest);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
