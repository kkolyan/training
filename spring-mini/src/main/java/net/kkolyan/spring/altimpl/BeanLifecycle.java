package net.kkolyan.spring.altimpl;

/**
 * @author nplekhanov
 */
public interface BeanLifecycle {

    Object instantiate() throws Exception;
    void configure(Object o) throws Exception;
    void initialize(Object o) throws Exception;
    void destroy(Object o) throws Exception;

}
