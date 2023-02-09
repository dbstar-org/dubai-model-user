package io.github.dbstarll.dubai.user.entity.join;

import io.github.dbstarll.dubai.model.entity.JoinBase;
import org.bson.types.ObjectId;

/**
 * 提供基于主体的关联属性，主体通常是可登录的用户或者设备.
 */
public interface PrincipalBase extends JoinBase {
    String FIELD_NAME_PRINCIPAL_ID = "principalId";

    /**
     * 获得主体ID.
     *
     * @return 主体ID
     */
    ObjectId getPrincipalId();

    /**
     * 设置主体ID.
     *
     * @param principalId 新的主体ID
     */
    void setPrincipalId(ObjectId principalId);
}
