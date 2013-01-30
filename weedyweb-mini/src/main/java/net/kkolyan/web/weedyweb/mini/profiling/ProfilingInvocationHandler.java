package net.kkolyan.web.weedyweb.mini.profiling;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
* @author NPlekhanov
*/
public class ProfilingInvocationHandler implements InvocationHandler {
    private Object instance;
    private String metric;
    private ThreadLocal<ProfilerLogSection> store;

    public ProfilingInvocationHandler(Object instance, String metric, ThreadLocal<ProfilerLogSection> store) {
        this.instance = instance;
        this.metric = metric;
        this.store = store;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(instance, args);
        }
        long time = System.nanoTime();
        Object result = method.invoke(instance, args);
        time = System.nanoTime() - time;
        ProfilerLogSection section = store.get();
        if (!Boolean.FALSE.equals(result)) {
            ProfilerLogRecord record = new ProfilerLogRecord();
            record.setMetric(metric);
            record.setValue(time / 1e6);
            section.getRecords().add(record);
        }
        return result;
    }
}
