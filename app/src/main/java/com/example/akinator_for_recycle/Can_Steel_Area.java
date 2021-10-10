package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class Can_Steel_Area extends AppCompatActivity {

    private Button Btn_Can_Steel_Yes;
    private Button Btn_Can_Steel_No;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_steel_area);

        Btn_Can_Steel_Yes=findViewById(R.id.Btn_Can_Steel_Yes);
        Btn_Can_Steel_No=findViewById(R.id.Btn_Can_Steel_No);

        Btn_Can_Steel_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Can_Steel_Area.this, Can_Steel_Area_Aluminum.class);
                startActivity(intent);// 상자(qkrt) 아키네이터로 이동
            }
        });
        Btn_Can_Steel_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Can_Steel_Area.this, Can_Steel_Area_Aluminum_Foil.class);
                startActivity(intent);// 상자(qkrt) 아키네이터로 이동
            }
        });
    }
}