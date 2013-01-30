package net.kkolyan.spring.altimpl.xml;

import net.kkolyan.spring.altimpl.Application;
import net.kkolyan.spring.altimpl.xml.traverse.ContainerConfigurator;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * @author nplekhanov
 */
@Root
@Namespace(reference = "http://www.springframework.org/schema/context")
public class AnnotationConfigElement implements ContainerConfigurator {
    @Override
    public void configureApplication(Application application) throws Exception {
        application.setAnnotationConfigEnabled(true);
    }
}
