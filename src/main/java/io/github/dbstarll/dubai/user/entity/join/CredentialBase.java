package io.github.dbstarll.dubai.user.entity.join;

import io.github.dbstarll.dubai.model.entity.JoinBase;
import org.bson.types.ObjectId;

public interface CredentialBase extends JoinBase {
    String FIELD_NAME_CREDENTIAL_ID = "credentialId";

    /**
     * 获得认证ID.
     *
     * @return 认证ID
     */
    ObjectId getCredentialId();

    /**
     * 设置认证ID.
     *
     * @param credentialId 新的认证ID
     */
    void setCredentialId(ObjectId credentialId);
}
