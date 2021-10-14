package com.example.re_cycle.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.re_cycle.R;
import com.example.re_cycle.Writeinfo;
import com.example.re_cycle.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class PostAdaper extends RecyclerView.Adapter<PostAdaper.GalleryViewHolder>
{
    private ArrayList<Writeinfo> mDataset;
    private Activity activity;

    static class GalleryViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        GalleryViewHolder(CardView v)
        {
            super(v);
            cardView = v;
        }
    }

    public PostAdaper(Activity activity, ArrayList<Writeinfo> myDataset)
    {
        mDataset = myDataset;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PostAdaper.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final GalleryViewHolder galleryViewHolder = new GalleryViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryViewHolder holder, int position)
    {

        CardView cardView = holder.cardView;
        LinearLayout contentLayout = cardView.findViewById(R.id.contentLayout);

        TextView titleTextView = cardView.findViewById(R.id.titleTextView);
        titleTextView.setText(mDataset.get(position).getTitle());//제목

        TextView createAtTextView = cardView.findViewById(R.id.createAtTextView);
        createAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));//작성일

        TextView contentTextView = cardView.findViewById(R.id.contentView);
        contentTextView.setText(mDataset.get(position).getContents());

        if (contentLayout.getChildCount() == 2)
        {
            ImageView imageView = new ImageView(activity);
            contentLayout.addView(imageView);
            Glide.with(activity).load(mDataset.get(position).getPhotoUrl()).into(imageView);
        }

        Log.e("로그","데이터"+ contentLayout.getChildCount());
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}

