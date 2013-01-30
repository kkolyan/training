package net.kkolyan.tasksequences;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
public class SyncTaskChainFactory implements TaskChainFactory {
    @Override
    public TaskChain createTaskChain() {
        return new SyncTaskChain();
    }

    private static class SyncTaskChain implements TaskChain {
        private List<Task> tasks = new ArrayList<Task>();

        @Override
        public void add(Task task) {
            if (task == null) {
                throw new NullPointerException("task");
            }
            tasks.add(task);
        }

        @Override
        public void submit() throws Exception {
            // every task can add extra task to this chain
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < tasks.size(); i ++) {
                tasks.get(i).execute();
            }
        }
    }
}
