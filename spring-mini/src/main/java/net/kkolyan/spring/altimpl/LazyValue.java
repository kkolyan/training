package net.kkolyan.spring.altimpl;

/**
 * @author nplekhanov
 */
public interface LazyValue {
    Object evaluate() throws Exception;
}
