package com.leosanqing.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: leosanqing
 * @Date: 2019-12-07 19:42
 */
@Component
@Aspect
public class ServiceLogAspect {
    private static Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     * AOP 的五种通知
     * 1.前置通知    方法调用前执行
     * 2.后置通知    方法正常调用后执行
     * 3.环绕通知    方法调用前后分别执行
     * 4.异常通知    方法调用发生异常，则通知
     * 5.最终通知    方法调用后通知。不管调用正不正常 相当于finally
     */


    /**
     * 切面表达式
     *
     * 第一处 * 代表方法返回类型 * 代表所有类型
     * 第二次 包名  代表aop监控的包
     * 第三处 .. 代表该包及其子包下的所有类方法
     * 第四处 * 代表类名 *代表所有类
     * 第五处 . 表示引用的点
     * 第六处 *(..) * 代表类名，(..)表示方法中的任何参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */

    @Around("execution(* com.leosanqing.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("=====  开始执行 {}.{} =====",
                // 获取类名
                joinPoint.getTarget().getClass(),
                // 获取方法
                joinPoint.getSignature().getName());

        // 记录开始时间
        long begin = System.currentTimeMillis();

        // 执行目标 service
        Object result = joinPoint.proceed();

        //结束时间
        long end = System.currentTimeMillis();
        long takeTime = end - begin;
        if(takeTime > 3000){
            logger.error("===== 执行结束 耗时: {} 毫秒=====",takeTime);
        }else if(takeTime > 2000){
            logger.warn("===== 执行结束 耗时: {} 毫秒=====",takeTime);
        }else{
            logger.info("===== 执行结束 耗时: {} 毫秒=====",takeTime);
        }

        return result;
    }
}
