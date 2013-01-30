package net.kkolyan.web.http.server;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author nplekhanov
 */
public class CaseInsensitiveSet implements Set<String> {
    private Set<String> set;

    public CaseInsensitiveSet(Set<String> set) {
        this.set = set;
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        o = lower(o);
        return set.contains(o);
    }

    @Override
    public Iterator<String> iterator() {
        return new Itr(set.iterator());
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(String s) {
        s = lower(s);
        return set.add(s);
    }

    @Override
    public boolean remove(Object o) {
        o = lower(o);
        return set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object s : c)
            if (!contains(lower(s))) {
                return false;
            }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {
        boolean modified = false;
        for (String s : c) {
            if (add(lower(s))) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<String> e = iterator();
        while (e.hasNext()) {
            if (!c.contains(lower(e.next()))) {
                e.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Iterator<?> e = iterator();
        while (e.hasNext()) {
            if (c.contains(lower(e.next()))) {
                e.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public boolean equals(Object o) {
        return set.equals(o);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    private static String lower(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString().toLowerCase();
    }

    private static class Itr implements Iterator<String> {
        private Iterator<String> it;

        private Itr(Iterator<String> it) {
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public String next() {
            return lower(it.next());
        }

        @Override
        public void remove() {
            it.remove();
        }
    }
}
