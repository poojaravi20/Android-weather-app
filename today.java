package com.example.weatherapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link today.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link today#newInstance} factory method to
 * create an instance of this fragment.
 */
public class today extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public today() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment today.
     */
    // TODO: Rename and change types and number of parameters
    public static today newInstance(String param1, String param2) {
        today fragment = new today();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spinner = (ProgressBar) getView().findViewById(R.id.progressBar1);
        fetching = (TextView) getView().findViewById(R.id.fetchingweather);
        wind = (CardView) getView().findViewById(R.id.windCardView);
        pressure = (CardView) getView().findViewById(R.id.pressureCardView);
        precipitation = (CardView) getView().findViewById(R.id.precipCardView);
        tempCard = (CardView) getView().findViewById(R.id.tempCardView);
        icon = (CardView) getView().findViewById(R.id.iconCardView);
        humidity = (CardView) getView().findViewById(R.id.humCardView);
        visibility = (CardView) getView().findViewById(R.id.visibilityCardView);
        cloud = (CardView) getView().findViewById(R.id.cloudCoverCardView);
        ozone = (CardView) getView().findViewById(R.id.ozoneCardView);

        //System.out.println("Ozone1 is: " +ozone);

        spinner.setVisibility(View.VISIBLE);
        fetching.setVisibility(View.VISIBLE);

        icon.setVisibility(View.GONE);
        cloud.setVisibility(View.GONE);
        humidity.setVisibility(View.GONE);
        tempCard.setVisibility(View.GONE);
        ozone.setVisibility(View.GONE);
        precipitation.setVisibility(View.GONE);
        visibility.setVisibility(View.GONE);
        pressure.setVisibility(View.GONE);
        wind.setVisibility(View.GONE);

        spinner.postDelayed(new Runnable() {
            public void run() {
                spinner.setVisibility(View.GONE);
                fetching.setVisibility(View.GONE);

                icon.setVisibility(View.VISIBLE);
                cloud.setVisibility(View.VISIBLE);
                humidity.setVisibility(View.VISIBLE);
                tempCard.setVisibility(View.VISIBLE);
                ozone.setVisibility(View.VISIBLE);
                precipitation.setVisibility(View.VISIBLE);
                visibility.setVisibility(View.VISIBLE);
                pressure.setVisibility(View.VISIBLE);
                wind.setVisibility(View.VISIBLE);
            }
        }, 4000);
        super.onViewCreated(view, savedInstanceState);
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
