package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class General_Waste extends AppCompatActivity {

    private Button Restart_From_General_Waste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_waste);

        Restart_From_General_Waste=findViewById(R.id.Restart_From_General_Waste);

        Restart_From_General_Waste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(General_Waste.this, MainActivity.class);
                startActivity(intent);// 상자(기름종이) 아키네이터로 이동
            }
        });
    }
}