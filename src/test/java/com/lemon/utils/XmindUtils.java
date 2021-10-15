package com.lemon.utils;

import org.xmind.core.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Project: api_auto_v8
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2021 版权所有 湖南省零檬信息技术有限公司
 * @Author: luojie
 * @Create: 2021-07-12 17:19
 * @Desc：
 **/
public class XmindUtils {
    public static void main(String[] args) throws Exception {
        IWorkbookBuilder builder = Core.getWorkbookBuilder();// 初始化builder
        IWorkbook workbook = null;
        workbook = builder.loadFromFile(new File("D:\\code\\java21\\api_auto_v8\\src\\test\\resources\\test.xmind"));// 打开XMind文件
        ISheet defSheet = workbook.getPrimarySheet();// 获取主Sheet
        ITopic rootTopic = defSheet.getRootTopic(); // 获取根Topic
        String className = rootTopic.getTitleText();//节点TitleText
        List<ITopic> allChildren = rootTopic.getAllChildren();//获取所有子节点
        for (ITopic allChild : allChildren) {
            System.out.println(allChild);
        }
    }
}
