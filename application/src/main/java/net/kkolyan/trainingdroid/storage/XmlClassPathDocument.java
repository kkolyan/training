package net.kkolyan.trainingdroid.storage;

import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author NPlekhanov
 */
public class XmlClassPathDocument<T> implements ActiveRecordDocument<T> {

    private Class<T> mappedClass;
    private String resource;

    private T object;
    private Persister persister = new Persister();

    @Override
    public T getObject() {
        if (object == null) {
            try {
                URL url = getClass().getClassLoader().getResource(resource);
                if (url == null) {
                    throw new IllegalStateException("classpath resource not found: '"+resource+"'");
                }
                InputStream stream = url.openStream();
                System.out.println("[XmlClassPathDocument] read "+ url);
                try {
                    object = persister.read(mappedClass, stream);
                } finally {
                    stream.close();
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return object;
    }

    @Override
    public void beginEditing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<T> getMappedClass() {
        return mappedClass;
    }

    @Override
    public void save() throws Exception {
    }

    @Override
    public void commit() throws IOException {
    }

    @Override
    public void rollback() {
    }

    @Override
    public String toString() {
        return "XmlClassPathDocument{" +
                "resource=" + resource +
                ", mappedClass=" + mappedClass +
                '}';
    }

    public void setMappedClass(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
