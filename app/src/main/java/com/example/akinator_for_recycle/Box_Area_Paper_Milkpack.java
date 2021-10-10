package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class Box_Area_Paper_Milkpack extends AppCompatActivity {

    private Button Btn_Yes_For_PaperMilkPack;
    private Button Btn_No_For_PaperMilkPack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_area_paper_milkpack);
        Btn_No_For_PaperMilkPack=findViewById(R.id.Btn_No_For_PaperMilkPack);
        Btn_Yes_For_PaperMilkPack=findViewById(R.id.Btn_Yes_For_PaperMilkPack);

        Btn_Yes_For_PaperMilkPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Box_Area_Paper_Milkpack.this, Box_Area_Paper_MilkpackYes.class);
                startActivity(intent);// 상자(qkrt) 아키네이터로 이동
            }
        });
        Btn_No_For_PaperMilkPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Box_Area_Paper_Milkpack.this, Box_Area_Paper_ReceiptLeaflet.class);
                startActivity(intent);// 상자(qkrt) 아키네이터로 이동
            }
        });
    }
}