package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {

//    private int[] images;
    private ArrayList<String> images;

//    public RecyclerAdapter(int[] images){
//        this.images = images;
//    }

    public RecyclerAdapter(ArrayList<String> images){
        this.images = images;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_layout, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String imageUri = images.get(position);
        Picasso.with(holder.context).load(imageUri).into(holder.Album);

//        Log.i("images:", imageUri);
//        ImageView ivBasicImage =  getItemId(R.id.album);
//        Picasso.with(getActivity()).load(imageUri).into(holder.Album);
////
//        Picasso.with(Context).load(imageUri).into(holder.Album);
//        int image_id = images[position];
//        holder.Album.setImageResource(image_id);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView Album;
        Context context;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            Album = itemView.findViewById(R.id.album);
            context = itemView.getContext();
        }
    }
}
