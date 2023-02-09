package io.github.dbstarll.dubai.user.entity;

import io.github.dbstarll.dubai.model.entity.Table;
import io.github.dbstarll.dubai.model.entity.func.Notifiable;
import io.github.dbstarll.dubai.user.entity.enums.SourceType;
import io.github.dbstarll.dubai.user.entity.join.CredentialBase;
import io.github.dbstarll.dubai.user.entity.join.PrincipalBase;

import java.io.Serializable;
import java.util.Map;

@Table
public interface Authentication extends UserEntities, Notifiable, CredentialBase, PrincipalBase {
    /**
     * 获得认证方式.
     *
     * @return 认证方式
     */
    SourceType getSource();

    /**
     * 设置认证方式.
     *
     * @param source 认证方式
     */
    void setSource(SourceType source);

    /**
     * 获得认证详情.
     *
     * @return 认证详情
     */
    Map<String, Map<String, Serializable>> getDetails();

    /**
     * 设置认证详情.
     *
     * @param details 认证详情
     */
    void setDetails(Map<String, Map<String, Serializable>> details);
}
