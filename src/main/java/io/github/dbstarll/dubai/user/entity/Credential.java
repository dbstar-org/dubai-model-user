package io.github.dbstarll.dubai.user.entity;

import io.github.dbstarll.dubai.model.entity.Table;
import io.github.dbstarll.dubai.model.entity.func.Cacheable;
import io.github.dbstarll.dubai.model.entity.func.Defunctable;
import io.github.dbstarll.dubai.user.entity.enums.SourceType;
import io.github.dbstarll.dubai.user.entity.join.PrincipalBase;

import java.util.Map;

@Table
public interface Credential extends UserEntities, Defunctable, Cacheable, PrincipalBase {
  SourceType getSource();

  void setSource(SourceType source);

  Map<String, Object> getCredentials();

  void setCredentials(Map<String, Object> credentials);

  boolean isDisabled();

  void setDisabled(boolean disabled);
}
