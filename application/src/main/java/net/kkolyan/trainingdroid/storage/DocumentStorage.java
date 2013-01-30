package net.kkolyan.trainingdroid.storage;

/**
 * @author nplekhanov
 */
public interface DocumentStorage {
    <T> T view(Class<T> type);
    <T> T edit(Class<T> type);
    void commit();
}
