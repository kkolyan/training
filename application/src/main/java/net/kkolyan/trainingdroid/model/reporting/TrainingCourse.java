package net.kkolyan.trainingdroid.model.reporting;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
@Root
public class TrainingCourse {

    @Attribute
    private String course;

    @ElementList(entry = "training", inline = true, required = false)
    private List<Training> trainings = new ArrayList<Training>();

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }
}
