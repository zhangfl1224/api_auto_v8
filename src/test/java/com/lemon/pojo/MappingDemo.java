package com.lemon.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @Project: api_auto_v2
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: 2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: luojie
 * @Create: 2020-09-05 10:51
 * @Desc： 映射案例类
 **/
public class MappingDemo {

    @Excel(name="CaseId")
    private int caseId;

    @Excel(name="Name")
    private String name;

    @Excel(name="Url")
    private String url;

    @Excel(name="Type")
    private String type;

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MappingDemo{" +
                "caseId=" + caseId +
                ", name=" + name +
                ", url=" + url +
                ", type=" + type +
                '}';
    }
}
