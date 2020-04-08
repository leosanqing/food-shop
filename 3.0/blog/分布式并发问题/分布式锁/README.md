# 前言

我们在上一节使用了单体锁-synchronized 和 ReentrantLock 解决了 超卖的现象，但是由于它是单体锁，我们在分布式的环境下就无法保证数据的准确性。

我们这节就用讲下分布式的情况下我们怎么使用分布式锁来保证高并发下的数据一致性

这个项目对应的是  **distribute-lock**



# 怎么启动两个程序

1. 复制一个项目
2. 设置不同的端口
3. 依次启动

![](img/Xnip2020-04-08_17-29-53.jpg)

![](img/Xnip2020-04-08_17-32-13.jpg)



# 单体锁的局限性

我们先用程序模拟下单体锁在分布式项目下的问题

项目代码

```java

    /**
     * 单体锁
     * @return
     * @throws Exception
     */
    @RequestMapping("singleLock")
    @Transactional(rollbackFor = Exception.class)
    public  String singleLock() throws Exception {
        log.info("我进入了方法！");
        synchronized (this) {
            log.info("我进入了锁！");
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return "我已经执行完成！";
    }

```

我们分别访问 这两个方法

我们发现，他并没有锁住相应的语句，正常一个应该只能进入方法，因为另一个没有执行完，他无法获取锁。这就是单体锁的局限性

![](img/Xnip2020-04-08_17-38-02.jpg)

# 数据库锁

select ... for update

简单的说就是 如果不 commit，那么后面sql访问同一条数据，上述语句都无法进行提交，都被阻塞

我们用程序来验证一下，我们让他执行20s之后结束，再释放锁

```java
/**
 * dbDisLock
 * @return
 * @throws Exception
 */
@RequestMapping("dbDisLock")
@Transactional(rollbackFor = Exception.class)
public String dbDisLock() throws Exception {
    log.info("我进入了方法！");
    DistributeLock distributeLock = distributeLockMapper.selectDistributeLock("demo");
    if (distributeLock==null) throw new Exception("分布式锁找不到");
    log.info("我进入了锁！");
    try {
        Thread.sleep(20000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    log.info("我已经执行完，我要释放锁");
    return "我已经执行完成！";
}
```

 这个是 上面执行的 sql 语句

```xml
<select id="selectDistributeLock" resultType="com.example.distributelock.model.DistributeLock">
  select * from distribute_lock
  where business_code = #{businessCode,jdbcType=VARCHAR}
  for update
</select>
```



我们来看结果

![](img/Xnip2020-04-08_19-17-08.jpg)

![](img/Xnip2020-04-08_19-17-23.jpg)

我们看到 8081 的这个程序 是在 8080这个 程序执行完之后才进入的锁，所以这个已经达到了我们的目的。

