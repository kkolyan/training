package net.kkolyan.web.weedyweb.mini.profiling;

/**
* @author NPlekhanov
*/
public interface Aggregator {
    void addValue(double value);
    String getValue();
}
