package net.kkolyan.spring.altimpl.container;

import net.kkolyan.spring.altimpl.*;
import net.kkolyan.spring.altimpl.misc.FieldBasedProperty;
import net.kkolyan.spring.altimpl.misc.MethodBasedProperty;
import net.kkolyan.spring.altimpl.LazyValue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author nplekhanov
 */
public class GenericBean implements BeanLifecycle, Bean {
    private Application application;
    private String name;
    private Class beanClass;
    private Scope scope;
    private List<Method> initializers = new ArrayList<Method>();
    private List<Method> destructors = new ArrayList<Method>();
    private List<Property> properties = new ArrayList<Property>();
    private List<GenericBean> innerBeans = new ArrayList<GenericBean>();
    private boolean useScopedProxy;

    //==========================================================

    public GenericBean(BeanBuilder b, Application application) throws Exception {
        name = b.getName();
        scope = application.getScope(b.getScope());
        beanClass = Class.forName(b.getClassName());
        if (b.getInitMethod() != null) {
            initializers.add(beanClass.getMethod(b.getInitMethod()));
        }
        if (b.getDestroyMethod() != null) {
            destructors.add(beanClass.getMethod(b.getDestroyMethod()));
        }
        this.application = application;
    }

    //==========================================================

    @Override
    public void addProperty(Property property) {
        properties.add(property);
    }

    @Override
    public void enableScopedProxy() {
        useScopedProxy = true;
    }

    @Override
    public Object instantiate() throws Exception {
        System.out.println("[GenericBean] "+ name +" = new "+beanClass.getSimpleName()+"()");
        return beanClass.newInstance();
    }

    @Override
    public void configure(Object o) throws Exception {
        for (Property property : properties) {
            property.injectInto(o, application.getTransformer());
        }
    }

    @Override
    public void initialize(Object o) throws Exception {
        for (Method initMethod: initializers) {
            System.out.println("[GenericBean] "+ name +"."+initMethod.getName()+"()");
            initMethod.invoke(o);
        }
        scope.get(name, LifecyclePhase.INITIALIZE, this);
        for (GenericBean innerBean: innerBeans) {
            innerBean.scope.get(innerBean.name, LifecyclePhase.INITIALIZE, innerBean);
        }
    }

    @Override
    public void destroy(Object o) throws Exception {
        for (Method destroyMethod: destructors) {
            System.out.println("[GenericBean] "+beanClass.getSimpleName()+"."+destroyMethod.getName());
            destroyMethod.invoke(o);
        }
    }

    @Override
    public Object evaluate() throws Exception {
        if (useScopedProxy) {
            ClassLoader classLoader = getClass().getClassLoader();
            List<Class> interfaces = new ArrayList<Class>();
            for (Class c = beanClass; c != Object.class; c = c.getSuperclass()) {
                interfaces.addAll(Arrays.asList(c.getInterfaces()));
            }
            return Proxy.newProxyInstance(classLoader, interfaces.toArray(new Class<?>[interfaces.size()]), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Object instance = evaluateInternal();
                    return method.invoke(instance, args);
                }
            });
        }
        return evaluateInternal();
    }

    private Object evaluateInternal() throws Exception {
        if (scope == application.getScope("singleton")) {
            return scope.get(name, LifecyclePhase.INSTANTIATE, this);
        }
        return scope.get(name, LifecyclePhase.INITIALIZE, this);
    }

    //============================================================================================================

    public void activateAnnotationConfig() {
        for (Method method: beanClass.getMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                initializers.add(method);
            }
            if (method.isAnnotationPresent(PreDestroy.class)) {
                destructors.add(method);
            }
            if (method.isAnnotationPresent(Resource.class)) {
                Type requiredType = method.getGenericParameterTypes()[0];
                LazyValue value = application.lookupByType(requiredType);
                properties.add(new MethodBasedProperty(method, value));
            }
        }
        for (Class c = beanClass; c != Object.class; c = c.getSuperclass()) {
            for (Field field: c.getDeclaredFields()) {
                if (field.isAnnotationPresent(Resource.class)) {
                    Type requiredType = field.getGenericType();
                    LazyValue value = application.lookupByType(requiredType);
                    properties.add(new FieldBasedProperty(field, value));
                }
            }
        }
        for (GenericBean inner: innerBeans) {
            inner.activateAnnotationConfig();
        }
    }

    @Override
    public BeanBuilder createInnerBean() throws Exception {
        return new BeanBuilder() {
            @Override
            public Bean build() throws Exception {
                if (getName() == null) {
                    setName(GenericBean.this.name + "$" + innerBeans.size());
                }
                GenericBean bean = new GenericBean(this, application);
                innerBeans.add(bean);
                return bean;
            }
        };
    }

    //============================================================================================================

    public Scope getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }

    @Override
    public Class getJavaType() {
        return beanClass;
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public String toString() {
        return "GenericBean{" +
                "id='" + name + '\'' +
                ", class=" + beanClass +
                ", initializers=" + initializers +
                ", destructors=" + destructors +
                ", scope=" + scope +
                '}';
    }
}
