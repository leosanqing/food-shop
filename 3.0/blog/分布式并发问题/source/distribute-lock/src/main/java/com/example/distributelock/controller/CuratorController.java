package com.example.distributelock.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class CuratorController {

    @Autowired
    private CuratorFramework client;

    @RequestMapping("curatorLock")
    public String curatorLock(){
        log.info("我进入了方法！");
        InterProcessMutex lock = new InterProcessMutex(client, "/order");
        try{
            if (lock.acquire(30, TimeUnit.SECONDS)){
                log.info("我获得了锁！！");
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                log.info("我释放了锁！！");
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("方法执行完成！");
        return "方法执行完成！";
    }
}
