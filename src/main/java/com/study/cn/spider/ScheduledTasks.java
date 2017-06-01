package com.study.cn.spider;

import com.gargoylesoftware.htmlunit.WebClient;

import com.study.cn.model.SinaAccount;
import com.study.cn.model.SinaUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author dell、
 * @version 1.0.0 createTime: 2017/5/10 14:21
 * @see
 * @since 1.0
 */
@Component
@Configurable
@EnableScheduling
@Slf4j
public class ScheduledTasks {

    @Value("classpath:sinaUser.txt")
    private Resource sinaUserFile;

    private String[] nicknameList;

    public void init(){
        try {
            if (sinaUserFile != null){
                BufferedReader bff = new BufferedReader(new FileReader(sinaUserFile.getFile()));
                String lineTxt = null;
                while ((lineTxt = bff.readLine()) != null){
                    nicknameList = lineTxt.split(",");
                }
                bff.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public ScheduledTasks(){
        super();
    }

    public void work(){
        System.err.println(System.currentTimeMillis());
        init();
        if (nicknameList == null || nicknameList.length < 1){
            return;
        }
        int index = 0;
        int threadSize = 5;
        int singleThreadSize = 0;
        if (nicknameList.length % threadSize == 0){
            singleThreadSize = nicknameList.length / threadSize;
        }else {
            singleThreadSize = (int) Math.floor((double)nicknameList.length / threadSize );
            threadSize += 1;
        }

        ExecutorService pool = Executors.newFixedThreadPool(threadSize);
        List<MoniotrTask> moniotrTaskList = new ArrayList<>();
        for (int i = 0; i < threadSize; i++){
            if (i == threadSize -1){
                moniotrTaskList.add(new MoniotrTask(new ArrayList<>(Arrays.asList(Arrays.copyOfRange(nicknameList, index, nicknameList.length)))));
                break;
            }
            moniotrTaskList.add(new MoniotrTask(new ArrayList<>(Arrays.asList(Arrays.copyOfRange(nicknameList, index, (i + 1) * singleThreadSize)))));
            index = (i + 1) * singleThreadSize;
        }
        for (MoniotrTask task : moniotrTaskList){
            pool.execute(task);
        }
        pool.shutdown();
        System.err.println(System.currentTimeMillis());
    }
}
