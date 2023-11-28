package com.tuberose.clock.dispatch.controller;

import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import com.tuberose.clock.common.response.BaseRes;
import com.tuberose.clock.dispatch.request.JobReq;
import com.tuberose.clock.dispatch.response.JobRes;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/job")
@Slf4j
public class JobController {
    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    @PostMapping("/add")
    public BaseRes<Void> add(@RequestBody JobReq jobReq) {
        String jobClassName = "com.tuberose.clock.dispatch.job." + jobReq.getName();
        String jobGroupName = jobReq.getGroup();
        String cronExpression = jobReq.getCronExpression();
        String description = jobReq.getDescription();

        try {
            // 获取调度器
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.start();

            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(jobClassName))
                    .withIdentity(jobReq.getName(), jobGroupName).build();
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobReq.getName(), jobGroupName)
                    .withDescription(description)
                    .withSchedule(cronScheduleBuilder)
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCodeEnum.SCHEDULER_ERROR);
        } catch (ClassNotFoundException e) {
            throw new BusinessException(ErrorCodeEnum.JOB_NOT_EXIST);
        }

        log.info("创建定时任务{}", jobReq.getName());
        return BaseRes.success();
    }

    @PostMapping("/pause/{group}/{name}")
    public BaseRes<Void> pause(@PathVariable String group, @PathVariable String name) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCodeEnum.SCHEDULER_ERROR);
        }
        log.info("暂停定时任务{}", name);
        return BaseRes.success();
    }

    @PostMapping("/resume/{group}/{name}")
    public BaseRes<Void> resume(@PathVariable String group, @PathVariable String name) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.resumeJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCodeEnum.SCHEDULER_ERROR);
        }
        log.info("重启定时任务{}", name);
        return BaseRes.success();
    }

    @PutMapping(value = "/reschedule")
    public BaseRes<Void> reschedule(@RequestBody JobReq jobReq) {
        String jobName = jobReq.getName();
        String jobGroup = jobReq.getGroup();
        String cronExpression = jobReq.getCronExpression();
        String description = jobReq.getDescription();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTriggerImpl triggerImpl = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
            triggerImpl.setStartTime(new Date());
            CronTrigger trigger = triggerImpl;

            trigger = trigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withDescription(description)
                    .withSchedule(scheduleBuilder)
                    .build();

            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCodeEnum.SCHEDULER_ERROR);
        }
        log.info("更新定时任务{}", jobName);
        return BaseRes.success();
    }

    @DeleteMapping("/delete/{group}/{name}")
    public BaseRes<Void> delete(@PathVariable String group, @PathVariable String name) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(name, group));
            scheduler.unscheduleJob(TriggerKey.triggerKey(name, group));
            scheduler.deleteJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCodeEnum.SCHEDULER_ERROR);
        }
        log.info("删除定时任务{}", name);
        return BaseRes.success();
    }

    @GetMapping("/query")
    public BaseRes<List<JobRes>> query() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<JobRes> jobResList = new ArrayList<>();
        try {
            for (String group : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.groupEquals(group))) {
                    JobRes jobRes = new JobRes();
                    jobRes.setName(jobKey.getName());
                    jobRes.setGroup(group);

                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    CronTrigger cronTrigger = (CronTrigger) triggers.get(0);

                    jobRes.setNextFireTime(cronTrigger.getNextFireTime());
                    jobRes.setPreFireTime(cronTrigger.getPreviousFireTime());
                    jobRes.setCronExpression(cronTrigger.getCronExpression());
                    jobRes.setDescription(cronTrigger.getDescription());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                    jobRes.setState(triggerState.name());

                    jobResList.add(jobRes);
                }
            }
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCodeEnum.SCHEDULER_ERROR);
        }
        return BaseRes.success(jobResList);
    }

    @PostMapping("/execute/{group}/{name}")
    public BaseRes<Void> execute(@PathVariable String group, @PathVariable String name) {
        try {
            schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCodeEnum.SCHEDULER_ERROR);
        }
        log.info("主动执行定时任务{}", name);
        return BaseRes.success();
    }

}
