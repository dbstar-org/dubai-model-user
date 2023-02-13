package io.github.dbstarll.dubai.user.service;

import io.github.dbstarll.dubai.model.service.EntityService;
import io.github.dbstarll.dubai.user.entity.AuthTypeEntity;
import io.github.dbstarll.dubai.user.service.attach.AuthTypeAttach;

@EntityService
public interface AuthTypeService extends UserServices<AuthTypeEntity>, AuthTypeAttach<AuthTypeEntity> {
}
