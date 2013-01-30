package net.kkolyan.spring.altimpl.xml;

import net.kkolyan.spring.altimpl.Bean;
import net.kkolyan.spring.altimpl.LazyValue;
import net.kkolyan.spring.altimpl.xml.traverse.ValueConfigurator;
import org.simpleframework.xml.Attribute;

/**
 * @author nplekhanov
 */
public class RefElement implements ValueConfigurator {

    @Attribute
    private String bean;

    @Override
    public LazyValue resolve(Bean consumer) throws Exception {
        return consumer.getApplication().lookupByName(bean);
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "RefElement{" +
                "bean='" + bean + '\'' +
                '}';
    }
}
