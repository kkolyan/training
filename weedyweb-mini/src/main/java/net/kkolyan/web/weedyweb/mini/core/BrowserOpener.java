package net.kkolyan.web.weedyweb.mini.core;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.net.URL;

/**
 * @author nplekhanov
 */
public class BrowserOpener {
    private URL url;
    private boolean active = true;

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BrowserOpener{" + url + "}";
    }

    @PostConstruct
    public void openBrowser() throws Exception {
        if (active) {
            Desktop.getDesktop().browse(url.toURI());
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
