# 前言

在项目中，都会涉及到并发请求的逻辑，这篇文章主要讲下**超卖**的情况

我们上面的程序在并发的时候会出问题，会出什么问题，我们下面通过程序模拟一下

整个程序在

# 超卖现象

## 什么是超卖？

我们的商品a只有一个库存，但是多个用户在购买的时候，却出现了用户 a 用户b都下了单，但是库存是0，相当于库存减了1，但是实际减了2

第二种情况是库存数被减成了 负数



这个并发部分，就是将如何使用并发控制语句或者组件来达到防止高并发下的超卖现象

主要可分为：

- 单体锁
  - 使用数据库的 行锁
  - 使用 Synchronized 关键字
  - 使用ReentrantLock
- 分布式锁
  - 使用 Zookeeper
  - 使用redis的 setnx
  - curator 
  - redission

# 优缺点





## 一

我们在程序中可能会这样写下单的步骤

```java
@Transactional(rollbackFor = Exception.class)
public Integer createOrderNormal() throws Exception {
    Product product = productMapper.selectByPrimaryKey(purchaseProductId);

    if (product == null) {

        throw new Exception("购买商品：" + purchaseProductId + "不存在");
    }


    //商品当前库存
    Integer currentCount = product.getCount();
    System.out.println(Thread.currentThread().getName() + "库存数：" + currentCount);
    //校验库存
    if (purchaseProductNum > currentCount) {
        throw
                new Exception("商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
    }


    Integer leftCount = currentCount - purchaseProductNum;
    // 更新库存
    product.setCount(leftCount);
    product.setUpdateTime(new Date());
    product.setUpdateUser("xxx");
    productMapper.updateByPrimaryKeySelective(product);


    Order order = new Order();
    order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
    order.setOrderStatus(1);//待处理
    order.setReceiverName("xxx");
    order.setReceiverMobile("13311112222");
    order.setCreateTime(new Date());
    order.setCreateUser("xxx");
    order.setUpdateTime(new Date());
    order.setUpdateUser("xxx");
    orderMapper.insertSelective(order);

    OrderItem orderItem = new OrderItem();
    orderItem.setOrderId(order.getId());
    orderItem.setProductId(product.getId());
    orderItem.setPurchasePrice(product.getPrice());
    orderItem.setPurchaseNum(purchaseProductNum);
    orderItem.setCreateUser("xxx");
    orderItem.setCreateTime(new Date());
    orderItem.setUpdateTime(new Date());
    orderItem.setUpdateUser("xxx");
    orderItemMapper.insertSelective(orderItem);
    return order.getId();
}
```

请注意我们的更新库存的步骤，我们在程序里面写了更新的语句，然后扣减库存设置好相应的字段之后，再调用数据库去进行更新

但是我们的数据库中却出现了这样诡异的事情，5笔订单，但是库存数是0，(用5个线程模拟5个用户同时进行下单操作)；

那么问题出在哪里呢