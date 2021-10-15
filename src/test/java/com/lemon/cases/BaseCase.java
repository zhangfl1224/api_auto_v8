package com.lemon.cases;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackData;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.UserData;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.*;

import java.util.Map;
import java.util.Set;

/**
 * @Project: api_auto_v3
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: 2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: luojie
 * @Create: 2020-09-08 21:24
 * @Desc： 用例父类
 **/
public class BaseCase {

    private Logger logger = Logger.getLogger(BaseCase.class);

    //sheet开始索引
    public int startSheetIndex;
    //sheet个数
    public int sheetNum;

    @BeforeSuite
    public void setup() {
        //注册手机号码，是一个随机
        logger.info("=====================自动化开始=========================");
        UserData.vars.put("${register_mb}",ChineseMobileNumberGenerator.getInstance().generate());
        UserData.vars.put("${register_pwd}","12345678");
        UserData.vars.put("${login_mb}","13333251004");
        UserData.vars.put("${login_pwd}","lemon123456");
        UserData.vars.put("${amount}","0.01");
        UserData.vars.put("${admin_mb}","18910802654");
        UserData.vars.put("${admin_pwd}","12345678");
        //member_id 和 token 是通过登录接口获取，不需要提前写死。
    }

    @BeforeClass
    @Parameters({"startSheetIndex","sheetNum"})
    public void beforeClass(int startSheetIndex,int sheetNum) {
        //接受testng.xml中parameters 参数
        this.startSheetIndex = startSheetIndex;
        this.sheetNum = sheetNum;
    }

    @AfterMethod
    public void afterMethod() {
        logger.info("==============================================================");
    }

    @AfterSuite
    public void tearDown() throws Exception {
        //批量回写
        logger.info("====tearDown====");
        ExcelUtils.batchWrite();
    }

    /**
     * 添加回写内容到wbdList中
     * @param rowNum
     * @param cellNum
     * @param sheetIndex
     * @param content
     */
    public void addWriteBackData(int rowNum, int cellNum, int sheetIndex, String content) {
        WriteBackData wbd = new WriteBackData
                (rowNum,cellNum,sheetIndex, content);
        ExcelUtils.wbdList.add(wbd);
    }

    /**
     * 通过jsonpath从响应体中取出值存储到UserData中
     * @param body          响应体
     * @param path          jsonPath表达式
     * @param key           存储key
     */
    public void response2UserData(String body,String path,String key) {
        //read(json字符串，jsonpath表达式)
        //从响应体中获取jsonPath对应的值
        Object value = JSONPath.read(body, path);
        if(value != null) {
            //把value存储到vars（接口数据储存map）
            UserData.vars.put(key, value);
        }
    }

    /**
     *  接口响应断言，直接多字段
     * @param caseInfo              获取期望值
     * @param body                  响应体
     */
    public boolean responseAssert(CaseInfo caseInfo, String body) {
        //断言结果
        boolean assertResult = true;
        //{"$.code":1,"$.msg":"密码为空"} 格式解释：{实际值表达式1：期望值1,实际值表达式2：期望值2}
        String expectedResult = caseInfo.getExpectedResult();
        //转Map<String,Object>
        Map<String,Object> map = JSONObject.parseObject(expectedResult, Map.class);
        //取出所有的key（实际值表达式），断言多个字段
        Set<String> keySet = map.keySet();
        for (String actualJsonPath : keySet) {
            //通过key取出期望值
            Object expectedValue = map.get(actualJsonPath);
            //实际值表达式+响应体=》获取实际值
            Object actualValue = JSONPath.read(body, actualJsonPath);
            //断言：期望值和实际值比较
            if(!expectedValue.equals(actualValue)) {
//                System.out.println("断言失败：实际值表达式："+actualJsonPath+"，实际值："+actualValue+"，期望值：" + expectedValue);
                logger.error("断言失败：实际值表达式："+actualJsonPath+"，实际值："+actualValue+"，期望值：" + expectedValue);
                //break;
                assertResult = false;
            }
        }
        if(assertResult) {
            logger.info("响应断言成功");
        }
        return assertResult;
    }

    /**
     * 参数化
     * @param caseInfo      需要替换的参数
     */
    @Step("参数化")
    public void paramsReplace(CaseInfo caseInfo) {
        String url = caseInfo.getUrl();
        String params = caseInfo.getParams();
        String expectedResult = caseInfo.getExpectedResult();
        String sql = caseInfo.getSql();
        //获取vars中所有占位符
        Set<String> keySet = UserData.vars.keySet();
        for (String placeholder : keySet) {
            //{"mobile_phone":"${register_mb}","pwd":"${register_pwd}"}
            String value = UserData.vars.get(placeholder).toString();
            //如果url有为空，占位符和实际值进行替换
            if(StringUtils.isNotBlank(url)) {
                url = url.replace(placeholder, value);
            }
            //如果params有为空，占位符和实际值进行替换
            if(StringUtils.isNotBlank(params)) {
                params = params.replace(placeholder, value);
            }
            //如果expectedResult有为空，占位符和实际值进行替换
            if(StringUtils.isNotBlank(expectedResult)) {
                expectedResult = expectedResult.replace(placeholder, value);
            }
            //如果sql有为空，占位符和实际值进行替换
            if(StringUtils.isNotBlank(sql)) {
                sql = sql.replace(placeholder, value);
            }
        }
        //重新赋值参数化之后的值
        caseInfo.setUrl(url);
        caseInfo.setParams(params);
        caseInfo.setExpectedResult(expectedResult);
        caseInfo.setSql(sql);
    }

}
