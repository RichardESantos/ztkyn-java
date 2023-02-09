
# 项目使用说明

## transmittable-thread-local
[GitHub - Dreamroute/ttl: transmittable-thread-local](https://hub.yzuu.cf/Dreamroute/ttl)

-   系统中被@Async标记的方法回从Spring线程池中获取线程，获取到的线程已经被ttl处理过，对用户来说是透明的
-   如果系统中其他地方需要使用线程池，并且希望带着ThreadLocal信息，那么直接注入ExecutorService即可，starter已经将ExecutorService用ttl代理， 只要引入starter包，系统中任何地方注入ExecutorService，都可以享受到ThreadLocal的传递