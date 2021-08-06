package io.github.dbstarll.dubai.user.entity;

import io.github.dbstarll.dubai.model.entity.Table;
import io.github.dbstarll.dubai.model.entity.func.Notifiable;
import io.github.dbstarll.dubai.user.entity.enums.SourceType;
import io.github.dbstarll.dubai.user.entity.join.CredentialBase;
import io.github.dbstarll.dubai.user.entity.join.PrincipalBase;

import java.util.Map;

@Table
public interface Authentication extends UserEntities, Notifiable, CredentialBase, PrincipalBase {
  SourceType getSource();

  void setSource(SourceType source);

  Map<String, Map<String, Object>> getDetails();

  void setDetails(Map<String, Map<String, Object>> details);
}
