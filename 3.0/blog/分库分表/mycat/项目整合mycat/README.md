# 天天吃货整合MyCAT

在天天吃货项目中，我们只对订单相关的表做分库分表，也就是orders,order_items,order_status，这3张表做分库分表，我们需要做以下操作：

1. **新建一个数据库，将原数据库中的orders,order_items,order_status，复制到新建的数据库中。**这时，原数据库中存在所有的表，新数据库中只有订单的3张表。
2. 在mycat中，配置新老数据库，配置分片表（订单表3张），分片规则：orders的分片字段为user_id，order_items和order_status两张表作为订单表的子表，用order_id字段关联订单表的id字段。分片规则使用的是mycat中存在的`sharding-by-murmur`规则（一致性Hash）。启动mycat。
3. 修改项目中的数据库连接，直接连接到Mycat即可。

schema.xml中的内容如下：

```xml
<?xml version="1.0"?>
<!DOCTYPE mycat:schema SYSTEM "schema.dtd">
<mycat:schema xmlns:mycat="http://io.mycat/">

        <schema name="foodie-shop-dev" checkSQLschema="false" sqlMaxLimit="100" dataNode="dn131">
                <table name="orders" dataNode="dn131,dn132" rule="sharding-by-murmur">
                        <childTable name="order_items" joinKey="order_id" parentKey="id"/>
                        <childTable name="order_status" joinKey="order_id" parentKey="id" />
                </table>
        </schema>
        <dataNode name="dn131" dataHost="db131" database="foodie-shop-dev" />
        <dataNode name="dn132" dataHost="db132" database="foodie-shop-dev" />
        <dataHost name="db131" maxCon="1000" minCon="10" balance="0"
                          writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
                <heartbeat>select user()</heartbeat>
                <writeHost host="M1" url="192.168.73.131:3306" user="imooc"
                                   password="Imooc@123456">
                </writeHost>
        </dataHost>
        <dataHost name="db132" maxCon="1000" minCon="10" balance="0"
                          writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
                <heartbeat>select user()</heartbeat>
                <writeHost host="M1" url="192.168.73.132:3306" user="imooc"
                                   password="Imooc@123456">
                </writeHost>
        </dataHost>
</mycat:schema>

```

server.xml中，我们新创建用户

```xml
		<user name="root" defaultAccount="true">
                <property name="password">root</property>
                <property name="schemas">foodie-shop-dev</property>

                <!-- 表级 DML 权限设置 -->
                <!--            
                <privileges check="false">
                        <schema name="TESTDB" dml="0110" >
                                <table name="tb01" dml="0000"></table>
                                <table name="tb02" dml="1111"></table>
                        </schema>
                </privileges>           
                 -->
        </user>

        <user name="user">
                <property name="password">user</property>
                <property name="schemas">foodie-shop-dev</property>
                <property name="readOnly">true</property>
        </user>

```

由于订单表是主表，我们在生成订单时，要先插入主表，然后才能插入子表，我们的程序也要做相应的调整。

```java
newOrder.setIsComment(YesOrNo.NO.type);
newOrder.setIsDelete(YesOrNo.NO.type);
newOrder.setCreatedTime(new Date());
newOrder.setUpdatedTime(new Date());

/**
         *  分库分表：orderItems作为orders的子表，所有插入时，要先插入Orders，
         *  这样在插入OrderItems时，才能找到对应的分片。所以这里先插入Orders,
         *  计算金额后，再更新一下Orders.
         */
newOrder.setTotalAmount(0);
newOrder.setRealPayAmount(0);
ordersMapper.insert(newOrder);


// 2. 循环根据itemSpecIds保存订单商品信息表
String itemSpecIdArr[] = itemSpecIds.split(",");
```

在循环操作之前，我们先插入orders表，由于金额在循环中计算，这里我们先插入0。这时，订单数据已经根据分片规则，确定了自己的数据库。后续在插入order_items和order_status时，就可以根据主表，确定自己所在的分片库了。

循环计算完金额后，在更新以下orders表的数据。

```java
newOrder.setTotalAmount(totalAmount);
newOrder.setRealPayAmount(realPayAmount);
/**
  * 分库分表：计算金额后，更新Orders，
  *  由于userId是分片项，不能修改，所以在更新时，设置为null
  */
newOrder.setUserId(null);
ordersMapper.updateByPrimaryKeySelective(newOrder);
```

# 完整代码

```java
@Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(List<ShopCartBO> shopCartBOList,SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        String addressId = submitOrderBO.getAddressId();
        String leftMsg = submitOrderBO.getLeftMsg();
        Integer payMethod = submitOrderBO.getPayMethod();

        Integer postAmount = 0;

        // 1.生成 新订单 ，填写 Orders表
        final String orderId = sid.nextShort();

        UserAddress userAddress = addressService.queryAddress(userId, addressId);

        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setLeftMsg(leftMsg);
        orders.setPayMethod(payMethod);

        orders.setReceiverAddress(userAddress.getProvince() + " " + userAddress.getCity() + " "
                + userAddress.getDistrict() + " " + userAddress.getDetail());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setReceiverName(userAddress.getReceiver());

        orders.setPostAmount(postAmount);


        orders.setIsComment(YesOrNo.NO.type);
        orders.setIsDelete(YesOrNo.NO.type);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());


        /*
            分库分表：orderItems作为orders的子表，所有插入时，要先插入Orders，
            这样在插入OrderItems时，才能找到对应的分片。所以这里先插入Orders,
            计算金额后，再更新一下Orders.
         */
        ordersMapper.insert(orders);



        // 2.1 循环根据商品规格表，保存到商品规格表

        int totalAmount = 0;
        int realPayTotalAmount = 0;
        final String[] itemSpecIdArray = StringUtils.split(itemSpecIds, ',');
        List<ShopCartBO> toBeRemovedList = new ArrayList<>();
        for (String itemSpecId : itemSpecIdArray) {

            // 查询每个商品的规格

            final ItemsSpec itemsSpec = itemService.queryItemBySpecId(itemSpecId);
            final ShopCartBO shopCartBO = getShopCartBOFromList(shopCartBOList, itemSpecId);

            toBeRemovedList.add(shopCartBO);
            int counts = 0;
            if (shopCartBO != null) {
                counts = shopCartBO.getBuyCounts();
            }
            // 获取价格
            totalAmount += itemsSpec.getPriceNormal() * counts;
            realPayTotalAmount += itemsSpec.getPriceDiscount() * counts;

            // 2.2 根据商品id，获得商品图片和信息
            final String itemId = itemsSpec.getItemId();
            final String imgUrl = itemService.queryItemImgByItemId(itemId);

            // 2.3 将商品规格信息写入 订单商品表
            final OrderItems subOrderItem = new OrderItems();
            subOrderItem.setBuyCounts(counts);
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setItemId(itemId);
            subOrderItem.setId(sid.nextShort());
            subOrderItem.setItemName(itemsSpec.getName());
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);


            // 2.4 减库存
            itemService.decreaseItemSpecStock(itemSpecId, counts);

        }

        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayTotalAmount);

        // 因为 userId 是分片项.不能修改，所以在更新时设置成 null
        orders.setUserId(null);
        ordersMapper.updateByPrimaryKeySelective(orders);

//        ordersMapper.insert(orders);

        // 3. 订单状态表
        final OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        try {
            Thread.sleep(3 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_DELIVER.type);
        orderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(orderStatus);


        final OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setToBeRemovedList(toBeRemovedList);
        return orderVO;
    }
```