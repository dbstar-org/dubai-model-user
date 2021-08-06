package io.github.dbstarll.dubai.user.service;

import io.github.dbstarll.dubai.model.service.EntityService;
import io.github.dbstarll.dubai.user.entity.Authentication;
import io.github.dbstarll.dubai.user.service.attach.CredentialAttach;
import io.github.dbstarll.dubai.user.service.attach.PrincipalAttach;

@EntityService
public interface AuthenticationService
        extends UserServices<Authentication>, PrincipalAttach<Authentication>, CredentialAttach<Authentication> {

}
