package io.github.dbstarll.dubai.user.service.attach;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import io.github.dbstarll.dubai.model.entity.Entity;
import io.github.dbstarll.dubai.model.service.Implementation;
import io.github.dbstarll.dubai.model.service.attach.CoreAttachs;
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.entity.join.CredentialBase;
import io.github.dbstarll.dubai.user.service.impl.CredentialAttachImplemental;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Map.Entry;

@Implementation(CredentialAttachImplemental.class)
public interface CredentialAttach<E extends Entity & CredentialBase> extends CoreAttachs {
  Bson filterByCredentialId(ObjectId credentialId);

  long countByCredentialId(ObjectId credentialId);

  FindIterable<E> findByCredentialId(ObjectId credentialId);

  MongoIterable<Entry<E, Credential>> findWithCredential(Bson filter);
}
