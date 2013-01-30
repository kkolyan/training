package net.kkolyan.trainingdroid.storage;

import net.kkolyan.trainingdroid.MemoryOutputStream;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
* @author nplekhanov
*/
public class XmlDocument<T> implements ActiveRecordDocument<T> {
    private File file;
    private Class<T> mappedClass;

    private final Persister persister = new Persister();

    private T object;
    private boolean dirty;
    private MemoryOutputStream writeBuffer = new MemoryOutputStream();
    private MemoryOutputStream previousCommit = new MemoryOutputStream();

    @Override
    public void save() throws Exception {
        if (!dirty || object == null) {
            return;
        }
        System.out.println("[XmlDocument] write "+file);
        writeBuffer.reset();
        persister.write(object, writeBuffer);

        if (writeBuffer.equals(previousCommit)) {
            dirty = false;
        }
    }

    @Override
    public void commit() throws IOException {
        if (!dirty || object == null) {
            return;
        }
        if (!file.getParentFile().exists()) {
            FileUtil.mkdir(file.getParentFile());
            System.out.println("[XmlDocument] create "+file);
        }
        OutputStream stream = new PrintStream(file);
        writeBuffer.writeTo(stream);
        stream.flush();
        stream.close();

        previousCommit.reset();
        writeBuffer.writeTo(previousCommit);

        dirty = false;
    }

    @Override
    public void rollback() {
        if (object == null || !dirty) {
            return;
        }
        object = null;
        writeBuffer.reset();
        dirty = false;
    }

    @Override
    public T getObject() {
        if (object == null) {
            try {
                load();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return object;
    }

    @Override
    public void beginEditing() {
        dirty = true;
    }

    private void load() throws Exception {
        if (file.exists()) {
            System.out.println("[XmlDocument] read "+file);
            object = persister.read(mappedClass, file);
        }
        else if (dirty) {
            object = mappedClass.newInstance();
        }
    }

    @Override
    public Class<T> getMappedClass() {
        return mappedClass;
    }

    @Override
    public String toString() {
        return "XmlDocument{" +
                "file=" + file +
                ", mappedClass=" + mappedClass +
                '}';
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setMappedClass(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
    }
}
