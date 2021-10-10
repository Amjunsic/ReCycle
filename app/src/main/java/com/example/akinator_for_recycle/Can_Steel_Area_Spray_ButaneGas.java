package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class Can_Steel_Area_Spray_ButaneGas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_steel_area_spray_butane_gas);

        Button Btn_Can_Steel_Spray_ButaneGas_Yes=findViewById(R.id.Btn_Can_Steel_Spray_ButaneGas_Yes);
        Button Btn_Can_Steel_Spray_ButaneGas_No=findViewById(R.id.Btn_Can_Steel_Spray_ButaneGas_No);

        Btn_Can_Steel_Spray_ButaneGas_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Can_Steel_Area_Spray_ButaneGas.this, Can_Steel_Area_Spray_ButanesGasYes.class);
                startActivity(intent);// 상자(qkrt) 아키네이터로 이동
            }
        });

        Btn_Can_Steel_Spray_ButaneGas_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Can_Steel_Area_Spray_ButaneGas.this, No_Result.class);
                startActivity(intent);// 상자(qkrt) 아키네이터로 이동
            }
        });

    }
}