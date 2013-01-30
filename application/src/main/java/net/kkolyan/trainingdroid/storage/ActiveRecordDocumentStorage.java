package net.kkolyan.trainingdroid.storage;


import java.util.ArrayList;
import java.util.List;

/**
 * @author NPlekhanov
 * //todo add entity file mapping
 */
public class ActiveRecordDocumentStorage implements DocumentStorage {

    private List<ActiveRecordDocument> documents = new ArrayList<ActiveRecordDocument>();

    @Override
    public <T> T view(Class<T> type) {
        ActiveRecordDocument<T> document = getDocument(type);
        return document.getObject();
    }

    @Override
    public synchronized <T> T edit(Class<T> type) {
        ActiveRecordDocument<T> document = getDocument(type);
        document.beginEditing();
        return document.getObject();
    }

    @Override
    public synchronized void commit() {
        try {
            for (ActiveRecordDocument document : documents) {
                document.save();
            }
            for (ActiveRecordDocument document : documents) {
                document.commit();
            }
        } catch (Exception e) {
            for (ActiveRecordDocument document : documents) {
                document.rollback();
            }
            throw new IllegalStateException(e);
        }
    }

    public void setDocuments(List<ActiveRecordDocument> documents) {
        this.documents = documents;
    }

    @SuppressWarnings("unchecked")
    private <T> ActiveRecordDocument<T> getDocument(Class<T> aClass) {
        for (ActiveRecordDocument document : documents) {
            if (document.getMappedClass() == aClass) {
                return document;
            }
        }
        throw new IllegalStateException("unmapped class: "+aClass);
    }

}
