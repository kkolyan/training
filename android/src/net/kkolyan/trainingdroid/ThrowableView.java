package net.kkolyan.trainingdroid;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author nplekhanov
 */
public class ThrowableView {
    public static String format(Throwable e) {
        StringWriter s = new StringWriter();
        s.append(e.toString());
        e.printStackTrace(new PrintWriter(s));
        return s.toString();
    }
}
