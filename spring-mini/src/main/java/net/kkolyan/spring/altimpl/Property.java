package net.kkolyan.spring.altimpl;

import net.kkolyan.spring.altimpl.Transformer;

/**
 * @author nplekhanov
 */
public interface Property {

    void injectInto(Object instance, Transformer transformer) throws Exception;
}
