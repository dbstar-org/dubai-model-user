package io.github.dbstarll.dubai.user.service.impl;

import com.mongodb.Function;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.result.DeleteResult;
import io.github.dbstarll.dubai.model.collection.Collection;
import io.github.dbstarll.dubai.model.collection.CollectionNameGenerator;
import io.github.dbstarll.dubai.model.entity.Entity;
import io.github.dbstarll.dubai.model.service.Service;
import io.github.dbstarll.dubai.model.service.impl.CoreImplementals;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.model.service.validation.GeneralValidation;
import io.github.dbstarll.dubai.model.service.validation.Validation;
import io.github.dbstarll.dubai.user.entity.join.PrincipalBase;
import io.github.dbstarll.dubai.user.service.attach.PrincipalAttach;
import io.github.dbstarll.utils.lang.wrapper.EntryWrapper;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import static com.mongodb.client.model.Filters.eq;
import static org.apache.commons.lang3.Validate.notNull;

public final class PrincipalAttachImplemental<E extends Entity & PrincipalBase, S extends Service<E>>
        extends CoreImplementals<E, S> implements PrincipalAttach<E> {
  private static DecoderContext decoderContext = DecoderContext.builder().build();

  private CodecRegistry registry;
  private Codec<E> entityCodec;
  private CollectionNameGenerator collectionNameGenerator;

  public PrincipalAttachImplemental(S service, Collection<E> collection) {
    super(service, collection);
  }

  public void setMongoDatabase(MongoDatabase mongoDatabase) {
    this.registry = mongoDatabase.getCodecRegistry();
    this.entityCodec = registry.get(entityClass);
  }

  public void setCollectionNameGenerator(CollectionNameGenerator collectionNameGenerator) {
    this.collectionNameGenerator = collectionNameGenerator;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();
    notNull(registry, "registry is null");
    notNull(entityCodec, "entityCodec is null");
    notNull(collectionNameGenerator, "collectionNameGenerator is null");
  }

  @Override
  public Bson filterByPrincipalId(ObjectId principalId) {
    return eq(PrincipalBase.FIELD_NAME_PRINCIPAL_ID, principalId);
  }

  @Override
  public long countByPrincipalId(ObjectId principalId) {
    return service.count(filterByPrincipalId(principalId));
  }

  @Override
  public FindIterable<E> findByPrincipalId(ObjectId principalId) {
    return service.find(filterByPrincipalId(principalId));
  }

  @Override
  public DeleteResult deleteByPrincipalId(ObjectId principalId) {
    return getCollection().deleteMany(filterByPrincipalId(principalId));
  }

  @Override
  public <P extends Entity, SPT extends Service<P>> MongoIterable<Entry<E, P>> findWithPrincipal(SPT principalService,
                                                                                                 Bson filter) {
    final Class<P> principalClass = principalService.getEntityClass();
    final Codec<P> principalCodec = registry.get(principalClass);

    final List<Bson> pipeline = new LinkedList<>();
    final Bson matchFilter = aggregateMatchFilter(filter);
    if (matchFilter != null) {
      pipeline.add(Aggregates.match(matchFilter));
    }
    pipeline.add(Aggregates.lookup(collectionNameGenerator.generateCollectionName(principalClass),
            PrincipalBase.FIELD_NAME_PRINCIPAL_ID, Entity.FIELD_NAME_ID, "principals"));

    return getCollection().aggregate(pipeline, BsonDocument.class).map(new Function<BsonDocument, Entry<E, P>>() {
      @Override
      public Entry<E, P> apply(BsonDocument t) {
        final BsonArray principals = t.getArray("principals");
        final E entity = entityCodec.decode(t.asBsonReader(), decoderContext);
        final P principal = principals.size() > 0
                ? principalCodec.decode(((BsonDocument) principals.get(0)).asBsonReader(), decoderContext)
                : null;
        return EntryWrapper.wrap(entity, principal);
      }
    });
  }

  /**
   * 主体校验.
   *
   * @return finalPrincipalIdValidation
   */
  @GeneralValidation
  public Validation<E> finalPrincipalIdValidation() {
    return new AbstractEntityValidation() {
      @Override
      public void validate(E entity, E original, Validate validate) {
        if (entity.getPrincipalId() == null) {
          validate.addFieldError(PrincipalBase.FIELD_NAME_PRINCIPAL_ID, "主体未设置");
        } else if (original != null && !entity.getPrincipalId().equals(original.getPrincipalId())) {
          validate.addFieldError(PrincipalBase.FIELD_NAME_PRINCIPAL_ID, "主体不可更改");
        }
      }
    };
  }
}
