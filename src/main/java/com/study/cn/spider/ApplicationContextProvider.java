package com.study.cn.spider;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author dell、
 * @version 1.0.0 createTime: 2017/5/12 9:45
 * @see
 * @since 1.0
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    public ApplicationContextProvider(){};

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static Object getBeanByName(String beanName){
        if (context == null) {
            return null;
        }
        return context.getBean(beanName);

    }

    public static <T> T getBean( Class<T> aClass){
        return context.getBean(aClass);
    }
}
