# food-shop

# 前言

这个项目是 慕课网的『Java架构师成长直通车』的内容，如果想看详细的，可以买这个课程。我只是借助这个课程，将之后可能会用到的技术，梳理一遍。

如果你刚入门springboot，需要一个项目，那么这个项目也是可以直接拿来用的。我尽量做到把环境部署步骤写详尽，你们按照这个步骤就能搭建好项目的环境，然后运行起来。

**毕竟，我也是从小白开始的，我知道很多时候，我们过来人一看就知道的坑，小白不知道，可能就那一小步，导致项目没有跑起来，然后否定自己的能力。**

# 需要准备的环境

1. JDK8

2. tomcat9(用于部署前端)

3. Mysql 5.7.24

4. idea 的lombok插件。@Data注解用

   

# 需要准备的工具

1. idea

2. VSCode(前端用，其他的也行，直接使用txt也行，反正改不了多少东西)

3. Navicat(数据库用)

4. Parallel 部署虚拟机 centos7

5. secureCRT 链接虚拟机

6. postman 测试端口

7. filezilla 传输文件使用

    

# 版本

目前已经完成两个版本

## 1.0

单体项目，前端运行在tomcat即可，后端直接使用idea运行，更改数据库等配置即可

查看1.0的文件夹就行，但是现在还没有更新文章，

## 2.0

- 采用 LVS+Keepalived+Nginx 实现高可用以及高并发集群
- 使用 Redis作为缓存，提升系统性能，搭建集群提高并发和高可用

- LVS+Keepalived+Nginx
  - [Nginx](https://github.com/leosanqing/food-shop/tree/master/2.0/blog/nginx%E5%AE%89%E8%A3%85)(待更)
  - [keepalived](https://github.com/leosanqing/food-shop/tree/master/2.0/blog/keepalived)
  - [LVS](https://github.com/leosanqing/food-shop/tree/master/2.0/blog/lvs)
  - [LVS+Keepalived](https://github.com/leosanqing/food-shop/tree/master/2.0/blog/lvs%2Bkeepalived)
- Redis相关
  - [redis安装和配置](https://github.com/leosanqing/food-shop/tree/master/2.0/blog/redis)
  - [主从复制和哨兵模式](https://github.com/leosanqing/food-shop/tree/master/2.0/blog/redis/%E9%85%8D%E7%BD%AE%E4%B8%BB%E4%BB%8E%E5%A4%8D%E5%88%B6)
  - Redis集群配置(待更)

## 3.0

这个版本主要增加了 几个个中间件

- 使用ES 做为搜索，搜索的关键词可高亮
- 使用FastDFS 作为图片上传的工具。
- 使用 RabbitMQ 作为消息队列
- 使用 ELK(ES，Kibana、LogStash) + kafka 作为日志搜集

