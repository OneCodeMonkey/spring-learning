package com.liuyang1.impl.utils.concurrent2;

import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.ExecutorService;

public class ExecutorFactory {
    public static ExecutorService DEFAULT_EXECUTOR_SERVICE = new TraceAsyncExecutor();
    public static AsyncTaskExecutor DEFAULT_ASYNC_TASK_EXECUTOR = new TraceAsyncTaskExecutor();

    public ExecutorFactory() {
    }

    public static ExecutorService newExecutorService() {
        return new TraceAsyncExecutor();
    }

    public static AsyncTaskExecutor newAsyncTaskExecutor() {
        return new TraceAsyncTaskExecutor();
    }

    public static ExecutorService getExecutorService() {
        return DEFAULT_EXECUTOR_SERVICE;
    }

    public static AsyncTaskExecutor getAsyncTaskExecutor() {
        return DEFAULT_ASYNC_TASK_EXECUTOR;
    }

    public static ExecutorService newExecutorService(ExecutorService executor) {
        return new TraceAsyncExecutor(executor);
    }

    public static AsyncTaskExecutor newAsyncTaskExecutor(AsyncTaskExecutor executor) {
        return new TraceAsyncTaskExecutor(executor);
    }
}