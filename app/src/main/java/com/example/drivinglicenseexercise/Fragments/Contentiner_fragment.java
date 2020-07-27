package com.example.drivinglicenseexercise.Fragments;


import android.content.Intent;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drivinglicenseexercise.Adapter.ResultAdapter;
import com.example.drivinglicenseexercise.Entity.Quests;
import com.example.drivinglicenseexercise.Entity.ResultBean;
import com.example.drivinglicenseexercise.JuheAPI.DLAPI;
import com.example.drivinglicenseexercise.R;
import com.example.drivinglicenseexercise.Utils.DialogUtils;
import com.example.drivinglicenseexercise.Activity.TestActivity;

import org.litepal.LitePal;


public class Contentiner_fragment extends Fragment implements View.OnClickListener {

    private String context = "1";
    private TextView start_quests;
    private TextView error_quests;
    private ListView error_list;
    private Intent intent;

    private Handler handler;

    private String subject;
    private String model;

    //要显示的页面
    private int FragmentPage;

    //目前页面
    private int fg;

    public static Contentiner_fragment newInstance(String context, int iFragmentPage) {

        Contentiner_fragment myFragment = new Contentiner_fragment();
        myFragment.context = context;
        myFragment.FragmentPage = iFragmentPage;

        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = context;
        //处理两个不同的界面
        View v = init_error_fragment(inflater, container);
        if (v == null) {
            return init_start_fragment(inflater, container);
        } else {
            return v;
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_quests:
                check(1);


                break;
            case R.id.error_quests:
                check(2);
                break;

        }
    }

    /**
     * 检查是否设置好参数
     */
    public void check(int type) {
        if (this.subject == null) {
            String[] list_choose = new String[]{"科一", "科四"};
            DialogUtils.showTipsNoneDialog(getActivity(), list_choose, "请选择科目", 3, type, handler);

        } else if (this.model == null) {
            String[] list_choose_type = new String[]{"c1", "c2", "a1", "a2", "b1", "b2"};
            DialogUtils.showTipsNoneDialog(getActivity(), list_choose_type, "请选择驾照类型", 4, type, handler);

        } else {
            intent = new Intent(getActivity(), TestActivity.class);
            //判断测试类型(全新考试/错题训练)
            if (type == 1) {
                intent.putExtra("type", 0);
                DLAPI dlapi = new DLAPI();
                intent.putExtra("url", dlapi.gengrateUrl(subject, model, "rand"));
                intent.putExtra("subject", subject);
                intent.putExtra("model", model);
            } else {
                intent.putExtra("type", 1);
                DLAPI dlapi = new DLAPI();
                intent.putExtra("url", dlapi.gengrateUrl(subject, model, "order"));
                intent.putExtra("subject", subject);
                intent.putExtra("model", model);
            }
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        this.model = null;
        this.subject = null;

    }

    private View init_error_fragment(LayoutInflater inflater, ViewGroup container) {
        if (FragmentPage == R.layout.fragment_error) {
            View view = inflater.inflate(FragmentPage, container, false);
            error_list = view.findViewById(R.id.error_list);
            Quests quests = new Quests();
            quests.setResult(LitePal.findAll(ResultBean.class));
            ResultAdapter adapter = new ResultAdapter(getActivity(), subject, model, quests, true);
            error_list.setAdapter(adapter);
            if (quests.getResult().size() == 0) {
                Toast.makeText(getActivity(), "您还没有错题记录哦~", Toast.LENGTH_SHORT).show();
            }


            return view;
        } else {
            return null;
        }
    }

    private View init_start_fragment(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(FragmentPage, container, false);

        start_quests = view.findViewById(R.id.start_quests);
        error_quests = view.findViewById(R.id.error_quests);
        if (start_quests != null)
            start_quests.setOnClickListener(this);
        if (error_quests != null)
            error_quests.setOnClickListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    //处理科目
                    case 3:
                        String[] list_choose_code = new String[]{"1", "4"};
                        subject = list_choose_code[(int) msg.obj] + "";
                        break;
                    //处理驾照类型
                    case 4:
                        model = new String[]{"c1", "c2", "a1", "a2", "b1", "b2"}[(int) msg.obj];
                        break;
                    case -1:
                        check((Integer) msg.obj);
                        break;
                }
            }
        };
        return view;
    }
}