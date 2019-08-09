package com.fishpro.quartz;

import com.fishpro.quartz.job.WelcomeJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

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
