package net.kkolyan.spring.altimpl.xml.traverse;

import net.kkolyan.spring.altimpl.LazyValue;
import net.kkolyan.spring.altimpl.Bean;

/**
 * @author nplekhanov
 */
public interface ValueConfigurator {
    LazyValue resolve(Bean consumer) throws Exception;
}
