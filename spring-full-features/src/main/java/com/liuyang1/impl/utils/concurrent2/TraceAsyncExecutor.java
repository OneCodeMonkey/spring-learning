package com.liuyang1.impl.utils.concurrent2;

import com.liuyang1.impl.utils.TraceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * @author OneCodeMonkey
 */
class TraceAsyncExecutor implements ExecutorService {
    private static final Logger log = LoggerFactory.getLogger(TraceAsyncExecutor.class);
    private ExecutorService executor;

    public TraceAsyncExecutor() {
        int coreThreadNum = 40;
        this.executor = new ThreadPoolExecutor(coreThreadNum, coreThreadNum * 4, 10L, TimeUnit.MINUTES,
                new LinkedBlockingQueue(1000), (r, executor1) -> {
            log.warn("log thread pool has been full, call main thread");
            if (!executor1.isShutdown()) {
                r.run();
            }
        });
    }

    public TraceAsyncExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public void execute(Runnable task) {
        this.executor.execute(TraceUtils.wapper(task));
    }

    public Future<?> submit(Runnable task) {
        return this.executor.submit(TraceUtils.wapper(task));
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.executor.invokeAll((Collection) tasks.stream().map(TraceUtils::wapper).collect(Collectors.toUnmodifiableList()));
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return this.executor.invokeAll((Collection) tasks.stream().map(TraceUtils::wapper).collect(Collectors.toUnmodifiableList()), timeout, unit);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return (T) this.executor.invokeAny((Collection) tasks.stream().map(TraceUtils::wapper).collect(Collectors.toUnmodifiableList()));
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return (T) this.executor.invokeAny((Collection) tasks.stream().map(TraceUtils::wapper).collect(Collectors.toUnmodifiableList()), timeout, unit);
    }

    public void shutdown() {
        this.executor.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return this.executor.shutdownNow();
    }

    public boolean isShutdown() {
        return this.executor.isShutdown();
    }

    public boolean isTerminated() {
        return this.executor.isTerminated();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.executor.awaitTermination(timeout, unit);
    }

    public <V> Future<V> submit(Callable<V> task) {
        return this.executor.submit(TraceUtils.wapper(task));
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return this.executor.submit(TraceUtils.wapper(task), result);
    }
}
