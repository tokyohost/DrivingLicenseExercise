package com.example.drivinglicenseexercise.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.drivinglicenseexercise.Entity.Quests;
import com.example.drivinglicenseexercise.Entity.ResultBean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import static androidx.constraintlayout.widget.Constraints.TAG;

public  class QuestsUtil {

    /**
     * 提取出答案错误的题目列表
     * @param quests
     * @return
     */
    public static List<ResultBean> initData(Quests quests,String subject,String model,boolean isFromDatabase){
        List<ResultBean> error_list = new ArrayList<>();

        for(ResultBean item :quests.getResult()){
            if(check_quests(item) != 1){
                error_list.add(item);
                //存入数据库

                if(!isFromDatabase){
                    item.setUtil_id(item.getId()+"");
                    item.setSubject(subject);
                    item.setModel(model);
                    item.save();
                }

            }
        }


        return error_list;
    }

    /**
     * 检查是否答对
     * @param bean
     * @return
     */
    public static int check_quests(ResultBean bean){
        if(bean.getUserChoose() == null){
            return -1;
        }

        String ans ="0";
        switch (bean.getUserChoose()){
            case "A":
                ans = "1";
                break;
            case "B":
                ans = "2";
                break;
            case "C":
                ans = "3";
                break;
            case "D":
                ans = "4";
                break;


        }

        if(!ans.equals("0") && ans.equals( bean.getAnswer()) || check_is_empty(bean)){
            return 1;
        }
        return 0;
    }

    /**
     * 检查所有选项是否为空，以辨别题目是否出错
     * @param bean
     * @return
     */
    public static boolean check_is_empty(ResultBean bean){

        return bean.getItem1().equals("")&&bean.getItem2().equals("")&&bean.getItem3().equals("")&& bean.getItem4().equals("") ? true:false;
    }


    /**
     * 科目转换
     */

    public static String replace_subject(String subject){
        //返回科目的全称
        switch (subject){
            case "1":
                return "科目一";
            case "4":
                return "科目四";
        }
        return "null";
    }



    /**
     * 检查网络是否可用
     *
     * @param activity
     * @return
     */
    public static int isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return 0;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    Log.d("Network", "isNetworkAvailable: " + "===状态===" + networkInfo[i].getState() + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
}
