package io.github.dbstarll.dubai.user.entity.ext;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractCredentials implements CredentialDetails, Cloneable, Serializable {
    private static final long serialVersionUID = 7011651370922942849L;

    private final Map<String, Serializable> map;

    AbstractCredentials() {
        this(null);
    }

    AbstractCredentials(final Map<String, Serializable> map) {
        this.map = map == null ? new HashMap<>() : new HashMap<>(map);
    }

    public final boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    public final Serializable get(final Object key) {
        return map.get(key);
    }

    public final Object put(final String key, final Serializable value) {
        return map.put(key, value);
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof AbstractCredentials)) {
            return false;
        }
        final AbstractCredentials that = (AbstractCredentials) o;
        return new EqualsBuilder()
                .append(map, that.map)
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return map.hashCode();
    }

    @Override
    public final AbstractCredentials clone() throws CloneNotSupportedException {
        return (AbstractCredentials) super.clone();
    }

    @Override
    public final String toString() {
        return getClass().getName() + ": " + map;
    }

    Map<String, Serializable> map() {
        return this.map;
    }
}
