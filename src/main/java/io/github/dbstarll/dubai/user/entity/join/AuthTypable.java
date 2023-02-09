package io.github.dbstarll.dubai.user.entity.join;

import io.github.dbstarll.dubai.model.entity.InfoBase;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;

/**
 * 提供对认证类型字段的扩展.
 */
public interface AuthTypable extends InfoBase {
    String FIELD_NAME_AUTH_TYPE = "source";

    /**
     * 获得认证类型.
     *
     * @return 认证类型
     */
    AuthType getSource();

    /**
     * 设置认证类型.
     *
     * @param authType 新的认证类型
     */
    void setSource(AuthType authType);
}
