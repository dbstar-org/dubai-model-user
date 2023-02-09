package io.github.dbstarll.dubai.user.entity.join;

import io.github.dbstarll.dubai.model.entity.JoinBase;
import org.bson.types.ObjectId;

/**
 * 提供基于认证凭据的关联属性.
 */
public interface CredentialBase extends JoinBase {
    String FIELD_NAME_CREDENTIAL_ID = "credentialId";

    /**
     * 获得认证凭据ID.
     *
     * @return 认证凭据ID
     */
    ObjectId getCredentialId();

    /**
     * 设置认证凭据ID.
     *
     * @param credentialId 新的认证凭据ID
     */
    void setCredentialId(ObjectId credentialId);
}
