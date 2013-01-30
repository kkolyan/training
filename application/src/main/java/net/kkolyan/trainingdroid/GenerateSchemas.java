package net.kkolyan.trainingdroid;


import net.kkolyan.trainingdroid.model.TrainingSession;
import net.kkolyan.trainingdroid.model.reference.Library;
import net.kkolyan.trainingdroid.model.reporting.TrainingLog;
import net.kkolyan.trainingdroid.utils.SchemaGenerator;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @author nplekhanov
 */
public class GenerateSchemas {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        new SchemaGenerator("http://kkolyan.net/schema/training-droid")
                .addRoot(Library.class, "library")
                .addRoot(TrainingLog.class, "trainingLog")
                .addRoot(TrainingSession.class, "trainingSession")
                .generate(new PrintStream("src/main/resources/training-droid.xsd"));
    }
}
