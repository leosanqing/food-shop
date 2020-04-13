package com.leosanqing.utils;

import org.slf4j.MDC;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


/**
 * @Author: leosanqing
 * @Date: 2020/3/17 下午11:02
 * @Package: com.leosanqing.utils
 * @Description: 将信息存入MDC
 */
@Component
public class InputMDC implements EnvironmentAware {
    private static Environment environment;


    @Override
    public void setEnvironment(Environment environment) {
        InputMDC.environment = environment;
    }

    public static void inputMDC(){
        MDC.put("hostName",NetUtil.getLocalHostName());
        MDC.put("ip",NetUtil.getLocalIp());
        MDC.put("applicationName",environment.getProperty("spring.application.name"));

    }
}
