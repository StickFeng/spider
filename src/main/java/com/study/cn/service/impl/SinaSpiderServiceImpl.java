package com.study.cn.service.impl;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.study.cn.model.SinaUser;
import com.study.cn.service.SinaSipderService;
import com.study.cn.spider.Login;
import com.study.cn.spider.SinaSpider;
import com.study.cn.util.StrUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fengzhiming
 * @version 1.0.0 createTime: 2017/6/1 11:21
 * @see
 * @since 1.0
 */
@Service
public class SinaSpiderServiceImpl implements SinaSipderService {

    @Autowired
    private SinaSpider sinaSpider;

    @Override
    public void spiderUserTopic(List<String> sinaUserList) {
        try {
            FileWriter fw = new FileWriter("F:\\weibo.txt");
            for (String nickname : sinaUserList){
                String topic = sinaSpider.spiderUserTopic(nickname);
                String record = nickname + "发表了微博==>>" + topic + "\r\n";
                fw.write(record);
            }
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
