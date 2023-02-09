package io.github.dbstarll.dubai.user.entity;

import io.github.dbstarll.dubai.model.entity.Table;
import io.github.dbstarll.dubai.model.entity.func.Notifiable;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;
import io.github.dbstarll.dubai.user.entity.join.CredentialBase;
import io.github.dbstarll.dubai.user.entity.join.PrincipalBase;

import java.util.Map;

/**
 * 认证实体类，每一个Authentication记录一次成功的认证过程.
 */
@Table
public interface Authentication extends UserEntities, Notifiable, AuthTypable, PrincipalBase, CredentialBase {
    /**
     * 获得认证详情.
     *
     * @return 认证详情
     */
    Map<String, Map<String, Object>> getDetails();

    /**
     * 设置认证详情.
     *
     * @param details 认证详情
     */
    void setDetails(Map<String, Map<String, Object>> details);
}
