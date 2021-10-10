package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Box_Area_Paper_MilkpackYes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_area_paper_milkpack_yes);

        Button Restart_From_MilkPack=findViewById(R.id.Restart_From_MilkPack);

        Restart_From_MilkPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Box_Area_Paper_MilkpackYes.this, MainActivity.class);
                startActivity(intent);// 상자(기름종이) 아키네이터로 이동
            }
        });
    }
}