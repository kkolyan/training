package net.kkolyan.trainingdroid.model;

import net.kkolyan.trainingdroid.Constants;
import net.kkolyan.trainingdroid.model.reference.TrainingTemplate;
import net.kkolyan.trainingdroid.model.reporting.Training;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * @author nplekhanov
 */
@Root
@Namespace(reference = Constants.XML_NAMESPACE)
public class TrainingSession {

    @Attribute
    private boolean started;

    @Element(required = false)
    private Training training;

    @Element(required = false)
    private TrainingTemplate target;

    @Attribute(required = false)
    private int selectedIndex;

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public TrainingTemplate getTarget() {
        return target;
    }

    public void setTarget(TrainingTemplate target) {
        this.target = target;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}