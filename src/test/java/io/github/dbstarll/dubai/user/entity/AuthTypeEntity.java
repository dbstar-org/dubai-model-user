package io.github.dbstarll.dubai.user.entity;

import io.github.dbstarll.dubai.model.entity.Table;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;

@Table
public interface AuthTypeEntity extends UserEntities, AuthTypable {
}
