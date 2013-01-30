package net.kkolyan.trainingdroid;

import net.kkolyan.trainingdroid.model.TrainingSession;
import net.kkolyan.trainingdroid.model.reference.*;
import net.kkolyan.trainingdroid.model.reporting.ExecutionSet;
import net.kkolyan.trainingdroid.model.reporting.Training;
import net.kkolyan.trainingdroid.model.reporting.TrainingCourse;
import net.kkolyan.trainingdroid.model.reporting.TrainingLog;
import net.kkolyan.trainingdroid.storage.DocumentStorage;
import net.kkolyan.web.weedyweb.api.Action;
import net.kkolyan.web.weedyweb.api.ModelAndView;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author nplekhanov
 */
public class TrainingController {

    @Resource
    private DocumentStorage storage;

    public void setStorage(DocumentStorage storage) {
        this.storage = storage;
    }

    @Action(path = "/trainingSession", view = "/trainingSession.vm", params = "selected")
    public ModelAndView showTrainingPage(int selectedIndex) {
        TrainingSession session = storage.view(TrainingSession.class);
        if (session == null) {
            session = storage.edit(TrainingSession.class);
            storage.commit();
        }
        if (session.isStarted() && session.getTarget().getSets().isEmpty()) {
            return new ModelAndView("redirect:/commitTraining");
        }
        if (session.getSelectedIndex() != selectedIndex) {
            session = storage.edit(TrainingSession.class);
            session.setSelectedIndex(selectedIndex);
            storage.commit();
        }
        return new ModelAndView("/trainingSession.vm", this);
    }

    @Action(path = "/", view = "/index.vm")
    public void showIndexPage() {
    }

    @Action(path = "/trainingLog", view = "/trainingLog.vm")
    public void showTrainingLogPage() {
    }

    @Action(path = "/trainingExercises", view = "/trainingExercises.vm")
    public void showExercisesPage() {
    }

    @Action(path = "/trainingCourses", view = "/trainingCourses.vm")
    public void showTrainingCoursesPage() {
    }

    @Action(path = "/submitSet", params = {"exercise","begin","reps","amount","feedback","cheating"})
    public void submitSet(String exerciseName, long begin, int reps, float amount, String feedback, int cheatingReps) {

        ExecutionSet set = new ExecutionSet();
        set.setAmount(amount);
        set.setBegin(new Date(begin));
        set.setCheatingReps(cheatingReps);
        set.setEnd(new Date());
        set.setExercise(exerciseName);
        set.setFeedback(feedback);
        set.setReps(reps);

        TrainingSession session = storage.edit(TrainingSession.class);
        if (!session.isStarted()) {
            throw new IllegalStateException("training is not started yet");
        }
        Iterator<SetTemplate> it = session.getTarget().getSets().iterator();
        while (it.hasNext()) {
            SetTemplate template = it.next();
            if (template.getExercise().equals(exerciseName)) {
                it.remove();
                break;
            }
        }
        session.getTraining().getSets().add(set);
        session.setSelectedIndex(0);
        storage.commit();
    }

    @Action(path = "/beginTraining", params = {"condition"})
    public void beginTraining(String condition) {
        Training training = new Training();
        training.setBegin(new Date());
        training.setCondition(condition);

        TrainingLog log = storage.view(TrainingLog.class);
        TrainingCourse course = last(log.getCourses());
        if (course == null) {
            throw new IllegalStateException("you must begin course before training");
        }
        CourseTemplate courseTemplate = findCourse(course.getCourse());
        Training lastTraining = last(course.getTrainings());
        if (lastTraining != null) {
            int courseStep = lastTraining.getCourseStep() + 1;
            courseStep = courseStep % courseTemplate.getTrainings().size();
            training.setCourseStep(courseStep);
        }

        TrainingSession session = storage.edit(TrainingSession.class);
        if (session.isStarted()) {
            throw new IllegalStateException("current training is already started");
        }
        TrainingTemplate target = courseTemplate.getTrainings().get(training.getCourseStep());
        target = target.deepCopy();
        for (SetTemplate set: target.getSets()) {
            float lastAmount = getLastAmount(set.getExercise());
            set.setAmount(lastAmount);
        }
        session.setTarget(target);
        session.setStarted(true);
        session.setTraining(training);
        storage.commit();
    }
    
    private float getLastAmount(String exercise) {
        TrainingLog log = storage.view(TrainingLog.class);
        for (TrainingCourse course: inverted(log.getCourses())) {
            for (Training training: inverted(course.getTrainings())) {
                for (ExecutionSet set: inverted(training.getSets())) {
                    if (set.getExercise().equals(exercise)) {
                        return set.getAmount();
                    }
                }
            }
        }
        return 0;
    }

    @Action(path = "/beginNewCourse", params = "course")
    public void beginNewCourse(String courseName) {
        TrainingLog log = storage.edit(TrainingLog.class);
        TrainingCourse course = new TrainingCourse();
        course.setCourse(courseName);
        log.getCourses().add(course);
        storage.commit();
    }
    
    private static <T> T last(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    @Action(path = "/commitTraining", params = {}, view = "redirect:/trainingLog")
    public void commitTraining() {
        TrainingLog log = storage.edit(TrainingLog.class);
        TrainingSession session = storage.edit(TrainingSession.class);

        session.getTraining().setEnd(new Date());

        TrainingCourse course = last(log.getCourses());
        if (course == null) {
            throw new IllegalStateException("you must begin course before training");
        }

        course.getTrainings().add(session.getTraining());

        session.setStarted(false);
        session.setTarget(null);
        session.setTraining(null);

        storage.commit();
    }

    public TrainingSession getSession() {
        return storage.view(TrainingSession.class);
    }

    public Library getLibrary() {
        return storage.view(Library.class);
    }

    public Exercise findExercise(String name) {
        List<Exercise> exercises = getLibrary().getExercises();
        for (Exercise exercise: exercises) {
            if (name.equals(exercise.getName())) {
                return exercise;
            }
        }
        return null;
    }

    public CourseTemplate findCourse(String name) {
        List<CourseTemplate> courses = getLibrary().getCourses();
        for (CourseTemplate course: courses) {
            if (name.equals(course.getName())) {
                return course;
            }
        }
        return null;
    }

    public TrainingLog getLog() {
        TrainingLog log = storage.view(TrainingLog.class);
        if (log == null) {
            log = storage.edit(TrainingLog.class);
            storage.commit();
        }
        return log;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
    
    public String formatDate(String pattern, Object date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    private <T> List<T> inverted(final List<T> list) {
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                return list.get(size() - index - 1);
            }

            @Override
            public int size() {
                return list.size();
            }
        };
    }
}
