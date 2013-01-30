package net.kkolyan.tasksequences;

/**
* @author nplekhanov
*/
public interface TaskChain {
    void add(Task task);
    void submit() throws Exception;
}
