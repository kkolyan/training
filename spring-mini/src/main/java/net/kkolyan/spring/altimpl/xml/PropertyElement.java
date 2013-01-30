package net.kkolyan.spring.altimpl.xml;

import net.kkolyan.spring.altimpl.Bean;
import net.kkolyan.spring.altimpl.misc.MethodBasedProperty;
import net.kkolyan.spring.altimpl.xml.traverse.BeanConfigurator;
import net.kkolyan.spring.altimpl.LazyValue;
import net.kkolyan.spring.altimpl.xml.traverse.ValueConfigurator;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;

/**
 * @author nplekhanov
 */
public class PropertyElement implements BeanConfigurator {
    @Attribute
    private String name;

    @Attribute(required = false)
    private String value;

    @Attribute(required = false)
    private String ref;

    @ElementUnion({
            @Element(name = "bean", type = BeanElement.class, required = false),
            @Element(name = "list", type = ListElement.class, required = false),
            @Element(name = "value", type = ValueElement.class, required = false)
    })
    private ValueConfigurator content;

    //=============================================================

    private LazyValue resolveValue(Bean bean) throws Exception {
        if (content != null) {
            return content.resolve(bean);
        }
        if (value != null) {
            return new ValueElement(value);
        }
        if (ref != null) {
            return bean.getApplication().lookupByName(ref);
        }
        throw new IllegalStateException();
    }

    @Override
    public void configureBean(Bean bean) throws Exception {
        LazyValue value = resolveValue(bean);
        bean.addProperty(new MethodBasedProperty(bean.getJavaType(), name, value));
    }

    //=============================================================


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public ValueConfigurator getContent() {
        return content;
    }

    public void setContent(ValueConfigurator content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PropertyElement{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", ref='" + ref + '\'' +
                ", content=" + content +
                '}';
    }
}
