package net.kkolyan.spring.altimpl.xml;

import net.kkolyan.spring.altimpl.Application;
import net.kkolyan.spring.altimpl.Bean;
import net.kkolyan.spring.altimpl.BeanBuilder;
import net.kkolyan.spring.altimpl.xml.traverse.BeanConfigurator;
import net.kkolyan.spring.altimpl.xml.traverse.ContainerConfigurator;
import net.kkolyan.spring.altimpl.LazyValue;
import net.kkolyan.spring.altimpl.xml.traverse.ValueConfigurator;
import org.simpleframework.xml.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
@Root(name = "bean")
public class BeanElement implements ValueConfigurator, ContainerConfigurator {
    @Attribute(name = "class")
    private String beanClass;

    @Attribute(name = "id", required = false)
    private String beanId;

    @Attribute(name = "init-method", required = false)
    private String initMethod;

    @Attribute(name = "destroy-method", required = false)
    private String destroyMethod;

    @Attribute(required = false)
    private String scope = "singleton";

    @ElementListUnion({
            @ElementList(entry = "property", type = PropertyElement.class, inline = true, required = false),
            @ElementList(entry = "scoped-proxy", type = ScopedProxy.class, inline = true, required = false)
    })
    private List<BeanConfigurator> children = new ArrayList<BeanConfigurator>();

    //======================================================================================================

    @Override
    public void configureApplication(Application application) throws Exception {
        Bean bean = buildBean(application.createRootBean());
        for (BeanConfigurator child: children) {
            child.configureBean(bean);
        }
    }

    @Override
    public LazyValue resolve(Bean consumer) throws Exception {
        Bean bean = buildBean(consumer.createInnerBean());
        for (BeanConfigurator child: children) {
            child.configureBean(bean);
        }
        return bean;
    }

    private Bean buildBean(BeanBuilder b) throws Exception {
        b.setName(beanId);
        b.setClassName(beanClass);
        b.setDestroyMethod(destroyMethod);
        b.setInitMethod(initMethod);
        b.setScope(scope);
        return b.build();
    }

    //======================================================================================================


    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public String getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    public String getDestroyMethod() {
        return destroyMethod;
    }

    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<BeanConfigurator> getChildren() {
        return children;
    }

    public void setChildren(List<BeanConfigurator> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "BeanElement{" +
                "beanId='" + beanId + '\'' +
                ", beanClass='" + beanClass + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
