package io.github.dbstarll.dubai.user.service.attach;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import io.github.dbstarll.dubai.model.entity.Entity;
import io.github.dbstarll.dubai.model.service.Implementation;
import io.github.dbstarll.dubai.model.service.Service;
import io.github.dbstarll.dubai.model.service.attach.CoreAttachs;
import io.github.dbstarll.dubai.user.entity.join.PrincipalBase;
import io.github.dbstarll.dubai.user.service.impl.PrincipalAttachImplemental;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Map.Entry;

@Implementation(PrincipalAttachImplemental.class)
public interface PrincipalAttach<E extends Entity & PrincipalBase> extends CoreAttachs {
  Bson filterByPrincipalId(ObjectId principalId);

  long countByPrincipalId(ObjectId principalId);

  FindIterable<E> findByPrincipalId(ObjectId principalId);

  DeleteResult deleteByPrincipalId(ObjectId principalId);

  <P extends Entity, SPT extends Service<P>> MongoIterable<Entry<E, P>> findWithPrincipal(SPT principalService,
                                                                                          Bson filter);
}
