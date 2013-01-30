package net.kkolyan.trainingdroid.standalone;

import net.kkolyan.spring.altimpl.container.Container;

/**
 * @author nplekhanov
 */
public class StandaloneLauncher {
    public static void main(String[] args) throws Exception {
        Container container = new Container("weedyweb-mini.xml", "app.xml", "filter.properties");
    }
}
