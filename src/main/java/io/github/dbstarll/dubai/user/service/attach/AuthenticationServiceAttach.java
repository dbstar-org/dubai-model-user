package io.github.dbstarll.dubai.user.service.attach;

import io.github.dbstarll.dubai.model.service.Implementation;
import io.github.dbstarll.dubai.user.service.impl.AuthenticationServiceImplemental;

@Implementation(AuthenticationServiceImplemental.class)
public interface AuthenticationServiceAttach extends UserAttachs {
}
