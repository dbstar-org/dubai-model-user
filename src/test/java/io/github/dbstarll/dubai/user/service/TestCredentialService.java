package io.github.dbstarll.dubai.user.service;

import io.github.dbstarll.dubai.model.service.EntityService;
import io.github.dbstarll.dubai.user.entity.TestCredentialEntity;
import io.github.dbstarll.dubai.user.service.attach.CredentialAttach;

@EntityService
public interface TestCredentialService extends UserServices<TestCredentialEntity>, CredentialAttach<TestCredentialEntity> {
}
