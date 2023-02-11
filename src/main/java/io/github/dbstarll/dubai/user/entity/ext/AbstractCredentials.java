package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.user.entity.Credential;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractCredentials implements CredentialDetails, Cloneable, Serializable {
    private final Map<String, Object> map;

    protected AbstractCredentials() {
        this(null);
    }

    protected AbstractCredentials(final Map<String, Object> map) {
        this.map = map == null ? new HashMap<>() : new HashMap<>(map);
    }

    @SuppressWarnings("unchecked")
    protected final <C> C get(final String key) {
        return (C) map.get(key);
    }

    protected final void put(final String key, final Object value) {
        map.put(key, value);
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
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

    final Credential apply(final Credential credential) {
        credential.setCredentials(this.map);
        return credential;
    }
}
