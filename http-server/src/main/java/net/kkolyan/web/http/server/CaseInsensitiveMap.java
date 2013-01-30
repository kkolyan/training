package net.kkolyan.web.http.server;

import java.util.*;

/**
 * @author nplekhanov
 */
public class CaseInsensitiveMap<V> implements Map<String,V> {
    private Map<String,V> map;

    public CaseInsensitiveMap(Map<String, V> map) {
        this.map = map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        key = lower(key);
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        key = lower(key);
        return map.get(key);
    }

    public V put(String key, V value) {
        key = lower(key);
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        key = lower(key);
        return map.remove(key);
    }

    public void putAll(Map<? extends String, ? extends V> m) {
        for (Entry<? extends String, ? extends V> entry: m.entrySet()) {
            map.put(lower(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return new CaseInsensitiveSet(map.keySet());
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return map.entrySet();
    }
    
    private static String lower(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString().toLowerCase();
    }
}
