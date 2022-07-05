package com.example.ioc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    // ApplicationContext applicationContext: 외부로부터 주입을 받음(스프링이 알아서 주입 해줌)
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {

        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
