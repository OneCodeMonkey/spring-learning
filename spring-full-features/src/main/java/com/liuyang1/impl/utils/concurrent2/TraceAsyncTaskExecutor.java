package com.liuyang1.impl.utils.concurrent2;


import com.liuyang1.impl.utils.TraceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author OneCodeMonkey
 */
class TraceAsyncTaskExecutor implements AsyncTaskExecutor {
    private static final Logger log = LoggerFactory.getLogger(TraceAsyncTaskExecutor.class);
    private AsyncTaskExecutor executor;

    public TraceAsyncTaskExecutor() {
        int coreThreadNum = 40;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreThreadNum);
        executor.setMaxPoolSize(coreThreadNum * 4);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(600);
        executor.setThreadNamePrefix("async");
        executor.setRejectedExecutionHandler((r, executor1) -> {
            log.warn("log thread pool has been full, call main thread");
            if (!executor1.isShutdown()) {
                r.run();
            }

        });
        this.executor = executor;
    }

    public TraceAsyncTaskExecutor(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    public void execute(Runnable task) {
        this.executor.execute(TraceUtils.wapper(task));
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void execute(Runnable task, long startTimeout) {
        this.executor.execute(TraceUtils.wapper(task), startTimeout);
    }

    public Future<?> submit(Runnable task) {
        return this.executor.submit(TraceUtils.wapper(task));
    }

    public <V> Future<V> submit(Callable<V> task) {
        return this.executor.submit(TraceUtils.wapper(task));
    }
}
