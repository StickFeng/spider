package com.study.cn.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.study.cn.model.SinaUser;

import java.util.List;

/**
 * @author fengzhiming
 * @version 1.0.0 createTime: 2017/6/1 11:21
 * @see
 * @since 1.0
 */
public interface SinaSipderService {

    void spiderUserTopic(List<String> sinaUserList);
}
