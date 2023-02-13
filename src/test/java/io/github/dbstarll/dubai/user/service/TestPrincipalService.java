package io.github.dbstarll.dubai.user.service;

import io.github.dbstarll.dubai.model.service.EntityService;
import io.github.dbstarll.dubai.user.entity.TestPrincipalEntity;
import io.github.dbstarll.dubai.user.service.attach.PrincipalAttach;

@EntityService
public interface TestPrincipalService extends UserServices<TestPrincipalEntity>, PrincipalAttach<TestPrincipalEntity> {
}
