package com.example.lottoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random random;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();

        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);
        tv5 = (TextView)findViewById(R.id.tv5);
        tv6 = (TextView)findViewById(R.id.tv6);

        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RandomBall();
            }
        });
    }

    public void RandomBall() {
        tv1.setText((String.valueOf(random.nextInt(45)+1)));
        tv2.setText((String.valueOf(random.nextInt(45)+1)));
        tv3.setText((String.valueOf(random.nextInt(45)+1)));
        tv4.setText((String.valueOf(random.nextInt(45)+1)));
        tv5.setText((String.valueOf(random.nextInt(45)+1)));
        tv6.setText((String.valueOf(random.nextInt(45)+1)));
    }
}