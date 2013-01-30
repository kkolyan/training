package net.kkolyan.trainingdroid.model.reporting;

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
public class TrainingLog {

    @ElementList(entry = "course", inline = true, required = false)
    private List<TrainingCourse> courses = new ArrayList<TrainingCourse>();

    //=====================================


    public List<TrainingCourse> getCourses() {
        return courses;
    }

    public void setCourses(List<TrainingCourse> courses) {
        this.courses = courses;
    }
}
