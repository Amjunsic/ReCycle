package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class Box_Area_box extends AppCompatActivity
{

    private Button Restart_From_Box;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_area_box);

        Restart_From_Box=findViewById(R.id.Restart_From_Box);

        Restart_From_Box.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Box_Area_box.this, MainActivity.class);
                startActivity(intent);// 상자 아키네이터로 이동
            }
        });
    }
}