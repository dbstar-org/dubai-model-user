package io.github.dbstarll.dubai.user.service.impl;

import io.github.dbstarll.dubai.model.collection.Collection;
import io.github.dbstarll.dubai.model.service.AbstractImplemental;
import io.github.dbstarll.dubai.user.entity.UserEntities;
import io.github.dbstarll.dubai.user.service.UserServices;

/**
 * 本模块所有Implemental的基类.
 *
 * @param <E> 实体类
 * @param <S> 服务类
 */
public abstract class UserImplementals<E extends UserEntities, S extends UserServices<E>>
        extends AbstractImplemental<E, S> {
    protected UserImplementals(final S service, final Collection<E> collection) {
        super(service, collection);
    }
}
