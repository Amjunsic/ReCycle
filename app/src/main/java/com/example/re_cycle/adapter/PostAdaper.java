package com.example.re_cycle.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.re_cycle.R;
import com.example.re_cycle.Writeinfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.example.re_cycle.listener.OnPostListener;

public class PostAdaper extends RecyclerView.Adapter<PostAdaper.PostViewHolder>
{
    private ArrayList<Writeinfo> mDataset;
    private Activity activity;
    private FirebaseFirestore db;
    private OnPostListener onPostListener;

    static class PostViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        PostViewHolder(CardView v)
        {
            super(v);
            cardView = v;
        }
    }

    public PostAdaper(Activity activity, ArrayList<Writeinfo> myDataset)
    {
        mDataset = myDataset;
        this.activity = activity;
        db = FirebaseFirestore.getInstance();
    }


    public void setOnPostListener(OnPostListener onPostListener)
    {
        this.onPostListener = onPostListener;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @NonNull
    @Override
    public PostAdaper.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final PostViewHolder postViewHolder = new PostViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPopup(v, postViewHolder.getAbsoluteAdapterPosition());
            }
        });

        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position)
    {

        CardView cardView = holder.cardView;
        LinearLayout contentLayout = cardView.findViewById(R.id.contentLayout);


        TextView titleTextView = cardView.findViewById(R.id.titleTextView);
        titleTextView.setText(mDataset.get(position).getTitle());//제목

        TextView createAtTextView = cardView.findViewById(R.id.createAtTextView);
        createAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));//작성일

        Log.e("로그","데이터:"+ contentLayout.getChildCount());
        Log.e("사이즈","사이즈: "+getItemCount());


        //게시글 내용 부분
        TextView contentTextView = cardView.findViewById(R.id.contentView);
        contentTextView.setText(mDataset.get(position).getContents());//게시글 내용(글)

        if (contentLayout.getChildCount() == 2)//게시글 이미지 중복 출력 방지 contentLayout 의 자식들값일 때만 생성
        {
            ImageView imageView = new ImageView(activity);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            contentLayout.addView(imageView);
            Glide.with(activity).load(mDataset.get(position).getPhotoUrl()).thumbnail(0.1f).into(imageView);
        }

    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    public void showPopup(View v, int position)
    {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                String id = mDataset.get(position).getId();
                switch (item.getItemId())
                {

                    case R.id.delete://삭제 버튼
                            onPostListener.onDelete(id);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
        popup.show();
    }

    private void toast(String text)
    {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }
}

