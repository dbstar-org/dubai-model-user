package io.github.dbstarll.dubai.user.service.attach;

import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import io.github.dbstarll.dubai.model.entity.Entity;
import io.github.dbstarll.dubai.model.service.Implementation;
import io.github.dbstarll.dubai.model.service.attach.CoreAttachs;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;
import io.github.dbstarll.dubai.user.service.impl.AuthTypeAttachImplemental;
import org.bson.conversions.Bson;

@Implementation(AuthTypeAttachImplemental.class)
public interface AuthTypeAttach<E extends Entity & AuthTypable> extends CoreAttachs {
    /**
     * 按认证类型来过滤.
     *
     * @param authType 认证类型
     * @return 过滤条件
     */
    Bson filterByAuthType(AuthType authType);

    /**
     * 按认证类型来统计匹配的实体数量.
     *
     * @param authType 认证类型
     * @return 匹配的实体数量
     */
    long countByAuthType(AuthType authType);

    /**
     * 按认证类型来查询匹配的实体列表.
     *
     * @param authType 认证类型
     * @return 匹配的实体列表
     */
    FindIterable<E> findByAuthType(AuthType authType);

    /**
     * 按认证类型来删除所有匹配的实体.
     *
     * @param authType 认证类型
     * @return 删除结果
     */
    DeleteResult deleteByAuthType(AuthType authType);
}
