package com.liuyang1.impl.utils.concurrent2;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 通用并发工具类
 *
 * @author OneCodeMonkey
 */
@Slf4j
public class ConcurrentUtils {
    // 最大任务并发数限制
    private static final int DEFAULT_THREAD_POOL_MAX_SIZE = 10;

    private static final long DEFAULT_TASK_TIMEOUT_MILLIS = 6000;

    private static ExecutorService newExecutor(int size) {
        ExecutorService es = new ThreadPoolExecutor(size, size, 1000,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(size), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        return ExecutorFactory.newExecutorService(es);
    }

    public static List<BaseRspDTO<Object>> getResultList(List<Callable<BaseRspDTO<Object>>> tasks) {
        return getResultList(tasks, newExecutor(Math.min(DEFAULT_THREAD_POOL_MAX_SIZE, tasks.size())),
                DEFAULT_TASK_TIMEOUT_MILLIS);
    }

    public static List<BaseRspDTO<Object>> getResultList(List<Callable<BaseRspDTO<Object>>> tasks, ExecutorService executor) {
        return getResultList(tasks, executor, DEFAULT_TASK_TIMEOUT_MILLIS);
    }

    public static List<BaseRspDTO<Object>> getResultList(List<Callable<BaseRspDTO<Object>>> tasks, Long timeoutMillis) {
        return getResultList(tasks, newExecutor(Math.min(DEFAULT_THREAD_POOL_MAX_SIZE, tasks.size())), timeoutMillis);
    }

    private static List<BaseRspDTO<Object>> getResultList(List<Callable<BaseRspDTO<Object>>> tasks, ExecutorService executor, Long timeoutMillis) {
        long beginTime = System.currentTimeMillis();

        timeoutMillis = Math.max(1, timeoutMillis);
        CompletionService<BaseRspDTO<Object>> baseDtoCompletionService = new ExecutorCompletionService<>(executor);
        for (Callable<BaseRspDTO<Object>> task : tasks) {
            baseDtoCompletionService.submit(task);
        }
        List<BaseRspDTO<Object>> resultList = new LinkedList<>();
        try {
            for (int i = 0; i < tasks.size(); i++) {
                Future<BaseRspDTO<Object>> baseRspDtoFuture = baseDtoCompletionService.poll(timeoutMillis, TimeUnit.MILLISECONDS);
                if (baseRspDtoFuture != null) {
                    BaseRspDTO<Object> baseRspDTO = baseRspDtoFuture.get();
                    resultList.add(baseRspDTO);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        log.info("并行调用耗时：{} ms", System.currentTimeMillis() - beginTime);

        return resultList;
    }
}
