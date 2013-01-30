package net.kkolyan.spring.altimpl.xml;

import net.kkolyan.spring.altimpl.Application;
import net.kkolyan.spring.altimpl.xml.traverse.ContainerConfigurator;
import org.simpleframework.xml.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
@Root
@Namespace(reference = "http://www.springframework.org/schema/beans")
public class BeansElement implements ContainerConfigurator {

    @Attribute(required = false)
    private String schemaLocation;

    @ElementListUnion({
            @ElementList(entry = "bean", type = BeanElement.class, inline = true, required = false),
            @ElementList(entry = "annotation-config", type = AnnotationConfigElement.class, inline = true, required = false)
    })
    private List<ContainerConfigurator> children = new ArrayList<ContainerConfigurator>();

    public List<ContainerConfigurator> getChildren() {
        return children;
    }

    public void setChildren(List<ContainerConfigurator> children) {
        this.children = children;
    }

    @Override
    public void configureApplication(Application application) throws Exception {
        for (ContainerConfigurator child: children) {
            child.configureApplication(application);
        }
    }

}
