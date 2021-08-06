package io.github.dbstarll.dubai.user.entity.join;

import io.github.dbstarll.dubai.model.entity.JoinBase;
import org.bson.types.ObjectId;

public interface PrincipalBase extends JoinBase {
    String FIELD_NAME_PRINCIPAL_ID = "principalId";

    ObjectId getPrincipalId();

    void setPrincipalId(ObjectId principalId);
}
