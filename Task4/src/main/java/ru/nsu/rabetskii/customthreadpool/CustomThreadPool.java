package ru.nsu.rabetskii.customthreadpool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomThreadPool implements ExecutorService {
    private final int maxThreads;
    private final BlockingQueue<Runnable> taskQueue;
    private final List<Thread> workers = new ArrayList<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition termination = lock.newCondition();

    public CustomThreadPool(int maxThreads, BlockingQueue<Runnable> taskQueue) {
        this.maxThreads = maxThreads;
        this.taskQueue = taskQueue;
    }

    @Override
    public void execute(Runnable command) {
        if (!isRunning.get()) {
            throw new RejectedExecutionException("ThreadPool is shutdown");
        }
        try {
            taskQueue.put(command);
            lock.lock();
            try {
                if (workers.size() < maxThreads && !taskQueue.isEmpty()) {
                    Thread workerThread = new Thread(new Worker());
                    workers.add(workerThread);
                    workerThread.start();
                }
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void shutdown() {
        lock.lock();
        try {
            isRunning.set(false);
            termination.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        lock.lock();
        try {
            isRunning.set(false);
            List<Runnable> unfinishedTasks = new ArrayList<>(taskQueue);
            taskQueue.clear();
            for (Thread worker : workers) {
                worker.interrupt();
            }
            termination.signalAll();
            return unfinishedTasks;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isShutdown() {
        return !isRunning.get();
    }

    @Override
    public boolean isTerminated() {
        return !isRunning.get() && workers.stream().noneMatch(Thread::isAlive);
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        /*
        Блокирует вызывающий поток до тех пор,
        пока все потоки пула не завершат выполнение после его остановки,
        или пока не истечет тайм-аут
        */
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            while (!isTerminated()) {
                if (nanos <= 0L)
                    return false;
                nanos = termination.awaitNanos(nanos);
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        /*
        Выполняет задачу, возвращая Future<T>,
        который можно использовать для получения результата
         */
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
        /*
        Выполняет коллекцию задач, блокируя до тех пор,
        пока все задачи не будут выполнены, и возвращает список результатов
        */
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
        /*
        Аналогичен invokeAll, но с таймаутом на выполнение всех задач
         */
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
        /*
        Выполняет коллекцию задач Callable<T> и возвращает результат одной успешно выполненной задачи
         */
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
        /*
        Аналогичен invokeAny, но с таймаутом, после которого выбрасывается TimeoutException,
        если ни одна задача не была успешно выполнена
         */
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

    private class Worker implements Runnable {
        public void run() {
            try {
                while (isRunning.get() || !taskQueue.isEmpty()) {
                    Runnable task;
                    try {
                        task = taskQueue.poll(500, TimeUnit.MILLISECONDS);
                        if (task != null) {
                            task.run();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            } finally {
                lock.lock();
                try {
                    workers.remove(Thread.currentThread());
                    if (workers.isEmpty()) {
                        termination.signalAll();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}


