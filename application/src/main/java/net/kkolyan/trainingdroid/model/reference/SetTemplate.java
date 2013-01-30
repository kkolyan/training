package net.kkolyan.trainingdroid.model.reference;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * @author nplekhanov
 */
@Root
public class SetTemplate {

    @Attribute
    private String exercise;

    @Attribute
    private int reps;

    @Attribute(required = false)
    private float amount;

    //==============================

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
