package io.github.dbstarll.dubai.user.service.impl;

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
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.entity.join.CredentialBase;
import io.github.dbstarll.dubai.user.service.attach.CredentialAttach;
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

public final class CredentialAttachImplemental<E extends Entity & CredentialBase, S extends Service<E>>
        extends CoreImplementals<E, S> implements CredentialAttach<E> {
    private static DecoderContext decoderContext = DecoderContext.builder().build();

    private Codec<E> entityCodec;
    private Codec<Credential> credentialCodec;
    private CollectionNameGenerator collectionNameGenerator;

    /**
     * 构造CredentialAttachImplemental.
     *
     * @param service    服务类
     * @param collection 集合类
     */
    public CredentialAttachImplemental(final S service, final Collection<E> collection) {
        super(service, collection);
    }

    /**
     * 传入mongoDatabase.
     *
     * @param mongoDatabase mongoDatabase
     */
    public void setMongoDatabase(final MongoDatabase mongoDatabase) {
        final CodecRegistry registry = mongoDatabase.getCodecRegistry();
        this.entityCodec = registry.get(entityClass);
        this.credentialCodec = registry.get(Credential.class);
    }

    public void setCollectionNameGenerator(final CollectionNameGenerator collectionNameGenerator) {
        this.collectionNameGenerator = collectionNameGenerator;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        notNull(entityCodec, "entityCodec is null");
        notNull(credentialCodec, "credentialCodec is null");
        notNull(collectionNameGenerator, "collectionNameGenerator is null");
    }

    @Override
    public Bson filterByCredentialId(final ObjectId credentialId) {
        return eq(CredentialBase.FIELD_NAME_CREDENTIAL_ID, credentialId);
    }

    @Override
    public long countByCredentialId(final ObjectId credentialId) {
        return service.count(filterByCredentialId(credentialId));
    }

    @Override
    public FindIterable<E> findByCredentialId(final ObjectId credentialId) {
        return service.find(filterByCredentialId(credentialId));
    }

    @Override
    public DeleteResult deleteByCredentialId(final ObjectId credentialId) {
        return getCollection().deleteMany(filterByCredentialId(credentialId));
    }

    @Override
    public MongoIterable<Entry<E, Credential>> findWithCredential(final Bson filter) {
        final List<Bson> pipeline = new LinkedList<>();
        final Bson matchFilter = aggregateMatchFilter(filter);
        if (matchFilter != null) {
            pipeline.add(Aggregates.match(matchFilter));
        }
        pipeline.add(Aggregates.lookup(collectionNameGenerator.generateCollectionName(Credential.class),
                CredentialBase.FIELD_NAME_CREDENTIAL_ID, Entity.FIELD_NAME_ID, "_credentials"));

        return getCollection().aggregate(pipeline, BsonDocument.class).map(t -> {
            final BsonArray credentials = t.getArray("_credentials");
            final E entity = entityCodec.decode(t.asBsonReader(), decoderContext);
            final Credential credential = credentials.size() > 0
                    ? credentialCodec.decode(((BsonDocument) credentials.get(0)).asBsonReader(), decoderContext)
                    : null;
            return EntryWrapper.wrap(entity, credential);
        });
    }
}
