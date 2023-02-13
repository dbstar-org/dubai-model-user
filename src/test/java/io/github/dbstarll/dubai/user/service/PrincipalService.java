package io.github.dbstarll.dubai.user.service;

import io.github.dbstarll.dubai.model.service.EntityService;
import io.github.dbstarll.dubai.user.entity.PrincipalEntity;
import io.github.dbstarll.dubai.user.service.attach.PrincipalAttach;

@EntityService
public interface PrincipalService extends UserServices<PrincipalEntity>, PrincipalAttach<PrincipalEntity> {
}
