package net.kkolyan.spring.altimpl;

/**
* @author nplekhanov
*/
public abstract class BeanBuilder {
    private String name;
    private String scope;
    private String className;
    private String initMethod;
    private String destroyMethod;

    public void setName(String name) {
        this.name = name;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }

    public abstract Bean build() throws Exception;

    public String getName() {
        return name;
    }

    public String getScope() {
        return scope;
    }

    public String getClassName() {
        return className;
    }

    public String getInitMethod() {
        return initMethod;
    }

    public String getDestroyMethod() {
        return destroyMethod;
    }
}
