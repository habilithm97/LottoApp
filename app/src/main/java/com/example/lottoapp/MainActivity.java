package com.example.lottoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Random random;
    TextView ball1, ball2, ball3, ball4, ball5, ball6, ballList;

    HashSet<String> lottoSet = new HashSet<String>(); // 랜덤 숫자 중복 제거를 위해 HashSet 사용
    ArrayList<String> arrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();

        ball1 = (TextView)findViewById(R.id.ball1);
        ball2 = (TextView)findViewById(R.id.ball2);
        ball3 = (TextView)findViewById(R.id.ball3);
        ball4 = (TextView)findViewById(R.id.ball4);
        ball5 = (TextView)findViewById(R.id.ball5);
        ball6 = (TextView)findViewById(R.id.ball6);

        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RandomBall();
            }
        });

        ballList = (TextView)findViewById(R.id.ballList);
        Button btn2 = (Button)findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RandomBall2();
            }
        });
    }

    public void RandomBall() {
        ball1.setText((String.valueOf(random.nextInt(45)+1)));
        ball2.setText((String.valueOf(random.nextInt(45)+1)));
        ball3.setText((String.valueOf(random.nextInt(45)+1)));
        ball4.setText((String.valueOf(random.nextInt(45)+1)));
        ball5.setText((String.valueOf(random.nextInt(45)+1)));
        ball6.setText((String.valueOf(random.nextInt(45)+1)));
    }

    public void RandomBall2() {
        ballList.setText(""); // 로또 번호가 표시될 텍스트뷰 초기화

        while(lottoSet.size() < 7) {
            int num = (int)(Math.random()*45); // 45까지 난수 생성
            Log.d(TAG, String.valueOf(num));
            // 난수가 0이 아니면 HashSet에 추가
            if(num != 0) {
                lottoSet.add(num + "");
            }
        }
        arrayList.addAll(lottoSet); // HashSet을 ArrayList에 담기
        for(int i = 0; i < arrayList.size(); i++) {
            if(i < 5) { // 1, 2, 3, 4, 5번째 숫자
                ballList.append(arrayList.get(i) + ",    ");
            } else if(i == 5) { // 6번째 숫자
                ballList.append(arrayList.get(i) + "    ");
            } else {
                ballList.append(" \n 보너스 번호 : " + arrayList.get(i));
            }
        }
        // 초기화
        lottoSet.clear();
        arrayList.clear();
    }
}