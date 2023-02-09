package io.github.dbstarll.dubai.user.service.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import io.github.dbstarll.dubai.model.collection.Collection;
import io.github.dbstarll.dubai.model.entity.Entity;
import io.github.dbstarll.dubai.model.service.Service;
import io.github.dbstarll.dubai.model.service.impl.CoreImplementals;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.model.service.validation.GeneralValidation;
import io.github.dbstarll.dubai.model.service.validation.Validation;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;
import io.github.dbstarll.dubai.user.service.attach.AuthTypeAttach;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

public final class AuthTypeAttachImplemental<E extends Entity & AuthTypable, S extends Service<E>>
        extends CoreImplementals<E, S> implements AuthTypeAttach<E> {
    /**
     * 构建AuthTypeAttachImplemental实例.
     *
     * @param service    服务类
     * @param collection 集合类
     */
    public AuthTypeAttachImplemental(final S service, final Collection<E> collection) {
        super(service, collection);
    }

    @Override
    public Bson filterByAuthType(final AuthType authType) {
        return eq(AuthTypable.FIELD_NAME_AUTH_TYPE, authType);
    }

    @Override
    public long countByAuthType(final AuthType authType) {
        return service.count(filterByAuthType(authType));
    }

    @Override
    public FindIterable<E> findByAuthType(final AuthType authType) {
        return service.find(filterByAuthType(authType));
    }

    @Override
    public DeleteResult deleteByAuthType(final AuthType authType) {
        return getCollection().deleteMany(filterByAuthType(authType));
    }

    /**
     * 认证类型校验.
     *
     * @return authTypeValidation
     */
    @GeneralValidation
    public Validation<E> authTypeValidation() {
        return new AbstractEntityValidation() {
            @Override
            public void validate(final E entity, final E original, final Validate validate) {
                if (entity.getSource() == null) {
                    validate.addFieldError(AuthTypable.FIELD_NAME_AUTH_TYPE, "认证类型未设置");
                } else if (original != null && !entity.getSource().equals(original.getSource())) {
                    validate.addFieldError(AuthTypable.FIELD_NAME_AUTH_TYPE, "认证类型不可更改");
                }
            }
        };
    }
}
