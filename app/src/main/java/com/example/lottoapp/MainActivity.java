package com.example.lottoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView resultTv, resultTv1;

    private HashSet<String> lottoSet = new HashSet<String>(); // 랜덤 숫자 중복 제거를 위한 HashSet 사용
    private ArrayList<String> arrayList = new ArrayList<String>();

    private RequestQueue requestQueue;

    private String num; // 로또 회차 번호
    private EditText edt;
    private String[] nums = {"drwtNo1", "drwtNo2", "drwtNo3", "drwtNo4", "drwtNo5", "drwtNo6", "bnusNo"}; // 당첨번호 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        resultTv = (TextView) findViewById(R.id.resultTv);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomBall();
            }
        });

        resultTv1 = (TextView)findViewById(R.id.resultTv1);
        edt = (EditText) findViewById(R.id.edt);
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLottoNums();
            }
        });

        if (requestQueue == null) { // 요청큐가 없을 때만 생성(중복 생성 방지)
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    private void randomBall() {
        resultTv.setText("");

        while (lottoSet.size() < 7) {
            int num = (int) (Math.random() * 45);
            if (num != 0) {
                lottoSet.add(num + "");
            }
        }
        arrayList.addAll(lottoSet);
        Collections.sort(arrayList); // arrayList를 오름차순 정렬 -> 한자리수만 정렬이 안 되는 문제 발생

        for (int i = 0; i < arrayList.size(); i++) {
            if (i < 6) {
                resultTv.append(arrayList.get(i) + "    ");
            } else {
                resultTv.append(" \n 보너스 번호 : " + arrayList.get(i)); // 보너스 번호는 정렬이 필요없는데...
            }
        }
        lottoSet.clear();
        arrayList.clear();
    }

    private void requestLottoNums() {
        num = edt.getText().toString();

        if (num.equals("")) {
            Toast.makeText(getApplicationContext(), "회차 번호를 입력하세요. ", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=" + num; // api 링크를 가져와서 url 변수에 할당

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // JsonObject는 객체를 Json 객체로 바꿔주거나 Json 객체를 새로 만드는 역할
                JsonObject jsonObject = (JsonObject) JsonParser.parseString(response);

                String data = num + "회차 당첨 번호 : \n";

                for (int i = 0; i < nums.length - 1; i++) { // 당첨번호 배열 크기만큼 반복
                    data += jsonObject.get(nums[i]) + ", "; // 당첨번호 6개와
                }
                data += "\n보너스 번호 : " + jsonObject.get(nums[nums.length - 1]); // 보너스 번호를
                resultTv1.setText(data); // TextView에 출력
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
        requestQueue.add(request); // 요청큐에 요청 객체 넣기 -> 요청 큐가 알아서 요청과 응답 과정 진행
    }
}