package com.augurit.efficiency.utils;

import com.augurit.efficiency.job.Job;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class JobUtil {

    public static void doJob(AtomicBoolean jobStatus, String jobName, Job job, Logger logger) {
        try {

            if (checkJobStatus(jobStatus, jobName, logger)) {
                return;
            }

            long startTime = System.currentTimeMillis();
            logger.info(jobName + " － 任务开始");

            job.doJob();

            logger.info(jobName + " － 任务结束 耗时{}ms", System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            logger.error(jobName + " - " + e.getMessage(), e);
        } finally {
            jobStatus.set(false);
        }
    }

    /**
     * 检查job是否已在运行
     *
     * @param jobStatus
     * @param jobName
     * @param logger
     * @return true:运行中  false:未运行
     */
    private static boolean checkJobStatus(AtomicBoolean jobStatus, String jobName, Logger logger) {
        synchronized (jobStatus) {
            if (jobStatus.get()) {
                logger.info(jobName + " - 任务进行中");
                return true;
            }

            jobStatus.set(true);

            return false;
        }
    }
}
