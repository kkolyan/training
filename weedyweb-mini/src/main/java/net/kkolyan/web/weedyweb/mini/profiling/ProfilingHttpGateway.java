package net.kkolyan.web.weedyweb.mini.profiling;

import net.kkolyan.web.http.api.HttpGateway;
import net.kkolyan.web.http.api.HttpRequest;
import net.kkolyan.web.http.api.HttpResponse;
import net.kkolyan.web.http.api.HttpStatusException;
import net.kkolyan.web.weedyweb.api.Action;
import net.kkolyan.web.weedyweb.api.ModelAndView;
import net.kkolyan.web.weedyweb.mini.core.view.View;
import net.kkolyan.web.weedyweb.mini.core.FrameworkHttpGateway;
import net.kkolyan.web.weedyweb.mini.core.RequestHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ListIterator;

/**
 * @author NPlekhanov
 */
public class ProfilingHttpGateway implements HttpGateway {
    private FrameworkHttpGateway serverAdapter;
    private ThreadLocal<ProfilerLogSection> currentLogSection = new ThreadLocal<ProfilerLogSection>();
    private File file;

    private HttpGateway gatewayProxy;
    private ProfilerLog log = new ProfilerLog();

    @Action(path = "/stat", params = "clear")
    public ModelAndView showStat(boolean clear) {
        if (clear) {
            log.getSections().clear();
            saveLog();
            return new ModelAndView("redirect:/stat", null);
        }
        Table<Aggregator> summary = new Table<Aggregator>("");
        ProfilingSummaryGenerator.generateSummary(log, summary);
        return new ModelAndView("stat.vm", summary);
    }

    @PostConstruct
    public void startup() throws Exception {

        gatewayProxy = wrap(HttpGateway.class, serverAdapter, "HttpGateway");

        ListIterator<View> views = serverAdapter.getViews().listIterator();
        while (views.hasNext()) {
            View view = views.next();
            view = wrap(View.class, view, "View");
            view.toString();
            views.set(view);
        }

        ListIterator<RequestHandler> requestHandlers = serverAdapter.getHandlers().listIterator();
        while (requestHandlers.hasNext()) {
            RequestHandler requestHandler = requestHandlers.next();
            requestHandler = wrap(RequestHandler.class, requestHandler, "View + RequestHandler");
            requestHandlers.set(requestHandler);
        }

        if (file != null && file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            try {
                log.load(inputStream);
            } finally {
                inputStream.close();
            }
        }
    }

    private <T> T wrap(Class<T> aClass, final Object o, final String metric) {
        InvocationHandler handler = new ProfilingInvocationHandler(o, metric, currentLogSection);
        Object wrapped = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{aClass}, handler);
        return aClass.cast(wrapped);
    }

    @PreDestroy
    public void shutdown() throws Exception {
        saveLog();
    }

    @Override
    public boolean doServe(HttpRequest request, HttpResponse response) throws HttpStatusException {
        ProfilerLogSection section = new ProfilerLogSection();
        section.setName(request.getPath());
        currentLogSection.set(section);

        boolean served = gatewayProxy.doServe(request, response);

        if (served) {
            Double viewAndController = findValue("View + RequestHandler");
            Double view = findValue("View");
            if (viewAndController != null && view != null) {
                ProfilerLogRecord record = new ProfilerLogRecord();
                record.setMetric("RequestHandler");
                record.setValue(viewAndController - view);
                section.getRecords().add(record);
            }

            log.getSections().add(section);
        }

        return served;
    }
    
    private Double findValue(String metric) {
        for (ProfilerLogRecord record: currentLogSection.get().getRecords()) {
            if (record.getMetric().equals(metric)) {
                return record.getValue();
            }
        }
        return null;
    }

    private void saveLog() {
        if (file == null) {
            return;
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            try {
                log.save(outputStream);
            } finally {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setServerAdapter(FrameworkHttpGateway serverAdapter) {
        this.serverAdapter = serverAdapter;
    }
}
