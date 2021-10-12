package com.inspur.cloud.common.utils;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 多线程执行帮助类
 * @author mysterious guest
 */
public class ExecutionHelper<T> implements Iterable<T> {
    /**
     *  线程池
     */
    private final ExecutorService service;
    /**
     *  任务数目
     */
    private int numTasks;

    public ExecutionHelper(final ExecutorService executor) {
        this.service = executor;
    }

    private final Queue<Future<T>> futures = new LinkedList<>();

    public void submit(Callable<T> task) {
        futures.offer(service.submit(task));
        ++numTasks;
    }

    @Override
    @NonNull
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return numTasks > 0;
            }

            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException("next() is called but hasNext() returned false");
                }
                try {
                    return Objects.requireNonNull(futures.poll()).get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } finally {
                    --numTasks;
                }
            }
        };
    }
}
