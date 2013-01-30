package net.kkolyan.trainingdroid.model.reference;

import net.kkolyan.trainingdroid.Constants;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
@Root
@Namespace(reference = Constants.XML_NAMESPACE)
public class Library {

    @ElementList(entry = "exercise", inline = true)
    private List<Exercise> exercises = new ArrayList<Exercise>();
    
    @ElementList(entry = "course", inline = true)
    private List<CourseTemplate> courses = new ArrayList<CourseTemplate>();

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<CourseTemplate> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseTemplate> courses) {
        this.courses = courses;
    }
}
