package com.study.cn.spider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author dell、
 * @version 1.0.0 createTime: 2017/5/10 14:17
 * @see
 * @since 1.0
 */
@Configuration
public class SchedledConfiguration {

    @Bean(name = "detailFactoryBean")
    public MethodInvokingJobDetailFactoryBean detailFactoryBean(ScheduledTasks scheduledTasks){
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();

        bean.setTargetObject(scheduledTasks);
        bean.setTargetMethod("work");
        bean.setConcurrent(false);
        return bean;
    }

    @Bean(name = "cronTriggerBean")
    public CronTriggerFactoryBean cronTriggerFactoryBean(MethodInvokingJobDetailFactoryBean detailFactoryBean){
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(detailFactoryBean.getObject());
        try {
            //设置定时时间
            trigger.setCronExpression("0 31 15 ? * *");
        }catch (Exception e){
            e.printStackTrace();
        }
        return trigger;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory(CronTriggerFactoryBean  cronTriggerFactoryBean){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setTriggers(cronTriggerFactoryBean.getObject());
        return schedulerFactoryBean;
    }

}
