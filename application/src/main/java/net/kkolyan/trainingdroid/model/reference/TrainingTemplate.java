package net.kkolyan.trainingdroid.model.reference;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
@Root
public class TrainingTemplate {
    @ElementList(entry = "set", inline = true, required = false)
    private List<SetTemplate> sets = new ArrayList<SetTemplate>();

    public List<SetTemplate> getSets() {
        return sets;
    }

    public void setSets(List<SetTemplate> sets) {
        this.sets = sets;
    }

    public TrainingTemplate deepCopy() {
        try {
            Persister persister = new Persister();
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            persister.write(this, buf);
            InputStream inputStream = new ByteArrayInputStream(buf.toByteArray());
            return persister.read(TrainingTemplate.class, inputStream);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
