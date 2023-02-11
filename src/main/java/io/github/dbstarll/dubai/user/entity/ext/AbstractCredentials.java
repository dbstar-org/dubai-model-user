package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.user.entity.Credential;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractCredentials implements CredentialDetails, Serializable {
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

    final Credential apply(final Credential credential) {
        credential.setCredentials(this.map);
        return credential;
    }
}
