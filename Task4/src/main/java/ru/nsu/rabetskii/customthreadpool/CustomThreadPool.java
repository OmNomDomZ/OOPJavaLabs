package ru.nsu.rabetskii.customthreadpool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class CustomThreadPool implements ExecutorService {
    private final int maxThreads;
    private final BlockingQueue<Runnable> taskQueue;
    private final List<Thread> threads = new ArrayList<>();
    private final List<Future<?>> futures = new ArrayList<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private final ReentrantLock lock = new ReentrantLock();

    public CustomThreadPool(int maxThreads, BlockingQueue<Runnable> taskQueue) {
        this.maxThreads = maxThreads;
        this.taskQueue = taskQueue;
    }

    @Override
    public void execute(Runnable command) {
        if (!isRunning.get())
        {
            throw new IllegalStateException("ThreadPool is shutting down");
        }
        try {
            taskQueue.put(command);
            manageThreads();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void manageThreads() {
        lock.lock();
        try {
            while (threads.size() < maxThreads && !taskQueue.isEmpty()) {
                Runnable task = taskQueue.poll();
                if (task != null) {
                    Thread thread = new Thread(task);
                    threads.add(thread);
                    thread.start();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void shutdown() {
        isRunning.set(false);
    }

    @Override
    public List<Runnable> shutdownNow() {
        isRunning.set(false);
        List<Runnable> unfinishedTasks = new ArrayList<>();
        taskQueue.drainTo(unfinishedTasks);
        threads.forEach(Thread::interrupt);
        return unfinishedTasks;
    }

    @Override
    public boolean isShutdown() {
        return !isRunning.get();
    }

    @Override
    public boolean isTerminated() {
        return !isRunning.get() && threads.stream().noneMatch(Thread::isAlive);
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long startTime = System.nanoTime();
        while (System.nanoTime() - startTime < unit.toNanos(timeout)) {
            if (isTerminated()) {
                return true;
            }
            Thread.sleep(100);
        }
        return isTerminated();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        FutureTask<T> future = new FutureTask<>(task);
        execute(future);
        return future;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        FutureTask<T> future = new FutureTask<>(task, result);
        execute(future);
        return future;
    }

    @Override
    public Future<?> submit(Runnable task) {
        FutureTask<?> future = new FutureTask<>(task, null);
        execute(future);
        return future;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        List<Future<T>> futures = new ArrayList<>();
        for (Callable<T> task : tasks) {
            FutureTask<T> future = new FutureTask<>(task);
            futures.add(future);
            execute(future);
        }
        for (Future<T> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return futures;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        List<Future<T>> futures = new ArrayList<>();
        for (Callable<T> task : tasks) {
            futures.add(submit(task));
        }
        for (Future<T> future : futures) {
            try {
                long timeLeft = deadline - System.nanoTime();
                if (timeLeft > 0) {
                    future.get(timeLeft, TimeUnit.NANOSECONDS);
                } else {
                    future.cancel(true);
                }
            } catch (ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }
        return futures;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        ExecutionException lastException = null;
        for (Callable<T> task : tasks) {
            try {
                return submit(task).get();
            } catch (ExecutionException e) {
                lastException = e;
            }
        }
        throw lastException != null ? lastException : new ExecutionException("No successful task.", null);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        ExecutionException lastException = null;
        for (Callable<T> task : tasks) {
            try {
                long timeLeft = deadline - System.nanoTime();
                if (timeLeft > 0) {
                    return submit(task).get(timeLeft, TimeUnit.NANOSECONDS);
                } else {
                    throw new TimeoutException("Timeout");
                }
            } catch (ExecutionException e) {
                lastException = e;
            }
        }
        throw lastException != null ? lastException : new ExecutionException("No successful task.", null);
    }
}
