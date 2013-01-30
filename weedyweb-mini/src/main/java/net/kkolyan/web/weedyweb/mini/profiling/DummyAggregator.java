package net.kkolyan.web.weedyweb.mini.profiling;

/**
* @author NPlekhanov
*/
public class DummyAggregator implements Aggregator {
    private String value;

    public DummyAggregator(String value) {
        this.value = value;
    }

    public void addValue(double value) {
        throw new AssertionError();
    }

    public String getValue() {
        return value;
    }
}
