package net.kkolyan.spring.altimpl.xml.traverse;

import net.kkolyan.spring.altimpl.Bean;

/**
* @author nplekhanov
*/
public interface BeanConfigurator {
    void configureBean(Bean bean) throws Exception;
}
