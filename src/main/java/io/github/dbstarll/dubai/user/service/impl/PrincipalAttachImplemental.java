package io.github.dbstarll.dubai.user.service.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import io.github.dbstarll.dubai.model.collection.Collection;
import io.github.dbstarll.dubai.model.entity.Entity;
import io.github.dbstarll.dubai.model.service.Aggregator;
import io.github.dbstarll.dubai.model.service.Service;
import io.github.dbstarll.dubai.model.service.impl.CoreImplementals;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.model.service.validation.GeneralValidation;
import io.github.dbstarll.dubai.model.service.validation.Validation;
import io.github.dbstarll.dubai.user.entity.join.PrincipalBase;
import io.github.dbstarll.dubai.user.service.attach.PrincipalAttach;
import io.github.dbstarll.utils.lang.wrapper.EntryWrapper;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Map.Entry;

import static com.mongodb.client.model.Filters.eq;

public final class PrincipalAttachImplemental<E extends Entity & PrincipalBase, S extends Service<E>>
        extends CoreImplementals<E, S> implements PrincipalAttach<E> {
    /**
     * 构造PrincipalAttachImplemental.
     *
     * @param service    服务类
     * @param collection 服务集合
     */
    public PrincipalAttachImplemental(final S service, final Collection<E> collection) {
        super(service, collection);
    }

    @Override
    public Bson filterByPrincipalId(final ObjectId principalId) {
        return eq(PrincipalBase.FIELD_NAME_PRINCIPAL_ID, principalId);
    }

    @Override
    public long countByPrincipalId(final ObjectId principalId) {
        return service.count(filterByPrincipalId(principalId));
    }

    @Override
    public FindIterable<E> findByPrincipalId(final ObjectId principalId) {
        return service.find(filterByPrincipalId(principalId));
    }

    @Override
    public DeleteResult deleteByPrincipalId(final ObjectId principalId) {
        return getCollection().deleteMany(filterByPrincipalId(principalId));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E1 extends Entity, S1 extends Service<E1>> MongoIterable<Entry<E, E1>> findWithPrincipal(
            final S1 principalService, final Bson filter) {
        return Aggregator.builder(service, getCollection())
                .match(aggregateMatchFilter(filter))
                .join(principalService, PrincipalBase.FIELD_NAME_PRINCIPAL_ID)
                .build()
                .joinOne(DEFAULT_CONTEXT)
                .map(e -> EntryWrapper.wrap(e.getKey(), (E1) e.getValue().get(principalService.getEntityClass())));
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
            public void validate(final E entity, final E original, final Validate validate) {
                if (entity.getPrincipalId() == null) {
                    validate.addFieldError(PrincipalBase.FIELD_NAME_PRINCIPAL_ID, "主体未设置");
                } else if (original != null && !entity.getPrincipalId().equals(original.getPrincipalId())) {
                    validate.addFieldError(PrincipalBase.FIELD_NAME_PRINCIPAL_ID, "主体不可更改");
                }
            }
        };
    }
}
