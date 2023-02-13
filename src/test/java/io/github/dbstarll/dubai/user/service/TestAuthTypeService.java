package io.github.dbstarll.dubai.user.service;

import io.github.dbstarll.dubai.model.service.EntityService;
import io.github.dbstarll.dubai.user.entity.TestAuthTypeEntity;
import io.github.dbstarll.dubai.user.service.attach.AuthTypeAttach;

@EntityService
public interface TestAuthTypeService extends UserServices<TestAuthTypeEntity>, AuthTypeAttach<TestAuthTypeEntity> {
}
