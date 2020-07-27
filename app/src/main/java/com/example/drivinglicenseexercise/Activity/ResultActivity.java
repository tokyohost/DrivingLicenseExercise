package com.example.drivinglicenseexercise.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.drivinglicenseexercise.Adapter.ResultAdapter;
import com.example.drivinglicenseexercise.Entity.Quests;
import com.example.drivinglicenseexercise.Entity.ResultBean;
import com.example.drivinglicenseexercise.R;
import com.example.drivinglicenseexercise.Utils.QuestsUtil;

public class ResultActivity extends AppCompatActivity {
    private Quests quests;
    private String time;
    private String subject;
    private String model;

    private TextView tv_time;
    private TextView tv_score;

    private ListView error_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //初始化界面
        getSupportActionBar().hide();
        init();

        Intent intent = getIntent();
        quests = Quests.objectFromData(intent.getStringExtra("result"));
        subject = intent.getStringExtra("subject");
        model = intent.getStringExtra("model");

        time = intent.getStringExtra("time");

        ResultAdapter adapter = new ResultAdapter(ResultActivity.this,subject,model,quests,false);

        error_list.setAdapter(adapter);

        tv_time.setText(time);
        tv_score.setText(init_score()+"");


    }

    private int init_score() {
        int temp_score = 0;

        for(ResultBean item : quests.getResult()){
            if(QuestsUtil.check_quests(item) == 1){
                temp_score++;
            }
        }

        return temp_score;
    }

    private void init(){

        tv_time = findViewById(R.id.tv_time);
        tv_score = findViewById(R.id.tv_score);

        error_list = findViewById(R.id.result_list);
    }





}
