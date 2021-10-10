package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class Box_Area_Paper extends AppCompatActivity {

    private Button Btn_Yes_For_Paper;
    private Button Btn_No_For_Paper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_area_paper);

        Btn_No_For_Paper=findViewById(R.id.Btn_No_For_Paper);
        Btn_Yes_For_Paper=findViewById(R.id.Btn_Yes_For_Paper);

        Btn_Yes_For_Paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Box_Area_Paper.this, Box_Area_Paper_Oil.class);
                startActivity(intent);// 상자(qkrt) 아키네이터로 이동
            }
        });
        Btn_No_For_Paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Box_Area_Paper.this, Box_Area_Paper_Milkpack.class);
                startActivity(intent);// 상자(qkrt) 아키네이터로 이동
            }
        });
    }
}