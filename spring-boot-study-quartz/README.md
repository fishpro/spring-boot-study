Quartz是一个完全由java编写的开源作业调度框架，他使用非常简单。本章主要讲解 Quartz在Spring Boot 中的使用。
1. 快速集成 Quartz
2. 介绍 Quartz 几个主要技术点
3. Quartz 在 Spring Boot 的配置
4. Quartz 在 Spring Boot 中整合 Mybatis


# 1 新建 Spring Boot Maven 示例工程项目 
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=quartz
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-quartz`.
   
# 2 依赖引入 Pom
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-quartz</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

# 3 使用 Quartz 

## 3.1 在启动文件中编写一个简单的使用
QuartzApplication 中直接编写一启动关闭类代码
```java
@SpringBootApplication
public class QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            //启动
            scheduler.start();
            //关闭
            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }
}
```

运行在控制台观察结果
```cmd
2019-08-08 15:09:01.313  INFO 21565 --- [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler 'DefaultQuartzScheduler' initialized from default resource file in Quartz package: 'quartz.properties'
2019-08-08 15:09:01.313  INFO 21565 --- [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler version: 2.3.1
2019-08-08 15:09:01.314  INFO 21565 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED started.
2019-08-08 15:09:01.314  INFO 21565 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED shutting down.
2019-08-08 15:09:01.314  INFO 21565 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED paused.
2019-08-08 15:09:01.314  INFO 21565 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED shutdown complete.
```



## 3.2 使用 Job 和 Trigger
1. 新建一个 Job 类 WelcomeJob
2. 在启动类中调用他
WelcomeJob （路径 src/main/java/com/fishpro/quartz/job/WelcomeJob.java）
```java
public class WelcomeJob implements Job {

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
       System.out.println("这是一个quartz 任务");
    }
}
```
QuartzApplication （路径 src/main/java/com/fishpro/quartz/QuartzApplication.java）
```java
@SpringBootApplication
public class QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            //启动
            scheduler.start();
            //新建一个 Job WelcomeJob
            JobDetail job = JobBuilder.newJob(WelcomeJob.class)
                    .withIdentity("mySimpleJob", "simpleGroup")
                    .build();

            // 触发器 定义多长时间触发 JobDetail
            Trigger trigger = org.quartz.TriggerBuilder.newTrigger()
                    .withIdentity("simpleTrigger", "simpleGroup")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(10)
                            .repeatForever())
                    .build();
            scheduler.scheduleJob(job,trigger);
            //关闭
            //scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }
}
```

运行结果

```cmd
2019-08-08 16:46:36.599  INFO 21695 --- [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler 'DefaultQuartzScheduler' initialized from default resource file in Quartz package: 'quartz.properties'
2019-08-08 16:46:36.599  INFO 21695 --- [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler version: 2.3.1
2019-08-08 16:46:36.599  INFO 21695 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED started.
这是一个quartz 任务
这是一个quartz 任务
这是一个quartz 任务
这是一个quartz 任务
这是一个quartz 任务
这是一个quartz 任务
这是一个quartz 任务
这是一个quartz 任务
这是一个quartz 任务
```

-----
参考

[http://www.quartz-scheduler.org/documentation/](http://www.quartz-scheduler.org/documentation/)