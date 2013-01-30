package net.kkolyan.spring.altimpl.xml;

import net.kkolyan.spring.altimpl.Bean;
import net.kkolyan.spring.altimpl.misc.LazyList;
import net.kkolyan.spring.altimpl.LazyValue;
import net.kkolyan.spring.altimpl.xml.traverse.ValueConfigurator;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
public class ListElement implements ValueConfigurator {

    @ElementListUnion({
            @ElementList(entry = "value", type = ValueElement.class, inline = true),
            @ElementList(entry = "bean", type = BeanElement.class, inline = true),
            @ElementList(entry = "ref", type = RefElement.class, inline = true)
    })
    private List<ValueConfigurator> children = new ArrayList<ValueConfigurator>();

    public List<ValueConfigurator> getChildren() {
        return children;
    }

    public void setChildren(List<ValueConfigurator> children) {
        this.children = children;
    }

    @Override
    public LazyValue resolve(Bean consumer) throws Exception {
        List<LazyValue> list = new ArrayList<LazyValue>(children.size());
        for (ValueConfigurator child : children) {
            LazyValue value = child.resolve(consumer);
            list.add(value);
        }
        return new LazyList(list);
    }

    @Override
    public String toString() {
        return "ListElement{" +
                "" + children +
                '}';
    }

}
