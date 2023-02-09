package io.github.dbstarll.dubai.user.entity;

import io.github.dbstarll.dubai.model.entity.Table;
import io.github.dbstarll.dubai.model.entity.func.Cacheable;
import io.github.dbstarll.dubai.model.entity.func.Defunctable;
import io.github.dbstarll.dubai.user.entity.enums.SourceType;
import io.github.dbstarll.dubai.user.entity.join.PrincipalBase;

import java.io.Serializable;
import java.util.Map;

@Table
public interface Credential extends UserEntities, Defunctable, Cacheable, PrincipalBase {
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
     * 获得凭据详情.
     *
     * @return 凭据详情
     */
    Map<String, Serializable> getCredentials();

    /**
     * 设置凭据详情.
     *
     * @param credentials 凭据详情
     */
    void setCredentials(Map<String, Serializable> credentials);

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
