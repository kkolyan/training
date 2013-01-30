package net.kkolyan.spring.altimpl.xml.traverse;

import net.kkolyan.spring.altimpl.Application;

/**
* @author nplekhanov
*/
public interface ContainerConfigurator {
    void configureApplication(Application application) throws Exception;
}
