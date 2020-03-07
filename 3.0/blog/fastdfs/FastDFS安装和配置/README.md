# 前言

我们要升级我们的文件存储系统



我们需要准备两个机器 一台作为 storage 一台作为 tracker,

当然如果没有额外的虚拟机,全部在一台上也是可以的

![](img/Xnip2020-03-07_09-44-57.jpg)

# 下载

1. fastdfs
2. Fastdfs-nginx-module
3. libfastcommon
4. Nginx

# 安装

## 环境配置

1. 安装 c语言环境 `yum install -y gcc gcc-c++`
2. 安装 libevent `yum -y install libevent`

## 安装

1. 解压 `libfastcommon`,`tar -zxvf {包名}` 
2. 进入 文件夹  `./make.sh`
3. 安装 `./make.sh install`
4. 安装 fastdfs 。重复上述步骤

## 复制配置文件

进入 fastdfs文件夹下的 conf 文件夹. 拷贝配置文件 `cp * /etc/fdfs/`

![](img/Xnip2020-03-06_10-57-27.jpg)

## 修改配置文件

### 当 tracker 的机器

修改配置文件  `vim /etc/fdfs/tracker.conf`

```javascript
# the base path to store data and log files
# 你也可以不修改，不过改了之后知道自己数据存放在哪里
base_path = /opt/FastDFS/tracker
```

### 当 storage 的机器

修改配置文件 `vim /etc/fdfs/storage.conf`

```javascript
# 可以不改
base_path = /opt/FastDFS/storage
store_path0 = /opt/FastDFS/storage

# 这个改成我们 tracker的地址
tracker_server = 10.211.55.13:22122
```

## 启动机器

先启动 tracker

`/usr/bin/fdfs_trackerd /etc/fdfs/tracker.conf`

再启动 storage

`/usr/bin/fdfs_storaged /etc/fdfs/storage.conf`



# 验证

我们 切换至 storage 机器

我们用他的客户端测试

1. 修改配置文件`vim /etc/fdfs/client.conf`

   ```javascript
   # 把路径改了 
   base_path = /opt/FastDFS/client
   
   # 把tracker的ip改了
   tracker_server = 10.211.55.13:22122 
   ```

2. 新建文件夹  `mkdir /opt/FastDFS/client`

3. 我们把 `/etc/fdfs`文件夹下的 这个文件上传

   `/usr/bin/fdfs_test /etc/fdfs/client.conf upload anti-steal.jpg`

   ![](img/Xnip2020-03-07_09-18-21.jpg)

4. 可能遇到的错误

   ` ERROR - file: connection_pool.c, line: 142, connect to server 10.211.55.13:22122 fail, errno: 113, error info: No route to host`

   由于 tracker的防火墙没有关闭 

   切换至 tracker机器输入 `systemctl stop firewalld.service`

5. 再重新运行上面的命令

   ![](img/Xnip2020-03-07_09-33-13.jpg)

6. 我们访问下 上面的地址 访问storage 机器`cd /opt/FastDFS/storage/data/00/00/`

   ![](img/Xnip2020-03-07_09-38-06.jpg)

7. 我们现在不能使用 浏览器查看 ，需要配置好 Nginx只有，先使用  filezilla 下载下来查看 

   ![](img/Xnip2020-03-07_09-38-17.jpg)

8. 说明我们 FastDFS已经配置好了

