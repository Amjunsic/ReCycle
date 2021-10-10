package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Button Btn_Plastic; //플라스틱 버튼
    private Button Btn_Box; //상자 버튼
    private Button Btn_Styrofoam; //스타이로폼 버튼
    private Button Btn_Glass; //유리 버튼
    private Button Btn_Can_Steel; //캔,철 버튼
    private Button Btn_PlasticBag; //비닐 버튼




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Btn_Plastic = findViewById(R.id.Btn_Plastic);
        Btn_Box = findViewById(R.id.Btn_Box);
        Btn_Styrofoam = findViewById(R.id.Btn_Styrofoam);
        Btn_Glass = findViewById(R.id.Btn_Glass);
        Btn_Can_Steel = findViewById(R.id.Btn_Can_Steel);
        Btn_PlasticBag = findViewById(R.id.Btn_PlasticBag);

        Btn_Plastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Plastic_Area.class);
                startActivity(intent);// 플라스틱 아키네이터로 이동
           }
        });
        Btn_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Box_Area.class);
                startActivity(intent);// 상자 아키네이터로 이동
            }
        });
        Btn_Styrofoam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Styrofoam_Area.class);
                startActivity(intent);// 스타이로품 아키네이터로 이동
            }
        });
        Btn_Can_Steel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Can_Steel_Area.class);
                startActivity(intent);// 플라스틱 아키네이터로 이동
            }
        });
        Btn_Glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Glass_Area.class);
                startActivity(intent);// 플라스틱 아키네이터로 이동
            }
        });
        Btn_PlasticBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlasticBag_Area.class);
                startActivity(intent);// 플라스틱 아키네이터로 이동
            }
        });

    }
}