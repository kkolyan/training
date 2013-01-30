package net.kkolyan.trainingdroid;

import android.util.Log;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
@Root
public class Preferences {
    private static final File FILE = new File("/sdcard/TrainingDroid/preferences.xml");

    @ElementList(entry = "server", inline = true, required = false)
    private List<String> servers = new ArrayList<String>();

    @Element
    private String updateUrl;

    @Element(required = false)
    private String location;

    public void load() {
        try {
            new Persister().read(this, FILE);
            if (location == null) {
                throw new IllegalStateException("invalid preferences");
            }
        } catch (Exception e) {
            Log.w(getClass().getSimpleName(), "Invalid properties, resetting to defaults"+e.toString(), e);
            getServers().add("http://localhost:8080/");
            getServers().add("http://192.168.0.102:8080/");
            setUpdateUrl("http://dl.dropbox.com/u/11738562/TrainingDroid.apk");
            setLocation("http://localhost:8080/trainingSession");
            save();
        }
    }

    public void save() {
        try {
            new Persister().write(this, FILE);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.toString(), e);
        }
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
