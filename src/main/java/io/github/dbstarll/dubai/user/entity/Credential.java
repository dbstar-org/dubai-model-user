package io.github.dbstarll.dubai.user.entity;

import io.github.dbstarll.dubai.model.entity.Table;
import io.github.dbstarll.dubai.model.entity.func.Cacheable;
import io.github.dbstarll.dubai.model.entity.func.Defunctable;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;
import io.github.dbstarll.dubai.user.entity.join.PrincipalBase;

import java.util.Map;

/**
 * 认证凭据实体类，用于对用户身份进行认证时提供凭据，每个Credential存储一个具体的认证凭据.
 */
@Table
public interface Credential extends UserEntities, Defunctable, Cacheable, AuthTypable, PrincipalBase {
    /**
     * 获得凭据的详细内容.
     *
     * @return 凭据详情
     */
    Map<String, Object> getCredentials();

    /**
     * 设置凭据的详细内容.
     *
     * @param credentials 凭据的详细内容
     */
    void setCredentials(Map<String, Object> credentials);

    /**
     * 返回是否禁用.
     *
     * @return 是否禁用
     */
    boolean isDisabled();

    /**
     * 设置是否禁用.
     *
     * @param disabled 是否禁用
     */
    void setDisabled(boolean disabled);
}
