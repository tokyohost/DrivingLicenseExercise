package com.example.drivinglicenseexercise.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.drivinglicenseexercise.Entity.ResultBean;
import com.example.drivinglicenseexercise.R;
import com.example.drivinglicenseexercise.Entity.Quests;
import com.example.drivinglicenseexercise.Utils.QuestsUtil;

import java.util.List;

public class ResultAdapter extends BaseAdapter {
    private Context context;
    private List<ResultBean> quests;

    private String subject;
    private String model;

    public ResultAdapter(Context context,String subject,String model, Quests quests,boolean isFromDatabase) {
        this.context = context;
        //题目类型
        this.subject = subject;
        this.model  = model;

        //提取出错误题
        this.quests = QuestsUtil.initData(quests,subject,model,isFromDatabase);
    }

    @Override
    public int getCount() {
        return quests.size();
    }

    @Override
    public Object getItem(int i) {
        return quests.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position-1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = View.inflate(context, R.layout.list_item, null);

        TextView quests_id = layout.findViewById(R.id.quests_id);
        TextView quests_type = layout.findViewById(R.id.quests_type);
        TextView quests_body = layout.findViewById(R.id.quests_body);

        ImageView image_body = layout.findViewById(R.id.image_body);

        TextView ans_a = layout.findViewById(R.id.ans_a);
        TextView ans_b = layout.findViewById(R.id.ans_b);
        TextView ans_c = layout.findViewById(R.id.ans_c);
        TextView ans_d = layout.findViewById(R.id.ans_d);

        TextView my_ans = layout.findViewById(R.id.quests_my_ans);
        TextView quests_ans = layout.findViewById(R.id.quests_ans);

        //展示题目信息
        quests_ans.setText(replace_ans(quests.get(position).getAnswer(),check_quests_type(quests.get(position))));
        my_ans.setText(mychoose_replace(quests.get(position).getUserChoose()));

        if(quests.get(position).getItem1().equals("")){
            ans_a.setVisibility(View.GONE);
        }else if(quests.get(position).getItem1().equals("正确")){
            ans_a.setText(quests.get(position).getItem1());
            ans_a.setVisibility(View.GONE);
        }else{
            ans_a.setText("A:"+quests.get(position).getItem1());
            ans_a.setVisibility(View.VISIBLE);
        }

        if(quests.get(position).getItem2().equals("")){
            ans_b.setVisibility(View.GONE);
        }else if(quests.get(position).getItem2().equals("错误")){
            ans_b.setText(quests.get(position).getItem2());
            ans_b.setVisibility(View.GONE);
        }else{
            ans_b.setText("B:"+quests.get(position).getItem2());
            ans_b.setVisibility(View.VISIBLE);
        }

        if(quests.get(position).getItem3().equals("")){
            ans_c.setVisibility(View.GONE);
        }else{
            ans_c.setText("C:"+quests.get(position).getItem3());
            ans_c.setVisibility(View.VISIBLE);
        }

        if(quests.get(position).getItem4().equals("")){
            ans_d.setVisibility(View.GONE);
        }else{
            ans_d.setText("D:"+quests.get(position).getItem4());
            ans_d.setVisibility(View.VISIBLE);
        }


        //加载题目图片
        if(!quests.get(position).getUrl().equals("")){
            Glide.with(context).load(quests.get(position).getUrl()).into(image_body);
            image_body.setVisibility(View.VISIBLE);
        }else{
            image_body.setVisibility(View.GONE);
        }

        quests_id.setText(quests.get(position).getUtil_id() == null?quests.get(position).getId()+"":quests.get(position).getUtil_id());
        quests_type.setText(check_quests_type(quests.get(position)) == 0?"选择题":"判断题");

        quests_body.setText("\u3000\u3000"+quests.get(position).getQuestion());




        return layout;
    }

    /**
     * 格式化答案显示
     *
     * @param ans
     * @param type 0为选择题 ，其它值为判断题
     * @return
     */
    private String replace_ans(String ans, int type) {

        if (type == 0) {
            switch (ans) {
                case "1":
                    return "A";
                case "2":
                    return "B";
                case "3":
                    return "C";
                case "4":
                    return "D";
            }
        } else {
            switch (ans) {
                case "1":
                    return "正确";
                case "2":
                    return "错误";
            }
        }

        return "null";
    }

    private int check_quests_type(ResultBean item) {
        if (item.getItem3().equals("") && item.getItem4().equals("") && item.getItem1().equals("正确") && item.getItem2().equals("错误"))
            return 1;
        else
            return 0;
    }

    private String mychoose_replace(String choose){
        if(choose == null){
            return "未选择";
        }else{
            return choose;
        }

    }

}