package net.kkolyan.spring.altimpl.xml;

import net.kkolyan.spring.altimpl.Bean;
import net.kkolyan.spring.altimpl.xml.traverse.BeanConfigurator;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * @author nplekhanov
 */
@Root
@Namespace(reference = "http://www.springframework.org/schema/aop")
public class ScopedProxy implements BeanConfigurator {
    @Attribute(name = "proxy-target-class")
    private boolean proxyTargetClass = true;

    @Override
    public void configureBean(Bean bean) throws Exception {
        if (proxyTargetClass) {
            throw new IllegalArgumentException("proxy-target-class=true is unsupported");
        }
        bean.enableScopedProxy();
    }

    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }
}
