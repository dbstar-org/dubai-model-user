package io.github.dbstarll.dubai.user.service.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import io.github.dbstarll.dubai.model.collection.Collection;
import io.github.dbstarll.dubai.model.entity.Entity;
import io.github.dbstarll.dubai.model.service.Aggregator;
import io.github.dbstarll.dubai.model.service.Service;
import io.github.dbstarll.dubai.model.service.impl.CoreImplementals;
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.entity.join.CredentialBase;
import io.github.dbstarll.dubai.user.service.CredentialService;
import io.github.dbstarll.dubai.user.service.attach.CredentialAttach;
import io.github.dbstarll.utils.lang.wrapper.EntryWrapper;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Map.Entry;

import static com.mongodb.client.model.Filters.eq;
import static org.apache.commons.lang3.Validate.notNull;

public final class CredentialAttachImplemental<E extends Entity & CredentialBase, S extends Service<E>>
        extends CoreImplementals<E, S> implements CredentialAttach<E> {
    private CredentialService credentialService;

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
     * 设置CredentialService.
     *
     * @param credentialService CredentialService实例
     */
    public void setCredentialService(final CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Override
    public void afterPropertiesSet() {
        notNull(credentialService, "credentialService not set.");
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
        return Aggregator.builder(service, getCollection())
                .match(aggregateMatchFilter(filter))
                .join(credentialService, CredentialBase.FIELD_NAME_CREDENTIAL_ID)
                .build()
                .aggregateOne(DEFAULT_CONTEXT)
                .map(e -> EntryWrapper.wrap(e.getKey(), (Credential) e.getValue().get(Credential.class)));
    }
}
