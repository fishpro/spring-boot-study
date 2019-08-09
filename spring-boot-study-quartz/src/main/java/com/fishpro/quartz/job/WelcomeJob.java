package com.fishpro.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class WelcomeJob implements Job {

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

       System.out.println("这是一个quartz 任务");

    }
}
