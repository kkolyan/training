package net.kkolyan.spring.altimpl.misc;

import net.kkolyan.spring.altimpl.BeanLifecycle;
import net.kkolyan.spring.altimpl.LifecyclePhase;
import net.kkolyan.spring.altimpl.Scope;

/**
 * @author nplekhanov
 */
public class PrototypeScope implements Scope {
    private String name;

    public PrototypeScope(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object get(String name, LifecyclePhase atLeast, BeanLifecycle lifecycle) throws Exception {
        if (atLeast != LifecyclePhase.INITIALIZE) {
            throw new IllegalStateException();
        }
        Object o = lifecycle.instantiate();
        lifecycle.configure(o);
        lifecycle.initialize(o);
        return o;
    }
}
