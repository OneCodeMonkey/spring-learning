# Redis operations Tests

## 1.1 Basic dataType operations
## ✅String
## ✅List
## ✅Set
## ✅Sorted Set
## ✅Hash
## ✅Geo
## ✅HyperLogLog
## bitmap

## 1.2 使用细节
### 1.2.1 定义 redis 多数据源

### 1.2.2 使用pipeline 进行批量操作
优势：
1.不局限于 mget，mset 这样特定数据类型，可以封装不同数据类型的多个操作，甚至是多个复杂操作，让其并发执行
2.在需要大量命令快速执行完的场景下，如果每次执行单条 redis 命令，那么总体算下来时间会非常长，因为大量时间花在 发送/接受 的网络耗时，以及系统调用 read(), write()，系统频繁从 内核态到用户态，用户态到内核态的切换耗时。而实际 redis server 执行时间占比很小。
  所以在批量执行命令时，pipeline 能节省大量时间。

缺点：
1. 由于 pipeline 发送命令的特点，服务端是在内存中用队列来储备 待执行的命令，所以需要自己控制好 单次发送的数量，避免 server 端内存消耗过大。
2. pipeline 的特点是需要等准备好 所有命令后，再一次批量执行完。所以中途无法查询到中间结果，只有等整体执行完才知道结果。
类似下方的调用方式，会抛异常：
```java
for(Integer i = 0; i < 100000; i++) {
    p.set(i.toString(), i.toString());
}
 
Response<String> response = p.get("999");
// System.out.println(response.get()); // 执行报异常：redis.clients.jedis.exceptions.JedisDataException: Please close pipeline or multi block before calling this method.
 
/* 删除多条数据 */
for(Integer i = 0; i < 100000; i++) {
    p.del(i.toString());
}
p.sync();
```   

###### ref articles
https://mp.weixin.qq.com/s/D1S4cN5B-TXiufTDOVaoiQ