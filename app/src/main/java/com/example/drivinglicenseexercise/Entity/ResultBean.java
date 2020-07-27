package com.example.drivinglicenseexercise.Entity;

import com.google.gson.Gson;

import org.litepal.crud.LitePalSupport;

public class ResultBean extends LitePalSupport {
    /**
     * id : 23
     * question : 这个标志是何含义？
     * answer : 1
     * item1 : 地点距离
     * item2 : 行驶路线
     * item3 : 终点地名
     * item4 : 行驶方向
     * explains : 你看那个地点名和KM公里多明显，所以本题的答案是地点距离。如果有箭头的一半是行驶方向。
     * url : http://images.juheapi.com/jztk/c1c2subject1/23.jpg
     */

    private int id;
    private String subject; //选择考试科目类型，1：科目1；4：科目4
    private String model; //驾照类型，可选择参数为：c1,c2,a1,a2,b1,b2；当subject=4时可省略
    private String util_id;
    private String type;
    private String question;
    private String answer;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String explains;
    private String url;
    private String UserChoose;


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUtil_id() {
        return util_id;
    }

    public void setUtil_id(String util_id) {
        this.util_id = util_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserChoose() {
        return UserChoose;
    }

    public void setUserChoose(String userChoose) {
        UserChoose = userChoose;
    }

    public static ResultBean objectFromData(String str) {

        return new Gson().fromJson(str, ResultBean.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public String getItem4() {
        return item4;
    }

    public void setItem4(String item4) {
        this.item4 = item4;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
