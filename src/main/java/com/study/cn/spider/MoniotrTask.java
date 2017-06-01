package com.study.cn.spider;

import com.gargoylesoftware.htmlunit.WebClient;

import com.study.cn.model.SinaUser;
import com.study.cn.service.impl.SinaSpiderServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author fengzhiming
 * @version 1.0.0 createTime: 2017/5/12 9:51
 * @see
 * @since 1.0
 */
@Component("mTask")
@Scope("prototype")
public class MoniotrTask extends Thread{

     private SinaSpiderServiceImpl sinaSpiderServiceImpl;

     private List<String> sinaUserList;

     private static AtomicInteger count = new AtomicInteger(0);

    public MoniotrTask(List<String> sinaUserList){
        this.sinaUserList = sinaUserList;
        this.sinaSpiderServiceImpl = (SinaSpiderServiceImpl) ApplicationContextProvider.getBeanByName("sinaSpiderServiceImpl");
     }

    @Override
    public void run() {
        sinaSpiderServiceImpl.spiderUserTopic(sinaUserList);
        count.addAndGet(1);
        System.err.println("当前线程为==>>" + Thread.currentThread().getName() + "count：" + count    );
    }

    public List<String> getSinaUserList() {
        return sinaUserList;
    }

    public void setSinaUserList(List<String> sinaUserList) {
        this.sinaUserList = sinaUserList;
    }
}
