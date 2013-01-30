package net.kkolyan.web.weedyweb.mini.profiling;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author NPlekhanov
 */
public class ProfilerLog {
    private List<ProfilerLogSection> sections = new ArrayList<ProfilerLogSection>();
    
    public void load(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            line = line.trim();

            if (line.startsWith("[")) {
                ProfilerLogSection section = new ProfilerLogSection();
                section.setName(line.substring(1, line.lastIndexOf(']')));
                sections.add(section);
            } else {
                String[] parts = line.split("=");
                ProfilerLogRecord record = new ProfilerLogRecord();
                record.setMetric(parts[0].trim());
                double value = Double.parseDouble(parts[1].trim());
                record.setValue(value);
                sections.get(sections.size() - 1).getRecords().add(record);
            }
        }
    }

    public void save(OutputStream stream) {
        PrintStream out = new PrintStream(stream);
        for (ProfilerLogSection section: sections) {
            out.println("["+section.getName()+"]");
            for (ProfilerLogRecord record: section.getRecords()) {
                out.println(record.getMetric() + "=" + record.getValue()+"");
            }
        }
        out.flush();
    }

    public List<ProfilerLogSection> getSections() {
        return sections;
    }

    public void setSections(List<ProfilerLogSection> sections) {
        this.sections = sections;
    }
}
