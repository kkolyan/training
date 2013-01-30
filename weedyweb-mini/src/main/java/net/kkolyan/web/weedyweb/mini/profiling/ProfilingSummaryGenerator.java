package net.kkolyan.web.weedyweb.mini.profiling;

import java.io.*;

/**
 * @author NPlekhanov
 */
public class ProfilingSummaryGenerator {

    public static void main(String[] args) throws FileNotFoundException {
        ProfilerLog log = new ProfilerLog();
        log.load(new FileInputStream("profiling.log"));
        Table<Aggregator> data = new Table<Aggregator>("");
        generateSummary(log, data);
        writeSummaryAsPlainText(data, System.out);
    }

    public static void writeSummaryAsPlainText(Table<Aggregator> data, OutputStream outputStream) {

        PrintStream out = new PrintStream(outputStream);

        final int columnWidth = 20;
        
        for (String header: data.getColumns()) {
            out.print(prolong(header, columnWidth));
        }
        out.println();
        
        for (String key: data.getKeys()) {
            for (String header: data.getColumns()) {
                Aggregator aggregator = data.get(key, header);
                if (aggregator != null) {
                    out.print(prolong(aggregator.getValue(), columnWidth));
                } else {
                    out.print(prolong("", columnWidth));
                }
            }
            out.println();
        }

        out.flush();
    }

    public static void generateSummary(ProfilerLog log, Table<Aggregator> data) {
        for (ProfilerLogSection section: log.getSections()) {
            data.put(section.getName(), "", new DummyAggregator(section.getName()));

            for (ProfilerLogRecord record: section.getRecords()) {
                Aggregator aggregator = data.get(section.getName(), record.getMetric());
                if (aggregator == null) {
                    aggregator = new AverageNumberAggregator();
                    data.put(section.getName(), record.getMetric(), aggregator);
                }
                aggregator.addValue(record.getValue());
            }
        }
    }
    
    private static String prolong(String s, int i) {
        while (s.length() < i) {
            s = ' ' + s;
        }
        return s;
    }
}
