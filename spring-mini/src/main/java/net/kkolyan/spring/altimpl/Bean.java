package net.kkolyan.spring.altimpl;

/**
 * @author nplekhanov
 */
public interface Bean extends LazyValue {
    Application getApplication();
    Class getJavaType();
    BeanBuilder createInnerBean() throws Exception;
    void addProperty(Property property);
    void enableScopedProxy();
}
