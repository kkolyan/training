package net.kkolyan.web.weedyweb.mini.profiling;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
* @author NPlekhanov
*/
public class Table<T> {
    private Map<String,T> data = new TreeMap<String, T>();
    private Set<String> keys = new TreeSet<String>();
    private Set<String> columns = new TreeSet<String>();
    private String keyColumn;

    public Table(String keyColumn) {
        this.keyColumn = keyColumn;
    }

    public Set<String> getKeys() {
        return keys;
    }

    public Set<String> getColumns() {
        return columns;
    }

    public T get(String key, String column) {
        return data.get(key + ":" + column);
    }

    public void put(String key, String column, T value) {
        keys.add(key);
        columns.add(column);
        data.put(key+":"+column, value);
    }

    public String getKeyColumn() {
        return keyColumn;
    }
}
