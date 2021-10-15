package com.lemon.cases;

import com.alibaba.fastjson.JSONPath;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Project: api_auto_v2
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: 2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: luojie
 * @Create: 2020-09-05 09:51
 * @Desc： 充值用例类
 **/
public class RechargeCase extends BaseCase {

    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) {
        //  1、参数化替换
        paramsReplace(caseInfo);
        //	2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
        Object beforeSqlResult = SqlUtils.getSingleResult(caseInfo.getSql());
        //	3、调用接口
        //  3.1、获取一个带有鉴权和默认头
        Map<String, Object> headers = HttpUtils.getAuthorizationHeaders();
        //添加V3参数
        HttpUtils.authorizationParam(caseInfo);
        String body = HttpUtils.call(caseInfo, headers);
        //	4、断言响应结果
        boolean responseAssertResult = responseAssert(caseInfo, body);
        //	5、添加接口响应回写内容
        //回写=excel修改操作
        addWriteBackData(caseInfo.getCaseId(),Constants.RESPONSE_CELLNUM,startSheetIndex,body);
        //	6、数据库后置查询结果
        Object afterSqlResult = SqlUtils.getSingleResult(caseInfo.getSql());
        //	7、据库断言
        boolean sqlAssertFlag = sqlAssert(caseInfo,beforeSqlResult,afterSqlResult);
        //	8、添加断言回写内容
        String assertResult = responseAssertResult && sqlAssertFlag ? "passed" : "failed";
        addWriteBackData(caseInfo.getCaseId(),Constants.ASSERT_CELLNUM,startSheetIndex,assertResult);
        //	9、添加日志
        //	10、报表断言
        Assert.assertEquals(assertResult,"passed");
    }

    public boolean sqlAssert(CaseInfo caseInfo,Object beforeSqlResult,Object afterSqlResult) {
        boolean flag = true;
        if(!StringUtils.isBlank(caseInfo.getSql())) {
            BigDecimal beforeResult = (BigDecimal)beforeSqlResult;
            BigDecimal afterResult = (BigDecimal)afterSqlResult;
            //（jsonpath从Params中取出来）
            String params = caseInfo.getParams();
            String amountStr = JSONPath.read(params,"$.amount").toString();
            //String => BigDecimal
            BigDecimal amount = new BigDecimal(amountStr);
            // 200（充值之后的金额） - 100（充值之前的金额） == 参数（jsonpath从Params中取出来）
            // afterResult - beforeResult == amount
            // afterResult - beforeResult
            BigDecimal subtract = afterResult.subtract(beforeResult);
            System.out.println("beforeResult:" + beforeResult);
            System.out.println("afterResult:" + afterResult);
            System.out.println("subtract:" + subtract);
            System.out.println("amount:" + amount);
            // subtract.7(amount) == 0 表示两个数相等
            if(subtract.compareTo(amount) == 0) {
                System.out.println("断言成功");
            }else {
                flag = false;
                System.out.println("断言失败");
            }
        }
        return flag;
    }

    @DataProvider
    public Object[] datas() throws Exception {
        //1、从excel读取用例信息
        return ExcelUtils.read(startSheetIndex,sheetNum);
    }


}
