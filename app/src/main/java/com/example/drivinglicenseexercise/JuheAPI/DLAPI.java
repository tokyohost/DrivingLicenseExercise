package com.example.drivinglicenseexercise.JuheAPI;

/**
 * 聚合数据 驾照考试API
 */
public class DLAPI {
    //接口访问地址
    private String Default_url = "http://v.juhe.cn/jztk/query";
    //选择考试科目类型，1：科目1；4：科目4
    private String subject = "0";
    //驾照类型，可选择参数为：c1,c2,a1,a2,b1,b2；当subject=4时可省略
    private String model = "0";
    //测试类型，rand：随机测试（随机100个题目），order：顺序测试（所选科目全部题目）
    private String testType = "0";
    //请求KEY
    private String key="请填入聚合数据申请的KEY";


    public DLAPI() {
    }

    public DLAPI(String subject, String model, String testType,String key) {
        this.subject = subject;
        this.model = model;
        this.testType = testType;
        this.key = key;
    }

    /**
     * 构建请求
     * @param subject 科目类型 科目1:1;科目4：4
     * @param model 驾照类型，可选择参数为：c1,c2,a1,a2,b1,b2；当subject=4时可省略
     * @param testType 测试类型，rand：随机测试（随机100个题目），order：顺序测试（所选科目全部题目）
     * @return requests url
     */
    public String gengrateUrl(String subject, String model, String testType){
        return this.Default_url + "?subject="+subject + "&model="+model+"&testType="+testType+"&key="+key;
    }
    /**
     * 构建请求
     * @return requests url
     */
    public String gengrate(){
        return this.Default_url + "?subject="+this.subject + "&model="+this.model+"&testType="+this.testType+"&key="+key;
    }



    public String getDefault_url() {
        return Default_url;
    }

    public void setDefault_url(String default_url) {
        Default_url = default_url;
    }

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

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }
}
