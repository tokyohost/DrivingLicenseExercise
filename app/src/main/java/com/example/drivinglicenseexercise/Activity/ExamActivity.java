package com.example.drivinglicenseexercise.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.drivinglicenseexercise.Fragments.Contentiner_fragment;
import com.example.drivinglicenseexercise.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.litepal.LitePal;


public class ExamActivity extends AppCompatActivity {

    private MenuItem menuItem;
    private boolean is_start_app = true;
    private Contentiner_fragment f1, f2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        getSupportActionBar().hide();
        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        initLitePal();


        //bottomNavigationView Item 选择监听
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                hideAllFragment(transaction);
                switch (item.getItemId()) {
                    case R.id.tab_menu_start:
                        if (f1 == null) {
                            f1 = Contentiner_fragment.newInstance("开始答题", R.layout.fragment_start);
                            transaction.add(R.id.fragment_container, f1);
                        } else {
                            transaction.show(f1);
                        }
                        break;
                    case R.id.tab_menu_error:
                        if (f2 == null) {
                            f2 = Contentiner_fragment.newInstance("错题记录", R.layout.fragment_error);//"第二个Fragment"
                            transaction.add(R.id.fragment_container, f2);
                        } else {
                            transaction.show(f2);
                        }
                        break;
                }

                transaction.commit();

                is_start_app = false;

                //切换底部导航栏状态
                if (menuItem != null) {
                    //如果有已选中的item，则取消它的的选中状态
                    menuItem.setChecked(false);
                } else {
                    //如果没有，则取消默认的选中状态（第一个item）
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                //让与当前Pager相应的item变为选中状态
                int position = item.getItemId() == R.id.tab_menu_start ? 0 : 1;
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);

                return false;
            }


        });


        //如果初始化打开APP则默认展示开始答题Fragment
        if (is_start_app) {

            if (f1 != null)
                getFragmentManager().beginTransaction().add(R.id.fragment_container, f1).show(f1).commit();
            else {
                f1 = Contentiner_fragment.newInstance("开始答题", R.layout.fragment_start);
                getFragmentManager().beginTransaction().add(R.id.fragment_container, f1).show(f1).commit();


            }
            is_start_app = false;
        }


    }


    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (f1 != null) {
            transaction.hide(f1);
        }
        if (f2 != null) {
            transaction.hide(f2);
        }
    }


    public void init() {
    }

    private void initLitePal(){
        LitePal.initialize(this);
    }

    /**
     * 刷新错题记录
     */
    @Override
    protected void onResume() {
        super.onResume();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(f2!=null){
            transaction.detach(f2);
            f2 = Contentiner_fragment.newInstance("错题记录", R.layout.fragment_error);
            transaction.add(R.id.fragment_container, f2);
            transaction.hide(f2);
            transaction.show(f1);
            transaction.commit();
        }


    }
}