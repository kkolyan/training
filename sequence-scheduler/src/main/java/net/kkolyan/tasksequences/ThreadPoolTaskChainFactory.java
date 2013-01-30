package net.kkolyan.tasksequences;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author nplekhanov
 */
public class ThreadPoolTaskChainFactory implements TaskChainFactory {
    private Executor executor;

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public TaskChain createTaskChain() {
        return new ThreadPoolTaskChain();
    }

    private class ThreadPoolTaskChain implements TaskChain, Runnable {
        private Queue<Task> tasks = new ArrayDeque<Task>();
        private AtomicBoolean running = new AtomicBoolean();

        @Override
        public void add(Task task) {
            if (task == null) {
                throw new NullPointerException("task");
            }
            tasks.offer(task);
        }

        @Override
        public void submit() throws Exception {
            scheduleNext();
        }

        @Override
        public void run() {
            if (!running.compareAndSet(false, true)) {
                throw new IllegalStateException("non-sequential sequence execution!");
            }
            Task task = tasks.poll();
            if (task == null) {
                throw new IllegalStateException();
            }
            try {
                task.execute();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            running.set(false);
            scheduleNext();
        }

        private void scheduleNext() {
            executor.execute(this);
        }
    }
}
