package net.kkolyan.spring.altimpl.misc;

import net.kkolyan.spring.altimpl.BeanLifecycle;
import net.kkolyan.spring.altimpl.LifecyclePhase;
import net.kkolyan.spring.altimpl.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class MapBasedScope implements Scope {
    private Map<String,Entry> entries;
    private String name;

    public MapBasedScope(String name) {
        this.name = name;
    }

    protected Map<String,Entry> getEntries() {
        if (entries == null) {
            entries = new HashMap<String, Entry>();
        }
        return entries;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object get(String name, LifecyclePhase atLeast, BeanLifecycle lifecycle) throws Exception {
        if (name == null) {
            throw new IllegalStateException();
        }

        Entry entry = getEntries().get(name);
        if (entry == null) {
            entry = new Entry();
            getEntries().put(name, entry);
        }

        while (entry.stage.ordinal() < atLeast.ordinal()) {
            entry.stage = LifecyclePhase.values()[entry.stage.ordinal() + 1];

            if (entry.stage == LifecyclePhase.INSTANTIATE) {
                entry.instance = lifecycle.instantiate();
            }
            if (entry.stage == LifecyclePhase.CONFIGURE) {
                lifecycle.configure(entry.instance);
            }
            if (entry.stage == LifecyclePhase.INITIALIZE) {
                lifecycle.initialize(entry.instance);
            }
            if (entry.stage == LifecyclePhase.DESTROY) {
                lifecycle.destroy(entry.instance);
            }
        }
        return entry.instance;
    }

    protected static class Entry {
        private LifecyclePhase stage = LifecyclePhase.NIL;
        private Object instance;

        @Override
        public String toString() {
            return "Entry{" +
                    "stage=" + stage +
                    ", instance=" + instance +
                    '}';
        }
    }
}
