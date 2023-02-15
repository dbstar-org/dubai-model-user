package io.github.dbstarll.dubai.user.service;

import io.github.dbstarll.dubai.model.service.Service;
import io.github.dbstarll.dubai.user.entity.UserEntities;

/**
 * 本模块所有服务的基类.
 *
 * @param <E> 实体类
 */
public interface UserServices<E extends UserEntities> extends Service<E> {
}
