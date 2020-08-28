# dubbo性能测试

测试环境：

1consumer 1provider ab.exe

1.运行 damo-service/damo-service-provider

java -jar damo-service-provider-2.0.0.jar

2.运行 damo-service/damo-service-consumer

java -jar damo-service-consumer-2.0.0.jar

3.运行ab,在apache的bin目录下。

ab -n 800 -c 800  "http://127.0.0.1:9091/sayHello?name=11&time=100"
（-n发出800个请求，-c模拟800并发，相当800人同时访问，后面是测试url）

```
Document Path:          /sayHello?name=11&time=100
Document Length:        28 bytes

Concurrency Level:      800
Time taken for tests:   1.235 seconds
Complete requests:      800
Failed requests:        0
Write errors:           0
Total transferred:      179200 bytes
HTML transferred:       22400 bytes
Requests per second:    647.74 [#/sec] (mean)
Time per request:       1235.071 [ms] (mean)
Time per request:       1.544 [ms] (mean, across all concurrent requests)
Transfer rate:          141.69 [Kbytes/sec] received
```

4.调节-n,-c,time;来求Requests per second的最大值。

