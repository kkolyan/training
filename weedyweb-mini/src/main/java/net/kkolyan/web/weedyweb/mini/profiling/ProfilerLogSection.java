package net.kkolyan.web.weedyweb.mini.profiling;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NPlekhanov
 */
public class ProfilerLogSection {
    private String name;
    private List<ProfilerLogRecord> records = new ArrayList<ProfilerLogRecord>();

    public List<ProfilerLogRecord> getRecords() {
        return records;
    }

    public void setRecords(List<ProfilerLogRecord> records) {
        this.records = records;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
