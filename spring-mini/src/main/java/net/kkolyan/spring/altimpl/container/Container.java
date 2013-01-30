package net.kkolyan.spring.altimpl.container;

import net.kkolyan.spring.altimpl.*;
import net.kkolyan.spring.altimpl.misc.*;
import net.kkolyan.spring.altimpl.xml.BeansElement;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.TreeStrategy;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author nplekhanov
 */
public class Container implements Application {
    private List<GenericBean> beans = new ArrayList<GenericBean>();
    private List<Scope> scopes = new ArrayList<Scope>();

    private boolean annotationConfigEnabled;
    private Transformer transformer = new BasicTransformer();

    public Container(String... resources) throws Exception {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        List<String> xmlFiles = new ArrayList<String>();
        List<String> propFiles = new ArrayList<String>();

        for (String resource: resources) {
            if (resource.endsWith(".xml")) {
                xmlFiles.add(resource);
            } else {
                propFiles.add(resource);
            }
        }

        Properties filter = new Properties();
        for (String resource: propFiles) {
            InputStream stream = classLoader.getResourceAsStream(resource);
            if (stream == null) {
                throw new FileNotFoundException("classpath:"+resource);
            }
            filter.load(stream);
        }

        Persister persister = new Persister(new TreeStrategy("@class", "@length"), filter);

        BeansElement all = new BeansElement();
        for (String resource: xmlFiles) {
            InputStream stream = classLoader.getResourceAsStream(resource);
            if (stream == null) {
                throw new FileNotFoundException("classpath:"+resource);
            }
            BeansElement element = persister.read(BeansElement.class, stream);
            all.getChildren().addAll(element.getChildren());
        }
        initialize(all);
    }

    private void initialize(BeansElement definitions) throws Exception {
        Scope singletons = new MapBasedScope("singleton");
        scopes.add(singletons);
        scopes.add(new PrototypeScope("prototype"));

        definitions.configureApplication(this);

        if (annotationConfigEnabled) {
            for (GenericBean bean: beans) {
                bean.activateAnnotationConfig();
            }
        }

        for (GenericBean bean: beans) {
            Scope scope = bean.getScope();
            String beanId = bean.getName();
            if (scope == singletons) {
                scope.get(beanId, LifecyclePhase.INSTANTIATE, bean);
            }
        }

        for (GenericBean bean: beans) {
            Scope scope = bean.getScope();
            String beanId = bean.getName();
            if (scope == singletons) {
                scope.get(beanId, LifecyclePhase.CONFIGURE, bean);
            }
        }
        for (GenericBean bean: beans) {
            Scope scope = bean.getScope();
            String beanId = bean.getName();
            if (scope == singletons) {
                scope.get(beanId, LifecyclePhase.INITIALIZE, bean);
            }
        }

        System.out.println("[Container] started");
    }

    public void shutdown() throws Exception {

        Scope singletons = getScope("singleton");

        for (GenericBean bean: beans) {
            Scope scope = bean.getScope();
            String beanId = bean.getName();
            if (scope == singletons) {
                scope.get(beanId, LifecyclePhase.DESTROY, bean);
            }
        }
    }

    @Override
    public Scope getScope(String name) {
        for (Scope scope: scopes) {
            if (scope.getName().equals(name)) {
                return scope;
            }

        }
        throw new IllegalStateException();
    }

    @Override
    public GenericBean lookupByName(String name) {
        for (GenericBean bean: beans) {
            if (bean.getName().equals(name)) {
                return bean;
            }
        }
        throw new IllegalStateException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public LazyValue lookupByType(Type genericType) {
        Class type = GenericTypes.getRaw(genericType);

        if (type.isAssignableFrom(List.class)) {
            Class elementType = GenericTypes.getParameter(genericType, 0);
            List<GenericBean> matched = beansByType(elementType);
            return new LazyList(matched);
        }
        List<GenericBean> matched = beansByType(type);
        if (matched.size() != 1) {
            throw new IllegalStateException();
        }
        return matched.get(0);

    }

    private List<GenericBean> beansByType(Class type) {
        List<GenericBean> matched = new ArrayList<GenericBean>();
        for (GenericBean bean: beans) {
            if (type.isAssignableFrom(bean.getJavaType())) {
                matched.add(bean);
            }
        }
        return matched;
    }

    @Override
    public void setAnnotationConfigEnabled(boolean annotationConfigEnabled) {
        this.annotationConfigEnabled = annotationConfigEnabled;
    }

    @Override
    public Transformer getTransformer() {
        return transformer;
    }

    @Override
    public BeanBuilder createRootBean() throws Exception {
        return new BeanBuilder() {
            @Override
            public Bean build() throws Exception {
                if (getName() == null) {
                    setName("bean" + beans.size());
                }
                GenericBean bean = new GenericBean(this, Container.this);
                beans.add(bean);
                return bean;
            }
        };
    }
}
