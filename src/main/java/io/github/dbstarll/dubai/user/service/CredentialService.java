package io.github.dbstarll.dubai.user.service;

import io.github.dbstarll.dubai.model.service.EntityService;
import io.github.dbstarll.dubai.model.service.attach.DefunctAttach;
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.service.attach.CredentialServiceAttach;
import io.github.dbstarll.dubai.user.service.attach.PrincipalAttach;

@EntityService
public interface CredentialService
        extends UserServices<Credential>, PrincipalAttach<Credential>, DefunctAttach<Credential>, CredentialServiceAttach {
}
