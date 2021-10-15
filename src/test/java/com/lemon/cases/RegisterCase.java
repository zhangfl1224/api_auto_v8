package com.lemon.cases;

import com.lemon.pojo.CaseInfo;
import com.lemon.utils.Constants;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: api_auto_v2
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: 2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: luojie
 * @Create: 2020-09-05 09:51
 * @Desc： 注册用例类
 **/
public class RegisterCase extends BaseCase {

    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) {
        //  1、参数化替换
        paramsReplace(caseInfo);
        //	2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
        Object beforeSqlResult = SqlUtils.getSingleResult(caseInfo.getSql());
        //	3、调用接口
        Map<String, Object> headers = HttpUtils.getDefaultHeaders();
        String body = HttpUtils.call(caseInfo, headers);
        //	4、断言响应结果
        boolean responseAssertResult = responseAssert(caseInfo, body);
        //	5、添加接口响应回写内容
        //回写=excel修改操作
        addWriteBackData(caseInfo.getCaseId(),Constants.RESPONSE_CELLNUM,startSheetIndex,body);
        //	6、数据库后置查询结果
        Object afterSqlResult = SqlUtils.getSingleResult(caseInfo.getSql());
        //	7、据库断言
        // 接口执行之前期望值： 0
        // 接口执行之后期望值： 1
        boolean sqlAssertFlag = sqlAssert(caseInfo.getSql(),beforeSqlResult,afterSqlResult);
        //	8、添加断言回写内容
        String assertResult = responseAssertResult && sqlAssertFlag ? "passed" : "failed";
        addWriteBackData(caseInfo.getCaseId(),Constants.ASSERT_CELLNUM,startSheetIndex,assertResult);
        //	9、添加日志
        //	10、报表断言
        Assert.assertEquals(assertResult,"passed");
    }

    /**
     * 数据库断言
     * @param sql                   sql语句
     * @param beforeSqlResult       接口执行前的实际值
     * @param afterSqlResult        接口执行后的实际值
     */
    public boolean sqlAssert(String sql,Object beforeSqlResult,Object afterSqlResult) {
        //如果sql不为空执行数据库断言
        boolean flag = true;
        if(!StringUtils.isBlank(sql)) {
            //转换数据类型
            Long beforeResult = (Long)beforeSqlResult;
            Long afterResult = (Long)afterSqlResult;
            boolean sqlAssertResult = false;
            //接口执行之前期望值： 0 接口执行之后期望值： 1
            if (beforeResult == 0 && afterResult == 1) {
                System.out.println("数据库断言成功");
                sqlAssertResult = true;
            } else {
                System.out.println(beforeResult);
                System.out.println(afterResult);
                System.out.println("数据库断言失败");
                flag = false;
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
