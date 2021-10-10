package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Can_Steel_Area_Spray_ButanesGasYes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_steel_area_spray_butanes_gas_yes);

        Button Restart_From_Spray_ButaneGas=findViewById(R.id.Restart_From_Spray_ButaneGas);

        Restart_From_Spray_ButaneGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Can_Steel_Area_Spray_ButanesGasYes.this, MainActivity.class);
                startActivity(intent);// 상자(기름종이) 아키네이터로 이동
            }
        });
    }
}