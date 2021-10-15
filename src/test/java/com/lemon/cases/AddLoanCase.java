package com.lemon.cases;

import com.lemon.pojo.CaseInfo;
import com.lemon.utils.Constants;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @Project: api_auto_v6
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: 2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: luojie
 * @Create: 2020-09-17 21:46
 * @Desc： 新增项目用例类
 **/
public class AddLoanCase extends BaseCase {

    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) {
        //  1、参数化替换
        paramsReplace(caseInfo);
        //	2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
        //	3、调用接口
        Map<String, Object> headers = HttpUtils.getAuthorizationHeaders();
        //添加V3参数
        HttpUtils.authorizationParam(caseInfo);
        String body = HttpUtils.call(caseInfo, headers);
        //把loan_id存储到UserData
        response2UserData(body, "$.data.id", "${loan_id}");
        //	4、断言响应结果
        boolean responseAssertResult = responseAssert(caseInfo, body);
        //	5、添加接口响应回写内容
        //回写=excel修改操作
        addWriteBackData(caseInfo.getCaseId(), Constants.RESPONSE_CELLNUM,startSheetIndex,body);
        //	6、数据库后置查询结果
        //	7、据库断言
        //	8、添加断言回写内容
        String assertResult = responseAssertResult ? "passed" : "failed";
        addWriteBackData(caseInfo.getCaseId(),Constants.ASSERT_CELLNUM,startSheetIndex,assertResult);
        //	9、添加日志
        //	10、报表断言
        Assert.assertEquals(assertResult,"passed");
    }

    @DataProvider
    public Object[] datas() throws Exception {
        //1、从excel读取用例信息
        return ExcelUtils.read(startSheetIndex,sheetNum);
    }
}
