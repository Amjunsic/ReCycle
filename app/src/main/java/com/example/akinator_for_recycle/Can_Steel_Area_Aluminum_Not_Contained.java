package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class Can_Steel_Area_Aluminum_Not_Contained extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_steel_area_aluminum_not_contained);

        Button Restart_From_Aluminum_Not_Contained=findViewById(R.id.Restart_From_Aluminum_Not_Contained);

        Restart_From_Aluminum_Not_Contained.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Can_Steel_Area_Aluminum_Not_Contained.this, MainActivity.class);
                startActivity(intent);// 상자(qkrt) 아키네이터로 이동
            }
        });
    }
}