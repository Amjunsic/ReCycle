package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class Box_Area extends AppCompatActivity {


    private Button Btn_Yes_Box;
    private Button Btn_No_Box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_area);

        Btn_No_Box=findViewById(R.id.Btn_No_Box);
        Btn_Yes_Box=findViewById(R.id.Btn_Yes_Box);

        Btn_Yes_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Box_Area.this, Box_Area_box.class);
                startActivity(intent);// 상자(qkrt) 아키네이터로 이동
            }
        });

        Btn_No_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Box_Area.this, Box_Area_Paper.class);
                startActivity(intent);// 상자 아키네이터로 이동
            }
        });
    }
}