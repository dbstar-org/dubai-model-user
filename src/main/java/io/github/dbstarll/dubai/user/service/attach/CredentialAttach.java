package io.github.dbstarll.dubai.user.service.attach;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
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
    /**
     * 生成基于认证ID的过滤条件.
     *
     * @param credentialId 认证ID
     * @return 基于认证ID的过滤条件
     */
    Bson filterByCredentialId(ObjectId credentialId);

    /**
     * 按照认证ID来统计匹配的记录条数.
     *
     * @param credentialId 认证ID
     * @return 匹配的记录条数
     */
    long countByCredentialId(ObjectId credentialId);

    /**
     * 查找认证ID下匹配的所有记录.
     *
     * @param credentialId 认证ID
     * @return 匹配的所有记录
     */
    FindIterable<E> findByCredentialId(ObjectId credentialId);

    /**
     * 删除认证ID下匹配的所有记录.
     *
     * @param credentialId 认证ID
     * @return 删除结果
     */
    DeleteResult deleteByCredentialId(ObjectId credentialId);

    /**
     * 与认证表进行left join查询，返回实体与认证关联的结果列表.
     *
     * @param filter 过滤条件
     * @return 实体与认证关联的结果列表
     */
    MongoIterable<Entry<E, Credential>> findWithCredential(Bson filter);
}
