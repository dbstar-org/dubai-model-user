package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.user.entity.Credential;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

abstract class AbstractCredentials implements CredentialDetails {
    protected static final String DEFAULT_NOT_EMPTY_EX_MESSAGE = "%s is blank";

    private final Map<String, Object> map;

    protected AbstractCredentials() {
        this(null);
    }

    protected AbstractCredentials(final Map<String, Object> map) {
        this.map = map == null ? new HashMap<>() : map;
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
        } else if (o == null || o.getClass() != getClass()) {
            return false;
        }
        final AbstractCredentials that = (AbstractCredentials) o;
        return new EqualsBuilder().append(map, that.map).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(map).toHashCode();
    }

    @Override
    public final String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                .add("map=" + map.size())
                .toString();
    }

    final Credential apply(final Credential credential) {
        credential.setCredentials(this.map);
        return credential;
    }
}
