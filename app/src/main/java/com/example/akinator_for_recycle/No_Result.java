package com.example.akinator_for_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class No_Result extends AppCompatActivity {

    private Button Restart_From_No_Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_result);
        Restart_From_No_Result=findViewById(R.id.Restart_From_No_Result);

        Restart_From_No_Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(No_Result.this, MainActivity.class);
                startActivity(intent);// 상자(qkrt) 아키네이터로 이동
            }
        });
    }
}