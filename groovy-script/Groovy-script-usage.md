## Groovy 脚本使用整理

### 1.常见的三种用法
1.1 Groovy shell
1.2 ScriptEngineManager (javax提供能力)
1.3 GroovyClassLoader (最常用)

### 2.坑点
原始的 GroovyClassLoader，通过分析底层加载代码可知，它不会根据 脚本内容去重，即使内容相同，那么两次加载中都会 new 出不同的 Class
如果大量调用，会导致 JVM metaspace 内存溢出，所以我们在使用时，最好根据 脚本 content 做一个内容 hash 去重，做一下对象复用

### 