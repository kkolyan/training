package net.kkolyan.trainingdroid.model.reporting;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * @author nplekhanov
 */
@Root
public class ExecutionSet {

    @Attribute
    private Date begin;

    @Attribute
    private Date end;

    @Attribute
    private int reps;

    @Attribute
    private int cheatingReps;

    @Attribute
    private String exercise;

    @Attribute
    private float amount;

    @Attribute(required = false)
    private String feedback;

    //================================

    public int getCheatingReps() {
        return cheatingReps;
    }

    public void setCheatingReps(int cheatingReps) {
        this.cheatingReps = cheatingReps;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
