package net.kkolyan.web.weedyweb.mini.profiling;

/**
* @author NPlekhanov
*/
public class AverageNumberAggregator implements Aggregator {
    private double sum;
    private int count;

    @Override
    public void addValue(double value) {
        sum += value;
        count ++;
    }

    public String getValue() {
        double av = sum / count;
        return String.format("%.03f", av);
    }
}
