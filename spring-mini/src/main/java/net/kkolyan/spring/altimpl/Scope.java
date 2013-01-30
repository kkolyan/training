package net.kkolyan.spring.altimpl;

/**
 * @author nplekhanov
 */
public interface Scope {
    String getName();
    Object get(String name, LifecyclePhase atLeast, BeanLifecycle lifecycle) throws Exception;
}
