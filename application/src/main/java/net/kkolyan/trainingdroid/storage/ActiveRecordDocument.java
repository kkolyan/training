package net.kkolyan.trainingdroid.storage;

import java.io.IOException;

/**
 * @author nplekhanov
 */
public interface ActiveRecordDocument<T> {

    void save() throws Exception;

    void commit() throws Exception;

    void rollback();

    T getObject();

    void beginEditing();

    Class<T> getMappedClass();
}
