package com.example.drivinglicenseexercise.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.drivinglicenseexercise.Dialog.BaseDialog;
import com.example.drivinglicenseexercise.R;

import static com.example.drivinglicenseexercise.Utils.UiUtils.getContext;


/**
 * @desciption:
 */
public class DialogUtils {
    private static BaseDialog dialog;

    public static void showTipsSingleDialog(Context context, String content,String tips, final View.OnClickListener listener) {
        BaseDialog.Builder builder = new BaseDialog.Builder(context);
        dialog = builder
                .setView(R.layout.dialog_common_tips)
                .addViewOnClick(R.id.btn_positive, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismissDialog();
                        if(listener != null){
                            listener.onClick(v);
                        }
                    }
                })
                .build();
        //隐藏取消按钮
        builder.findViewById(R.id.btn_negative).setVisibility(View.GONE);
        //隐藏分割线
        builder.findViewById(R.id.btn_viewline).setVisibility(View.GONE);
        AppCompatTextView txtContent = builder.findViewById(R.id.txt_content);
        txtContent.setText( Html.fromHtml(content));
        AppCompatTextView tipsContent = builder.findViewById(R.id.tip);
        tipsContent.setText(tips);
        dialog.show();
    }

    public static void showTipsTwoDialog(Context context, String content, String negative, String positive, final View.OnClickListener listener) {
        BaseDialog.Builder builder = new BaseDialog.Builder(context);
        dialog = builder
                .setView(R.layout.dialog_common_tips)
                .addViewOnClick(R.id.btn_positive, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismissDialog();
                        listener.onClick(v);
                    }
                })
                .addViewOnClick(R.id.btn_negative, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismissDialog();

                    }
                })
                .build();
        AppCompatTextView txtContent = builder.findViewById(R.id.txt_content);
        AppCompatTextView btnNegative = builder.findViewById(R.id.btn_negative);
        AppCompatTextView btnPositive = builder.findViewById(R.id.btn_positive);
        txtContent.setText(content);
        btnNegative.setText(negative);
        btnPositive.setText(positive);
        dialog.show();
    }


    /**
     * 选择列表
     *
     * @param context
     * @param list_choose
     * @param tips_text
     */
    public static void showTipsNoneDialog(final Context context, String[] list_choose, String tips_text, final int what, final int type, final Handler handler) {
        BaseDialog.Builder builder = new BaseDialog.Builder(context);
        dialog = builder
                .setView(R.layout.dialog_choose_tips)
                .addViewOnClick(R.id.btn_positive, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message msg = new Message();
                        msg.what = -1;
                        msg.obj = type;
                        handler.sendMessage(msg);
                        dialog.dismissDialog();
                    }
                })
                .build();
        //隐藏取消按钮
        builder.findViewById(R.id.btn_negative).setVisibility(View.GONE);
        //隐藏分割线
        builder.findViewById(R.id.btn_viewline).setVisibility(View.GONE);


        //初始化列表数据
        ListView listView = builder.findViewById(R.id.list_context);
        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_single_choice, list_choose));
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message msg = new Message();
                msg.what = what;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        });

        AppCompatTextView text_tip = builder.findViewById(R.id.tip);
        text_tip.setText(tips_text);
        dialog.show();
    }
}