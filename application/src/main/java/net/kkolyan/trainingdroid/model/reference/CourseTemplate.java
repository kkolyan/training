package net.kkolyan.trainingdroid.model.reference;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
@Root
public class CourseTemplate {

    @Attribute
    private String name;
    
    @Element(required = false)
    private String title;

    @ElementList(entry = "training", inline = true, required = false)
    private List<TrainingTemplate> trainings = new ArrayList<TrainingTemplate>();

    //==========================================

    public List<TrainingTemplate> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<TrainingTemplate> trainings) {
        this.trainings = trainings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
