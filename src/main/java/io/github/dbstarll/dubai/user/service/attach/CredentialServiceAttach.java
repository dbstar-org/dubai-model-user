package io.github.dbstarll.dubai.user.service.attach;

import io.github.dbstarll.dubai.model.service.Implementation;
import io.github.dbstarll.dubai.user.entity.enums.SourceType;
import io.github.dbstarll.dubai.user.service.impl.CredentialServiceImplemental;
import org.bson.conversions.Bson;

@Implementation(CredentialServiceImplemental.class)
public interface CredentialServiceAttach extends UserAttachs {
    Bson filterBySource(SourceType source);
}
