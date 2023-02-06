package io.github.dbstarll.dubai.user.entity.ext;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

abstract class AbstractCredentials implements CredentialDetails, Cloneable, Serializable {
    private static final long serialVersionUID = 7011651370922942849L;

    private final Map<String, Object> map;

    AbstractCredentials() {
        this(null);
    }

    AbstractCredentials(Map<String, Object> map) {
        this.map = map == null ? new HashMap<String, Object>() : new HashMap<>(map);
    }

    @Override
    public final int size() {
        return map.size();
    }

    @Override
    public final boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public final boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public final boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public final Object get(Object key) {
        return map.get(key);
    }

    @Override
    public final Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public final Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public final void putAll(Map<? extends String, ? extends Object> m) {
        map.putAll(m);
    }

    @Override
    public final void clear() {
        map.clear();
    }

    @Override
    public final Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public final Collection<Object> values() {
        return map.values();
    }

    @Override
    public final Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + map.hashCode();
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (AbstractCredentials.class.isInstance(obj)) {
            return map.equals(((AbstractCredentials) obj).map);
        } else if (Map.class.isInstance(obj)) {
            return map.equals(obj);
        } else {
            return false;
        }
    }

    @Override
    public final AbstractCredentials clone() throws CloneNotSupportedException {
        final AbstractCredentials details = (AbstractCredentials) super.clone();
        return details;
    }

    @Override
    public final String toString() {
        return getClass().getName() + ": " + map;
    }
}
