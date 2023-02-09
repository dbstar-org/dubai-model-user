package io.github.dbstarll.dubai.user.service.impl;

import io.github.dbstarll.dubai.model.collection.Collection;
import io.github.dbstarll.dubai.user.entity.Authentication;
import io.github.dbstarll.dubai.user.service.AuthenticationService;
import io.github.dbstarll.dubai.user.service.attach.AuthenticationServiceAttach;

public final class AuthenticationServiceImplemental extends UserImplementals<Authentication, AuthenticationService>
        implements AuthenticationServiceAttach {
    /**
     * 构建AuthenticationServiceImplemental.
     *
     * @param service    服务类
     * @param collection 集合类
     */
    public AuthenticationServiceImplemental(final AuthenticationService service,
                                            final Collection<Authentication> collection) {
        super(service, collection);
    }
}
