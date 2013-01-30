package net.kkolyan.web.weedyweb.mini.profiling;

/**
 * @author NPlekhanov
 */
public class ProfilerLogRecord {
    private String metric;
    private double value;

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
