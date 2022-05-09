package com.example.lottoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Random random;
    TextView ball1, ball2, ball3, ball4, ball5, ball6, ballList, ballList2;

    HashSet<String> lottoSet = new HashSet<String>(); // 랜덤 숫자 중복 제거를 위해 HashSet 사용
    ArrayList<String> arrayList = new ArrayList<String>();

    RequestQueue requestQueue;

    String num; // 로또 회차 번호
    String[] nums = {"drwtNo1", "drwtNo2", "drwtNo3", "drwtNo4", "drwtNo5", "drwtNo6", "bnusNo"}; // 당첨번호 배열
    EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();

        ball1 = (TextView) findViewById(R.id.ball1);
        ball2 = (TextView) findViewById(R.id.ball2);
        ball3 = (TextView) findViewById(R.id.ball3);
        ball4 = (TextView) findViewById(R.id.ball4);
        ball5 = (TextView) findViewById(R.id.ball5);
        ball6 = (TextView) findViewById(R.id.ball6);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RandomBall();
            }
        });

        ballList = (TextView) findViewById(R.id.ballList);
        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RandomBall2();
            }
        });

        ballList2 = (TextView) findViewById(R.id.ballList2);
        edt = (EditText) findViewById(R.id.edt);
        Button btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLottoNums();
            }
        });

        if (requestQueue == null) { // 요청 큐가 없을 경우에만 생성함 -> 여러번 생성되는 것을 방지함
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    public void RandomBall() {
        ball1.setText((String.valueOf(random.nextInt(45) + 1)));
        ball2.setText((String.valueOf(random.nextInt(45) + 1)));
        ball3.setText((String.valueOf(random.nextInt(45) + 1)));
        ball4.setText((String.valueOf(random.nextInt(45) + 1)));
        ball5.setText((String.valueOf(random.nextInt(45) + 1)));
        ball6.setText((String.valueOf(random.nextInt(45) + 1)));
    }

    public void RandomBall2() {
        ballList.setText(""); // 로또 번호가 표시될 텍스트뷰 초기화

        while (lottoSet.size() < 7) {
            int num = (int) (Math.random() * 45); // 45까지 난수 생성
            Log.d(TAG, String.valueOf(num));
            // 난수가 0이 아니면 HashSet에 추가
            if (num != 0) {
                lottoSet.add(num + "");
            }
        }
        arrayList.addAll(lottoSet); // HashSet을 ArrayList에 담기
        Collections.sort(arrayList); // ArrayList를 오름차순 정렬 -> 한자리수만 정렬이 안 되는 문제 발생

        for (int i = 0; i < arrayList.size(); i++) {
            if (i < 6) {
                ballList.append(arrayList.get(i) + "    ");
            } else {
                ballList.append(" \n 보너스 번호 : " + arrayList.get(i));
            }
        }
        // 초기화
        lottoSet.clear();
        arrayList.clear();
    }

    public void requestLottoNums() {
        num = edt.getText().toString();

        if (num.equals("")) { // 회차 번호를 입력하지 않았을 경우
            Toast.makeText(getApplicationContext(), "회차 번호를 입력하세요. ", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=" + num; // api 링크를 가져와서 url 변수에 할당

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // JsonObject는 객체를 Json 객체로 바꿔주거나 Json 객체를 새로 만드는 역할
                JsonObject jsonObject = (JsonObject)JsonParser.parseString(response);

                String data = num + "회차 당첨 번호 : \n";

                for (int i = 0; i < nums.length - 1; i++) { // 당첨번호 배열 크기만큼 반복
                    data += jsonObject.get(nums[i]) + ", "; // 당첨번호 6개와
                }
                data += "\n보너스 : " + jsonObject.get(nums[nums.length - 1]); // 보너스 번호를
                ballList2.setText(data); // 텍스트뷰에 출력
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        request.setShouldCache(false); // 이전 결과가 있어도 새로 요청하여 응답을 보여줌
        requestQueue.add(request); // 요청 큐에 요청 객체 넣기 -> 요청 큐가 알아서 요청과 응답 과정 진행
    }
}

//