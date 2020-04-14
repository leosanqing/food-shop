package com.example.distributedemo;

import com.example.distributedemo.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributeDemoApplicationTests {
    @Autowired
    private OrderService orderService;

    @Test
    public void concurrentOrder1() throws InterruptedException {
//        Thread.sleep(60000);
        CountDownLatch cdl = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            es.execute(() -> {
                try {
                    cyclicBarrier.await();
                    Integer orderId = orderService.createOrder1();
                    System.out.println("订单id：" + orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cdl.countDown();
                }
            });
        }
        cdl.await();
        es.shutdown();
    }


    @Test
    public void concurrentOrderSyn() throws InterruptedException {
//        Thread.sleep(60000);
        CountDownLatch cdl = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            es.execute(() -> {
                try {
                    cyclicBarrier.await();
                    Integer orderId = orderService.createOrderSyn();
                    System.out.println("订单id：" + orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cdl.countDown();
                }
            });
        }
        cdl.await();
        es.shutdown();
    }
    @Test
    public void concurrentOrderNormal() throws InterruptedException {
//        Thread.sleep(60000);
        CountDownLatch cdl = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            es.execute(() -> {
                try {
                    cyclicBarrier.await();
                    Integer orderId = orderService.createOrderNormal();
                    System.out.println("订单id：" + orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cdl.countDown();
                }
            });
        }
        cdl.await();
        es.shutdown();
    }

    @Test
    public void concurrentOrderReentrantLock() throws InterruptedException {
//        Thread.sleep(60000);
        CountDownLatch cdl = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            es.execute(() -> {
                try {
                    cyclicBarrier.await();
                    Integer orderId = orderService.createOrderReentrantLock();
                    System.out.println("订单id：" + orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cdl.countDown();
                }
            });
        }
        cdl.await();
        es.shutdown();
    }
}
