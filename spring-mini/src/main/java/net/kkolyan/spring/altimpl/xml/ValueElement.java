package net.kkolyan.spring.altimpl.xml;

import net.kkolyan.spring.altimpl.Bean;
import net.kkolyan.spring.altimpl.LazyValue;
import net.kkolyan.spring.altimpl.xml.traverse.ValueConfigurator;
import org.simpleframework.xml.Text;

/**
 * @author nplekhanov
 */
public class ValueElement implements ValueConfigurator, LazyValue {

    @Text
    private String text;

    public ValueElement(String text) {
        this.text = text;
    }

    public ValueElement() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public LazyValue resolve(Bean consumer) throws Exception {
        return this;
    }

    @Override
    public Object evaluate() throws Exception {
        return text;
    }

    @Override
    public String toString() {
        return "ValueElement{" +
                "'" + text + '\'' +
                '}';
    }
}
