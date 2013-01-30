package net.kkolyan.trainingdroid.model.reporting;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Root
public class Training {

    @Attribute
    private int courseStep;

    @Attribute(required = false)
    private String condition;

    @Attribute
    private Date begin;

    @ElementList(entry = "set", inline = true, required = false)
    private List<ExecutionSet> sets = new ArrayList<ExecutionSet>();

    @Attribute(required = false)
    private Date end;

    //============================

    public List<ExecutionSet> getSets() {
        return sets;
    }

    public void setSets(List<ExecutionSet> sets) {
        this.sets = sets;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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

    public int getCourseStep() {
        return courseStep;
    }

    public void setCourseStep(int courseStep) {
        this.courseStep = courseStep;
    }
}
