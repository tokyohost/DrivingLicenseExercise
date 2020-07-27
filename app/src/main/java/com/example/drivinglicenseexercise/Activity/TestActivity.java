package com.example.drivinglicenseexercise.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drivinglicenseexercise.Entity.Quests;
import com.example.drivinglicenseexercise.Entity.ResultBean;
import com.example.drivinglicenseexercise.R;
import com.example.drivinglicenseexercise.Utils.DialogUtils;
import com.example.drivinglicenseexercise.Utils.QuestsUtil;
import com.google.gson.Gson;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private int exam_type;
    private String url;
    private int nowQuestsIndex; //当前题目
    private String subject;
    private String model;

    private Quests quests;

    private Handler handler;

    private TextView back_btn;
    private TextView next_btn;

    private TextView quests_id;
    private Chronometer count_time;
    private TextView quests_type;
    private TextView explains;

    private TextView quests_body;
    private CheckBox checkBox_a;
    private CheckBox checkBox_b;
    private CheckBox checkBox_c;
    private CheckBox checkBox_d;

    private ImageView pic_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //获取出题类型
        exam_type = (int) getIntent().getIntExtra("type", 1);
        url = getIntent().getStringExtra("url");
        subject = getIntent().getStringExtra("subject");
        model = getIntent().getStringExtra("model");

        //判断网络状态
        if (QuestsUtil.isNetworkAvailable(this) != 1) {
            DialogUtils.showTipsSingleDialog(this, "请检查网络链接是否正常！\n此程序必须联网下载题目！", "错误", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == R.id.btn_positive) {
                        TestActivity.this.finish();
                    }
                }
            });
        }

        //初始化
        init();


    }


    private void init() {

        getSupportActionBar().hide();
        back_btn = findViewById(R.id.back_btn);
        next_btn = findViewById(R.id.next_btn);

        quests_id = findViewById(R.id.quests_id);
        count_time = findViewById(R.id.time_count);
        quests_type = findViewById(R.id.quests_type);
        quests_body = findViewById(R.id.quests_body);
        checkBox_a = findViewById(R.id.checkBox_a);
        checkBox_b = findViewById(R.id.checkBox_b);
        checkBox_c = findViewById(R.id.checkBox_c);
        checkBox_d = findViewById(R.id.checkBox_d);
        explains = findViewById(R.id.explains);
        pic_img = findViewById(R.id.quests_image);
        //注册监听
        checkBox_a.setOnClickListener(this);
        checkBox_b.setOnClickListener(this);
        checkBox_c.setOnClickListener(this);
        checkBox_d.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        explains.setOnClickListener(this);


        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    //获取题库
                    case 0:
                        quests = (Quests) msg.obj;
                        init_quests();
                        init_chronometer();
                        break;
                    //错题库
                    case 1:
                        quests = new Quests();
                        quests.setResult(LitePal.where("subject=? and model=?", subject, model).find(ResultBean.class));
                        //判断数据库中是否有错题
                        if (quests.getResult().size() == 0) {
                            Toast.makeText(TestActivity.this, "您暂时没有" + QuestsUtil.replace_subject(subject) + "的" + model + "驾照测试错题记录哦~请先开始测试一下吧~", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TestActivity.this, ExamActivity.class);
                            startActivity(intent);
                            TestActivity.this.finish();
                        } else if (quests.getResult().size() >= 100) { //大于等于100就只抽数据库中存的错题
                            if (quests.getResult().size() <= 150) {
                                quests.setResult(quests.getResult().subList(0, 100));
                            }
                            random_quests();
                            init_quests();
                        } else { //错题小于100，从API中获取题目补齐
                            getApi(2);
                        }
                        break;
                    case 2:
                        Quests quests_temp = (Quests) msg.obj;
                        int size = quests.getResult().size() - 1;
                        int rand_num = 0;
                        while (true) {
                            rand_num = (int) (1 + Math.random() * (quests_temp.getResult().size()));
                            if (rand_num + (100 - size) <= quests_temp.getResult().size() - 1) {
                                if (!quests.getResult().equals(quests_temp.getResult().get(rand_num))) {
                                    quests.getResult().add(quests_temp.getResult().get(rand_num));
                                }
                            }
                            if (quests.getResult().size() == 100) {
                                break;
                            }
                        }
                        //清空数据库内保存的答案
                        List<ResultBean> newResultBeanList = new ArrayList<>();
                        for (ResultBean item : quests.getResult()) {
                            item.setUserChoose(null);
                            newResultBeanList.add(item);
                        }
                        quests.setResult(newResultBeanList);

                        init_quests();
                        init_chronometer();
                        break;
                }
            }
        };

        if (exam_type != 1) {
            getApi(0);
        } else {
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }


    }

    //取随机题
    private void random_quests() {

        //取随机100题
        List<Integer> q_list = new ArrayList<>();
        List<ResultBean> quests_bean_list = new ArrayList<>();
        while (true) {
            int temp = (int) (1 + Math.random() * (quests.getResult().size())) - 1;
            if (q_list.equals(temp)) {
                continue;
            } else {
                quests_bean_list.add(quests.getResult().get(temp));
            }

            if (quests_bean_list.size() == 100) {
                break;
            }
        }
        quests.setResult(quests_bean_list);
    }

    //计时器初始化
    private void init_chronometer() {
        count_time.setBase(SystemClock.elapsedRealtime());//计时器清零
        count_time.start();
    }

    //初始化答题界面
    private void init_quests() {
        change_q(0);
    }

    //答题界面路由
    private void modfiy_quests(String act) {
        if (act.equals("next")) {
            change_q(nowQuestsIndex += 1);
        } else if (act.equals("back")) {
            if (nowQuestsIndex - 1 == -1) {
                Toast.makeText(this, "没有题目了哦！", Toast.LENGTH_SHORT).show();
                return;
            }

            change_q(nowQuestsIndex -= 1);
        } else if (act.equals("exp")) {
            DialogUtils.showTipsSingleDialog(this, quests.getResult().get(nowQuestsIndex).getExplains(), "提示", null);
        }
    }

    //检查答题进度
    private boolean check_status(int id) {
        if (id == quests.getResult().size() - 1) {
            next_btn.setText("完成测验");
        } else if (id > quests.getResult().size() - 1) {
            Intent intent = new Intent(TestActivity.this, ResultActivity.class);
            intent.putExtra("result", new Gson().toJson(quests));
            intent.putExtra("time", count_time.getText());
            intent.putExtra("subject", subject);
            intent.putExtra("model", model);
            startActivity(intent);
            this.finish();
            return true;
        } else {
            next_btn.setText("下一题");
        }
        return false;

    }

    //更换题目
    private void change_q(int id) {
        //判断是否完成答题
        if (check_status(id)) {
            return;
        }
        quests_id.setText((id + 1) + "/" + quests.getResult().size());
        if (quests.getResult().get(id).getItem1().equals("正确") && quests.getResult().get(id).getItem2().equals("错误")) {
            quests_type.setText("判断题");
            checkBox_a.setText("正确");
            checkBox_b.setText("错误");
            checkBox_c.setVisibility(View.GONE);
            checkBox_d.setVisibility(View.GONE);

        } else {
            quests_type.setText("选择题");
            if (quests.getResult().get(id).getItem1().equals("")) {
                checkBox_a.setVisibility(View.GONE);
            } else {
                checkBox_a.setText(quests.getResult().get(id).getItem1());
                checkBox_a.setVisibility(View.VISIBLE);
            }
            if (quests.getResult().get(id).getItem2().equals("")) {
                checkBox_b.setVisibility(View.GONE);
            } else {
                checkBox_b.setText(quests.getResult().get(id).getItem2());
                checkBox_b.setVisibility(View.VISIBLE);
            }
            if (quests.getResult().get(id).getItem3().equals("")) {
                checkBox_c.setVisibility(View.GONE);
            } else {
                checkBox_c.setText(quests.getResult().get(id).getItem3());
                checkBox_c.setVisibility(View.VISIBLE);
            }
            if (quests.getResult().get(id).getItem4().equals("")) {
                checkBox_d.setVisibility(View.GONE);
            } else {
                checkBox_d.setText(quests.getResult().get(id).getItem4());
                checkBox_d.setVisibility(View.VISIBLE);
            }

        }
        quests_body.setText("\u3000\u3000" + quests.getResult().get(id).getQuestion());

        if (quests.getResult().get(id).getUrl().equals("")) {
            pic_img.setVisibility(View.GONE);
        } else {
            Glide.with(this)
                    .load(quests.getResult().get(id).getUrl())
                    .override(330, 240)
                    .into(pic_img);

            pic_img.setVisibility(View.VISIBLE);
            Log.d("Image URL：", quests.getResult().get(id).getUrl());
        }

        //初始化选项
        checkBox_a.setSelected(false);
        checkBox_a.setChecked(false);
        checkBox_b.setSelected(false);
        checkBox_b.setChecked(false);
        checkBox_c.setSelected(false);
        checkBox_c.setChecked(false);
        checkBox_d.setSelected(false);
        checkBox_d.setChecked(false);

        //记录选中项返回时显示
        if (quests.getResult().get(id).getUserChoose() != null) {
            unSelectChoose(quests.getResult().get(id).getUserChoose());
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            //下一页
            case R.id.next_btn:
                modfiy_quests("next");
                break;
            //上一页
            case R.id.back_btn:
                modfiy_quests("back");
                break;
            //提示
            case R.id.explains:
                modfiy_quests("exp");
                break;
            case R.id.checkBox_a:
                unSelectChoose("A");
                break;
            case R.id.checkBox_b:
                unSelectChoose("B");
                break;
            case R.id.checkBox_c:
                unSelectChoose("C");
                break;
            case R.id.checkBox_d:
                unSelectChoose("D");
                break;
        }


    }

    //控制选择框
    public void unSelectChoose(String item) {
        //写入答案
        quests.getResult().get(nowQuestsIndex).setUserChoose(item);

        if (item.equals("A")) {
            checkBox_a.setSelected(true);
            checkBox_a.setChecked(true);
        } else {
            checkBox_a.setSelected(false);
            checkBox_a.setChecked(false);
        }
        if (item.equals("B")) {
            checkBox_b.setSelected(true);
            checkBox_b.setChecked(true);
        } else {
            checkBox_b.setSelected(false);
            checkBox_b.setChecked(false);
        }
        if (item.equals("C")) {
            checkBox_c.setSelected(true);
            checkBox_c.setChecked(true);
        } else {
            checkBox_c.setSelected(false);
            checkBox_c.setChecked(false);
        }
        if (item.equals("D")) {
            checkBox_d.setSelected(true);
            checkBox_d.setChecked(true);
        } else {
            checkBox_d.setSelected(false);
            checkBox_d.setChecked(false);
        }


    }

    /**
     * 获取远程API题库数据
     *
     * @param what
     */
    private void getApi(final int what) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                //创建一个Request
                Request request = new Request.Builder()
                        .get()
                        .url(url)
                        .build();
                //通过client发起请求
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String str = response.body().string();
                            Message message = new Message();
                            message.what = what;
                            message.obj = Quests.objectFromData(str);
                            handler.sendMessage(message);
                        }

                    }
                });
            }
        }).start();
    }

    /**
     * 监听退出答题界面
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            DialogUtils.showTipsTwoDialog(this, "您要退出答题吗？", "取消", "退出", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.btn_positive) {
                        Intent intent = new Intent(TestActivity.this, ExamActivity.class);
                        startActivity(intent);
                        TestActivity.this.finish();
                    }
                }
            });
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
