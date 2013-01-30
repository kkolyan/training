package net.kkolyan.spring.altimpl;

import java.lang.reflect.Type;

/**
 * @author nplekhanov
 */
public interface Application {
    Scope getScope(String scope);
    LazyValue lookupByName(String name);
    LazyValue lookupByType(Type genericType);
    Transformer getTransformer();
    BeanBuilder createRootBean() throws Exception;
    void setAnnotationConfigEnabled(boolean annotationConfigEnabled);
}
