package io.github.dbstarll.dubai.user.entity.join;

import io.github.dbstarll.dubai.model.entity.JoinBase;
import org.bson.types.ObjectId;

public interface CredentialBase extends JoinBase {
    String FIELD_NAME_CREDENTIAL_ID = "credentialId";

    ObjectId getCredentialId();

    void setCredentialId(ObjectId credentialId);
}
