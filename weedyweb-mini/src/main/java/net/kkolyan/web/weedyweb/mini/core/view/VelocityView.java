package net.kkolyan.web.weedyweb.mini.core.view;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

/**
 * @author nplekhanov
 */
public class VelocityView implements View {
    private String properties;

    @PostConstruct
    public void init() throws IOException {
        Properties props = new Properties();
        props.load(new StringReader(properties));
        Velocity.init(props);
    }

    @Override
    public boolean renderView(Object model, String viewName, RenderingContext renderingContext) throws Exception {
        if (!viewName.endsWith(".vm")) {
            return false;
        }

        Template template = Velocity.getTemplate(viewName, "utf8");
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("this", model);
        StringWriter s = new StringWriter();
        template.merge(velocityContext, s);
        renderingContext.getContent().write(s.toString().getBytes("utf8"));
        renderingContext.setResponseHeader("Content-Type", "text/html");
        return true;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
